package cn.flyexp.window.topic;

import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.adapter.TopicAdapter;
import cn.flyexp.callback.topic.TopicCallback;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.presenter.topic.TopicPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class TopicWindow extends BaseWindow implements NotifyManager.Notify, TopicCallback.ResponseCallback{

    @InjectView(R.id.rv_topic)
    LoadMoreRecyclerView rvTopic;

    private TopicPresenter topicPresenter;
    private TopicAdapter topicAdapter;
    private ArrayList<TopicResponseData> datas = new ArrayList<>();
    private View layoutTopic;
    private LinearLayoutManager linearLayoutManager;
    private int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.window_topic;
    }

    public TopicWindow(){
        topicPresenter = new TopicPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TOPIC, this);
        initView();
    }

    @Override
    public void onStart() {
        readyTopicList();
    }

    private void initView() {
        topicAdapter = new TopicAdapter(getContext(),datas);
        topicAdapter.setOnTopicClickListener(new TopicAdapter.OnTopicClickListener() {
            @Override
            public void onLongClick() {

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
    }

    private void readyTopicList() {
        Log.e("TAG","MY_ResponseTopList");
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

        datas.addAll(response.getData());
        topicAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {

    }
}
