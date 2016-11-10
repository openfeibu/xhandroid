package cn.flyexp.window.assn;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/11/1.
 */
public class AssnActiDetailWindow extends BaseWindow {

    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.img_state)
    ImageView imgState;
    @InjectView(R.id.img_bg)
    ImageView imgBg;
    @InjectView(R.id.tv_content)
    TextView tvContent;
    @InjectView(R.id.tv_place)
    TextView tvPlace;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_assnname)
    TextView tvAssnName;
    @InjectView(R.id.tv_viewnum)
    TextView tvViewNum;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_acti_detail;
    }

    public AssnActiDetailWindow(Bundle bundle) {
        AssnActivityResponse.AssnActivityResponseData data = (AssnActivityResponse.AssnActivityResponseData) bundle.getSerializable("assnacti");
        if (data != null) {
            initView(data);
        }
    }

    private void initView(AssnActivityResponse.AssnActivityResponseData data) {
        tvTitle.setText(data.getTitle().trim());
        tvContent.setText(data.getContent().trim());
        tvAssnName.setText(data.getAname());
        tvPlace.setText(String.format(getResources().getString(R.string.assnacti_place), data.getPlace()));
        tvViewNum.setText(String.format(getResources().getString(R.string.assnactiv_viewnum), data.getView_num()));
        tvDate.setText(DateUtil.dateFormat(data.getStart_time(), "MM-dd") + " 至 " + DateUtil.dateFormat(data.getEnd_time(), "MM-dd"));
        if (DateUtil.date2Long(data.getStart_time()) > new Date().getTime()) {
            imgState.setImageResource(R.mipmap.icon_activity_waiting);
            tvDate.setTextColor(getResources().getColor(R.color.light_blue));
        } else if (DateUtil.date2Long(data.getEnd_time()) < new Date().getTime()) {
            imgState.setImageResource(R.mipmap.icon_activity_end);
            tvDate.setTextColor(getResources().getColor(R.color.font_light));
        } else {
            imgState.setImageResource(R.mipmap.icon_activity_ongoing);
            tvDate.setTextColor(getResources().getColor(R.color.light_red));
        }
        if (!TextUtils.isEmpty(data.getImg_url())) {
            imgBg.setVisibility(VISIBLE);
            Glide.with(getContext()).load(data.getImg_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imgBg);
        }
    }

    @OnClick({R.id.img_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
        }
    }
}
