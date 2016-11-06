package cn.flyexp.mvc.mine;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.WalletDetailAdapter;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by txy on 2016/8/21 0021.
 */
public class WalletDetailWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private int page = 1;
    private boolean isResponse = true;
    private WalletDetailAdapter walletDetailAdapter;
    private LoadMoreRecyclerView rv_wallet;
    private ArrayList<WalletDetailResponse.WalletDetailResponseData> data = new ArrayList<>();

    public WalletDetailWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getWalletDetail(getWalletDetailRequest());
    }

    private WalletDetailRequest getWalletDetailRequest() {
        String token = getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return null;
        }
        WalletDetailRequest walletDetailRequest = new WalletDetailRequest();
        walletDetailRequest.setToken(token);
        walletDetailRequest.setPage(page);
        return walletDetailRequest;
    }

    private void initView() {
        setContentView(R.layout.window_wallet_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);

        walletDetailAdapter = new WalletDetailAdapter(getContext(), data);
        rv_wallet = (LoadMoreRecyclerView) findViewById(R.id.rv_wallet);
        rv_wallet.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_wallet.setAdapter(walletDetailAdapter);
        rv_wallet.setItemAnimator(new DefaultItemAnimator());
        rv_wallet.setFootLoadingView(ProgressView.BallPulse);
        rv_wallet.setFootEndView("没有更多了~");
        rv_wallet.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                page++;
                isResponse = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }

    public void responseData(ArrayList<WalletDetailResponse.WalletDetailResponseData> responseData) {
        data.addAll(responseData);
        walletDetailAdapter.notifyDataSetChanged();
        isResponse = true;
    }
}
