package cn.flyexp.push;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import java.util.List;

import cn.flyexp.MainActivity;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.PushOpenBean;
import cn.flyexp.entity.PushThroughBean;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.ControllerManager;
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
     * <p>
     * refresh: 1或0 是否刷新
     * target: 指定区域  ad、extra、assn_hotact、message、assn_member_review、assn_act、assn_notice、mytask、topic
     * data: 数据或null
     * <p>
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
                mes.what = NotifyIDDefine.NOTIFY_MESSAGE_PUSH;
            } else if (TextUtils.equals(target, "mytask")) {
                mes.what = NotifyIDDefine.NOTIFY_MYTASK_PUSH;
            } else if (TextUtils.equals(target, "topic")) {
                mes.what = NotifyIDDefine.NOTIFY_TOPIC_PUSH;
            }
            BaseWindow.getNotifyManager().notify(mes);
            BaseWindow.getNotifyManager().notify(NotifyIDDefine.NOTIFY_MAIN_PUSH);
        }
    }

    /**
     * 点击通知栏跳转页面
     * {open:"window",data:"json"}
     * open : web、task、topic
     * data: UrlResponse、TaskResponse、TopicResponse
     *
     * @param context
     * @param content
     */
    public static void openWindowFromPush(Context context, String content) {
        LogUtil.e(PushUtil.class.getSimpleName(), content);
        int windowId = 0;
        Bundle bundle = new Bundle();
        GsonUtil gsonUtil = GsonUtil.getInstance();
        PushOpenBean pushOpenBean = gsonUtil.fromJson(content, PushOpenBean.class);
        String open = pushOpenBean.getOpen();
        String data = pushOpenBean.getData();
        if (open.equals("web")) {
            windowId = WindowIDDefine.WINDOW_WEBVIEW;
            WebBean webBean = new WebBean();
            webBean.setTitle("");
            webBean.setUrl(data);
            webBean.setRequest(false);
            bundle.putSerializable("webbean", webBean);
        } else if (open.equals("task")) {
            windowId = WindowIDDefine.WINDOW_TASK_DETAIL;
            TaskResponse.TaskResponseData taskResponseData = gsonUtil.fromJson(data, TaskResponse.TaskResponseData.class);
            bundle.putSerializable("taskDetail", taskResponseData);
        } else if (open.equals("mytask")) {
            windowId = WindowIDDefine.WINDOW_MYTASK_DETAIL;
            MyTaskResponse.MyTaskResponseData myTaskResponseData = gsonUtil.fromJson(data, MyTaskResponse.MyTaskResponseData.class);
            bundle.putSerializable("myTaskDetail", myTaskResponseData);
        } else if (open.equals("topic")) {
            windowId = WindowIDDefine.WINDOW_TOPIC_DETAIL;
            TopicResponseData topicResponseData = gsonUtil.fromJson(data, TopicResponseData.class);
            bundle.putSerializable("topicDetail", topicResponseData);
        }
        if (MainActivity.getContext() == null) {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("cn.flyexp");
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            launchIntent.putExtra("pushBundle", bundle);
            launchIntent.putExtra("windowId", windowId);
            context.startActivity(launchIntent);
        } else {
            ControllerManager.getInstance().sendMessage(windowId, bundle);
        }
    }

    private static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
