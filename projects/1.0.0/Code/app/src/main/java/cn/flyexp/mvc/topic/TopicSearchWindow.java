package cn.flyexp.mvc.topic;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.TopicAdapter;
import cn.flyexp.adapter.HotComplaintsAdapter;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.entity.TopicSearchRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class TopicSearchWindow extends AbstractWindow implements View.OnClickListener {

    private TopicViewCallBack callBack;
    private TopicAdapter topicAdapter;
    private SearchView searchView;
    private HotComplaintsAdapter hotComplaintsAdapter;
    private RecyclerView rv_searchTopic;
    private ArrayList<TopicResponseData> data = new ArrayList<>();

    public TopicSearchWindow(TopicViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        hideSoftInput();
    }

    private void initView() {
        View layout = getView(R.layout.window_topic_search);
        layout.findViewById(R.id.iv_back).setOnClickListener(this);

        hotComplaintsAdapter = new HotComplaintsAdapter(getContext(), null);
        hotComplaintsAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        final RecyclerView rv_hotcomplaints = (RecyclerView) layout.findViewById(R.id.rv_hotcomplaints);
        rv_hotcomplaints.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_hotcomplaints.setAdapter(hotComplaintsAdapter);
        rv_hotcomplaints.setItemAnimator(new DefaultItemAnimator());

        topicAdapter = new TopicAdapter(getContext(), data, callBack);
        topicAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.detailEnter(null);
            }
        });
        rv_searchTopic = (RecyclerView) layout.findViewById(R.id.rv_complaints_result);
        rv_searchTopic.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_searchTopic.setAdapter(topicAdapter);
        rv_searchTopic.setItemAnimator(new DefaultItemAnimator());

        searchView = (SearchView) layout.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TopicSearchRequest topicSearchRequest = new TopicSearchRequest();
                topicSearchRequest.setPage(1);
                topicSearchRequest.setQ(query);
                callBack.searchTopic(topicSearchRequest);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View v = ((Activity) getContext()).getCurrentFocus();
            if (v == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }


    public void searchTopicResponse(ArrayList<TopicResponseData> topics) {
        data.addAll(topics);
        topicAdapter.notifyDataSetChanged();
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
