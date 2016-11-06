package cn.flyexp.push;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

import cn.flyexp.FBApplication;

/**
 * Created by tanxinye on 2016/9/19.
 */
public class XMPush {

    public static final String APP_ID = "2882303761517511230";
    public static final String APP_KEY = "5201751168230";
    public static final String TAG = "cn.flyexp";

    private static XMPush xmPush;
    private Context context;

    private XMPush() {
    }

    public static XMPush getInstance() {
        if (xmPush == null) {
            synchronized (XMPush.class) {
                if (xmPush == null) {
                    xmPush = new XMPush();
                }
            }
        }
        return xmPush;
    }

    public static void init() {
        getInstance()._init();
    }

    private void _init() {
        //初始化push推送服务
        context = FBApplication.APPLICATION_CONTEXT;
        if (shouldInit()) {
            MiPushClient.registerPush(context, APP_ID, APP_KEY);
        }
        //打开Log
        LoggerInterface newLogger = new LoggerInterface() {

            @Override
            public void setTag(String tag) {
                // ignore
            }

            @Override
            public void log(String content, Throwable t) {
                Log.d(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.d(TAG, content);
            }
        };
        Logger.setLogger(context, newLogger);
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
