package cn.flyexp.mvc.task;

import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.TaskAdapter;
import cn.flyexp.entity.TaskRequest;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;


/**
 * Created by txy on 2016/6/5.
 */
public class TaskWindow extends AbstractWindow implements View.OnClickListener {

    private ArrayList<OrderResponse.OrderResponseData> data = new ArrayList<OrderResponse
            .OrderResponseData>();

    private TaskViewCallBack callBack;
    private TaskAdapter taskAdapter;
    private LoadMoreRecyclerView rv_task;
    private SwipeRefreshLayout refreshlayout;
    private int page = 1;
    private boolean isUpLoading = true;
    private ContentLoadingProgressBar progressBar;
    private TextView tv_state;
    private boolean isResponse;

    public TaskWindow(TaskViewCallBack orderViewCallBack) {
        super(orderViewCallBack);
        this.callBack = orderViewCallBack;
        initView();
        callBack.getOrderList(getTaskRequest());
    }

    private void initView() {
        setContentView(R.layout.window_task);
        findViewById(R.id.iv_publish).setOnClickListener(this);
        findViewById(R.id.tv_my_task).setOnClickListener(this);

        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        progressBar.show();
        progressBar.hide();

        tv_state = (TextView) findViewById(R.id.tv_state);

        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshlayout.setColorSchemeResources(R.color.light_blue);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isResponse || !canRequest()) {
                    refreshlayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.setRefreshing(false);
                        }
                    }, 200);
                    return;
                }
                setRequestTimeNow();
                page = 1;
                isUpLoading = true;
                rv_task.loadMoreComplete();
                callBack.userCount("task");
                callBack.getOrderList(getTaskRequest());
            }
        });
        taskAdapter = new TaskAdapter(getContext(), data);
        taskAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(data.get(position).getStatus().equals("new")){
                    callBack.detailEnter(data.get(position));
                }
            }
        });
        rv_task = (LoadMoreRecyclerView) findViewById(R.id.rv_task);
        rv_task.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_task.setAdapter(taskAdapter);
        rv_task.setItemAnimator(new DefaultItemAnimator());
        rv_task.setFootLoadingView(ProgressView.BallPulse);
        rv_task.setFootEndView("没有更多任务了~");
        rv_task.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                page++;
                isUpLoading = false;
                callBack.userCount("task");
                callBack.getOrderList(getTaskRequest());
            }
        });
        rv_task.setVisibility(VISIBLE);
    }

    public void refreshData() {
        callBack.getOrderList(getTaskRequest());
    }

    public TaskRequest getTaskRequest() {
        isResponse = false;
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setPage(page);
        return taskRequest;
    }

    public void orderListResponse(ArrayList<OrderResponse.OrderResponseData> orderResponseDatas) {
        //隐藏加载提示
        progressBar.hide();
        tv_state.setVisibility(View.GONE);
        //上拉刷新清空数据 下拉加载追加数据
        if (isUpLoading) {
            data.clear();
        } else {
            //追加的数据为空 显示没有更多数据
            this.data.addAll(orderResponseDatas);
            if (orderResponseDatas.size() == 0) {
                rv_task.loadMoreEnd();
            } else {
                rv_task.loadMoreComplete();
            }
        }

        data.addAll(orderResponseDatas);
        //追加的数据为空显示友好提示
        if (data.size() == 0) {
            tv_state.setText("暂无任务");
            tv_state.setVisibility(View.VISIBLE);
            rv_task.setVisibility(View.GONE);
        } else {
            rv_task.setVisibility(View.VISIBLE);
            taskAdapter.notifyDataSetChanged();
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
        rv_task.setVisibility(View.GONE);
        rv_task.loadMoreComplete();
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_my_task:
                callBack.myTaskEnter();
                break;
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.iv_publish:
//                int isAuth = WindowHelper.getIntByPreference("is_auth");
//                if (isAuth == 0) {
//                    WindowHelper.showToast(getContext().getString(R.string.none_certifition));
//                    return;
//                } else if (isAuth == 2) {
//                    WindowHelper.showToast(getContext().getString(R.string.certifing));
//                    return;
//                }
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                callBack.publishEnter();
                break;
        }
    }

}
