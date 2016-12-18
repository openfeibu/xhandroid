package cn.flyexp.window.user;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/12/2.
 */
public class RegisterWindow extends BaseWindow implements NotifyManager.Notify, TextWatcher {

    @InjectView(R.id.img_avatar)
    ImageView imgAvatar;
    @InjectView(R.id.img_man)
    ImageView imgMan;
    @InjectView(R.id.img_women)
    ImageView imgWomen;
    @InjectView(R.id.edt_nickname)
    EditText edtNickname;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_password)
    EditText edtPassword;
    @InjectView(R.id.btn_next)
    Button btnNext;
    private String nickName;
    private String phone;
    private String pwd;
    private int gender;
    private String imgPath;

    @Override
    protected int getLayoutId() {
        return R.layout.window_register;
    }

    public RegisterWindow() {
        initView();
        getNotifyManager().register(NotifyIDDefine.NOTIFY_GALLERY, this);
    }

    private void initView() {
        imgMan.setSelected(true);
        gender = 1;
        edtNickname.addTextChangedListener(this);
        edtPhone.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.img_avatar, R.id.img_man, R.id.img_women, R.id.btn_next, R.id.tv_services_protocols, R.id.tv_privacy_policy})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.img_avatar:
                openGallery();
                break;
            case R.id.img_man:
                gender = 1;
                imgMan.setSelected(true);
                imgWomen.setSelected(false);
                break;
            case R.id.img_women:
                imgMan.setSelected(false);
                imgWomen.setSelected(true);
                break;
            case R.id.btn_next:
                readyRegister();
                break;
            case R.id.tv_services_protocols:
                WebBean protocolsBean = new WebBean();
                protocolsBean.setRequest(true);
                protocolsBean.setTitle(getResources().getString(R.string.user_agreement));
                protocolsBean.setName("userAgreement");
                openWebWindow(protocolsBean);
                break;
            case R.id.tv_privacy_policy:
                WebBean policyBean = new WebBean();
                policyBean.setRequest(true);
                policyBean.setTitle(getResources().getString(R.string.user_agreement));
                policyBean.setName("privacyPolicy");
                openWebWindow(policyBean);
                break;
        }
    }

    private void openGallery() {
        Bundle bundle = new Bundle();
        bundle.putInt("max", 1);
        openWindow(WindowIDDefine.WINDOW_GALLERY, bundle);
    }

    private void openWebWindow(WebBean webBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("webbean", webBean);
        openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
    }

    private void readyRegister() {
        if (checkPhoneAndPwd()) {
            return;
        }
        if (TextUtils.isEmpty(imgPath)) {
            showToast(R.string.hint_upload_avatar);
            return;
        }
        RegisterRequest registerRequest = new RegisterRequest();
        String deviceToken = SharePresUtil.getString(SharePresUtil.KEY_DEVICE_TOKEN);
        String push = SharePresUtil.getString(SharePresUtil.KEY_PUSH_TYPE);
        registerRequest.setPlatform("and");
        registerRequest.setMobile_no(phone);
        registerRequest.setNickname(nickName);
        registerRequest.setPassword(EncodeUtil.md5Encode(pwd));
        registerRequest.setPush_server(push);
        registerRequest.setDevice_token(TextUtils.isEmpty(deviceToken) ? "0" : deviceToken);
        registerRequest.setGender(gender);
        Bundle bundle = new Bundle();
        bundle.putSerializable("registerRequest", registerRequest);
        bundle.putString("imgpath", imgPath);
        openWindow(WindowIDDefine.WINDOW_REGISTER_VERCODE, bundle);
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
        nickName = edtNickname.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        pwd = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_GALLERY) {
            Bundle bundle = mes.getData();
            ArrayList<String> images = bundle.getStringArrayList("images");
            imgPath = images.get(0);
            Glide.with(getContext()).load(imgPath).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imgAvatar);
        }
    }
}
