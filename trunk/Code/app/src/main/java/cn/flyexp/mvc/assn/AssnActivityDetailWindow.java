package cn.flyexp.mvc.assn;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Date;

import cn.flyexp.R;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class AssnActivityDetailWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private TextView tv_title;
    private ImageView iv_state;
    private TextView tv_content;
    private TextView tv_place;
    private TextView tv_time;
    private TextView tv_viewnum;
    private TextView tv_assn;
    private RoundImageView iv_avatar;
    private int aid = -1;
    private ImageView iv_img;
    private TextView tv_stime;
    private TextView tv_etime;

    public AssnActivityDetailWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_activity_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_assn).setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_state = (ImageView) findViewById(R.id.iv_state);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_stime = (TextView) findViewById(R.id.tv_stime);
        tv_etime = (TextView) findViewById(R.id.tv_etime);
        tv_viewnum = (TextView) findViewById(R.id.tv_viewnum);
        tv_assn = (TextView) findViewById(R.id.tv_assn);
        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);

    }

    public void initData(AssnActivityResponse.AssnActivityResponseData
                                 responseData) {
        tv_title.setText(responseData.getTitle().trim());
        if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
            iv_state.setImageResource(R.mipmap.icon_activity_waiting);
            tv_stime.setTextColor(getResources().getColor(R.color.light_blue));
        } else if (DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()) {
            iv_state.setImageResource(R.mipmap.icon_activity_end);
            tv_stime.setTextColor(getResources().getColor(R.color.light_gray));
        } else {
            iv_state.setImageResource(R.mipmap.icon_activity_ongoing);
            tv_stime.setTextColor(getResources().getColor(R.color.light_red));
        }
        tv_content.setText(responseData.getContent().trim());
        tv_assn.setText(responseData.getAname());
        tv_stime.setText("开始时间："+DateUtil.long2Date(DateUtil.date2Long(responseData.getStart_time()),"yy-MM-dd HH:mm"));
        tv_etime.setText("结束时间："+DateUtil.long2Date(DateUtil.date2Long(responseData.getEnd_time()),"yy-MM-dd HH:mm"));
        tv_viewnum.setText("浏览数：" + responseData.getView_num());
        tv_place.setText("活动地点：" + responseData.getPlace());

        if (responseData.getImg_url() == null || responseData.getImg_url().equals("")) {
            iv_img.setVisibility(View.GONE);
        } else {
            iv_img.setVisibility(VISIBLE);
            Picasso.with(getContext()).load(responseData.getImg_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.getScreenWidth(getContext()), CommonUtil.dip2px(getContext(), 150))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_img);
        }
        aid = responseData.getAid();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_assn:
                if (aid == -1) {
                    return;
                }
                callBack.introduceEnter(aid);
                break;
        }
    }
}
