package cn.flyexp.window.main;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.RecommendTaskAdapter;
import cn.flyexp.callback.main.HomeCallback;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.main.HomePresenter;
import cn.flyexp.util.ScreenHelper;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.RefreshLayout;
import cn.flyexp.window.BaseWindow;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class HomeWindow extends BaseWindow implements HomeCallback.ResponseCallback {

    @InjectView(R.id.banner)
    ConvenientBanner banner;
    @InjectView(R.id.rv_recommend_task)
    RecyclerView rvRecommendTask;
    @InjectView(R.id.rv_assn_acti)
    RecyclerView rvAssnActi;
    @InjectView(R.id.tv_recommend_task)
    TextView tvRecommendTask;
    @InjectView(R.id.tv_assn_acti)
    TextView tvAssnActi;
    @InjectView(R.id.layout_refresh)
    RefreshLayout refreshLayout;

    private ArrayList<String> imgUrls = new ArrayList<>();
    private ArrayList<AdResponse.AdResponseData> adResponseDatas = new ArrayList<>();
    private ArrayList<TaskResponse.TaskResponseData> recommendTaskDatas = new ArrayList<>();
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> hotActiDatas = new ArrayList<>();
    private HomePresenter homePresenter;
    private RecommendTaskAdapter recommendTaskAdapter;
    private AssnActivityAdapter assnActivityAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_home;
    }

    public HomeWindow() {
        homePresenter = new HomePresenter(this);
        initView();
    }

    @Override
    public void onStart() {
        homePresenter.requestAd();
        homePresenter.requestRecommendTask();
        homePresenter.requestHotActivity();
    }

    @Override
    public void onRenew() {
    }


    private void initView() {
        banner.setCanLoop(true);
        banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgUrls);
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebBean webBean = new WebBean();
                webBean.setTitle(adResponseDatas.get(position).getTitle());
                webBean.setUrl(adResponseDatas.get(position).getAd_url());
                webBean.setRequest(false);
                Bundle bundle = new Bundle();
                bundle.putSerializable("webbean", webBean);
                openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
            }
        });

        recommendTaskAdapter = new RecommendTaskAdapter(getContext(), recommendTaskDatas);
        recommendTaskAdapter.setOnItemClickLinstener(new RecommendTaskAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskdetail", recommendTaskDatas.get(position));
                openWindow(WindowIDDefine.WINDOW_TASK_DETAIL, bundle);
            }
        });
        rvRecommendTask.setAdapter(recommendTaskAdapter);
        rvRecommendTask.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvRecommendTask.setHasFixedSize(true);
        rvRecommendTask.setNestedScrollingEnabled(false);

        assnActivityAdapter = new AssnActivityAdapter(getContext(), hotActiDatas);
        assnActivityAdapter.setOnItemClickLinstener(new AssnActivityAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("assnacti", hotActiDatas.get(position));
                openWindow(WindowIDDefine.WINDOW_ASSNACTI_DETAIL, bundle);
            }
        });
        rvAssnActi.setAdapter(assnActivityAdapter);
        rvAssnActi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAssnActi.addItemDecoration(new DividerItemDecoration(getContext()));
        rvAssnActi.setHasFixedSize(true);
        rvAssnActi.setNestedScrollingEnabled(false);

        refreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                homePresenter.requestRecommendTask();
            }

        });
    }

    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }

    @OnClick({R.id.tv_soup, R.id.tv_fault, R.id.tv_assn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_assn:
                openWindow(WindowIDDefine.WINDOW_ASSN);
                break;
            case R.id.tv_soup:
                WebBean bean = new WebBean();
                bean.setRequest(true);
                bean.setTitle(getResources().getString(R.string.chicken_soup));
                bean.setName("soup");
                openWebWindow(bean);
                break;
            case R.id.tv_fault:
                WebBean bean2 = new WebBean();
                bean2.setRequest(true);
                bean2.setTitle(getResources().getString(R.string.net_fault));
                bean2.setName("fault");
                openWebWindow(bean2);
                break;
        }
    }

    private void openWebWindow(WebBean bean) {
        Bundle soupBundle = new Bundle();
        soupBundle.putSerializable("webbean", bean);
        openWindow(WindowIDDefine.WINDOW_WEBVIEW, soupBundle);
    }

    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void requestFailure() {
        super.requestFailure();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.refreshComplete();
        }
    }

    @Override
    public void requestFinish() {
        super.requestFinish();
        if (refreshLayout.isRefreshing()) {
            refreshLayout.refreshComplete();
        }
    }

    @Override
    public void responseAd(AdResponse response) {
        adResponseDatas.clear();
        adResponseDatas.addAll(response.getData());
        for (int i = 0; i < adResponseDatas.size(); i++) {
            imgUrls.add(adResponseDatas.get(i).getAd_image_url());
        }
        banner.notifyDataSetChanged();
        banner.startTurning(3000);
    }

    @Override
    public void responseRecommendTask(TaskResponse response) {
        recommendTaskDatas.clear();
        if (response.getData().isEmpty()) {
            tvRecommendTask.setVisibility(GONE);
            rvRecommendTask.setVisibility(GONE);
        } else {
            tvRecommendTask.setVisibility(VISIBLE);
            rvRecommendTask.setVisibility(VISIBLE);
            recommendTaskDatas.addAll(response.getData());
            recommendTaskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void responseHotActivity(AssnActivityResponse response) {
        hotActiDatas.clear();
        if (response.getData().isEmpty()) {
            tvAssnActi.setVisibility(GONE);
            rvAssnActi.setVisibility(GONE);
        } else {
            tvAssnActi.setVisibility(VISIBLE);
            rvAssnActi.setVisibility(VISIBLE);
            hotActiDatas.addAll(response.getData());
            assnActivityAdapter.notifyDataSetChanged();
        }
    }
}
