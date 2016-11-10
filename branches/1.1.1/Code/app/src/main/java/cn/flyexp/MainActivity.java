package cn.flyexp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import cn.flyexp.constants.Config;
import cn.flyexp.framework.ControllerManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.WindowManager;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;

public class MainActivity extends AppCompatActivity {

    private static MainActivity activity;
    private static Tencent tencent;
    private static IWXAPI wxapi;

    public static MainActivity getActivity() {
        return activity;
    }

    public static Tencent getTencent() {
        return tencent;
    }

    public static IWXAPI getWxapi() {
        return wxapi;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setStatusBar();
        initQQApi();
        initWXApi();
        initWindow();
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initQQApi() {
        tencent = Tencent.createInstance(Config.QQAPI_APPID, this.getApplicationContext());
    }

    private void initWXApi() {
        wxapi = WXAPIFactory.createWXAPI(this.getApplicationContext(), Config.WXAPI_APPID);
        wxapi.registerApp(Config.WXAPI_APPID);
    }

    private void initWindow() {
        Bundle pushBundle = getIntent().getBundleExtra("pushbunble");
        if (pushBundle != null) {
            SharePresUtil.putInt(SharePresUtil.KEY_WINDOW_ID, pushBundle.getInt("windowid"));
            SharePresUtil.putString(SharePresUtil.KEY_PUSH_DATA, pushBundle.getString("pushdata"));
        }
        ControllerManager.getInstance().sendMessage(WindowIDDefine.WINDOW_SPLASH);
    }

    @Override
    protected void onResume() {
        super.onResume();
        WindowManager.getInstance(this).onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        WindowManager.getInstance(this).onStop();
    }

    @Override
    public void onBackPressed() {
        WindowManager.getInstance(this).onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (tencent != null) {
            tencent.onActivityResult(requestCode, resultCode, data);
        }
    }
}
