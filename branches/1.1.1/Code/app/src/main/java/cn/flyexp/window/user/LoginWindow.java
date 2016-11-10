package cn.flyexp.window.user;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.LoginCallback;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.window.BaseWindow;
import cn.flyexp.presenter.user.LoginPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class LoginWindow extends BaseWindow implements LoginCallback.ResponseCallback, TextWatcher {

    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_password)
    EditText edtPassword;
    @InjectView(R.id.edt_nickname)
    EditText edtNickname;
    @InjectView(R.id.img_man)
    ImageView imgMan;
    @InjectView(R.id.img_woman)
    ImageView imgWoman;
    @InjectView(R.id.tv_login)
    TextView tvLogin;
    @InjectView(R.id.registerlayout)
    View registerlayout;
    @InjectView(R.id.bottomlayout)
    View bottomlayout;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.btn_register)
    Button btnRegister;
    @InjectView(R.id.til_phone)
    TextInputLayout tilPhone;
    @InjectView(R.id.til_pwd)
    TextInputLayout tilPwd;

    private int duration = 200;
    private boolean isLogin = true;
    private LoginPresenter loginPresenter;
    private int gender;
    private View loginVercodeLayout;
    private AlertDialog loginVercodeDialog;
    private View registerVercodeLayout;
    private AlertDialog registerVercodeDialog;
    private String phone;
    private String pwd;
    private String nickName;
    private boolean needVercode;
    private LoginRequest loginRequest = new LoginRequest();
    private RegisterRequest registerRequest = new RegisterRequest();
    private SweetAlertDialog loginDialog;
    private ImageView imgVercode;
    private SweetAlertDialog registerDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_login;
    }

    public LoginWindow() {
        loginPresenter = new LoginPresenter(this);
        initView();
    }

    private void initView() {
        loginVercodeLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loginvercode, null);
        registerVercodeLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_registervercode, null);
        tvLogin.setTranslationY(tvLogin.getY() + 80);
        registerlayout.setTranslationY(registerlayout.getY() + 240);
        edtPhone.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
        edtNickname.addTextChangedListener(this);
        tilPhone.setHint(getResources().getString(R.string.hint_phone_number));
        tilPwd.setHint(getResources().getString(R.string.hint_password));
        loginDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.logining));
        registerDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.registering));
    }

    @OnClick({R.id.img_back, R.id.btn_login, R.id.btn_register, R.id.tv_resetpwd, R.id.tv_register,
            R.id.tv_login, R.id.img_man, R.id.img_woman})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_login:
                readyLogin();
                break;
            case R.id.btn_register:
                readyRegister();
                break;
            case R.id.tv_resetpwd:
                openWindow(WindowIDDefine.WINDOW_RESETPWD);
                break;
            case R.id.img_man:
                if (isLogin) {
                    return;
                }
                gender = 1;
                imgMan.setAlpha(1f);
                imgWoman.setAlpha(0.2f);
                break;
            case R.id.img_woman:
                if (isLogin) {
                    return;
                }
                gender = 2;
                imgMan.setAlpha(0.2f);
                imgWoman.setAlpha(1f);
                break;
            case R.id.tv_login:
                toLogin();
                break;
            case R.id.tv_register:
                toRegister();
                break;
        }
    }

    private boolean checkPhoneAndPwd() {
        if (!PatternUtil.validatePhone(phone)) {
            showToast(R.string.phone_format_illegal);
            return true;
        }
        if (pwd.length() > 16 || pwd.length() < 6) {
            showToast(R.string.pwd_length_illegal);
            return true;
        }
        if (!PatternUtil.validateUserPwd(pwd)) {
            showToast(R.string.pwd_format_illegal);
            return true;
        }
        return false;
    }

    private void readyLogin() {
        if (checkPhoneAndPwd()) {
            return;
        }
        String mid = SharePresUtil.getString(SharePresUtil.KEY_MID);
        String deviceToken = SharePresUtil.getString(SharePresUtil.KEY_DEVICE_TOKEN);
        String push = SharePresUtil.getString(SharePresUtil.KEY_PUSH_TYPE);
        loginRequest.setPlatform("and");
        loginRequest.setMid(mid);
        loginRequest.setMobile_no(phone);
        loginRequest.setPassword(EncodeUtil.md5Encode(pwd));
        loginRequest.setPush_server(push);
        loginRequest.setDevice_token(TextUtils.isEmpty(deviceToken) ? "0" : deviceToken);
        if (needVercode) {
            showLogionVercodeDialog();
        } else {
            loginRequest.setVerify_code("0");
            loginPresenter.requestLogin(loginRequest);
            loginDialog.show();
        }
    }

    private void readyRegister() {
        if (checkPhoneAndPwd()) {
            return;
        }
        String deviceToken = SharePresUtil.getString(SharePresUtil.KEY_DEVICE_TOKEN);
        String push = SharePresUtil.getString(SharePresUtil.KEY_PUSH_TYPE);
        registerRequest.setPlatform("and");
        registerRequest.setMobile_no(phone);
        registerRequest.setNickname(nickName);
        registerRequest.setPassword(EncodeUtil.md5Encode(pwd));
        registerRequest.setPush_server(push);
        registerRequest.setDevice_token(TextUtils.isEmpty(deviceToken) ? "0" : deviceToken);
        registerRequest.setGender(gender);
        showRegisterVercodeDialog();
    }

    private void showLogionVercodeDialog() {
        if (loginVercodeDialog == null) {
            loginVercodeDialog = new AlertDialog.Builder(getContext()).setView(loginVercodeLayout).create();
            final EditText edtVercode = (EditText) loginVercodeDialog.findViewById(R.id.edt_vercode);
            imgVercode = (ImageView) loginVercodeDialog.findViewById(R.id.img_vercode);
            imgVercode.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginPresenter.requestImgVercode();
                }
            });
            loginVercodeDialog.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String vercode = edtVercode.getText().toString().trim();
                    loginRequest.setVerify_code(vercode);
                    loginPresenter.requestLogin(loginRequest);
                    loginVercodeDialog.dismiss();
                    loginDialog.show();
                }
            });
        }
        loginVercodeDialog.show();
    }

    private void showRegisterVercodeDialog() {
        if (registerVercodeDialog == null) {
            registerVercodeDialog = new AlertDialog.Builder(getContext()).setView(registerVercodeLayout).create();
            final EditText edtVercode = (EditText) registerVercodeDialog.findViewById(R.id.edt_vercode);
            registerVercodeDialog.findViewById(R.id.tv_agreement).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebBean webBean = new WebBean();
                    webBean.setRequest(true);
                    webBean.setTitle(getResources().getString(R.string.user_agreement));
                    webBean.setName("userAgreement");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("webbean", webBean);
                    openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
                    registerVercodeDialog.dismiss();
                }
            });
            registerVercodeDialog.findViewById(R.id.tv_getvercode).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginPresenter.requestSmscode(new SmsCodeRequest(phone));
                    registerVercodeDialog.dismiss();
                }
            });
            registerVercodeDialog.findViewById(R.id.btn_confirm).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String verCode = edtVercode.getText().toString().trim();
                    registerRequest.setSms_code(verCode);
                    loginPresenter.requestRegister(registerRequest);
                    registerVercodeDialog.dismiss();
                    registerDialog.show();
                }
            });
        }
        registerVercodeDialog.show();
    }

    private void toLogin() {
        isLogin = true;
        ObjectAnimator.ofFloat(bottomlayout, "y", bottomlayout.getY(), bottomlayout.getY() - 80).setDuration(duration).start();
        ObjectAnimator.ofFloat(tvLogin, "y", tvLogin.getY(), tvLogin.getY() + 80).setDuration(duration).start();
        ObjectAnimator.ofFloat(registerlayout, "y", registerlayout.getY(), registerlayout.getY() + 240).setDuration(duration).start();
        ObjectAnimator.ofFloat(btnLogin, "y", btnLogin.getY(), btnLogin.getY() + 240).setDuration(duration).start();
        ObjectAnimator.ofFloat(bottomlayout, "alpha", 0, 1).setDuration(duration).start();
        ObjectAnimator.ofFloat(tvLogin, "alpha", 1, 0).setDuration(duration).start();
        ObjectAnimator.ofFloat(registerlayout, "alpha", 1, 0).setDuration(duration).start();
        ObjectAnimator.ofFloat(btnLogin, "alpha", 0, 1).setDuration(duration).start();
    }

    private void toRegister() {
        isLogin = false;
        ObjectAnimator.ofFloat(bottomlayout, "y", bottomlayout.getY(), bottomlayout.getY() + 80).setDuration(duration).start();
        ObjectAnimator.ofFloat(tvLogin, "y", tvLogin.getY(), tvLogin.getY() - 80).setDuration(duration).start();
        ObjectAnimator.ofFloat(registerlayout, "y", registerlayout.getY(), registerlayout.getY() - 240).setDuration(duration).start();
        ObjectAnimator.ofFloat(btnLogin, "y", btnLogin.getY(), btnLogin.getY() - 240).setDuration(duration).start();
        ObjectAnimator.ofFloat(bottomlayout, "alpha", 1, 0).setDuration(duration).start();
        ObjectAnimator.ofFloat(tvLogin, "alpha", 0, 1).setDuration(duration).start();
        ObjectAnimator.ofFloat(registerlayout, "alpha", 0, 1).setDuration(duration).start();
        ObjectAnimator.ofFloat(btnLogin, "alpha", 1, 0).setDuration(duration).start();
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(loginDialog);
        dismissProgressDialog(registerDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(loginDialog);
        dismissProgressDialog(registerDialog);
    }

    @Override
    public void responseLogin(TokenResponse response) {
        SharePresUtil.putString(SharePresUtil.KEY_TOKEN, response.getToken());
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
        hideWindow(true);
    }

    @Override
    public void responseRegister(TokenResponse response) {
        showToast(getResources().getString(R.string.welcome_join));
        SharePresUtil.putString(SharePresUtil.KEY_TOKEN, response.getToken());
        hideWindow(true);
    }

    @Override
    public void responseImgVerCode(ImgUrlResponse response) {
        if (imgVercode != null) {
            Glide.with(getContext()).load(response.getUrl()).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imgVercode);
        }
    }

    @Override
    public void vertifyLogin() {
        needVercode = true;
        loginPresenter.requestImgVercode();
        showLogionVercodeDialog();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        phone = edtPhone.getText().toString().trim();
        pwd = edtPassword.getText().toString().trim();
        nickName = edtNickname.getText().toString().trim();
        if (isLogin) {
            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
                btnLogin.setAlpha(0.5f);
                btnLogin.setEnabled(false);
            } else {
                btnLogin.setAlpha(1);
                btnLogin.setEnabled(true);
            }
        } else {
            if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(nickName)) {
                btnRegister.setAlpha(0.5f);
                btnRegister.setEnabled(false);
            } else {
                btnRegister.setAlpha(1);
                btnRegister.setEnabled(true);
            }
        }
    }

}
