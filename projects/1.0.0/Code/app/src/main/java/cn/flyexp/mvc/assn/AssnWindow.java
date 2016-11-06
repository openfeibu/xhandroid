package cn.flyexp.mvc.assn;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.AssnInfoAdapter;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoRequest;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by txy on 2016/7/26 0026.
 */
public class AssnWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private RadioButton btn_info;
    private RadioButton btn_activity;
    private View[] views;
    private ViewPager vp_assn;
    private ArrayList<AssnInfoResponse.AssnInfoResponseData> infoData = new ArrayList<>();
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> activityData = new
            ArrayList<>();
    private AssnInfoAdapter assnInfoAdapter;
    private AssnActivityAdapter assnActivityAdapter;
    private LoadMoreRecyclerView infoRecyclerView;
    private LoadMoreRecyclerView activityRecyclerView;
    private int infoPage = 1;
    private int activityPage = 1;
    private boolean isResponse = true;
    private ContentLoadingProgressBar activityProgressBar;
    private ContentLoadingProgressBar infoProgressBar;
    private TextView activityState;
    private TextView infoState;

    public AssnWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getAssnInfo(getInfoRequest());
        callBack.getAssnActivity(getAssnActivityRequest());
    }

    private void initView() {
        setContentView(R.layout.window_assn);
        findViewById(R.id.iv_back).setOnClickListener(this);
        btn_info = (RadioButton) findViewById(R.id.btn_info);
        btn_activity = (RadioButton) findViewById(R.id.btn_activity);
        btn_info.setOnClickListener(this);
        btn_activity.setOnClickListener(this);

        views = new View[2];
        views[0] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview,
                null);
        views[1] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview,
                null);
        infoProgressBar = (ContentLoadingProgressBar) views[0].findViewById(R.id.progressBar);
        activityProgressBar = (ContentLoadingProgressBar) views[1].findViewById(R.id.progressBar);
        infoProgressBar.show();
        activityProgressBar.show();
        infoState = (TextView) views[0].findViewById(R.id.tv_state);
        activityState = (TextView) views[1].findViewById(R.id.tv_state);
        infoRecyclerView = (LoadMoreRecyclerView) views[0].findViewById(R.id.recyclerView);
        activityRecyclerView = (LoadMoreRecyclerView) views[1].findViewById(R.id.recyclerView);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assnInfoAdapter = new AssnInfoAdapter(getContext(), infoData, callBack);
        assnActivityAdapter = new AssnActivityAdapter(getContext(), activityData, callBack);
        assnInfoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.infoDetailEnter(infoData.get(position));
            }
        });
        assnActivityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.activityDetailEnter(activityData.get(position));
            }
        });
        infoRecyclerView.setAdapter(assnInfoAdapter);
        activityRecyclerView.setAdapter(assnActivityAdapter);
        infoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        activityRecyclerView.setItemAnimator(new DefaultItemAnimator());

        infoRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        infoRecyclerView.setFootEndView("没有更多资讯了~");
        infoRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                infoPage++;
                callBack.getAssnInfo(getInfoRequest());
            }
        });

        activityRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        activityRecyclerView.setFootEndView("没有更多活动了~");
        activityRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                activityPage++;
                callBack.getAssnActivity(getAssnActivityRequest());
            }
        });

        vp_assn = (ViewPager) findViewById(R.id.vp_assn);
        vp_assn.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }
        });
        vp_assn.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btn_info.setChecked(true);
                    btn_activity.setChecked(false);
                } else {
                    btn_info.setChecked(false);
                    btn_activity.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setActivity() {
        vp_assn.setCurrentItem(1);
    }

    public AssnInfoRequest getInfoRequest() {
        AssnInfoRequest socialInfoRequest = new AssnInfoRequest();
        socialInfoRequest.setNum(20);
        socialInfoRequest.setPage(infoPage);
        return socialInfoRequest;
    }

    public AssnActivityRequest getAssnActivityRequest() {
        AssnActivityRequest socialActivityRequest = new AssnActivityRequest();
        socialActivityRequest.setNum(20);
        socialActivityRequest.setPage(activityPage);
        return socialActivityRequest;
    }

    public void assnInfoResponse(ArrayList<AssnInfoResponse.AssnInfoResponseData>
                                         assnInfoResponseDatas) {
        infoProgressBar.hide();
        infoState.setVisibility(View.GONE);
        if (assnInfoResponseDatas.size() == 0) {
            infoRecyclerView.loadMoreEnd();
        } else {
            infoRecyclerView.loadMoreComplete();
        }

        infoData.addAll(assnInfoResponseDatas);
        if (infoData.size() == 0) {
            infoState.setText("暂无资讯");
            infoState.setVisibility(View.VISIBLE);
            infoRecyclerView.setVisibility(View.GONE);
        } else {
            infoRecyclerView.setVisibility(View.VISIBLE);
            assnInfoAdapter.notifyDataSetChanged();
        }
        isResponse = true;
    }

    public void assnActivityResponse(ArrayList<AssnActivityResponse.AssnActivityResponseData> assnActivityResponseDatas) {
        activityProgressBar.hide();
        activityState.setVisibility(View.GONE);
        if (assnActivityResponseDatas.size() == 0) {
            activityRecyclerView.loadMoreEnd();
        } else {
            activityRecyclerView.loadMoreComplete();
        }

        activityData.addAll(assnActivityResponseDatas);
        if (activityData.size() == 0) {
            activityState.setText("暂无活动");
            activityState.setVisibility(View.VISIBLE);
            activityRecyclerView.setVisibility(View.GONE);
        } else {
            activityRecyclerView.setVisibility(View.VISIBLE);
            assnActivityAdapter.notifyDataSetChanged();
        }
        isResponse = true;
    }

    public void infoLoadingFailure() {
        infoProgressBar.hide();
        infoState.setText("数据加载失败...");
        infoState.setVisibility(View.VISIBLE);
        infoRecyclerView.setVisibility(View.GONE);
        infoRecyclerView.loadMoreComplete();
        isResponse = true;
    }

    public void activityLoadingFailure() {
        activityProgressBar.hide();
        activityState.setText("数据加载失败...");
        activityState.setVisibility(View.VISIBLE);
        activityRecyclerView.setVisibility(View.GONE);
        activityRecyclerView.loadMoreComplete();
        isResponse = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_info:
                vp_assn.setCurrentItem(0);
                break;
            case R.id.btn_activity:
                vp_assn.setCurrentItem(1);
                break;
        }
    }
}
