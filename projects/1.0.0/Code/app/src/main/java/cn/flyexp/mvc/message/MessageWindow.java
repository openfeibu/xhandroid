package cn.flyexp.mvc.message;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.MessageAdapter;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;


/**
 * Created by txy on 2016/6/6.
 */
public class MessageWindow extends AbstractWindow {

    private SwipeRefreshLayout refreshlayout;
    private MessageAdapter messageAdapter;
    private MessageViewCallBack callBack;
    private ArrayList<MessageResponse.MessageResponseData> data = new ArrayList<>();
    private ContentLoadingProgressBar progressBar;
    private TextView tv_state;
    private LoadMoreRecyclerView rv_message;
    private int page = 1;
    private boolean isUpLoading = true;
    private boolean isResponse;

    public MessageWindow(MessageViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getMessageList(getMessageRequest());
    }

    private void initView() {
        setContentView(R.layout.window_message);
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
                callBack.getMessageList(getMessageRequest());
            }
        });
        messageAdapter = new MessageAdapter(getContext(), data);
        rv_message = (LoadMoreRecyclerView) findViewById(R.id.rv_message);
        rv_message.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_message.setAdapter(messageAdapter);
        rv_message.setItemAnimator(new DefaultItemAnimator());
        rv_message.setFootLoadingView(ProgressView.BallPulse);
        rv_message.setFootEndView("没有更多纸条了~");
        rv_message.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                page++;
                isUpLoading = false;
                callBack.getMessageList(getMessageRequest());
            }
        });
    }

    public void refreshData(){
        callBack.getMessageList(getMessageRequest());
    }

    public MessageRequest getMessageRequest() {
        isResponse = false;
        String token = getStringByPreference("token");
        if (token.equals("")) {
            return null;
        }
        MessageRequest messageRequest = new MessageRequest();
        messageRequest.setToken(token);
        messageRequest.setNum(20);
        messageRequest.setPage(page);
        return messageRequest;
    }

    public void messageResponse(ArrayList<MessageResponse.MessageResponseData> messageResponseDatas) {
        //隐藏加载提示
        progressBar.hide();
        tv_state.setVisibility(View.GONE);
        //上拉刷新清空数据 下拉加载追加数据
        if (isUpLoading) {
            data.clear();
        } else {
            //追加的数据为空 显示没有更多数据
            if (messageResponseDatas.size() == 0) {
                rv_message.loadMoreEnd();
            } else {
                rv_message.loadMoreComplete();
            }
        }

        data.addAll(messageResponseDatas);
        //追加的数据为空显示友好提示
        if (data.size() == 0) {
            tv_state.setText("暂无纸条");
            tv_state.setVisibility(View.VISIBLE);
            rv_message.setVisibility(View.GONE);
        } else {
            rv_message.setVisibility(View.VISIBLE);
            messageAdapter.notifyDataSetChanged();
        }
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }

    /**
     * 网络连接失败或者请求失败的友好提示
     */
    public void loadingFailure() {
        progressBar.hide();
        tv_state.setText("数据加载失败...");
        tv_state.setVisibility(View.VISIBLE);
        rv_message.setVisibility(View.GONE);
        rv_message.loadMoreComplete();
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }

    public void noLogin() {
        progressBar.hide();
        tv_state.setText("你还没登录呢...");
        tv_state.setVisibility(View.VISIBLE);
        rv_message.setVisibility(View.GONE);
        rv_message.loadMoreComplete();
        refreshlayout.setRefreshing(false);
    }

}
