package cn.flyexp.window.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.mine.SettingCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.mine.SettingPresenter;
import cn.flyexp.util.DataCleanHelper;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/28.
 */
public class SettingWindow extends BaseWindow implements SettingCallback.ResponseCallback {

    @InjectView(R.id.tv_cachesize)
    TextView cacheSizeTextView;
    private final SettingPresenter settingPresenter;
    private SweetAlertDialog logoutDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_setting;
    }

    public SettingWindow() {
        showCacheSize();
        settingPresenter = new SettingPresenter(this);
        logoutDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.logouting));
    }

    private void showCacheSize() {
        String size = DataCleanHelper.getTotalCacheSize(getContext());
        if (!TextUtils.isEmpty(size)) {
            cacheSizeTextView.setText(size);
        }
    }

    @OnClick({R.id.img_back, R.id.layout_about, R.id.layout_clear,R.id.layout_changepwd, R.id.layout_feedback, R.id.layout_faq, R.id.btn_logout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.layout_about:
                openWindow(WindowIDDefine.WINDOW_ABOUT);
                break;
            case R.id.layout_changepwd:
                openWindow(WindowIDDefine.WINDOW_CHANGEPWD);
                break;
            case R.id.layout_clear:
                DataCleanHelper.clearAllCache(getContext());
                showCacheSize();
                DialogHelper.showSuccessDialog(getContext(), getResources().getString(R.string.clear_success));
                break;
            case R.id.layout_feedback:
                Bundle feedbackBundler = new Bundle();
                feedbackBundler.putBoolean("isCrash", false);
                openWindow(WindowIDDefine.WINDOW_FEEDBACK, feedbackBundler);
                break;
            case R.id.layout_faq:
                WebBean webBean = new WebBean();
                webBean.setRequest(true);
                webBean.setTitle(getResources().getString(R.string.faq));
                webBean.setName("faq");
                Bundle bundle = new Bundle();
                bundle.putSerializable("webbean", webBean);
                openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
                break;
            case R.id.btn_logout:
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                } else {
                    settingPresenter.requestLogout(new TokenRequest(token));
                    logoutDialog.show();
                }
                break;
        }
    }

    @Override
    public void requestFailure() {
        logoutDialog.dismissWithAnimation();
    }

    @Override
    public void responseLogout(BaseResponse response) {
        logoutDialog.dismissWithAnimation();
        SharePresUtil.putString(SharePresUtil.KEY_TOKEN, "");
        getNotifyManager().notify(NotifyIDDefine.NOTICE_MAIN_HOME);
        hideWindow(true);
    }
}
