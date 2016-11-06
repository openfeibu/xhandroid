package cn.flyexp.mvc.mine;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.XGPush;
import cn.flyexp.util.DataCleanManager;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class SettingWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private TextView tv_size;
    private Switch switchNotifiy;
    private boolean isNotifiy;

    public SettingWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_setting);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.layout_clear).setOnClickListener(this);
        findViewById(R.id.layout_feedbeak).setOnClickListener(this);
        findViewById(R.id.layout_faq).setOnClickListener(this);
        findViewById(R.id.layout_about).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);

        switchNotifiy = (Switch) findViewById(R.id.switchNotifiy);
        switchNotifiy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchNotifiy.setChecked(isChecked);
                isNotifiy = isChecked;
            }
        });
        boolean notifiy = getBooleanByPreference("notifiy");
        switchNotifiy.setChecked(notifiy);
        isNotifiy = notifiy;

        tv_size = (TextView) findViewById(R.id.tv_size);
        String size = DataCleanManager.getTotalCacheSize(getContext());
        if (!size.equals("")) {
            tv_size.setText(size);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        putBooleanByPreference("notifiy", isNotifiy);
        if (isNotifiy) {
            String openId = getStringByPreference("mine_openid");
            XGPush.registerPush(openId);
        } else {
            XGPush.unregisterPush();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.layout_about:
                callBack.aboutEnter();
                break;
            case R.id.layout_clear:
                DataCleanManager.clearAllCache(getContext());
                String size = DataCleanManager.getTotalCacheSize(getContext());
                if (!size.equals("")) {
                    tv_size.setText(size);
                }

                break;
            case R.id.layout_feedbeak:
                callBack.feedbeakEnter();
                break;
            case R.id.layout_faq:
                callBack.webWindowEnter(new String[]{"faq"}, 0);
                break;
            case R.id.btn_logout:
                String token = getStringByPreference("token");
                LogoutRequest logoutRequest = new LogoutRequest(token);
                callBack.logout(logoutRequest);
                break;
        }
    }
}
