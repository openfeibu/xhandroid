package cn.flyexp.mvc.shop;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.BusinessAdapter;
import cn.flyexp.adapter.ShopAdapter;
import cn.flyexp.entity.ShopRequest;
import cn.flyexp.entity.ShopResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class ShopWindow_temp extends AbstractWindow implements View.OnClickListener {

    private ShopViewCallBack callBack;
    private View[] views;
    private RadioButton btn_shop;
    private RadioButton btn_oldgoods;
    private BusinessAdapter businessAdapter;
    private ShopAdapter shopAdapter;
    private LoadMoreRecyclerView shopRecyclerView;
    private LoadMoreRecyclerView oldgoodsRecyclerView;
    private ViewPager vp_shop;
    private int shopPage = 1;
    private int oldgoodsPage = 1;
    private boolean isResponse = true;
    private ContentLoadingProgressBar shopProgressBar;
    private ContentLoadingProgressBar oldgoodsProgressBar;
    private TextView shopState;
    private TextView oldgoodsState;
    private ArrayList<ShopResponse.ShopResponseData> shopData = new ArrayList<>();

    public ShopWindow_temp(ShopViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getShopListRequest(getShopRequest());
    }

    private void initView() {
        View layout = getView(R.layout.window_shop_temp);
        layout.findViewById(R.id.iv_back).setOnClickListener(this);
        btn_shop = (RadioButton) layout.findViewById(R.id.btn_shop);
        btn_oldgoods = (RadioButton) layout.findViewById(R.id.btn_oldgoods);
        btn_shop.setOnClickListener(this);
        btn_oldgoods.setOnClickListener(this);

        views = new View[2];
        views[0] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview, null);
        views[1] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview, null);
        shopProgressBar = (ContentLoadingProgressBar) views[0].findViewById(R.id.progressBar);
        oldgoodsProgressBar = (ContentLoadingProgressBar) views[1].findViewById(R.id.progressBar);
        shopProgressBar.show();
        oldgoodsProgressBar.show();
        shopState = (TextView) views[0].findViewById(R.id.tv_state);
        oldgoodsState = (TextView) views[1].findViewById(R.id.tv_state);
        shopRecyclerView = (LoadMoreRecyclerView) views[0].findViewById(R.id.recyclerView);
        oldgoodsRecyclerView = (LoadMoreRecyclerView) views[1].findViewById(R.id.recyclerView);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        oldgoodsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        shopAdapter = new ShopAdapter(getContext(), shopData);
        businessAdapter = new BusinessAdapter(getContext(), null);
        shopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.detailEnter();
            }
        });
        businessAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.detailEnter();
            }
        });
        shopRecyclerView.setItemAnimator(new DefaultItemAnimator());
        oldgoodsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        shopRecyclerView.setAdapter(shopAdapter);
        oldgoodsRecyclerView.setAdapter(businessAdapter);

        shopRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        shopRecyclerView.setFootEndView("没有更多店铺了~");
        shopRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                shopPage++;
            }
        });

        oldgoodsRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        oldgoodsRecyclerView.setFootEndView("没有更多商品了~");
        oldgoodsRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                oldgoodsPage++;
            }
        });

        vp_shop = (ViewPager) layout.findViewById(R.id.vp_shop);
        vp_shop.setAdapter(new PagerAdapter() {
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
        vp_shop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btn_shop.setChecked(true);
                    btn_oldgoods.setChecked(false);
                } else {
                    btn_shop.setChecked(false);
                    btn_oldgoods.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public ShopRequest getShopRequest() {
        ShopRequest shopRequest = new ShopRequest();
        shopRequest.setPage(shopPage);
        return shopRequest;
    }

    public void shopListResponse(ArrayList<ShopResponse.ShopResponseData> responseDatas) {
        shopProgressBar.hide();
        shopState.setVisibility(View.GONE);
        if (responseDatas.size() == 0) {
            shopRecyclerView.loadMoreEnd();
        } else {
            shopRecyclerView.loadMoreComplete();
        }

        shopData.addAll(responseDatas);
        if (shopData.size() == 0) {
            shopState.setText("暂无店铺入驻");
            shopState.setVisibility(View.VISIBLE);
            shopRecyclerView.setVisibility(View.GONE);
        } else {
            shopRecyclerView.setVisibility(View.VISIBLE);
            shopAdapter.notifyDataSetChanged();
        }
        isResponse = true;
    }

    public void shopLoadingFailure() {
        shopProgressBar.hide();
        shopState.setText("数据加载失败...");
        shopState.setVisibility(View.VISIBLE);
        shopRecyclerView.setVisibility(View.GONE);
        shopRecyclerView.loadMoreComplete();
        isResponse = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_shop:
                vp_shop.setCurrentItem(0);
                break;
            case R.id.btn_oldgoods:
                vp_shop.setCurrentItem(1);
                break;
        }
    }


}
