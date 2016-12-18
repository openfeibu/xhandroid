package cn.flyexp.window.user;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.ResetPwdVercodeCallback;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.ResetPwdResponse;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.presenter.user.ResetPwdVercodePresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.view.VerCodeView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/12/2.
 */
public class ResetPwdVercodeWindow extends BaseWindow implements TextWatcher, ResetPwdVercodeCallback.ResponseCallback {

    @InjectView(R.id.btn_finish)
    Button btnFinish;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_countdown)
    TextView tvCountDown;
    @InjectView(R.id.layout_renewget)
    View renewGetLayout;
    @InjectView(R.id.view_vercode)
    VerCodeView viewVerCode;

    private String moblieNo;
    private ResetPwdVercodePresenter resetPwdPresenter;
    private ResetPwdRequest resetPwdRequest;
    private SweetAlertDialog loadingDialog;
    private String verCode;

    @Override
    protected int getLayoutId() {
        return R.layout.window_resetpwd_vercode;
    }

    public ResetPwdVercodeWindow(Bundle bundle) {
        resetPwdRequest = (ResetPwdRequest) bundle.getSerializable("resetPwdRequest");
        moblieNo = resetPwdRequest.getMobile_no();
        resetPwdPresenter = new ResetPwdVercodePresenter(this);
        initView();
        readySmscode();
    }

    private void initView() {
        loadingDialog = DialogHelper.getProgressDialog(getContext(), "");
        tvPhone.setText(String.format(getResources().getString(R.string.format_register_vercode_to), moblieNo));
        viewVerCode.addTextChangedListener(this);
    }

    private void readySmscode() {
        resetPwdPresenter.requestVercode(new SmsCodeRequest(moblieNo));
        countDown();
    }

    private void countDown() {
        new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText(String.format(getResources().getString(R.string.format_vercode_renew_get), String.valueOf(millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                tvCountDown.setVisibility(GONE);
                renewGetLayout.setVisibility(VISIBLE);
            }
        }.start();
    }

    @OnClick({R.id.img_back, R.id.tv_renewget, R.id.btn_finish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_renewget:
                tvCountDown.setVisibility(VISIBLE);
                renewGetLayout.setVisibility(GONE);
                readySmscode();
                break;
            case R.id.btn_finish:
                resetPwdRequest.setSms_code(verCode);
                resetPwdPresenter.requestResetPwd(resetPwdRequest);
                loadingDialog.show();
                break;
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void responseResetPwd(ResetPwdResponse response) {
        showToast(R.string.reset_success);
        hideWindow(true);
    }

    @Override
    public void responseVercodeSuccess() {
        showToast(R.string.send_sms_code_success);
    }

    @Override
    public void responseVercodeFailure() {
        showToast(R.string.send_sms_code_failure);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        verCode = viewVerCode.getText().toString().trim();
        if (verCode.length() == 4) {
            btnFinish.setEnabled(true);
        } else {
            btnFinish.setEnabled(false);
        }
    }

}
