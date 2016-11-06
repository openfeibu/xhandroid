package cn.flyexp.receiver;

import android.content.Context;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/29 0029.
 */
public class PushReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {

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
        String content = xgPushTextMessage.getCustomContent();
        JSONObject jsonObject = new JSONObject();
        int code = 0;
        try {
            code = jsonObject.getJSONObject(content).getInt("code");
            if (code == 200) {
                Message message = Message.obtain();
                message.what = NotifyIDDefine.NOTIFY_MESSAGE_REFRESH;
                AbstractController.notifyManager.notify(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {

    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {

    }
}
