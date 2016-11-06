package cn.flyexp.mvc.assn;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import cn.flyexp.R;
import cn.flyexp.adapter.PicDisPlayAdapter;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class AssnActivityDetailWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private PicDisPlayAdapter picAdapter;
    private TextView tv_title;
    private ImageView iv_state;
    private TextView tv_content;
    private TextView tv_place;
    private TextView tv_time;
    private TextView tv_viewnum;
    private TextView tv_assn;
    private RoundImageView iv_sculpture;
    private int aid = -1;
    private ArrayList<String> imgUrl = new ArrayList<String>();
    private RecyclerView rv_pic;

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
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_place = (TextView) findViewById(R.id.tv_place);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_viewnum = (TextView) findViewById(R.id.tv_viewnum);
        tv_assn = (TextView) findViewById(R.id.tv_assn);
        iv_sculpture = (RoundImageView) findViewById(R.id.iv_avatar);

        picAdapter = new PicDisPlayAdapter(getContext(), imgUrl);
        picAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PicBrowserBean picBrowserBean = new PicBrowserBean();
                picBrowserBean.setImgUrl(imgUrl);
                picBrowserBean.setCurSelectedIndex(position);
                picBrowserBean.setType(1);
                callBack.picBrowserEnter(picBrowserBean);
            }
        });
        rv_pic = (RecyclerView) findViewById(R.id.rv_pic);
        rv_pic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_pic.setAdapter(picAdapter);
        rv_pic.setItemAnimator(new DefaultItemAnimator());

    }

    public void initData(AssnActivityResponse.AssnActivityResponseData
                                 responseData) {
        Picasso.with(getContext()).load(responseData.getAvatar_url()).into
                (iv_sculpture);
        tv_title.setText(responseData.getTitle());
        if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
            iv_state.setImageResource(R.mipmap.icon_activity_waiting);
        } else if (DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()) {
            iv_state.setImageResource(R.mipmap.icon_activity_end);
        } else {
            iv_state.setImageResource(R.mipmap.icon_activity_ongoing);
        }
        tv_content.setText(responseData.getContent());
        tv_time.setText(DateUtil.long2Date(DateUtil.date2Long((responseData
                .getStart_time())), "yyyy-MM-dd HH:mm") + "至" + DateUtil.long2Date(DateUtil
                .date2Long((responseData.getEnd_time())), "yyyy-MM-dd HH:mm"));
        tv_assn.setText(responseData.getAname());
        tv_viewnum.setText("浏览数：" + responseData.getView_num());
        tv_place.setText("活动地点：" + responseData.getPlace());

        if (responseData.getImg_url() == null || responseData.getImg_url().equals("")) {
            rv_pic.setVisibility(View.GONE);
        } else {
            String[] urls = CommonUtil.splitImageUrl(responseData.getImg_url());
            rv_pic.setVisibility(View.VISIBLE);
            imgUrl.addAll(Arrays.asList(urls));
            picAdapter.notifyDataSetChanged();
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
