package cn.flyexp.mvc.wallet;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.mine.MineViewCallBack;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;


/**
 * Created by txy on 2016/8/24 0024.
 */
public class VerifiPayPwdWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private WalletViewCallBack callBack;
    private EditText et_pwd;
    private int requestCode;
    private Button btn_confirm;

    public VerifiPayPwdWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_verifipaypwd);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd.addTextChangedListener(this);
    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return false;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_confirm:
                String pwd = et_pwd.getText().toString().trim();
                if (pwd.equals("")) {
                    WindowHelper.showToast("请输入支付密码");
                    return;
                }
                if (pwd.length() != 6) {
                    WindowHelper.showToast("请完整输入支付密码");
                    return;
                }
                callBack.payPwdResult(CommonUtil.md5(pwd), requestCode);
                hideWindow(true);
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
        String pwd = et_pwd.getText().toString().trim();
        if (pwd.length() != 6) {
            btn_confirm.setEnabled(false);
            btn_confirm.setAlpha(0.5f);
        } else {
            btn_confirm.setEnabled(true);
            btn_confirm.setAlpha(1f);
        }
    }
}
