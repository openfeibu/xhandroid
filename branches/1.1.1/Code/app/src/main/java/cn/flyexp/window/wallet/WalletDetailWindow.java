package cn.flyexp.window.wallet;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.WalletDetailAdapter;
import cn.flyexp.callback.wallet.WalletDetailCallback;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.presenter.wallet.WalletDetailPresenter;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class WalletDetailWindow extends BaseWindow implements WalletDetailCallback.ResponseCallback {

    @InjectView(R.id.rv_walletdetail)
    LoadMoreRecyclerView loadMoreRecyclerView;

    private int page = 1;
    private ArrayList<WalletDetailResponse.WalletDetailResponseData> datas = new ArrayList<>();
    private WalletDetailPresenter walletDetailPresenter;
    private WalletDetailAdapter walletDetailAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_walletdetail;
    }

    public WalletDetailWindow() {
        walletDetailPresenter = new WalletDetailPresenter(this);
        initView();
        readyWalletDetail();
    }

    private void initView() {
        walletDetailAdapter = new WalletDetailAdapter(getContext(), datas);
        loadMoreRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        loadMoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadMoreRecyclerView.setAdapter(walletDetailAdapter);
        loadMoreRecyclerView.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                readyWalletDetail();
            }
        });
    }

    private void readyWalletDetail() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        WalletDetailRequest walletDetailRequest = new WalletDetailRequest();
        walletDetailRequest.setToken(token);
        walletDetailRequest.setPage(page);
        walletDetailPresenter.requestWalletDetail(walletDetailRequest);
    }

    @OnClick({R.id.img_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
        }
    }

    @Override
    public void responseWalletDetail(WalletDetailResponse response) {
        datas.addAll(response.getData());
        walletDetailAdapter.notifyDataSetChanged();
    }
}
