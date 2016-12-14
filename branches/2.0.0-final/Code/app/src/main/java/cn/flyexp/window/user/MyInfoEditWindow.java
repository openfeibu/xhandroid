package cn.flyexp.window.user;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.MyInfoEditCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.presenter.user.MyInfoEditPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/23.
 */
public class MyInfoEditWindow extends BaseWindow implements TextWatcher, MyInfoEditCallback.ResponseCallback {

    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.edt_nickname)
    EditText edtNickname;
    @InjectView(R.id.edt_profile)
    EditText edtProfile;
    @InjectView(R.id.edt_address)
    EditText edtAddress;
    @InjectView(R.id.img_man)
    ImageView imgMan;
    @InjectView(R.id.img_woman)
    ImageView imgWoman;

    private MyInfoResponse.MyInfoResponseData data;
    private String nickName;
    private String profile;
    private String address;
    private int gender = 1;
    private MyInfoEditPresenter myInfoEditPresenter;
    private SweetAlertDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_myinfo_edit;
    }

    public MyInfoEditWindow(Bundle bundle) {
        data = (MyInfoResponse.MyInfoResponseData) bundle.getSerializable("myinfo");
        myInfoEditPresenter = new MyInfoEditPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));
        initView();
    }

    private void initView() {
        if (data.getGender() == 1) {
            imgWoman.setAlpha(0.5f);
            imgMan.setAlpha(1f);
            gender = 1;
        } else if (data.getGender() == 2) {
            imgWoman.setAlpha(1f);
            imgMan.setAlpha(0.5f);
            gender = 2;
        }
        edtNickname.setText(data.getNickname());
        edtProfile.setText(data.getIntroduction());
        edtAddress.setText(data.getAddress());

        edtNickname.addTextChangedListener(this);
        edtProfile.addTextChangedListener(this);
        edtAddress.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.img_man, R.id.img_woman, R.id.tv_save})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.img_man:
                imgWoman.setAlpha(0.5f);
                imgMan.setAlpha(1f);
                gender = 1;
                checkEnabled();
                break;
            case R.id.img_woman:
                imgWoman.setAlpha(1f);
                imgMan.setAlpha(0.5f);
                gender = 2;
                checkEnabled();
                break;
            case R.id.tv_save:
                readyChangeMyInfo();
                break;
        }
    }

    private void readyChangeMyInfo() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            ChangeMyInfoRequest changeMyInfoRequest = new ChangeMyInfoRequest();
            changeMyInfoRequest.setToken(token);
            changeMyInfoRequest.setAddress(address);
            changeMyInfoRequest.setNickname(nickName);
            changeMyInfoRequest.setIntroduction(profile);
            changeMyInfoRequest.setGender(gender);
            myInfoEditPresenter.requestChangeMyInfo(changeMyInfoRequest);
            loadingDialog.show();
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
    public void responseChangeMyInfo(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
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
        nickName = edtNickname.getText().toString().trim();
        profile = edtProfile.getText().toString().trim();
        address = edtAddress.getText().toString().trim();
        checkEnabled();
    }

    private void checkEnabled() {
        if (TextUtils.isEmpty(nickName)) {
            tvSave.setEnabled(false);
            tvSave.setAlpha(0.5f);
        } else {
            if (TextUtils.equals(nickName, data.getNickname())
                    && TextUtils.equals(profile, data.getIntroduction())
                    && TextUtils.equals(address, data.getAddress())
                    && gender == data.getGender()) {
                tvSave.setEnabled(false);
                tvSave.setAlpha(0.5f);
            } else {
                tvSave.setEnabled(true);
                tvSave.setAlpha(1);
            }
        }
    }
}
