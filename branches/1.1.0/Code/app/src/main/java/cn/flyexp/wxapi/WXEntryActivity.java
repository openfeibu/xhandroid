package cn.flyexp.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/19 0019.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx13275568a3405957", false);
        api.registerApp("wx13275568a3405957");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
        }
        this.getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {//完全接管事件
        return true;
    }

}
