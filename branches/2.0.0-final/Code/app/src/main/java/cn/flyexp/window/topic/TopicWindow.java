package cn.flyexp.window.topic;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.TopicAdapter;
import cn.flyexp.callback.topic.TopicCallback;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.topic.TopicPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class TopicWindow extends BaseWindow implements NotifyManager.Notify, TopicCallback.ResponseCallback {

    @InjectView(R.id.rv_topic)
    LoadMoreRecyclerView rvTopic;

    private TopicPresenter topicPresenter;
    private TopicAdapter topicAdapter;
    private ArrayList<TopicResponseData> datas = new ArrayList<>();
    private View layoutTopic;
    private int page = 1;
    private SwipeRefreshLayout refreshLayout;
    private boolean isRefresh;
    private boolean isRequesting;

    @Override
    protected int getLayoutId() {
        return R.layout.window_topic;
    }

    public TopicWindow() {
        topicPresenter = new TopicPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TOPIC, this);
        initView();
    }

    @Override
    public void onStart() {
        readyTopicList();
    }

    private void initView() {
        topicAdapter = new TopicAdapter(getContext(), datas);
        topicAdapter.setOnItemClickLinstener(new TopicAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("topicDetail", datas.get(position));
                openWindow(WindowIDDefine.WINDOW_TOPIC_DETAIL, bundle);
            }
        });
        rvTopic.setAdapter(topicAdapter);
        rvTopic.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTopic.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                readyTopicList();
            }
        });


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.light_blue));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                readyTopicList();
            }
        });
    }

    private void readyTopicList() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        TopicListRequest topicListRequest = new TopicListRequest();
        if (!TextUtils.isEmpty(token)) {
            topicListRequest.setToken(token);
        }
        topicListRequest.setPage(page);
        topicPresenter.requestTopicList(topicListRequest);
    }

    @Override
    public void responseTopicList(TopicListResponse response) {
        if(isRefresh) {
            datas.clear();
            isRefresh = false;
        }
        datas.addAll(response.getData());
        topicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {

    }

    @Override
    public void requestFinish() {
        isRequesting = false;
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @OnClick({R.id.img_publish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_publish:
                openWindow(WindowIDDefine.WINDOW_TOPIC_PUBLISH);
                break;
        }
    }
}
