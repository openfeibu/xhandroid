package cn.flyexp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.framework.Environment;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.push.XGPush;
import cn.flyexp.push.XMPush;
import cn.flyexp.permission.PermissionInterceptor;
import cn.flyexp.util.CrashHandler;
import cn.flyexp.util.PicassoImageLoader;
import cn.flyexp.util.PicassoPauseOnScrollListener;


/**
 * Created by zlk on 2016/3/27.
 * Modified by zlk on 2016/3/31 添加广播消息系统
 * Modified by txy on 2016/8/4.
 */
public class MainActivity extends AppCompatActivity {

    public static Tencent mTencent;
    public static IWXAPI api;
    private boolean isSoftShowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void statusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.light_blue));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Environment.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
        Environment.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Environment.onSaveInstanceState(outState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 必须要调用这句
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Environment.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN ||  event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
            return super.dispatchKeyEvent(event);
        }
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (isSoftShowing) {
                hideInput();
                return true;
            }
        }
        Environment.dispatchKeyEvent(event);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            PermissionInterceptor.requestResult(permissions, grantResults);
        }
    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        statusBarColor();
        Environment.init(this);
        pushInit();
        galleryFinalInit();
//        关闭LogUtil
//       LogUtil.close();
//        全局捕抓异常
//        CrashHandler.getInstance().init(getApplicationContext());
        qqApiInit();
        wxApiInit();
        initInput();
    }

    private void initInput(){
        final View root = getRootView();
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                root.getWindowVisibleDisplayFrame(r);
                int screenHeight = root.getRootView().getHeight();
                int heightDiff = screenHeight - (r.bottom - r.top);
                boolean visible = heightDiff > screenHeight / 3;
                if (visible) {
                    isSoftShowing = true;
                } else {
                    isSoftShowing = false;
                }
            }
        });
    }


    private View getRootView() {
        return ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
    }

    private void hideInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v != null) {
            imm.hideSoftInputFromInputMethod(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private void pushInit() {
        if (checkIsMIUI()) {
            XMPush.init();
            WindowHelper.putStringByPreference(SharedPrefs.KET_PUSH, SharedPrefs.VALUE_XMPUSH);
        } else {
            XGPush.init();
            WindowHelper.putStringByPreference(SharedPrefs.KET_PUSH, SharedPrefs.VALUE_XGPUSH);
        }
    }

    private boolean checkIsMIUI() {
        // 检测MIUI
        String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
        String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
        String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

        Properties prop = new Properties();
        boolean isMIUI;
        try {
            prop.load(new FileInputStream(new File(android.os.Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        return isMIUI;
    }

    private void galleryFinalInit() {
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


    private void qqApiInit() {
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance("1105527191", this.getApplicationContext());
    }

    private void wxApiInit() {
        // 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
        api = WXAPIFactory.createWXAPI(this, "wx13275568a3405957");
        api.registerApp("wx13275568a3405957");
    }
}
