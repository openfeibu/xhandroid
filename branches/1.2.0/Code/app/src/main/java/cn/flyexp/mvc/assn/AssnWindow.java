package cn.flyexp.mvc.assn;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.AssnAdapter;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnRequest;
import cn.flyexp.entity.AssnResponse;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by txy on 2016/7/26 0026.
 */
public class AssnWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private View[] views;
    private ViewPager vp_assn;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> activityData = new
            ArrayList<>();
    private ArrayList<AssnResponse.DataBean.AssociationsBean> assnData = new
            ArrayList<>();
    private AssnActivityAdapter assnActivityAdapter;
    private LoadMoreRecyclerView activityRecyclerView;
    private int activityPage = 1;
    private int assnPage = 1;
    private boolean isResponse = true;
    private ContentLoadingProgressBar activityProgressBar;
    private TextView activityState;
    private final String[] tabTitle = new String[]{"校园社团", "社团活动"};
    private LoadMoreRecyclerView rv_assnlist;

    private TextView tv_assnMessage;

    public AssnWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        getAssociations();
    }

    private void getAssociations() {
        if (!TextUtils.isEmpty(CommonUtil.getStringData(getContext(), "token"))) {
            showProgress();
            AssnRequest request = new AssnRequest();
            request.setToken(CommonUtil.getStringData(getContext(), "token"));
            request.setPgae(assnPage);
            callBack.getAssociations(request);
        } else {
            callBack.loginWindowEnter();
        }
    }

    public void requsetAssnActi() {
        callBack.getAssnActivity(getAssnActivityRequest());
    }

    AssnAdapter assnAdapter;
    LinearLayout assnMain;
    ContentLoadingProgressBar progressBar;

    private void initView() {
        setContentView(R.layout.window_assn);
        findViewById(R.id.iv_back).setOnClickListener(this);

        views = new View[2];
        views[0] = LayoutInflater.from(getContext()).inflate(R.layout.layout_assnlist,
                null);
        views[1] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview,
                null);
        assnAdapter = new AssnAdapter(getContext(), new ArrayList<AssnResponse.DataBean.AssociationsBean>());
        assnAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.myAssnDetailEnter(assnData.get(position).getAid(), assnData.get(position).getMylevel());
            }
        });
        rv_assnlist = (LoadMoreRecyclerView) views[0].findViewById(R.id.rv_assnlist);
        rv_assnlist.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_assnlist.setHasFixedSize(false);
        rv_assnlist.setNestedScrollingEnabled(false);
        rv_assnlist.setAdapter(assnAdapter);

        tv_assnMessage = (TextView) views[0].findViewById(R.id.assnmessage);
        assnMain = (LinearLayout) findViewById(R.id.assn_main);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);

        activityProgressBar = (ContentLoadingProgressBar) views[1].findViewById(R.id.progressBar);
        activityState = (TextView) views[1].findViewById(R.id.tv_state);
        activityRecyclerView = (LoadMoreRecyclerView) views[1].findViewById(R.id.recyclerView);
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        assnActivityAdapter = new AssnActivityAdapter(getContext(), activityData);
        assnActivityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.activityDetailEnter(activityData.get(position));
            }
        });
        activityRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        activityRecyclerView.setAdapter(assnActivityAdapter);
        activityRecyclerView.setItemAnimator(new DefaultItemAnimator());


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

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitle[position];
            }
        });
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(vp_assn);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void showProgress() {
        progressBar.setVisibility(VISIBLE);
        assnMain.setVisibility(GONE);
    }

    private void hideProgress() {
        progressBar.setVisibility(GONE);
        assnMain.setVisibility(VISIBLE);
    }

    public void assnDataResponse(AssnResponse.DataBean data) {
        hideProgress();
        if (data == null) {
            return;
        }
        tv_assnMessage.setText(data.getActivity_sum() + "人活跃|" + data.getAssociation_sum() + "个社团");
        if (data.getAssociations() != null && data.getAssociations().size() > 0) {
            assnDataResponse(data.getAssociations());
        }
    }

    public void assnDataResponse(List<AssnResponse.DataBean.AssociationsBean> list) {
        int flag = assnAdapter.getItemCount();
        assnAdapter.getList().addAll(list);
        assnData.addAll(list);
        assnAdapter.notifyItemRangeInserted(flag, list.size());
    }

    public AssnActivityRequest getAssnActivityRequest() {
        AssnActivityRequest assnActivityRequest = new AssnActivityRequest();
        assnActivityRequest.setPage(activityPage);
        return assnActivityRequest;
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
        }
    }
}
