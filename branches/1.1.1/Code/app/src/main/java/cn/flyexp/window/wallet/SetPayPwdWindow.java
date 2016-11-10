package cn.flyexp.window.wallet;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.SetPayPwdCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.presenter.wallet.SetPayPwdPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.PasswordView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class SetPayPwdWindow extends BaseWindow implements TextWatcher, SetPayPwdCallback.ResponseCallback {

    @InjectView(R.id.edt_pwd)
    PasswordView edtPwd;
    @InjectView(R.id.edt_pwd2)
    PasswordView edtPwd2;
    @InjectView(R.id.btn_set)
    Button btnSet;
    private SetPayPwdPresenter setPayPwdPresenter;
    private SweetAlertDialog setPayPwdDialog;
    private String pwd;
    private String pwd2;

    @Override
    protected int getLayoutId() {
        return R.layout.window_setpaypwd;
    }

    public SetPayPwdWindow() {
        setPayPwdPresenter = new SetPayPwdPresenter(this);
        setPayPwdDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
        edtPwd.addTextChangedListener(this);
        edtPwd2.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.btn_set})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_set:
                readySetPayPwd();
                break;
        }
    }

    private void readySetPayPwd() {
        if (pwd != pwd2) {
            showToast(R.string.twotimes_input_error);
            edtPwd.setText("");
            edtPwd2.setText("");
            return;
        }
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        SetPayPwdRequest setPayPwdRequest = new SetPayPwdRequest();
        setPayPwdRequest.setPay_password(EncodeUtil.md5Encode(pwd));
        setPayPwdRequest.setToken(token);
        setPayPwdPresenter.requestSetPayPwd(setPayPwdRequest);
        setPayPwdDialog.show();
    }

    @Override
    public void requestFailure() {
        if (setPayPwdDialog.isShowing()) {
            setPayPwdDialog.dismissWithAnimation();
        }
    }

    @Override
    public void requestFinish() {
        if (setPayPwdDialog.isShowing()) {
            setPayPwdDialog.dismissWithAnimation();
        }
    }

    @Override
    public void responseSetPayPwd(BaseResponse response) {
        showToast(R.string.set_success);
        getNotifyManager().notify(NotifyIDDefine.NOTICE_WALLET);
        SharePresUtil.putInt(SharePresUtil.KEY_SETPAYPWD, 1);
        hideWindow(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        pwd = edtPwd.getText().toString().trim();
        pwd2 = edtPwd2.getText().toString().trim();
        if (pwd.length() == 6 && pwd2.length() == 6) {
            btnSet.setAlpha(1);
            btnSet.setEnabled(true);
        } else {
            btnSet.setAlpha(0.5f);
            btnSet.setEnabled(false);
        }
    }
}
