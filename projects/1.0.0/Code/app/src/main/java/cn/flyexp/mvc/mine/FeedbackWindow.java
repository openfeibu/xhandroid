package cn.flyexp.mvc.mine;

import android.view.View;
import android.widget.EditText;

import cn.flyexp.R;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class FeedbackWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private EditText et_content;
    private EditText et_contact;

    public FeedbackWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_feedback);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

        et_content = (EditText) findViewById(R.id.et_content);
        et_contact = (EditText) findViewById(R.id.et_contact);
    }


    public void response() {
        findViewById(R.id.btn_confirm).setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_confirm:
                String content = et_content.getText().toString().trim();
                String contact = et_contact.getText().toString().trim();
                if (content == null || content.equals("")) {
                    showToast("意见不能为空");
                    return;
                }
                if (contact == null || contact.equals("")) {
                    showToast("留下你的联系方式");
                    return;
                }
                String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                FeedbackRequest feedbackRequest = new FeedbackRequest();
                feedbackRequest.setToken(token);
                feedbackRequest.setContent(content);
                feedbackRequest.setContact_way(contact);
                callBack.feedback(feedbackRequest);
                v.setEnabled(false);
                break;
        }
    }

}
