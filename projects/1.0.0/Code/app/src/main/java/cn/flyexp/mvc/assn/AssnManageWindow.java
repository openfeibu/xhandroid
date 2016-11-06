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
 * Created by txy on 2016/7/20 0020.
 */
public class AssnManageWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private TextView tv_assnname;
    private RoundImageView iv_avatar;

    public AssnManageWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        assnInfoRequest();
    }

    private void initView() {
        setContentView(R.layout.window_assn_manage);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.layout_introduction).setOnClickListener(this);
        findViewById(R.id.layout_info).setOnClickListener(this);
        findViewById(R.id.layout_activity).setOnClickListener(this);
        findViewById(R.id.layout_notifiy).setOnClickListener(this);

        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        tv_assnname = (TextView) findViewById(R.id.tv_assnname);
    }

    public void assnInfoRequest() {
        int aid = getIntByPreference("association_id");
        AssnProfileRequest assnProfileRequest = new AssnProfileRequest();
        assnProfileRequest.setAssociation_id(aid);
        callBack.getAssnIntroduce(assnProfileRequest, 2);
    }

    public void profileResponse(AssnProfileResponse.AssnProfileResponseData data) {
        if (data == null) {
            return;
        }
        Picasso.with(getContext()).load(data.getAvatar_url()).into(iv_avatar);
        tv_assnname.setText(data.getAname());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.layout_introduction:
                callBack.introducePublishEnter();
                break;
            case R.id.layout_info:
                callBack.infoPublishEnter();
                break;
            case R.id.layout_activity:
                callBack.activityPublishEnter();
                break;
            case R.id.layout_notifiy:
                callBack.noticePublishEnter();
                break;
        }
    }
}
