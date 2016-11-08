package cn.flyexp.mvc.mine;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.flyexp.R;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;

/**
 * Created by txy on 2016/8/8 0008.
 */
public class ChangePwdWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private MineViewCallBack callBack;
    private EditText et_oldPwd;
    private EditText et_newPwd;
    private Button btn_change;

    public ChangePwdWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_changepwd);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);

        et_oldPwd = (EditText) findViewById(R.id.et_oldPwd);
        et_newPwd = (EditText) findViewById(R.id.et_newPwd);

        et_oldPwd.addTextChangedListener(this);
        et_newPwd.addTextChangedListener(this);
    }

    public void response() {
        btn_change.setEnabled(true);
        dismissProgressDialog();
    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return !(et_newPwd.isFocused() || et_oldPwd.isFocused());
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
                if (oldPwd.length() < 6 || oldPwd.length() > 16 || newPwd.length() < 6 || newPwd.length() > 16) {
                    WindowHelper.showToast("密码长度必须为6~12位");
                    return;
                }
                if (oldPwd.equals(newPwd)) {
                    WindowHelper.showToast("新旧密码不能一致");
                    return;
                }
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                ChangePwdRequest changePwdRequest = new ChangePwdRequest();
                changePwdRequest.setToken(token);
                changePwdRequest.setPassword(CommonUtil.md5(oldPwd));
                changePwdRequest.setNew_password(CommonUtil.md5(newPwd));
                callBack.changePwd(changePwdRequest);
                v.setEnabled(false);
                showProgressDialog("更换中...");
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
        if (oldPwd.equals("") || newPwd.equals("")) {
            btn_change.setAlpha(0.5f);
            btn_change.setEnabled(false);
        } else {
            btn_change.setAlpha(1f);
            btn_change.setEnabled(true);
        }
    }
}
