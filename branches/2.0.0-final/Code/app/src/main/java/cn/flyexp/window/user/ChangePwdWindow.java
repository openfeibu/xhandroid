package cn.flyexp.window.user;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.ChangePwdCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.presenter.user.ChangePwdPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/29.
 */
public class ChangePwdWindow extends BaseWindow implements TextWatcher, ChangePwdCallback.ResponseCallback {

    @InjectView(R.id.edt_oldpwd)
    EditText edtOldPwd;
    @InjectView(R.id.edt_newpwd)
    EditText edtNewPwd;
    @InjectView(R.id.btn_change)
    Button btnChange;

    private String oldPwd;
    private String newPwd;
    private ChangePwdPresenter changePwdPresenter;
    private SweetAlertDialog progressDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_changepwd;
    }

    public ChangePwdWindow() {
        changePwdPresenter = new ChangePwdPresenter(this);
        initView();
    }

    private void initView() {
        edtOldPwd.addTextChangedListener(this);
        edtNewPwd.addTextChangedListener(this);
        progressDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
    }

    @OnClick({R.id.img_back, R.id.btn_change})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_change:
                readyChangePwd();
                break;
        }
    }

    private void readyChangePwd() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            ChangePwdRequest changePwdRequest = new ChangePwdRequest();
            changePwdRequest.setToken(token);
            changePwdRequest.setPassword(oldPwd);
            changePwdRequest.setNew_password(newPwd);
            changePwdPresenter.requestChangePwd(changePwdRequest);
        }
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
        newPwd = edtNewPwd.getText().toString().trim();
        if (TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(newPwd)) {
            btnChange.setEnabled(false);
            btnChange.setAlpha(0.5f);
        } else {
            btnChange.setEnabled(true);
            btnChange.setAlpha(1f);
        }
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(progressDialog);
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(progressDialog);
    }

    @Override
    public void responseChangePwd(BaseResponse response) {
        showToast(R.string.hint_change_pwd_success);
        hideWindow(true);
    }

}
