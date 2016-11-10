package cn.flyexp;

import android.app.Application;

import com.tencent.tauth.Tencent;

import cn.flyexp.constants.Config;
import cn.flyexp.push.XMPush;
import cn.flyexp.util.CrashHandler;

/**
 * Created by tanxinye on 2016/10/26.
 */
public class XiaohuiApplication extends Application {

    private static XiaohuiApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initCrash();
        initPush();
    }

    private void initCrash() {
        if (!Config.isDebug) {
            new CrashHandler(this).start();
        }
    }

    private void initPush() {
        new XMPush().init(this);
    }

    public static XiaohuiApplication getApplication() {
        return application;
    }
}
