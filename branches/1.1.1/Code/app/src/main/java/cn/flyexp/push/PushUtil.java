package cn.flyexp.push;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.util.LogUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/4.
 */
public class PushUtil {

    /**
     * 分发透传数据
     * <p/>
     * refresh: 1或0 是否刷新
     * target: 指定区域  ad、extra、assn_hotact、message、assn_member_review、assn_act、assn_notice、mytask、topic
     * data: 数据或null
     * <p/>
     * 通知刷新的window未初始化用sharedpreference保存
     *
     * @param throughData 透传数据
     */
    public static void dispatchData(String throughData) {
        LogUtil.e(PushUtil.class.getSimpleName(), throughData);
        Message mes = Message.obtain();
        try {
            JSONObject jsonObject = new JSONObject(throughData);
            int refresh = jsonObject.getInt("refresh");
            String target = jsonObject.getString("target");
            String data = jsonObject.getString("data");

            if (refresh == 1) {
                if (target.equals("ad")) {
                    mes.what = NotifyIDDefine.NOTIFY_AD_REFRESH;
                } else if (target.equals("extra")) {
                    mes.what = NotifyIDDefine.NOTIFY_EXTRA_REFRESH;
                } else if (target.equals("assn_hotact")) {
                    mes.what = NotifyIDDefine.NOTIFY_HOTACT_REFRESH;
                } else if (target.equals("message")) {
                    mes.what = NotifyIDDefine.NOTIFY_MESSAGE_REFRESH;
                } else if (target.equals("assn_member_review")) {
                    mes.what = NotifyIDDefine.NOTIFY_ASSN_MENBER_REFRESH;
                    mes.obj = data;
                } else if (target.equals("assn_act")) {
                    mes.what = NotifyIDDefine.NOTIFY_ASSN_ACT_REFRESH;
                    mes.obj = data;
                } else if (target.equals("assn_notice")) {
                    mes.what = NotifyIDDefine.NOTIFY_ASSN_NOTICE_REFRESH;
                    mes.obj = data;
                } else if (target.equals("mytask")) {
                    mes.what = NotifyIDDefine.NOTIFY_MINE_MYTASK_REFRESH;
                } else if (target.equals("topic")) {
                    mes.what = NotifyIDDefine.NOTIFY_TOPIC_REFRESH;
                }
                BaseWindow.getNotifyManager().notify(mes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击通知栏跳转页面
     * {open:"window",data:{}}
     * open : web、task、order、topic
     *
     * @param context
     * @param content
     */
    public static void openWindow(Context context, String content) {
        LogUtil.e(PushUtil.class.getSimpleName(), content);
        int windowId = 0;
        Bundle bundle = new Bundle();
        try {
            JSONObject jsonObject = new JSONObject(content);
            String open = jsonObject.getString("open");
            String data = jsonObject.getString("data");
            if (open.equals("web")) {
                windowId = WindowIDDefine.WINDOW_WEBVIEW;
            } else if (open.equals("task")) {
                windowId = WindowIDDefine.WINDOW_TASK_DETAIL;
            } else if (open.equals("order")) {
                windowId = WindowIDDefine.WINDOW_ORDER_DETAIL;
            } else if (open.equals("topic")) {
                windowId = WindowIDDefine.WINDOW_TOPIC_DETAIL;
            }
            bundle.putInt("windowid", windowId);
            bundle.putString("pushdata", data);
            Intent intent = null;
            PackageManager packageManager = context.getPackageManager();
            intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            intent.putExtra("pushbunble", bundle);
            context.startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
