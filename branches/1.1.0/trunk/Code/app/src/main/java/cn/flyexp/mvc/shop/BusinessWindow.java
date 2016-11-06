package cn.flyexp.mvc.shop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.flyexp.R;
import cn.flyexp.adapter.BusinessAdapter;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by txy on 2016/7/17 0017.
 */
public class BusinessWindow extends AbstractWindow implements View.OnClickListener {

    private FloatingActionButton fab_top;
    private SwipeRefreshLayout refreshlayout;
    private BusinessAdapter businessAdapter;
    private BusinessViewCallBack callBack;
    private SearchView searchView;

    public BusinessWindow(BusinessViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        hideSoftInput();
    }

    private void initView() {
        View layout = getView(R.layout.window_business);
        layout.findViewById(R.id.iv_publish).setOnClickListener(this);
        layout.findViewById(R.id.iv_back).setOnClickListener(this);
        refreshlayout = (SwipeRefreshLayout) layout.findViewById(R.id.refreshlayout);
        refreshlayout.setColorSchemeColors(R.color.google_blue, R.color.google_blue, R.color.google_blue, R.color.google_blue);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshlayout.setRefreshing(false);
            }
        });
        businessAdapter = new BusinessAdapter(getContext(), null);
        businessAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.detailEnter();
            }
        });
        final LoadMoreRecyclerView rv_business = (LoadMoreRecyclerView) layout.findViewById(R.id.rv_business);
        rv_business.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_business.setAdapter(businessAdapter);
        rv_business.setItemAnimator(new DefaultItemAnimator());
        rv_business.setFootLoadingView(ProgressView.BallPulse);
        rv_business.setFootEndView("没有更多了~");
        rv_business.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rv_business.loadMoreEnd();
                    }
                }, 3000);
            }
        });

        searchView = (SearchView) layout.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.onActionViewExpanded();
        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
        textView.setTextColor(Color.WHITE);
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View v = ((Activity) getContext()).getCurrentFocus();
            if (v == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_publish:
                callBack.publishEnter();
                break;
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }
}
