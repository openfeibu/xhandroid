package cn.flyexp.window.assn;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.AssnListAdapter;
import cn.flyexp.callback.assn.AssnListCallback;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnListRequest;
import cn.flyexp.entity.AssnListResponse;
import cn.flyexp.entity.PageRequest;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.assn.AssnListPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/28.
 */
public class AssnWindow extends BaseWindow implements AssnListCallback.ResponseCallback {

    @InjectView(R.id.tablayout)
    TabLayout tabLayout;
    @InjectView(R.id.vp_assn)
    ViewPager vpAssn;

    private String[] title = new String[]{getContext().getResources().getString(R.string.campus_assn),
            getContext().getResources().getString(R.string.assn_activity)};
    private View[] layouts = new View[2];
    private TextView tvAssnMsg;
    private LoadMoreRecyclerView rvAssnList;
    private LoadMoreRecyclerView rvAssnActi;
    private AssnListAdapter assnListAdapter;
    private AssnActivityAdapter assnActivityAdapter;
    private AssnListPresenter assnPresenter;
    private int assnListPage = 1;
    private int assnActiPage = 1;
    private ArrayList<AssnListResponse.AssnListResponseData.AssnResponseData> assnListDatas = new ArrayList<>();
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> assnActiDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn;
    }

    public AssnWindow() {
        assnPresenter = new AssnListPresenter(this);
        initView();
        readyAssnList();
        readyAssnActi();
    }

    private void initView() {
        initAssnListView();
        initAssnActiView();
        vpAssn.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(layouts[position]);
                return layouts[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(layouts[position]);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });
        tabLayout.setupWithViewPager(vpAssn);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initAssnListView() {
        layouts[0] = LayoutInflater.from(getContext()).inflate(R.layout.layout_assnlist, null);
        assnListAdapter = new AssnListAdapter(getContext(), assnListDatas);
        assnListAdapter.setOnItemClickLinstener(new AssnListAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                int myLevel = assnListDatas.get(position).getMylevel();
                Bundle bundle = new Bundle();
                bundle.putInt("aid", assnListDatas.get(position).getAid());
                if (myLevel == -1 || TextUtils.isEmpty(token)) {
                    bundle.putString("aname", assnListDatas.get(position).getAname());
                    openWindow(WindowIDDefine.WINDOW_ASSN_DETAIL, bundle);
                } else {
                    bundle.putInt("level", assnListDatas.get(position).getMylevel());
                    openWindow(WindowIDDefine.WINDOW_MYASSN_DETAIL, bundle);
                }
            }
        });
        tvAssnMsg = (TextView) layouts[0].findViewById(R.id.tv_assnmsg);
        rvAssnList = (LoadMoreRecyclerView) layouts[0].findViewById(R.id.rv_assnlist);
        rvAssnList.setAdapter(assnListAdapter);
        rvAssnList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAssnList.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                assnListPage++;
                readyAssnList();
            }
        });
    }

    private void initAssnActiView() {
        layouts[1] = LayoutInflater.from(getContext()).inflate(R.layout.layout_loadmorerecycler, null);
        assnActivityAdapter = new AssnActivityAdapter(getContext(), assnActiDatas);
        assnActivityAdapter.setOnItemClickLinstener(new AssnActivityAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("assnacti", assnActiDatas.get(position));
                openWindow(WindowIDDefine.WINDOW_ASSNACTI_DETAIL, bundle);
            }
        });
        rvAssnActi = (LoadMoreRecyclerView) layouts[1].findViewById(R.id.loadmoreview);
        rvAssnActi.setAdapter(assnActivityAdapter);
        rvAssnActi.addItemDecoration(new DividerItemDecoration(getContext()));
        rvAssnActi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAssnList.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                assnListPage++;
                readyAssnList();
            }
        });

    }

    private void readyAssnList() {
        AssnListRequest assnListRequest = new AssnListRequest();
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            assnListRequest.setToken(token);
        }
        assnListRequest.setPage(assnListPage);
        assnPresenter.requestAssnList(assnListRequest);
    }

    private void readyAssnActi() {
        assnPresenter.requestAssnActivity(new PageRequest(assnActiPage));
    }

    @OnClick(R.id.img_back)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
        }
    }

    @Override
    public void responseAssnActivity(AssnActivityResponse response) {
        assnActiDatas.addAll(response.getData());
        assnActivityAdapter.notifyDataSetChanged();
    }

    @Override
    public void responseAssnList(AssnListResponse response) {
        AssnListResponse.AssnListResponseData responseData = response.getData();
        tvAssnMsg.setVisibility(VISIBLE);
        tvAssnMsg.setText(String.format(getResources().getString(R.string.assn_msg), responseData.getAssociation_sum(), responseData.getActivity_sum()));
        assnListDatas.addAll(responseData.getAssociations());
        assnListAdapter.notifyDataSetChanged();
    }
}
