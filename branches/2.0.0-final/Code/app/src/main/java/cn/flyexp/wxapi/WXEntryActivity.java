package cn.flyexp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.constants.Config;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;

/**
 * Created by txy on 2016/8/19 0019.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.getContext() == null){
            finish();
            return;
        }
        api = WXAPIFactory.createWXAPI(this, Config.WXAPI_APPID, false);
        api.registerApp(Config.WXAPI_APPID);
        api.handleIntent(getIntent(), this);
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (MainActivity.getContext() == null){
            finish();
            return;
        }
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            Toast.makeText(this, getResources().getString(R.string.share_success), Toast.LENGTH_LONG).show();
            NotifyManager.getInstance().notify(NotifyIDDefine.NOTICE_SHARE);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);
    }

}
