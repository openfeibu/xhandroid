package cn.flyexp.mvc.campus;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.RecomTaskAdapter;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.NoResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.entity.RecommendOrderRequest;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.UPMarqueeView;
import cn.flyexp.view.VerticalMarqueeView;
import me.relex.circleindicator.CircleIndicator;


/**
 * Created by zlk on 2016/3/26.
 */
public class CampusWindow extends AbstractWindow implements View.OnClickListener {

    private ViewPager vp_ad;
    private CampusViewCallBack callBack;
    private List<View> adViews = new ArrayList<View>();
    private Handler handler;
    private Runnable runn;
    private CircleIndicator indicator;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> activityData = new ArrayList<>();
    private ArrayList<OrderResponse.OrderResponseData> taskData = new ArrayList<>();
    private AssnActivityAdapter actiAdapter;
    private UPMarqueeView mMarqueeView;
    private RecomTaskAdapter taskAdapter;

    public CampusWindow(CampusViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_campus);
        findViewById(R.id.tv_assn).setOnClickListener(this);
        findViewById(R.id.tv_soup).setOnClickListener(this);
        findViewById(R.id.tv_fault).setOnClickListener(this);

        RecyclerView taskRecyclerView = (RecyclerView) findViewById(R.id.rv_task);

        taskAdapter = new RecomTaskAdapter(getContext(), taskData);
        taskAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.recomTaskDetailEnter(taskData.get(position));
            }
        });

        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskRecyclerView.setHasFixedSize(false);
        taskRecyclerView.setNestedScrollingEnabled(false);
        taskRecyclerView.setAdapter(taskAdapter);

        RecyclerView actiRecyclerView = (RecyclerView) findViewById(R.id.rv_activity);
        actiAdapter = new AssnActivityAdapter(getContext(), activityData);
        actiAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.activityDetailEnter(activityData.get(position));
            }
        });
        actiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        actiRecyclerView.setItemAnimator(new DefaultItemAnimator());
        actiRecyclerView.setHasFixedSize(false);
        actiRecyclerView.setNestedScrollingEnabled(false);
        actiRecyclerView.setAdapter(actiAdapter);



        indicator = (CircleIndicator) findViewById(R.id.indicator);
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
        mMarqueeView = (UPMarqueeView) findViewById(R.id.marqueeView);
    }

    //森彬改
    private int sp2px(Context context, int sp) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }


    public void responseNo(ArrayList<NoResponse.NoResponseData> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        List<View> views = new ArrayList<>();
        for (NoResponse.NoResponseData responseData : data) {
            View marqueeView = LayoutInflater.from(getContext()).inflate(R.layout.layout_marquee, null);
            TextView tv_no = (TextView) marqueeView.findViewById(R.id.tv_no);
            tv_no.setText(responseData.getExtra());
            views.add(marqueeView);
        }
        if (data.size() == 1) {
            views.add(views.get(0));
        }
        mMarqueeView.setViews(views);
    }


    private void playAd() {
        handler = new Handler();
        runn = new Runnable() {
            @Override
            public void run() {
                loopAd();
                handler.postDelayed(this, 4000);
            }
        };
        handler.postDelayed(runn, 4000);
    }

    private void loopAd() {
        int curItem = vp_ad.getCurrentItem();
        if (curItem == vp_ad.getAdapter().getCount() - 1) {
            vp_ad.setCurrentItem(0, false);
        } else {
            vp_ad.setCurrentItem(curItem + 1, true);
        }
    }

    public void removeRunn() {
        if (handler != null) {
            handler.removeCallbacks(runn);
        }
    }

    public void adResponse(final ArrayList<AdResponse.AdResponseData> adResponseDatas) {
        if (adResponseDatas == null) {
            return;
        }
        for (int i = 0; i < adResponseDatas.size(); i++) {
            final AdResponse.AdResponseData responseData = adResponseDatas.get(i);
            adViews.add(inflate(getContext(), R.layout.layout_ad, null));
            ImageView adbg = (ImageView) adViews.get(i).findViewById(R.id.adbg);
            Picasso.with(getContext()).load(responseData.getAd_image_url()).config(Bitmap.Config.RGB_565).resize(CommonUtil.getScreenWidth(getContext()), CommonUtil.dip2px(getContext(), 180)).into(adbg);
            final int finalI = i;
            if (!adResponseDatas.get(finalI).getAd_url().equals("")) {
                adbg.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebBean bean = new WebBean();
                        bean.setRequest(false);
                        bean.setTitle(adResponseDatas.get(finalI).getTitle());
                        bean.setUrl(adResponseDatas.get(finalI).getAd_url() + "?device=android");
                        callBack.webWindowEnter(bean);
                    }
                });
            }
        }
        vp_ad.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return adViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(adViews.get(position));
                return adViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(adViews.get(position));
            }
        });
        indicator.setViewPager(vp_ad);
        playAd();
    }

    public void responseOrder(ArrayList<OrderResponse.OrderResponseData> data) {
        taskData.addAll(data);
        taskAdapter.notifyDataSetChanged();
    }

    public void assnHotActiResponse(ArrayList<AssnActivityResponse.AssnActivityResponseData> assnActivityResponseDatas) {
        if (assnActivityResponseDatas == null) {
            return;
        }
        if (assnActivityResponseDatas.size() > 0) {
            activityData.addAll(assnActivityResponseDatas);
            actiAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_assn:
                callBack.userCount("assn_info");
                callBack.assnEnter();
                break;
            case R.id.tv_soup:
                WebBean bean = new WebBean();
                bean.setRequest(true);
                bean.setTitle("心灵鸡汤");
                bean.setName("soup");
                callBack.webWindowEnter(bean);
                break;
            case R.id.tv_fault:
                WebBean bean2 = new WebBean();
                bean2.setRequest(true);
                bean2.setTitle("网络报障");
                bean2.setName("fault");
                callBack.webWindowEnter(bean2);
                break;
        }
    }
}
