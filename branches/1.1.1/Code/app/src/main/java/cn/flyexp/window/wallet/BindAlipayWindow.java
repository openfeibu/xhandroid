package cn.flyexp.window.wallet;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.BindPayCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.presenter.wallet.BindPayPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class BindAlipayWindow extends BaseWindow implements TextWatcher, BindPayCallback.ResponseCallback {

    @InjectView(R.id.edt_account)
    EditText edtAccount;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.btn_bind)
    Button btnBind;

    private BindPayPresenter bindPayPresenter;
    private final SweetAlertDialog bindPayDialog;
    private String account;
    private String name;

    @Override
    protected int getLayoutId() {
        return R.layout.window_bindalipay;
    }

    public BindAlipayWindow() {
        bindPayPresenter = new BindPayPresenter(this);
        edtAccount.addTextChangedListener(this);
        edtName.addTextChangedListener(this);
        bindPayDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.binding));
    }

    @OnClick({R.id.img_back, R.id.btn_bind})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_bind:
                readyBindPay();
                break;
        }
    }

    private void readyBindPay() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        BindAlipayRequest bindAlipayRequest = new BindAlipayRequest();
        bindAlipayRequest.setToken(token);
        bindAlipayRequest.setAlipay(account);
        bindAlipayRequest.setAlipay_name(name);
        bindPayPresenter.requestBindAlipay(bindAlipayRequest);
        bindPayDialog.show();
    }

    @Override
    public void requestFailure() {
        if(bindPayDialog.isShowing()){
            bindPayDialog.dismissWithAnimation();
        }
    }

    @Override
    public void requestFinish() {
        if(bindPayDialog.isShowing()){
            bindPayDialog.dismissWithAnimation();
        }
    }

    @Override
    public void responseBindAlipay(BaseResponse response) {
        showToast(R.string.bind_success);
        getNotifyManager().notify(NotifyIDDefine.NOTICE_WALLET);
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
        account = edtAccount.getText().toString().trim();
        name = edtName.getText().toString().trim();
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(account)) {
            btnBind.setEnabled(false);
            btnBind.setAlpha(0.5f);
        } else {
            btnBind.setEnabled(true);
            btnBind.setAlpha(1);
        }
    }
}
