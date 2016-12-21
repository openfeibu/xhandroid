package cn.flyexp.window.topic;

import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.TopicAdapter;
import cn.flyexp.callback.topic.MyTopicCallback;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.topic.MyTopicPresenter;
import cn.flyexp.presenter.topic.TopicPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/11/21.
 */
public class MyTopicWindow extends BaseWindow implements NotifyManager.Notify, MyTopicCallback.ResponseCallback {

    @InjectView(R.id.rv_topic)
    LoadMoreRecyclerView rvTopic;

    private MyTopicPresenter myTopicPresenter;
    private TopicAdapter topicAdapter;
    private ArrayList<TopicResponseData> datas = new ArrayList<>();
    private View layoutTopic;
    private int page = 1;
    private SwipeRefreshLayout refreshLayout;
    private boolean isRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.window_mytopic;
    }

    public MyTopicWindow() {
        myTopicPresenter = new MyTopicPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TOPIC, this);
        initView();
        readyMyTopicList();
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
        rvTopic.addItemDecoration(new DividerItemDecoration(getContext()));
        rvTopic.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTopic.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                readyMyTopicList();
            }
        });


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.light_blue));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                readyMyTopicList();
            }
        });
    }

    private void readyMyTopicList() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        TopicListRequest topicListRequest = new TopicListRequest();
        if (!TextUtils.isEmpty(token)) {
            topicListRequest.setToken(token);
        }
        topicListRequest.setPage(page);
        myTopicPresenter.requestMyTopicList(topicListRequest);
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_TOPIC) {
            isRefresh = true;
            readyMyTopicList();
        }
    }

    @Override
    public void responseMyTopicList(TopicListResponse response) {
        if (isRefresh) {
            datas.clear();
            isRefresh = false;
        }
        datas.addAll(response.getData());
        topicAdapter.notifyDataSetChanged();
    }

    @Override
    public void requestFailure() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void requestFinish() {
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
