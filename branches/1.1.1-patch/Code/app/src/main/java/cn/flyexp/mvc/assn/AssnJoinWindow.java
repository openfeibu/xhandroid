package cn.flyexp.mvc.assn;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;

/**
 * Created by tanxinye on 2016/10/1.
 */
public class AssnJoinWindow extends AbstractWindow implements View.OnClickListener , TextWatcher {

    private AssnViewCallBack callBack;
    private EditText et_name;
    private EditText et_pro;
    private EditText et_phone;
    private EditText et_cause;
    private int aid;

    public AssnJoinWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_join);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_join).setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_name.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.JOINASSN_NAME));
        et_pro = (EditText) findViewById(R.id.et_pro);
        et_pro.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.JOINASSN_PRO));
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.JOINASSN_PHONE));
        et_cause = (EditText) findViewById(R.id.et_cause);
        et_cause.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.JOINASSN_CAUSE));
        et_cause.addTextChangedListener(this);
        et_cause.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    join();
                }
                return true;
            }
        });
        et_phone.addTextChangedListener(this);
        et_name.addTextChangedListener(this);
        et_pro.addTextChangedListener(this);
    }

    public void initAid(int aid) {
        this.aid = aid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_join:
                join();
                break;
        }
    }

    private void join() {
        String name = et_name.getText().toString().trim();
        String pro = et_pro.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        String cause = et_cause.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pro) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(cause)) {
            WindowHelper.showToast("填写信息不完整");
            return;
        }
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            AssnJoinRequest assnJoinRequest = new AssnJoinRequest();
            assnJoinRequest.setToken(token);
            assnJoinRequest.setAr_username(name);
            assnJoinRequest.setCauses(cause);
            assnJoinRequest.setProfession(pro);
            assnJoinRequest.setMobile_no(phone);
            assnJoinRequest.setAssociation_id(aid);
            callBack.assnJoin(assnJoinRequest);
        }else{
            callBack.loginWindowEnter();
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
        callBack.setUIData(WindowCallBack.UIDataKeysDef.JOINASSN_NAME, et_name.getText().toString());
        callBack.setUIData(WindowCallBack.UIDataKeysDef.JOINASSN_PRO, et_pro.getText().toString());
        callBack.setUIData(WindowCallBack.UIDataKeysDef.JOINASSN_PHONE, et_phone.getText().toString());
        callBack.setUIData(WindowCallBack.UIDataKeysDef.JOINASSN_CAUSE, et_cause.getText().toString());
    }
}
