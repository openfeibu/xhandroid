package cn.flyexp.window.assn;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.assn.AssnJoinCallback;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.presenter.assn.AssnJoinPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class AssnJoinWindow extends BaseWindow implements TextWatcher, AssnJoinCallback.ResponseCallback {

    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_pro)
    EditText edtPro;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_cause)
    EditText edtCause;
    @InjectView(R.id.btn_join)
    Button btnJoin;
    private AssnJoinPresenter assnJoinPresenter;
    private SweetAlertDialog assnJoinDialog;
    private int aid;
    private String cause;
    private String phone;
    private String pro;
    private String name;


    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_join;
    }

    public AssnJoinWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        assnJoinPresenter = new AssnJoinPresenter(this);
        assnJoinDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));

        edtName.addTextChangedListener(this);
        edtPro.addTextChangedListener(this);
        edtPhone.addTextChangedListener(this);
        edtCause.addTextChangedListener(this);

        String phone = SharePresUtil.getString(SharePresUtil.KEY_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            edtPhone.setText(phone);
        }
    }

    @OnClick({R.id.img_back, R.id.btn_join})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_join:
                readyAssnJoin();
                break;
        }
    }

    private void readyAssnJoin() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            if (!PatternUtil.validatePhone(phone)) {
                showToast(R.string.phone_format_illegal);
            } else {
                AssnJoinRequest assnJoinRequest = new AssnJoinRequest();
                assnJoinRequest.setAssociation_id(aid);
                assnJoinRequest.setToken(token);
                assnJoinRequest.setCauses(cause);
                assnJoinRequest.setMobile_no(phone);
                assnJoinRequest.setAr_username(name);
                assnJoinRequest.setProfession(pro);
                assnJoinPresenter.requestAssnJoin(assnJoinRequest);
                assnJoinDialog.show();
            }
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(assnJoinDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(assnJoinDialog);
    }

    @Override
    public void responseAssnJoin(BaseResponse response) {
        hideWindow(true);
        showToast(R.string.apply_success);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        name = edtName.getText().toString().trim();
        pro = edtPro.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        cause = edtCause.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pro) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(cause)) {
            btnJoin.setEnabled(false);
            btnJoin.setAlpha(0.5f);
        } else {
            btnJoin.setEnabled(true);
            btnJoin.setAlpha(1);
        }
    }
}
