package cn.flyexp.mvc.mine;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import cn.flyexp.R;
import cn.flyexp.entity.TaInfoRequest;
import cn.flyexp.entity.TaInfoResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/8/3 0003.
 */
public class TaWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private TextView tv_nickname;
    private TextView tv_college;
    private TextView tv_introduction;
    private RoundImageView iv_avatar;
    private TextView tv_year;
    private ImageView iv_bg;
    private ImageView iv_gender;

    public TaWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_ta);
        findViewById(R.id.iv_back).setOnClickListener(this);
        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_college = (TextView) findViewById(R.id.tv_college);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        tv_year = (TextView) findViewById(R.id.tv_year);
        iv_gender = (ImageView) findViewById(R.id.iv_gender);
    }

    public void getTaInfoRequset(String openId) {
        if (openId == null || openId.equals("")) {
            return;
        }
        TaInfoRequest taInfoRequest = new TaInfoRequest();
        taInfoRequest.setOpenid(openId);
        callBack.taInfo(taInfoRequest);
    }

    public void initData(TaInfoResponse.TaProileResponseData responseData) {
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 100), CommonUtil.dip2px(getContext(), 100))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(getResources().getDrawable(R.mipmap.icon_defaultavatar_big)).centerCrop().into(iv_avatar);
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 100), CommonUtil.dip2px(getContext(), 100))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(getResources().getDrawable(R.mipmap.icon_defaultavatar_big)).centerCrop().into(iv_bg);
        } else {
            iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_big);
        }
        tv_nickname.setText(responseData.getNickname());
        tv_college.setText(responseData.getCollege());
        tv_introduction.setText(responseData.getIntroduction());
        tv_year.setText(responseData.getEnrollment_year() + "级");

        if (responseData.getGender() == 1) {
            iv_gender.setImageResource(R.mipmap.icon_mysex_man);
        } else if (responseData.getGender() == 2) {
            iv_gender.setImageResource(R.mipmap.icon_mysex_woman);
        }
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
