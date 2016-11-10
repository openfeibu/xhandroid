package cn.flyexp.window.wallet;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.ChangePayPwdCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.presenter.wallet.ChangePayPwdPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.PasswordView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class ChangePayPwdWindow extends BaseWindow implements TextWatcher, ChangePayPwdCallback.ResponseCallback {

    @InjectView(R.id.edt_oldpwd)
    PasswordView edtOldPwd;
    @InjectView(R.id.edt_newpwd)
    PasswordView edtNewPwd;
    @InjectView(R.id.btn_change)
    Button btnChange;
    private ChangePayPwdPresenter changePayPwdPresenter;
    private SweetAlertDialog changePayPwdDialog;
    private String oldPwd;
    private String newPWd;

    @Override
    protected int getLayoutId() {
        return R.layout.window_changepaypwd;
    }

    public ChangePayPwdWindow() {
        changePayPwdPresenter = new ChangePayPwdPresenter(this);
        changePayPwdDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
        edtOldPwd.addTextChangedListener(this);
        edtNewPwd.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.btn_change})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_change:
                readyChangePayPwd();
                break;
        }
    }

    private void readyChangePayPwd() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        ChangePayPwdRequest changePayPwdRequest = new ChangePayPwdRequest();
        changePayPwdRequest.setToken(token);
        changePayPwdRequest.setOld_paypassword(EncodeUtil.md5Encode(oldPwd));
        changePayPwdRequest.setNew_paypassword(EncodeUtil.md5Encode(newPWd));
        changePayPwdPresenter.requestChangePayPwd(changePayPwdRequest);
        changePayPwdDialog.show();
    }

    @Override
    public void requestFailure() {
        if(changePayPwdDialog.isShowing()){
            changePayPwdDialog.dismissWithAnimation();
        }
    }

    @Override
    public void requestFinish() {
        if(changePayPwdDialog.isShowing()){
            changePayPwdDialog.dismissWithAnimation();
        }
    }

    @Override
    public void responseChangePayPwd(BaseResponse response) {
        showToast(R.string.change_success);
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
        oldPwd = edtOldPwd.getText().toString().trim();
        newPWd = edtNewPwd.getText().toString().trim();
        if (oldPwd.length() == 6 && newPWd.length() == 6) {
            btnChange.setAlpha(1);
            btnChange.setEnabled(true);
        } else {
            btnChange.setAlpha(0.5f);
            btnChange.setEnabled(false);
        }
    }
}
