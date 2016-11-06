package cn.flyexp.mvc.topic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.ReplyListAdapter;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.ReplyListResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class ReplyListWindow extends AbstractWindow {

    private ArrayList<ReplyListResponse.ReplyListResponseData> data = new ArrayList<>();
    private TopicViewCallBack callBack;
    private ReplyListAdapter replyListAdapter;

    public ReplyListWindow(TopicViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (TextUtils.isEmpty(token)) {
            callBack.loginWindowEnter();
            return;
        }
        callBack.replyListRequest(token);
    }

    private void initView() {
        setContentView(R.layout.window_replylist);
        findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideWindow(true);
            }
        });
        replyListAdapter = new ReplyListAdapter(getContext(), data);
        replyListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.replyEnter(data.get(position));
            }
        });
        RecyclerView rv_replylist = (RecyclerView) findViewById(R.id.rv_replylist);
        rv_replylist.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_replylist.setAdapter(replyListAdapter);
    }

    public void responseData(ArrayList<ReplyListResponse.ReplyListResponseData> data) {
        this.data.clear();
        this.data.addAll(data);
        replyListAdapter.notifyDataSetChanged();
    }
}
