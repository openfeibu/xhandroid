package cn.flyexp.window.assn;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
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
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/11/1.
 */
public class AssnActiDetailWindow extends BaseWindow {

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
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    private AssnActivityResponse.AssnActivityResponseData data;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_acti_detail;
    }

    public AssnActiDetailWindow(Bundle bundle) {
        data = (AssnActivityResponse.AssnActivityResponseData) bundle.getSerializable("assnacti");
        initView();
    }

    private void initView() {
        tvTitle.setText(data.getTitle().trim());
        tvContent.setText(data.getContent().trim());
        tvAssnName.setText(data.getAname());
        tvPlace.setText(String.format(getResources().getString(R.string.assnacti_place), data.getPlace()));
        tvViewNum.setText(String.format(getResources().getString(R.string.assnactiv_viewnum), data.getView_num()));
        tvDate.setText(DateUtil.dateFormat(data.getStart_time(), "MM-dd") + " 至 " + DateUtil.dateFormat(data.getEnd_time(), "MM-dd"));
        if (DateUtil.date2Long(data.getStart_time()) > new Date().getTime()) {
            tvDate.setTextColor(getResources().getColor(R.color.light_blue));
        } else if (DateUtil.date2Long(data.getEnd_time()) < new Date().getTime()) {
            tvDate.setTextColor(getResources().getColor(R.color.font_light));
        } else {
            tvDate.setTextColor(getResources().getColor(R.color.light_red));
        }
        Glide.with(getContext()).load(data.getImg_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imgBg);
    }

    @OnClick({R.id.img_back, R.id.tv_assnname})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_assnname:
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                int myLevel = data.getLevel();
                Bundle bundle = new Bundle();
                bundle.putInt("aid", data.getAid());
                if (myLevel == -1 || TextUtils.isEmpty(token)) {
                    bundle.putString("aname", data.getAname());
                    openWindow(WindowIDDefine.WINDOW_ASSN_DETAIL, bundle);
                } else {
                    bundle.putInt("level", data.getLevel());
                    openWindow(WindowIDDefine.WINDOW_MYASSN_DETAIL, bundle);
                }
                break;
        }
    }
}
