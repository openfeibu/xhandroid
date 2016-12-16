package cn.flyexp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.wxapi.Util;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class ShareHelper {

    public static final int SHARE_WX_FIREND = SendMessageToWX.Req.WXSceneSession;
    public static final int SHARE_WX_CIRCLE = SendMessageToWX.Req.WXSceneTimeline;

    public interface ShareLinstener {
        void onShareSuccess();
    }

    public static void shareQQ(Context context, String title, String description, String url, final ShareLinstener linstener) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, context.getResources().getString(R.string.share_qq_img_url));
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, context.getString(R.string.app_name));
        MainActivity.getTencent().shareToQQ((Activity) MainActivity.getContext(), params, new IUiListener() {
            @Override
            public void onComplete(Object o) {
                if (linstener != null) {
                    linstener.onShareSuccess();
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

    public static void shareWX(Context context, int scene, String title, String description, String url, final ShareLinstener linstener) {
        if (!MainActivity.getWxapi().isWXAppInstalled()) {
            Toast.makeText(context, R.string.wxapp_no_install, Toast.LENGTH_SHORT).show();
            return;
        }

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_launchericon);
        msg.thumbData = Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = scene;
        MainActivity.getWxapi().sendReq(req);
        NotifyManager.getInstance().register(NotifyIDDefine.NOTICE_SHARE, new NotifyManager.Notify() {
            @Override
            public void onNotify(Message mes) {
                if (mes.what == NotifyIDDefine.NOTICE_SHARE) {
                    if (linstener != null) {
                        linstener.onShareSuccess();
                    }
                }
            }
        });
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

}
