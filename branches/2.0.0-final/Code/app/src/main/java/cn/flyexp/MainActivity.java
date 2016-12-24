package cn.flyexp;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tencent.tinker.lib.tinker.TinkerInstaller;

import java.io.File;

import cn.flyexp.constants.Config;
import cn.flyexp.constants.Constants;
import cn.flyexp.framework.ControllerManager;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.WindowManager;
import cn.flyexp.permission.PermissionInterceptor;
import cn.flyexp.push.PushUtil;
import cn.flyexp.push.XMPush;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;

public class MainActivity extends AppCompatActivity {

    private static Context context;
    private static Tencent tencent;
    private static IWXAPI wxapi;

    public static Context getContext() {
        return context;
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
        context = this;
        setStatusBar();
        initQQApi();
        initWXApi();
        initWindow();
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.black));
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200) {
            PermissionInterceptor.requestResult(permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (tencent != null) {
            tencent.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.CAMERA_RESULT) {
                NotifyManager.getInstance().notify(NotifyIDDefine.NOTIFY_CAMERA_RESULT);
            } else if (requestCode == Constants.WEBVIEW_FILE_CHOOSER_RESULT) {
                Bundle bundle = new Bundle();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    bundle.putParcelable("clipData", data.getClipData());
                }
                bundle.putString("dataString", data.getDataString());
                Message mes = Message.obtain();
                mes.what = NotifyIDDefine.NOTIFY_WEBVIEW_FILE_CHOOSER_RESULT;
                mes.setData(bundle);
                NotifyManager.getInstance().notify(mes);
            }
        }
    }
}
