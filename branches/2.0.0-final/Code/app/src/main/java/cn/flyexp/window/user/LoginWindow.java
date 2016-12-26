package cn.flyexp.window.user;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.LoginCallback;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.TokenResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.user.LoginPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/12/2.
 */
public class LoginWindow extends BaseWindow implements TextWatcher, LoginCallback.ResponseCallback {

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_password)
    EditText edtPassword;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    private String pwd;
    private String phone;
    private SweetAlertDialog loginDialog;
    private LoginPresenter loginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_login;
    }

    public LoginWindow() {
        loginPresenter = new LoginPresenter(this);
        Config config = new Config();
        config.setAnimStyle(config.ANIMSTYLE_SLIDE);
        setConfig(config);
        initView();
        readDefault();
    }

    private void initView() {
        loginDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.logining));
        edtPhone.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
        edtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    readyLogin();
                }
                return false;
            }
        });
    }

    private void readDefault() {
        String lastPhone = SharePresUtil.getString(SharePresUtil.KEY_LAST_PHONE);
        String lastAvatar = SharePresUtil.getString(SharePresUtil.KEY_LAST_AVATAR);
        if (!TextUtils.isEmpty(lastPhone)) {
            edtPhone.setText(lastPhone);
        }
        if (!TextUtils.isEmpty(lastAvatar)) {
            Glide.with(getContext()).load(lastAvatar).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgAvatar);
        }
    }

    @OnClick({R.id.img_close, R.id.btn_login, R.id.tv_register, R.id.tv_resetpwd})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_close:
                hideWindow(true);
                break;
            case R.id.btn_login:
                readyLogin();
                break;
            case R.id.tv_register:
                openWindow(WindowIDDefine.WINDOW_REGISTER);
                break;
            case R.id.tv_resetpwd:
                openWindow(WindowIDDefine.WINDOW_RESETPWD);
                break;
        }
    }

    private void readyLogin() {
        if (checkPhoneAndPwd()) {
            return;
        }
        LoginRequest loginRequest = new LoginRequest();
        String mid = SharePresUtil.getString(SharePresUtil.KEY_MID);
        String deviceToken = SharePresUtil.getString(SharePresUtil.KEY_DEVICE_TOKEN);
        String push = SharePresUtil.getString(SharePresUtil.KEY_PUSH_TYPE);
        LogUtil.e("push"+push);
        loginRequest.setPlatform("and");
        loginRequest.setMid(mid);
        loginRequest.setMobile_no(phone);
        loginRequest.setPassword(EncodeUtil.md5Encode(pwd));
        loginRequest.setPush_server(push);
        loginRequest.setDevice_token(TextUtils.isEmpty(deviceToken) ? "0" : deviceToken);
        loginRequest.setVerify_code("0");
        loginPresenter.requestLogin(loginRequest);
        loginDialog.show();
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

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            btnLogin.setEnabled(false);
        } else {
            btnLogin.setEnabled(true);
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(loginDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(loginDialog);
    }

    @Override
    public void responseLogin(TokenResponse response) {
        SharePresUtil.putString(SharePresUtil.KEY_TOKEN, response.getToken());
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
        hideWindow(true);
    }
}
