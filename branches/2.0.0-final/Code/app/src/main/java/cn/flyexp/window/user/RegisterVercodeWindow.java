package cn.flyexp.window.user;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.RegisterVercodeCallback;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.presenter.user.RegisterVercodePresenter;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.util.UploadFileHelper;
import cn.flyexp.view.VerCodeView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/12/2.
 */
public class RegisterVercodeWindow extends BaseWindow implements TextWatcher, RegisterVercodeCallback.ResponseCallback {

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
    private RegisterVercodePresenter registerPresenter;
    private RegisterRequest registerRequest;
    private SweetAlertDialog loadingDialog;
    private String verCode;
    private String imgpath;
    private CountDownTimer downTimer;

    @Override
    protected int getLayoutId() {
        return R.layout.window_register_vercode;
    }

    public RegisterVercodeWindow(Bundle bundle) {
        registerRequest = (RegisterRequest) bundle.getSerializable("registerRequest");
        imgpath = bundle.getString("imgpath");
        moblieNo = registerRequest.getMobile_no();
        registerPresenter = new RegisterVercodePresenter(this);
        initView();
        readySmscode();
        openKeybroad();
    }

    private void openKeybroad() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(viewVerCode, InputMethodManager.SHOW_FORCED);
    }

    private void initView() {
        loadingDialog = DialogHelper.getProgressDialog(getContext(), "");
        tvPhone.setText(String.format(getResources().getString(R.string.format_register_vercode_to), moblieNo));
        viewVerCode.addTextChangedListener(this);
    }

    private void readySmscode() {
        registerPresenter.requestSmscode(new SmsCodeRequest(moblieNo));
        countDown();
    }

    private void countDown() {
        if (downTimer != null) {
            downTimer.cancel();
        }
        downTimer = new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText(String.format(getResources().getString(R.string.format_vercode_renew_get), String.valueOf(millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                tvCountDown.setVisibility(GONE);
                renewGetLayout.setVisibility(VISIBLE);
            }
        };
        downTimer.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (downTimer != null) {
            downTimer.cancel();
        }
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
                registerRequest.setSms_code(verCode);
                readyUploadAvatar();
                loadingDialog.show();
                break;
        }
    }

    private void readyUploadAvatar() {
        ArrayList<File> files = new ArrayList<>();
        files.add(BitmapUtil.compressBmpToFile(imgpath));
        MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(files);
        registerPresenter.requestUploadAvatar(multipartBody);
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
    public void responseRegister(TokenResponse response) {
        SharePresUtil.putString(SharePresUtil.KEY_TOKEN, response.getToken());
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
        hideWindow(true);
    }

    @Override
    public void responseUploadAvatar(ImgUrlResponse response) {
        registerRequest.setAvatar_url(response.getUrl());
        registerPresenter.requestRegister(registerRequest);
    }

    @Override
    public void responseSmscodeSuccess() {
        showToast(R.string.send_sms_code_success);
    }

    @Override
    public void responseSmscodeFailure() {
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
