package cn.flyexp.mvc.mine;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.IntegralRecordAdapter;
import cn.flyexp.adapter.WalletDetailAdapter;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.IntegralRecordResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by txy on 2016/8/20 0020.
 */
public class IntegralRecordWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private int page = 1;
    private boolean isResponse = false;
    private ArrayList<IntegralRecordResponse.IntegralRecordResponseData> data = new ArrayList<>();
    private IntegralRecordAdapter recordAdapter;
    private LoadMoreRecyclerView rv_integral;

    public IntegralRecordWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.integralRecordRequest(getIntegralRecordRequest());
    }

    private void initView() {
        setContentView(R.layout.window_myintegral_record);
        findViewById(R.id.iv_back).setOnClickListener(this);

        recordAdapter = new IntegralRecordAdapter(getContext(), data);
        rv_integral = (LoadMoreRecyclerView) findViewById(R.id.rv_integral);
        rv_integral.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_integral.setAdapter(recordAdapter);
        rv_integral.setItemAnimator(new DefaultItemAnimator());
        rv_integral.setFootLoadingView(ProgressView.BallPulse);
        rv_integral.setFootEndView("没有更多了~");
        rv_integral.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                page++;
                callBack.integralRecordRequest(getIntegralRecordRequest());
            }
        });
    }

    private IntegralRecordRequest getIntegralRecordRequest() {
        isResponse = false;
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return null;
        }
        IntegralRecordRequest integralRecordRequest = new IntegralRecordRequest();
        integralRecordRequest.setToken(token);
        integralRecordRequest.setPage(page);
        return integralRecordRequest;
    }

    public void responseData(ArrayList<IntegralRecordResponse.IntegralRecordResponseData> responseDatas) {
        data.addAll(responseDatas);
        recordAdapter.notifyDataSetChanged();
        isResponse = true;
    }

    public void responseNoneData() {
        isResponse = true;
        rv_integral.loadMoreEnd();
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
