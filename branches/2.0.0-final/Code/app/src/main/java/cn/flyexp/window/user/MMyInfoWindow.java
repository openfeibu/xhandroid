package cn.flyexp.window.user;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/11/23.
 */
public class MMyInfoWindow extends BaseWindow {

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.img_gender)
    ImageView imgGender;
    @InjectView(R.id.tv_nickname)
    TextView tvNickname;
    @InjectView(R.id.tv_campus)
    TextView tvCampus;
    @InjectView(R.id.tv_proflie)
    TextView tvProflie;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_address)
    TextView tvAddress;


    private Bundle bundle;
    private MyInfoResponse.MyInfoResponseData data;

    @Override
    protected int getLayoutId() {
        return R.layout.window_mmyinfo;
    }

    public MMyInfoWindow(Bundle bundle) {
        this.bundle = bundle;
        data = (MyInfoResponse.MyInfoResponseData) bundle.getSerializable("myinfo");
        initView();
    }

    private void initView() {
        tvNickname.setText(data.getNickname());
        tvCampus.setText(data.getCollege());
        tvProflie.setText(data.getIntroduction());
        tvPhone.setText(data.getMobile_no());
        tvAddress.setText(data.getAddress());

        if (data.getGender() == 1) {
            imgGender.setImageDrawable(getResources().getDrawable(R.mipmap.icon_mysex_man));
        } else if (data.getGender() == 2) {
            imgGender.setImageDrawable(getResources().getDrawable(R.mipmap.icon_mysex_woman));
        } else {
            imgGender.setVisibility(GONE);
        }
        Glide.with(getContext()).load(data.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgAvatar);
    }

    @OnClick({R.id.img_back, R.id.tv_edit, R.id.img_avatar, R.id.layout_changepwd})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_edit:
                openWindow(WindowIDDefine.WINDOW_MYINFO_EDIT, bundle);
                break;
            case R.id.img_avatar:
                //TODO 上传头像
                break;
            case R.id.layout_changepwd:
                openWindow(WindowIDDefine.WINDOW_CHANGEPWD);
                break;
        }
    }

}
