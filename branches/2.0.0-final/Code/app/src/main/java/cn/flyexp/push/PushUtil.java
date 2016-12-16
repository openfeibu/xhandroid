package cn.flyexp.push;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.flyexp.entity.PushOpenBean;
import cn.flyexp.entity.PushThroughBean;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.util.GsonUtil;
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
        PushThroughBean pushThroughBean = GsonUtil.getInstance().fromJson(throughData, PushThroughBean.class);
        int refresh = pushThroughBean.getRefresh();
        String target = pushThroughBean.getTarget();
        if (refresh == 1) {
            if (TextUtils.equals(target, "message")) {
                mes.what = NotifyIDDefine.NOTIFY_MESSAGE_REFRESH;
            } else if (TextUtils.equals(target, "mytask")) {
                mes.what = NotifyIDDefine.NOTIFY_MINE_MYTASK_REFRESH;
            } else if (TextUtils.equals(target, "topic")) {
                mes.what = NotifyIDDefine.NOTIFY_TOPIC_PUSH;
            }
            BaseWindow.getNotifyManager().notify(mes);
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

        PushOpenBean pushOpenBean = GsonUtil.getInstance().fromJson(content, PushOpenBean.class);
        String open = pushOpenBean.getOpen();
        ArrayList<String> data = pushOpenBean.getData();
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
        bundle.putStringArrayList("pushdata", data);
        Intent intent = null;
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        intent.putExtra("pushbunble", bundle);
        context.startActivity(intent);
    }
}
