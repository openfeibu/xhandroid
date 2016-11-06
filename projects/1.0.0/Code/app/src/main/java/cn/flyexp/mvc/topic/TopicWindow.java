package cn.flyexp.mvc.topic;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.TopicAdapter;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;


/**
 * Created by txy on 2016/6/6.
 */
public class TopicWindow extends AbstractWindow implements View.OnClickListener {

    private SwipeRefreshLayout refreshlayout;
    private TopicViewCallBack callBack;
    private TopicAdapter topicAdapter;
    private FloatingActionButton fab_top;
    private LoadMoreRecyclerView rv_topic;
    private ArrayList<TopicResponseData> data = new ArrayList<>();
    private ContentLoadingProgressBar progressBar;
    private TextView tv_state;
    private int page = 1;
    private boolean isUpLoading = true;
    private boolean isResponse;
    private boolean isVis = true;

    public TopicWindow(TopicController controller) {
        super(controller);
        this.callBack = controller;
        initView();
        callBack.getTopicList(getTopicListRequest());
    }

    private void initView() {
        setContentView(R.layout.window_topic);
        findViewById(R.id.fab_publish).setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
        fab_top = (FloatingActionButton) findViewById(R.id.fab_top);
        fab_top.setVisibility(View.GONE);
        fab_top.setOnClickListener(this);

        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        progressBar.show();
        tv_state = (TextView) findViewById(R.id.tv_state);
        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshlayout.setColorSchemeResources(R.color.light_blue);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isResponse) {
                    return;
                }
                page = 1;
                isUpLoading = true;
                rv_topic.loadMoreComplete();
                callBack.getTopicList(getTopicListRequest());
            }
        });
        topicAdapter = new TopicAdapter(getContext(), data, callBack);
        topicAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.detailEnter(data.get(position));
            }
        });
        topicAdapter.setOnLike(new TopicAdapter.OnLike() {
            @Override
            public void onlike(int pos) {
                TopicResponseData topicResponseData = data.get(pos);
                topicResponseData.setFavorited(1);
                topicResponseData.setFavourites_count(topicResponseData.getFavourites_count() + 1);
                ThumbUpRequest thumbUpRequest = new ThumbUpRequest();
                thumbUpRequest.setTopic_id(topicResponseData.getTid());
                callBack.thumbUp(thumbUpRequest);
            }
        });

        rv_topic = (LoadMoreRecyclerView) findViewById(R.id.rv_topic);
        rv_topic.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_topic.setAdapter(topicAdapter);
        rv_topic.setItemAnimator(new DefaultItemAnimator());
        rv_topic.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                boolean isSignificantDelta = Math.abs(dy) > 10;
                if (isSignificantDelta) {
                    if (dy > 0 && isVis) {
                        fab_top.setVisibility(View.VISIBLE);
                        isVis = false;
                    } else if (dy < 0 && !isVis) {
                        fab_top.setVisibility(View.GONE);
                        isVis = true;
                    }
                }

            }
        });
        rv_topic.setFootLoadingView(ProgressView.BallPulse);
        rv_topic.setFootEndView("没有话题了~");
        rv_topic.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                page++;
                isUpLoading = false;
                callBack.getTopicList(getTopicListRequest());
            }
        });

    }

    public void thumbUpResponse() {
        topicAdapter.notifyDataSetChanged();
    }

    private TopicListRequest getTopicListRequest() {
        isResponse = false;
        TopicListRequest topicListRequest = new TopicListRequest();
        String token = getStringByPreference("token");
        topicListRequest.setToken(token);
        topicListRequest.setPage(page);
        return topicListRequest;
    }

    public void refreshTopic() {
        isUpLoading = true;
        callBack.getTopicList(getTopicListRequest());
    }

    public void topicResponse(ArrayList<TopicResponseData> topicResponseDatas) {
        //隐藏加载提示
        progressBar.hide();
        tv_state.setVisibility(View.GONE);
        //上拉刷新清空数据 下拉加载追加数据
        if (isUpLoading) {
            data.clear();
        } else {
            //追加的数据为空 显示没有更多数据
            this.data.addAll(topicResponseDatas);
            if (topicResponseDatas.size() == 0) {
                rv_topic.loadMoreEnd();
            } else {
                rv_topic.loadMoreComplete();
            }
        }

        data.addAll(topicResponseDatas);
        //追加的数据为空显示友好提示
        if (data.size() == 0) {
            tv_state.setText("暂无话题");
            tv_state.setVisibility(View.VISIBLE);
            rv_topic.setVisibility(View.GONE);
        } else {
            rv_topic.setVisibility(View.VISIBLE);
            topicAdapter.notifyDataSetChanged();
        }
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }

    public void loadingFailure() {
        progressBar.hide();
        tv_state.setText("数据加载失败...");
        tv_state.setVisibility(View.VISIBLE);
        rv_topic.setVisibility(View.GONE);
        rv_topic.loadMoreComplete();
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_publish:
                String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                } else {
                    callBack.publishEnter();
                }
                break;
            case R.id.iv_search:
                callBack.searchEnter();
                break;
            case R.id.fab_top:
                rv_topic.scrollToPosition(0);
                fab_top.setVisibility(View.GONE);
                isVis = true;
                break;
        }
    }
}
