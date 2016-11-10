package cn.flyexp.window.topic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.adapter.TopicAdapter;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class TopicWindow extends BaseWindow {

    @InjectView(R.id.recyview)
    RecyclerView recyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.window_topic;
    }

    public TopicWindow() {
        initView();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new TopicAdapter(getContext()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
    }

}
