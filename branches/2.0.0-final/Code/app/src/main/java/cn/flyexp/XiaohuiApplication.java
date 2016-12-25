package cn.flyexp;

import android.app.Application;

import com.tencent.tauth.Tencent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import cn.flyexp.constants.Config;
import cn.flyexp.push.XGPush;
import cn.flyexp.push.XMPush;
import cn.flyexp.util.CrashHandler;
import cn.flyexp.util.SharePresUtil;

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
    }

    private void initCrash() {
        if (!Config.isDebug) {
            new CrashHandler(this).start();
        }
    }

    public static XiaohuiApplication getApplication() {
        return application;
    }
}
