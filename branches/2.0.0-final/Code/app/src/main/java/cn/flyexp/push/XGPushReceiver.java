package cn.flyexp.push;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;

/**
 * Created by txy on 2016/12/25 0029.
 */
public class XGPushReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        if (xgPushRegisterResult!=null) {
            LogUtil.e("device_token" + xgPushRegisterResult.getToken());
            SharePresUtil.putString(SharePresUtil.KEY_DEVICE_TOKEN, xgPushRegisterResult.getToken());
        }
    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {

    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {

    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        String content = xgPushTextMessage.getContent();
        if(!TextUtils.isEmpty(content)){
            PushUtil.dispatchData(content);
        }
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        String content = xgPushClickedResult.getContent();
        if(!TextUtils.isEmpty(content)){
            PushUtil.openWindowFromPush(context,content);
        }
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
