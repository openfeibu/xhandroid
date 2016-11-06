package cn.flyexp.mvc.campus;

import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.AssnInfoAdapter;
import cn.flyexp.entity.AdRequest;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
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
    private ArrayList<AssnInfoResponse.AssnInfoResponseData> infoData = new ArrayList<>();
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> activityData = new ArrayList<>();
    private AssnActivityAdapter actiAdapter;
    private AssnInfoAdapter infoAdapter;

    public CampusWindow(CampusViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getAd(getAdRequest());
        callBack.getHotAssnActivity();
        callBack.getHotAssnInfo();
    }

    private void initView() {
        setContentView(R.layout.window_campus);
        findViewById(R.id.tv_delivery).setOnClickListener(this);
        findViewById(R.id.tv_shop).setOnClickListener(this);
        findViewById(R.id.tv_assn).setOnClickListener(this);
        findViewById(R.id.tv_soup).setOnClickListener(this);

        RecyclerView actiRecyclerView = (RecyclerView) findViewById(R.id.rv_activity);
        RecyclerView infoRecyclerView = (RecyclerView) findViewById(R.id.rv_info);
        actiAdapter = new AssnActivityAdapter(getContext(), activityData);
        infoAdapter = new AssnInfoAdapter(getContext(), infoData);
        infoAdapter.setHot(true);
        actiAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.activityDetailEnter(activityData.get(position));
            }
        });
        infoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.infoDetailEnter(infoData.get(position));
            }
        });
        actiRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        actiRecyclerView.setItemAnimator(new DefaultItemAnimator());
        actiRecyclerView.setHasFixedSize(false);
        actiRecyclerView.setNestedScrollingEnabled(false);
        infoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        infoRecyclerView.setHasFixedSize(false);
        infoRecyclerView.setNestedScrollingEnabled(true);
        actiRecyclerView.setAdapter(actiAdapter);
        infoRecyclerView.setAdapter(infoAdapter);

        indicator = (CircleIndicator) findViewById(R.id.indicator);
        vp_ad = (ViewPager) findViewById(R.id.vp_ad);
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

    public AdRequest getAdRequest() {
        AdRequest adRequest = new AdRequest();
        adRequest.setTime(DateUtil.long2Date(new Date().getTime()));
        return adRequest;
    }

    public void adResponse(final ArrayList<AdResponse.AdResponseData> adResponseDatas) {
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
                        callBack.userCount("ad");
                        callBack.webWindowEnter(new String[]{adResponseDatas.get(finalI).getAd_url() + "?device=android", adResponseDatas.get(finalI).getTitle()}, 1);
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

    public void assnInfoResponse(ArrayList<AssnInfoResponse.AssnInfoResponseData>
                                         assnInfoResponseDatas) {
        if (assnInfoResponseDatas == null) {
            return;
        }
        if (assnInfoResponseDatas.size() > 0) {
            infoData.addAll(assnInfoResponseDatas);
            infoAdapter.notifyDataSetChanged();
        }
    }

    public void assnActivityResponse(ArrayList<AssnActivityResponse.AssnActivityResponseData> assnActivityResponseDatas) {
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
            case R.id.tv_delivery:
                callBack.userCount("task");
                callBack.taskEnter();
                break;
            case R.id.tv_assn:
                callBack.userCount("assn_info");
                callBack.assnEnter();
                break;
            case R.id.tv_shop:
                showToast("店铺建设ing~");
                callBack.userCount("shop");
//                callBack.webWindowEnter("store", 0);
                break;
            case R.id.tv_soup:
                showToast("鸡汤熬制ing~");
                break;
        }
    }

}
