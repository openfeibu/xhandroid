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
    }

}
