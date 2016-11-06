package cn.flyexp.mvc.wallet;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.flyexp.R;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.mine.MineViewCallBack;
import cn.flyexp.util.CommonUtil;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class ChangePayPwdWindow extends AbstractWindow implements View.OnClickListener ,TextWatcher{

    private WalletViewCallBack callBack;
    private EditText et_oldPwd;
    private EditText et_newPwd;
    private Button btn_change;

    public ChangePayPwdWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_changepaypwd);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);

        et_oldPwd = (EditText) findViewById(R.id.et_oldPwd);
        et_newPwd = (EditText) findViewById(R.id.et_newPwd);

        et_oldPwd.requestFocus();

        et_oldPwd.addTextChangedListener(this);
        et_newPwd.addTextChangedListener(this);
    }

    public void response() {
        dismissProgressDialog();
        btn_change.setEnabled(true);
    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_change:
                String oldPwd = et_oldPwd.getText().toString().trim();
                String newPwd = et_newPwd.getText().toString().trim();
                if (oldPwd.length() + newPwd.length() != 12) {
                    WindowHelper.showToast("请输入完整");
                    return;
                }
                String token = WindowHelper. getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                ChangePayPwdRequest changePayPwdRequest = new ChangePayPwdRequest();
                changePayPwdRequest.setToken(token);
                changePayPwdRequest.setOld_paypassword(CommonUtil.md5(oldPwd));
                changePayPwdRequest.setNew_paypassword(CommonUtil.md5(newPwd));
                callBack.changePayPwd(changePayPwdRequest);
                v.setEnabled(false);
                showProgressDialog("正在更换...");
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
        String oldPwd = et_oldPwd.getText().toString().trim();
        String newPwd = et_newPwd.getText().toString().trim();
        if(oldPwd.equals("") || newPwd.equals("")){
            btn_change.setEnabled(false);
            btn_change.setAlpha(0.5f);
        }else{
            btn_change.setEnabled(true);
            btn_change.setAlpha(1f);
        }
    }
}
