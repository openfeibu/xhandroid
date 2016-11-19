package cn.flyexp.mvc.task;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.entity.CancelTaskRequest;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.constants.Config;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.StepView;
import cn.flyexp.wxapi.Util;


/**
 * Created by txy on 2016/8/2.
 */
public class MyOrderDetailWindow extends AbstractWindow implements View.OnClickListener {

    private TaskViewCallBack callBack;
    private TextView tv_nickname;
    private TextView tv_money;
    private TextView tv_destination;
    private TextView tv_description;
    private int orderId;
    private String openId;
    private Button btn_finish;
    private String phone;
    private String status;
    private FinishOrderRequest finishOrderRequest;
    private PopupWindow picPopupWindow;
    private View taskLayout;
    private MyTaskResponse.MyTaskResponseData data = new MyTaskResponse().new MyTaskResponseData();
    private ImageView iv_share;
    private StepView stepView;

    public MyOrderDetailWindow(TaskViewCallBack callBack) {
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

    public void initMyOrderDetail(MyTaskResponse.MyTaskResponseData responseData) {
        if (responseData == null) {
            return;
        }
        data = responseData;
        orderId = responseData.getOid();
        openId = responseData.getOpenid();
        phone = responseData.getPhone();
        tv_destination.setText("到达地点：" + responseData.getDestination());
        tv_money.setText(responseData.getFee() + "");
        tv_description.setText(responseData.getDescription());
        if (responseData.getNickname().equals("")) {
            tv_nickname.setText("还没有人接");
            tv_nickname.setTextColor(getResources().getColor(R.color.font_brown_light));
        } else {
            tv_nickname.setText("接单人：" + responseData.getNickname());
            tv_nickname.setTextColor(getResources().getColor(R.color.font_brown_dark));
        }
        status = responseData.getStatus();
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
        if (status.equals("new")) {
            btn_finish.setVisibility(VISIBLE);
            btn_finish.setText("取消任务");
            btn_finish.setOnClickListener(this);
            iv_share.setVisibility(VISIBLE);
            iv_share.setOnClickListener(this);
        } else if (status.equals("accepted")) {
            btn_finish.setVisibility(GONE);
        } else if (status.equals("finish")) {
            btn_finish.setVisibility(VISIBLE);
            btn_finish.setText("结算任务");
            btn_finish.setOnClickListener(this);
        } else if (status.equals("completed")) {
            btn_finish.setVisibility(GONE);
        } else {
            btn_finish.setVisibility(GONE);
        }

    }

    public void finishWork(String pwd) {
        finishOrderRequest.setPay_password(pwd);
        callBack.finishOrder(finishOrderRequest);
        showProgressDialog("正在结算...");
        btn_finish.setEnabled(false);
    }


    public void response() {
        dismissProgressDialog();
        btn_finish.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_report:
                callBack.reportEnter(orderId);
                break;
            case R.id.tv_nickname:
                callBack.taWindowEnter(openId);
                break;
            case R.id.btn_finish:
                final String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                int is_paypwd = WindowHelper.getIntByPreference("is_paypwd");
                if (is_paypwd == 0) {
                    callBack.setPayPwdEnter();
                    return;
                }
                if (status.equals("new")) {
                    WindowHelper.showAlertDialog("确定要取消任务吗~", "考虑一下", "取消任务", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelTaskRequest cancelTaskRequest = new CancelTaskRequest();
                            cancelTaskRequest.setToken(token);
                            cancelTaskRequest.setOrder_id(orderId);
                            callBack.cancelTask(cancelTaskRequest, true);
                        }
                    });

                } else if (status.equals("finish")) {
                    WindowHelper.showAlertDialog("对方完成任务了吗", "还没", "完成了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishOrderRequest = new FinishOrderRequest();
                            finishOrderRequest.setToken(token);
                            finishOrderRequest.setOrder_id(orderId);
                            callBack.verifiPayPwdEnter(NotifyIDDefine.RESULT_PAY_FINISHTAK);
                        }
                    });
                }
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
