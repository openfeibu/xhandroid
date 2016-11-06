package cn.flyexp.mvc.topic;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import cn.flyexp.R;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.ReplyListResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class ReplyWindow extends AbstractWindow implements View.OnClickListener , TextWatcher {

    private EditText et_content;
    private ReplyListResponse.ReplyListResponseData responseData;
    private TopicViewCallBack callBack;

    public ReplyWindow(TopicViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_reply);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_comfirm).setOnClickListener(this);

        et_content = (EditText) findViewById(R.id.et_content);
        et_content.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.REPLY_CONTENT));
        et_content.addTextChangedListener(this);
    }


    public void initData(ReplyListResponse.ReplyListResponseData responseData) {
        this.responseData = responseData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_comfirm:
                String content = et_content.getText().toString().trim();
                String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
                if(TextUtils.isEmpty(token)){
                    callBack.loginWindowEnter();
                    return;
                }
                CommentRequest commentRequest = new CommentRequest();
                commentRequest.setToken(token);
                commentRequest.setTopic_id(responseData.getTid());
                commentRequest.setTopic_comment(content);
                commentRequest.setComment_id(responseData.getTcid());
                callBack.replyRequest(commentRequest);
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
        callBack.setUIData(WindowCallBack.UIDataKeysDef.REPLY_CONTENT, et_content.getText().toString());
    }

}
