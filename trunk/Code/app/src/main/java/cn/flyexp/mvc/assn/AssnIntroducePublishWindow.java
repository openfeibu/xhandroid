package cn.flyexp.mvc.assn;

import android.view.View;
import android.widget.EditText;

import cn.flyexp.R;
import cn.flyexp.entity.AssnProfilePublishRequest;
import cn.flyexp.entity.AssnProfileRequest;
import cn.flyexp.entity.AssnProfileResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;

/**
 * Created by txy on 2016/7/27 0027.
 */
public class AssnIntroducePublishWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private EditText et_content;

    public AssnIntroducePublishWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getAssnIntroduce(getProfileRequest(), 1);
    }

    private void initView() {
        setContentView(R.layout.window_assn_introduce_publish);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);

        et_content = (EditText) findViewById(R.id.et_content);
    }

    public AssnProfileRequest getProfileRequest() {
        int association_id = WindowHelper.getIntByPreference("association_id");
        if (association_id == -1) {
            return null;
        }
        AssnProfileRequest assnProfileRequest = new AssnProfileRequest();
        assnProfileRequest.setAssociation_id(association_id);
        return assnProfileRequest;
    }


    public void profileResponse(AssnProfileResponse.AssnProfileResponseData
                                        profileResponseData) {
        et_content.setText(profileResponseData.getIntroduction());
    }

    public void response(){
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
                if (content == null || content.equals("")) {
                    WindowHelper.showToast("内容不能为空");
                    return;
                }
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    return;
                }
                AssnProfilePublishRequest assnProfilePublishRequest = new
                        AssnProfilePublishRequest();
                assnProfilePublishRequest.setToken(token);
                assnProfilePublishRequest.setIntroduction(content);
                callBack.submitProfile(assnProfilePublishRequest);
                findViewById(R.id.btn_confirm).setEnabled(false);
                break;
        }
    }
}
