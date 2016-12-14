package cn.flyexp.window.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.MessageAdapter;
import cn.flyexp.callback.user.MessageCallback;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.presenter.mine.MessagePresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MessageWindow extends BaseWindow implements MessageCallback.ResponseCallback {

    @InjectView(R.id.rv_message)
    LoadMoreRecyclerView rvMessage;

    private ArrayList<MessageResponse.MessageResponseData> datas = new ArrayList<>();
    private MessagePresenter messagePresenter;
    private int page = 1;
    private MessageAdapter messageAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_message;
    }

    public MessageWindow() {
        messagePresenter = new MessagePresenter(this);

        initView();
        readyMessage();
    }

    private void initView() {
        messageAdapter = new MessageAdapter(getContext(), datas);
        rvMessage.setAdapter(messageAdapter);
        rvMessage.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMessage.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                readyMessage();
            }
        });
    }

    private void readyMessage() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            MessageRequest messageRequest = new MessageRequest();
            messageRequest.setToken(token);
            messageRequest.setNum(20);
            messageRequest.setPage(page);
            messagePresenter.requestMessage(messageRequest);
        }
    }

    @OnClick(R.id.img_back)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
        }
    }

    @Override
    public void responseMessage(MessageResponse response) {
        datas.addAll(response.getData());
        messageAdapter.notifyDataSetChanged();
    }
}
