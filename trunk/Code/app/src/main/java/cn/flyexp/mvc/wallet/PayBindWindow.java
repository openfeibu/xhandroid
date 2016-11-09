package cn.flyexp.mvc.wallet;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.flyexp.R;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.mine.MineViewCallBack;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class PayBindWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private WalletViewCallBack callBack;
    private EditText et_account;
    private EditText et_name;
    private Button btn_bind;

    public PayBindWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_bindalipay);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_bind = (Button) findViewById(R.id.btn_bind);
        btn_bind.setOnClickListener(this);

        et_account = (EditText) findViewById(R.id.et_account);
        et_name = (EditText) findViewById(R.id.et_name);
        et_account.addTextChangedListener(this);
        et_name.addTextChangedListener(this);
    }

    public void response() {
        btn_bind.setEnabled(true);
        dismissProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_bind:
                String account = et_account.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                BindAlipayRequest bindAlipayRequest = new BindAlipayRequest();
                bindAlipayRequest.setToken(token);
                bindAlipayRequest.setAlipay(account);
                bindAlipayRequest.setAlipay_name(name);
                callBack.bindAlipay(bindAlipayRequest);
                v.setEnabled(false);
                showProgressDialog("正在绑定...");
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String account = et_account.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        if (account.equals("") || name.equals("")) {
            btn_bind.setAlpha(0.5f);
            btn_bind.setEnabled(false);
        } else {
            btn_bind.setAlpha(1f);
            btn_bind.setEnabled(true);
        }
    }
}
