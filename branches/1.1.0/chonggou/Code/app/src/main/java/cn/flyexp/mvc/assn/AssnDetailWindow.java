package cn.flyexp.mvc.assn;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import cn.flyexp.R;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.view.RoundImageView;

/**
 * Created by tanxinye on 2016/10/1.
 */
public class AssnDetailWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private Button btn_join;
    private TextView tv_intro;
    private TextView tv_detail;
    private TextView tv_assn;
    private RoundImageView iv_avatar;
    private ImageView iv_bg;
    private int aid;

    public AssnDetailWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_join.setOnClickListener(this);

        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        tv_assn = (TextView) findViewById(R.id.tv_assn);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        tv_intro = (TextView) findViewById(R.id.tv_intro);
    }

    public void requestData(int aid) {
        this.aid = aid;
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (TextUtils.isEmpty(token)) {
            callBack.loginWindowEnter();
            return;
        }
        AssnDetailRequest assnDetailRequest = new AssnDetailRequest();
        assnDetailRequest.setToken(token);
        assnDetailRequest.setAssociation_id(aid);
        callBack.getAssnDetails(assnDetailRequest);
    }

    public void responseData(AssnDetailResponse.AssnDetailResponseData responseData) {
        tv_assn.setText(responseData.getAname());
        tv_intro.setText(responseData.getIntroduction());
        tv_detail.setText(responseData.getMember_number() + "人活跃 · " + responseData.getActivity_count() + "活动 · " + responseData.getLabel());
        if (!TextUtils.isEmpty(responseData.getAvatar_url())) {
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 50), CommonUtil.dip2px(getContext(), 50))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().into(iv_avatar);
        }
        if (!TextUtils.isEmpty(responseData.getBackground_url())) {
            Picasso.with(getContext()).load(responseData.getBackground_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.getScreenWidth(getContext()), CommonUtil.dip2px(getContext(), 150))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().into(iv_bg);
        }
        if (responseData.getUid() != 0) {
            btn_join.setVisibility(GONE);
        } else {
            btn_join.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_join:
                callBack.assnJoinEnter(aid);
                break;
        }
    }
}
