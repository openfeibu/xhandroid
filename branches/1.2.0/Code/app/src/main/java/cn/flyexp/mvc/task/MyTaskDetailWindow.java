package cn.flyexp.mvc.task;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.entity.FinishWorkRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.StepView;


/**
 * Created by txy on 2016/8/2.
 */
public class MyTaskDetailWindow extends AbstractWindow implements View.OnClickListener {

    private TaskViewCallBack callBack;
    private String openId;
    private int orderId;
    private String phone;
    private TextView tv_nickname;
    private TextView tv_money;
    private TextView tv_destination;
    private TextView tv_description;
    private ImageView iv_share;
    private StepView stepView;
    private PopupWindow picPopupWindow;
    private View taskLayout;
    private Button btn_finish;

    public MyTaskDetailWindow(TaskViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_mytask_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_report).setOnClickListener(this);
        findViewById(R.id.tv_contact).setOnClickListener(this);

        stepView = (StepView) findViewById(R.id.stepView);
        iv_share = (ImageView) findViewById(R.id.iv_share);

        taskLayout = findViewById(R.id.taskLayout);

        View popShareLayout = LayoutInflater.from(getContext()).inflate(R.layout
                .pop_share, null);
        popShareLayout.findViewById(R.id.tv_qq).setOnClickListener(this);
        popShareLayout.findViewById(R.id.tv_wxf).setOnClickListener(this);
        popShareLayout.findViewById(R.id.tv_wxq).setOnClickListener(this);
        picPopupWindow = new PopupWindow(popShareLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        picPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        picPopupWindow.setFocusable(true);
        picPopupWindow.setOutsideTouchable(true);
        picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);

        btn_finish = (Button) findViewById(R.id.btn_finish);

        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setOnClickListener(this);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tv_description = (TextView) findViewById(R.id.tv_description);
    }

    public void initMyTaskDetail(MyTaskResponse.MyTaskResponseData responseData) {
        if (responseData == null) {
            return;
        }
        orderId = responseData.getOid();
        openId = responseData.getOpenid();
        phone = responseData.getAlt_phone();
        tv_destination.setText("到达地点：" + responseData.getDestination());
        tv_money.setText(responseData.getFee() + "");
        tv_description.setText(responseData.getDescription());
        tv_nickname.setText("发单人：" + responseData.getNickname());
        tv_nickname.setTextColor(getResources().getColor(R.color.font_brown_dark));
        String status = responseData.getStatus();
        MyTaskResponse.MyTaskResponseData.TimeData timeData = responseData.getTime();
        if (!TextUtils.isEmpty(timeData.getCancelled_time())) {
            stepView.cancel(timeData.getCancelled_time());
        } else {
            String[] dates = new String[4];
            int len = 0;
            if (!TextUtils.isEmpty(timeData.getNew_time())) {
                dates[0] = DateUtil.long2Date(DateUtil.date2Long(timeData.getNew_time()), "MM-dd HH:mm");
                len++;
                if (!TextUtils.isEmpty(timeData.getAccepted_time())) {
                    dates[1] = DateUtil.long2Date(DateUtil.date2Long(timeData.getAccepted_time()), "MM-dd HH:mm");
                    len++;
                    if (!TextUtils.isEmpty(timeData.getFinish_time())) {
                        dates[2] = DateUtil.long2Date(DateUtil.date2Long(timeData.getFinish_time()), "MM-dd HH:mm");
                        len++;
                        if (!TextUtils.isEmpty(timeData.getCompleted_time())) {
                            dates[3] = DateUtil.long2Date(DateUtil.date2Long(timeData.getCompleted_time()), "MM-dd HH:mm");
                            len++;
                        }
                    }
                }
            }
            stepView.setDate(dates, len);
        }
        stepView.show();
        if (status.equals("accepted")) {
            btn_finish.setVisibility(VISIBLE);
            btn_finish.setText("完成任务");
            btn_finish.setOnClickListener(this);
        } else {
            btn_finish.setVisibility(GONE);
        }
    }

    public void response() {
        btn_finish.setEnabled(true);
        dismissProgressDialog();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.sender_name:
                callBack.taWindowEnter(openId);
                break;
            case R.id.tv_report:
                callBack.reportEnter(orderId);
                break;
            case R.id.btn_finish:
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                FinishWorkRequest finishWorkRequest = new FinishWorkRequest();
                finishWorkRequest.setToken(token);
                finishWorkRequest.setOrder_id(orderId);
                callBack.finishWork(finishWorkRequest);
                v.setEnabled(false);
                showProgressDialog("正在提交...");
                break;
            case R.id.tv_nickname:
                callBack.taWindowEnter(openId);
                break;
            case R.id.tv_contact:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    WindowHelper.showToast("无法调用电话界面");
                }
                break;
        }
    }

}
