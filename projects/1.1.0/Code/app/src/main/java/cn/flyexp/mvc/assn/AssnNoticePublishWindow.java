package cn.flyexp.mvc.assn;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.flyexp.R;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;

/**
 * Created by txy on 2016/7/27 0027.
 */
public class AssnNoticePublishWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private AssnViewCallBack callBack;
    private EditText et_content;
    private Button btn_confirm;
    private int aid;

    public AssnNoticePublishWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_notice_publish);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        et_content = (EditText) findViewById(R.id.et_content);
        et_content.addTextChangedListener(this);
    }

    public void response() {
        btn_confirm.setEnabled(true);
        dismissProgressDialog();
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
            case R.id.btn_confirm:
                String content = et_content.getText().toString().trim();
                noticePublish(content);
                btn_confirm.setEnabled(false);
                showProgressDialog("发布中...");
                break;
        }
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    private void noticePublish(String content) {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            return;
        }
        AssnNoticePublishRequest assnNoticePublishRequest = new AssnNoticePublishRequest();
        assnNoticePublishRequest.setToken(token);
        assnNoticePublishRequest.setNotice(content);
        assnNoticePublishRequest.setAssociation_id(aid);
        callBack.submitNotice(assnNoticePublishRequest);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = et_content.getText().toString().trim();
        if (content.equals("")) {
            btn_confirm.setEnabled(false);
            btn_confirm.setAlpha(0.5f);
        } else {
            btn_confirm.setEnabled(true);
            btn_confirm.setAlpha(1f);
        }
    }
}
