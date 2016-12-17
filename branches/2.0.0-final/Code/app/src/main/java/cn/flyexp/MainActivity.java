package cn.flyexp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageHelper;

import cn.flyexp.constants.Config;
import cn.flyexp.constants.Constants;
import cn.flyexp.framework.ControllerManager;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.WindowManager;
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

    public void getPushData() {
        MiPushMessage message = (MiPushMessage) getIntent().getSerializableExtra(PushMessageHelper.KEY_MESSAGE);
        if(message==null){
            LogUtil.e("mes null");
        }
        if (message != null && !TextUtils.isEmpty(message.getContent())) {
            String content = message.getContent();
            LogUtil.e("mes activity", content);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPushData();
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
        if (requestCode == Constants.CAMERA_RESULT && resultCode == RESULT_OK) {
            LogUtil.e("camera ok");
            NotifyManager.getInstance().notify(NotifyIDDefine.NOTIFY_CAMERA_RESULT);
        }
    }
}
