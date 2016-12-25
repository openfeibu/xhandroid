package cn.flyexp.push;

import android.content.Context;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;

/**
 * Created by txy on 2016/12/25 0019.
 */
public class XGPush {

    public void init(Context context) {
        XGPushManager.registerPush(context, "-1", new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                LogUtil.e("device_token" + o);
                SharePresUtil.putString(SharePresUtil.KEY_DEVICE_TOKEN, (String) o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
            }
        });
    }

    public static void unbundingPush(Context context) {
        XGPushManager.registerPush(context, "*");
    }
}
