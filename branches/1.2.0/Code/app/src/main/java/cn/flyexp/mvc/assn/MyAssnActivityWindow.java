package cn.flyexp.mvc.assn;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by tanxinye on 2016/10/1.
 */
public class MyAssnActivityWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private LoadMoreRecyclerView rv_assnactivity;
    private int page = 1;
    private boolean isResponse;
    private int aid;
    private AssnActivityAdapter assnActivityAdapter;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> data = new ArrayList<>();
    private ImageView iv_publish;

    public MyAssnActivityWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    public void setAid(int aid, int level) {
        this.aid = aid;
        if (level == 0) {
            iv_publish.setVisibility(GONE);
        } else {
            iv_publish.setVisibility(VISIBLE);
        }
        getMyAssnActivity();
    }

    private void getMyAssnActivity() {
        MyAssnActivityRequest myAssnActivityRequest = new MyAssnActivityRequest();
        myAssnActivityRequest.setAssociation_id(aid);
        myAssnActivityRequest.setPage(page);
        callBack.getMyAssnActivity(myAssnActivityRequest);
    }

    private void initView() {
        setContentView(R.layout.window_myassn_activity);
        findViewById(R.id.iv_back).setOnClickListener(this);

        iv_publish = (ImageView) findViewById(R.id.iv_publish);
        iv_publish.setOnClickListener(this);

        assnActivityAdapter = new AssnActivityAdapter(getContext(), data);
        assnActivityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.activityDetailEnter(data.get(position));
            }
        });
        rv_assnactivity = (LoadMoreRecyclerView) findViewById(R.id.rv_assnactivity);
        rv_assnactivity.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_assnactivity.setNestedScrollingEnabled(false);
        rv_assnactivity.setHasFixedSize(false);
        rv_assnactivity.setAdapter(assnActivityAdapter);
        rv_assnactivity.setItemAnimator(new DefaultItemAnimator());
        rv_assnactivity.setFootLoadingView(ProgressView.BallPulse);
        rv_assnactivity.setFootEndView("没有更多活动了~");
        rv_assnactivity.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                page++;
                getMyAssnActivity();
            }
        });
    }

    public void responseData(ArrayList<AssnActivityResponse.AssnActivityResponseData> responseData) {
        data.clear();
        data.addAll(responseData);
        assnActivityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.iv_publish:
                callBack.activityPublishEnter(aid);
                break;
        }
    }
}
