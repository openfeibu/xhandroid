package cn.flyexp;

import android.app.Application;

import com.facebook.stetho.Stetho;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.flyexp.util.CrashHandler;
import cn.flyexp.util.PicassoImageLoader;
import cn.flyexp.util.PicassoPauseOnScrollListener;

/**
 * Created by guo on 2016/6/9.
 * Modifity by txy on 2016/7/21.
 */
public class FBApplication extends Application {

    public static FBApplication APPLICATION_CONTEXT;

    public void onCreate() {
        super.onCreate();
        APPLICATION_CONTEXT = this;

        //关闭LogUtil
//        LogUtil.close();

        //全局捕抓异常
        CrashHandler.getInstance().init(getApplicationContext());
        Stetho.initializeWithDefaults(this);

        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(getResources().getColor(R.color.light_blue))
                .setFabNornalColor(getResources().getColor(R.color.light_blue))
                .setFabPressedColor(getResources().getColor(R.color.dark_blue))
                .setCheckSelectedColor(getResources().getColor(R.color.light_blue))
                .setCheckSelectedColor(getResources().getColor(R.color.dark_blue))
                .build();
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setMutiSelectMaxSize(9)
                .setEnablePreview(true)
                .build();
        CoreConfig coreConfig = new CoreConfig.Builder(this, new PicassoImageLoader(), theme)
                .setFunctionConfig(functionConfig)
                .setPauseOnScrollListener(new PicassoPauseOnScrollListener(false, true))
                .build();
        GalleryFinal.init(coreConfig);
    }

}
