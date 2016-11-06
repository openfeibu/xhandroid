package cn.flyexp.mvc.assn;

import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.flyexp.R;
import cn.flyexp.entity.AssnProfileRequest;
import cn.flyexp.entity.AssnProfileResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class AssnIntroduceWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private RoundImageView iv_avatar;
    private TextView tv_assn;
    private TextView tv_content;

    public AssnIntroduceWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_introduce);
        findViewById(R.id.iv_back).setOnClickListener(this);

        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        tv_assn = (TextView) findViewById(R.id.tv_assn);
        tv_content = (TextView) findViewById(R.id.tv_content);
    }

    public void introduceRequest(int aid) {
        AssnProfileRequest assnProfileRequest = new AssnProfileRequest();
        assnProfileRequest.setAssociation_id(aid);
        callBack.getAssnIntroduce(assnProfileRequest, 0);
    }

    public void profileResponse(AssnProfileResponse.AssnProfileResponseData
                                        profileResponseData) {
        Picasso.with(getContext()).load(profileResponseData.getAvatar_url()).into(iv_avatar);
        tv_assn.setText(profileResponseData.getAname());
        tv_content.setText(profileResponseData.getIntroduction());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }
}
