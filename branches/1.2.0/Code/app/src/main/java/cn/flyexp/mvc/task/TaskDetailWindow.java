package cn.flyexp.mvc.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.RoundImageView;
import cn.flyexp.wxapi.Util;


/**
 * Created by txy on 2016/6/5.
 */
public class TaskDetailWindow extends AbstractWindow implements View.OnClickListener {

    private TaskViewCallBack callBack;
    private OrderResponse.OrderResponseData data;
    private TextView tv_nickname;
    private TextView tv_fee;
    private TextView tv_destination;
    private TextView tv_description;
    private TextView tv_report;
    private Button btn_claim;
    private PopupWindow picPopupWindow;
    private View taskLayout;
    private TextView tv_state;

    public TaskDetailWindow(TaskViewCallBack orderViewCallBack) {
        super(orderViewCallBack);
        this.callBack = orderViewCallBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_task_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_taskStatement).setOnClickListener(this);
        findViewById(R.id.iv_share).setOnClickListener(this);

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


        btn_claim = (Button) findViewById(R.id.btn_claim);
        btn_claim.setOnClickListener(this);
        tv_report = (TextView) findViewById(R.id.tv_report);
        tv_report.setOnClickListener(this);


        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setOnClickListener(this);
        tv_fee = (TextView) findViewById(R.id.tv_fee);
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tv_description = (TextView) findViewById(R.id.tv_description);

    }

    public void initOrderDetail(OrderResponse.OrderResponseData responseData) {
        if (responseData == null) {
            return;
        }
        this.data = responseData;
        String mineOpenId = WindowHelper.getStringByPreference("mine_openid");
        if(responseData.getStatus().equals("new")){
            btn_claim.setVisibility(VISIBLE);
        }
        if (mineOpenId.equals(responseData.getOpenid())) {
            btn_claim.setVisibility(GONE);
            tv_report.setVisibility(GONE);
        } else {
            btn_claim.setVisibility(VISIBLE);
            tv_report.setVisibility(VISIBLE);
        }
        tv_destination.setText(responseData.getDestination());
        tv_fee.setText(responseData.getFee());
        tv_description.setText(responseData.getDescription());
        tv_nickname.setText("发单人：" + responseData.getNickname());
        tv_state.setText(parseStatus(responseData.getStatus()));
    }

    private String parseStatus(String status) {
        String str = "";
        if (status.equals("new")) {
            str = "可接单";
        } else if (status.equals("finish")) {
            str = "待结算";
        } else if (status.equals("accepted")) {
            str = "已接单";
        } else if (status.equals("completed")) {
            str = "已结算";
        }
        return str;
    }

    public void detailRequest(int arg1) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_claim:
                final String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                int isAuth = WindowHelper.getIntByPreference("is_auth");
                if (isAuth == 0) {
                    WindowHelper.showToast(getContext().getString(R.string.none_certifition));
                    return;
                } else if (isAuth == 2) {
                    WindowHelper.showToast(getContext().getString(R.string.certifing));
                    return;
                }
                WindowHelper.showAlertDialog("接受任务之后必须完成哦~", "考虑一下", "乐意接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TaskClaimRequest taskClaimRequest = new TaskClaimRequest();
                        taskClaimRequest.setToken(token);
                        taskClaimRequest.setOrder_id(data.getOid());
                        showProgressDialog("正在提交接收申请...");
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
                WebBean taskWebBean = new WebBean();
                taskWebBean.setRequest(true);
                taskWebBean.setTitle("任务声明");
                taskWebBean.setName("taskStatement");
                callBack.webWindowEnter(taskWebBean);
                break;
            case R.id.iv_avatar:
            case R.id.tv_nickname:
                callBack.taWindowEnter(data.getOpenid());
                break;
            case R.id.iv_share:
                picPopupWindow.showAtLocation(taskLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_qq:
                shareQQ();
                break;
            case R.id.tv_wxf:
                shareWX(SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.tv_wxq:
                shareWX(SendMessageToWX.Req.WXSceneTimeline);
                break;
        }
    }

    public void shareQQ() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "校汇校园任务");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, data.getDescription());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, data.getShare_url());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://xhplus.feibu.info/fb/images/logo.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "校汇");
        MainActivity.mTencent.shareToQQ((Activity) getContext(), params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        });
    }


    private void shareWX(int scene) {
        if (!MainActivity.api.isWXAppInstalled()) {
            WindowHelper.showToast("您还未安装微信客户端");
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = data.getShare_url();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "校汇校园任务";
        msg.description = data.getDescription();
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_launchericon);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        MainActivity.api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
