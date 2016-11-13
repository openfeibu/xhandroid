package cn.flyexp.mvc.mine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import cn.flyexp.FBApplication;
import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.wxapi.Util;

/**
 * Created by txy on 2016/8/7 0007.
 */
public class InvitaionWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;

    public InvitaionWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_invitation);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_qq).setOnClickListener(this);
        findViewById(R.id.tv_wxf).setOnClickListener(this);
        findViewById(R.id.tv_wxq).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
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
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "校汇召唤你—一起玩赚校园");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "先给自己定个小目标，比方说我先下载校汇");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, "http://feibu.info/download/download.html");
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, "http://xhplus.feibu.info/fb/images/logo.png");
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "校汇");
        MainActivity.mTencent.shareToQQ((Activity) getContext(), params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                String token = WindowHelper.getStringByPreference("token");
                if(token.equals("")){
                    callBack.loginWindowEnter();
                    return;
                }
                callBack.share(token);
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
        webpage.webpageUrl = "http://feibu.info/download/download.html";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "校汇召唤你—一起玩赚校园";
        msg.description = "先给自己定个小目标，比方说我先下载校汇";
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
