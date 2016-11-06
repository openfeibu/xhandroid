package cn.flyexp.mvc.task;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import cn.flyexp.R;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.RoundImageView;


/**
 * Created by txy on 2016/6/5.
 */
public class TaskDetailWindow extends AbstractWindow implements View.OnClickListener {

    private TaskViewCallBack callBack;
    private OrderResponse.OrderResponseData data;
    private TextView tv_nickname;
    private RoundImageView iv_avatar;
    private TextView tv_surplustime;
    private TextView tv_fee;
    private TextView tv_destination;
    private TextView tv_description;
    private TextView tv_report;
    private Button btn_claim;

    public TaskDetailWindow(TaskViewCallBack orderViewCallBack) {
        super(orderViewCallBack);
        this.callBack = orderViewCallBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_task_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_taskStatement).setOnClickListener(this);

        btn_claim = (Button) findViewById(R.id.btn_claim);
        btn_claim.setOnClickListener(this);
        tv_report = (TextView) findViewById(R.id.tv_report);
        tv_report.setOnClickListener(this);

        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        iv_avatar.setOnClickListener(this);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setOnClickListener(this);
        tv_surplustime = (TextView) findViewById(R.id.tv_surplustime);
        tv_fee = (TextView) findViewById(R.id.tv_fee);
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tv_description = (TextView) findViewById(R.id.tv_description);

    }

    public void initOrderDetail(OrderResponse.OrderResponseData responseData) {
        if (responseData == null) {
            return;
        }
        this.data = responseData;
        String mineOpenId = getStringByPreference("mine_openid");
        if (mineOpenId.equals(responseData.getOpenid())) {
            btn_claim.setVisibility(GONE);
            tv_report.setVisibility(GONE);
        } else {
            btn_claim.setVisibility(VISIBLE);
            tv_report.setVisibility(VISIBLE);
        }
        tv_surplustime.setText(DateUtil.countDown(DateUtil.addOneDay(DateUtil.date2Long(responseData.getCreated_at()))));
        tv_destination.setText(responseData.getDestination());
        tv_fee.setText(responseData.getFee());
        tv_description.setText(responseData.getDescription());
        tv_nickname.setText(responseData.getNickname());
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 32), CommonUtil.dip2px(getContext(), 32))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(getContext().getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(iv_avatar);
        } else {
            iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
    }

    public void detailRequest(int arg1) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_claim:
                final String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                int isAuth = getIntByPreference("is_auth");
                if (isAuth == 0) {
                    showToast(getContext().getString(R.string.none_certifition));
                    return;
                } else if (isAuth == 2) {
                    showToast(getContext().getString(R.string.certifing));
                    return;
                }
                showAlertDialog("接受任务之后必须完成哦~", "考虑一下", "乐意接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskClaimRequest taskClaimRequest = new TaskClaimRequest();
                        taskClaimRequest.setToken(token);
                        taskClaimRequest.setOrder_id(data.getOid());
                        callBack.claimOrder(taskClaimRequest);
                    }
                });
                break;
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_report:
                callBack.reportEnter(data.getOid());
                break;
            case R.id.tv_taskStatement:
                callBack.webWindowEnter(new String[]{"taskStatement"}, 0);
                break;
            case R.id.iv_avatar:
            case R.id.tv_nickname:
                callBack.taWindowEnter(data.getOpenid());
                break;
        }
    }

}
