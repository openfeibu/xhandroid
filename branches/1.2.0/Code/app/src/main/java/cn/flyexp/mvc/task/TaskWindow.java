package cn.flyexp.mvc.task;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
 * modified bt zlk 2016/11/19，改成3个tab
 */
public class TaskWindow extends AbstractWindow implements View.OnClickListener {
    private TaskViewCallBack callBack;
    private  final String[] tabTitle = new String[]{"全部", "校友", "商家"};
    private  final String[] tabType = new String[]{TaskRequest.ALL, TaskRequest.PERSONAL, TaskRequest.BUSINESS};
    private  final SwipeRefreshLayout[] refreshLayouts = new SwipeRefreshLayout[3];
    private  final ContentLoadingProgressBar[] progressBars = new ContentLoadingProgressBar[3];
    private  final TextView[] states = new TextView[3];
    private  final TaskAdapter[] adapters = new TaskAdapter[3];
    private  final boolean[] uploads = new boolean[]{true, true, true};
    private  final LoadMoreRecyclerView[] recyclerViews = new LoadMoreRecyclerView[3];
    private  final int[] pages = new int[]{1,1,1};
    private  final int allIndex = 0;
    private  final int personalIndex = 1;
    private  final int businessIndex = 2;

    public TaskWindow(TaskViewCallBack orderViewCallBack) {
        super(orderViewCallBack);
        this.callBack = orderViewCallBack;
        initView();
        refreshData();
    }

    View[] views;

    private void initView() {
        setContentView(R.layout.window_task);
        findViewById(R.id.iv_publish).setOnClickListener(this);
        findViewById(R.id.tv_my_task).setOnClickListener(this);



        views = new View[3];
        views[allIndex] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview_refresh, null);
        views[personalIndex] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview_refresh, null);
        views[businessIndex] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview_refresh, null);


        progressBars[allIndex] = (ContentLoadingProgressBar)views[allIndex].findViewById(R.id.progressBar);
        states[allIndex] = (TextView)views[allIndex].findViewById(R.id.tv_state);
        LoadMoreRecyclerView taskRecyclerView = (LoadMoreRecyclerView) views[allIndex].findViewById(R.id.recyclerView);
        recyclerViews[allIndex] = taskRecyclerView;
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapters[allIndex] = new TaskAdapter(getContext(), new ArrayList<OrderResponse.OrderResponseData>());
        adapters[allIndex].setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderResponse.OrderResponseData data = adapters[allIndex].getTaskData().get(position);
                if ("new".equals(data.getStatus())) {
                    callBack.detailEnter(data);
                }
            }
        });
        taskRecyclerView.setAdapter(adapters[allIndex]);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());

        taskRecyclerView.setVisibility(VISIBLE);

        taskRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        taskRecyclerView.setFootEndView("没有更多任务了~");
        taskRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                callBack.getOrderList(getTaskRequest(TaskRequest.ALL, pages[allIndex] + 1));
                uploads[allIndex] = false;
                progressBars[allIndex].show();
            }
        });


        progressBars[personalIndex] = (ContentLoadingProgressBar)views[personalIndex].findViewById(R.id.progressBar);
        states[personalIndex] = (TextView)views[personalIndex].findViewById(R.id.tv_state);
        taskRecyclerView = (LoadMoreRecyclerView) views[personalIndex].findViewById(R.id.recyclerView);
        recyclerViews[personalIndex] = taskRecyclerView;
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapters[personalIndex] = new TaskAdapter(getContext(), new ArrayList<OrderResponse.OrderResponseData>());
        adapters[personalIndex].setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderResponse.OrderResponseData data = adapters[personalIndex].getTaskData().get(position);
                if ("new".equals(data.getStatus())) {
                    callBack.detailEnter(data);
                }
            }
        });
        taskRecyclerView.setAdapter(adapters[personalIndex]);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());

        taskRecyclerView.setVisibility(VISIBLE);

        taskRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        taskRecyclerView.setFootEndView("没有更多任务了~");
        taskRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                callBack.getOrderList(getTaskRequest(TaskRequest.PERSONAL, pages[personalIndex] + 1));
                uploads[personalIndex] = false;
                progressBars[personalIndex].show();
            }
        });


        progressBars[businessIndex] = (ContentLoadingProgressBar)views[businessIndex].findViewById(R.id.progressBar);
        states[businessIndex] = (TextView)views[businessIndex].findViewById(R.id.tv_state);
        taskRecyclerView = (LoadMoreRecyclerView) views[businessIndex].findViewById(R.id.recyclerView);
        recyclerViews[businessIndex] = taskRecyclerView;
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapters[businessIndex] = new TaskAdapter(getContext(), new ArrayList<OrderResponse.OrderResponseData>());
        adapters[businessIndex].setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OrderResponse.OrderResponseData data = adapters[businessIndex].getTaskData().get(position);
                if ("new".equals(data.getStatus())) {
                    callBack.detailEnter(data);
                }
            }
        });
        taskRecyclerView.setAdapter(adapters[businessIndex]);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());

        taskRecyclerView.setVisibility(VISIBLE);

        taskRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        taskRecyclerView.setFootEndView("没有更多任务了~");
        taskRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                callBack.getOrderList(getTaskRequest(TaskRequest.BUSINESS, pages[businessIndex] + 1));
                uploads[businessIndex] = false;
                progressBars[businessIndex].show();
            }
        });

        ViewPager vp_mytask = (ViewPager) findViewById(R.id.tasks);
        vp_mytask.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitle[position];
            }
        });
        final TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        tablayout.setupWithViewPager(vp_mytask);
        tablayout.setTabMode(TabLayout.MODE_FIXED);


        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final int selected = tablayout.getSelectedTabPosition();
                if (!canRequest()) {
                    refreshLayouts[selected].postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayouts[selected].setRefreshing(false);
                        }
                    }, 200);
                    return;
                }
                setRequestTimeNow();
                uploads[selected] = true;
                callBack.getOrderList(getTaskRequest(tabType[selected], 1));
            }
        };

        SwipeRefreshLayout refreshlayout = (SwipeRefreshLayout)views[allIndex].findViewById(R.id.refreshlayout);
        refreshLayouts[allIndex] = refreshlayout;
        refreshlayout.setColorSchemeResources(R.color.light_blue);
        refreshlayout.setOnRefreshListener(listener);

        refreshlayout = (SwipeRefreshLayout)views[personalIndex].findViewById(R.id.refreshlayout);
        refreshLayouts[personalIndex] = refreshlayout;
        refreshlayout.setColorSchemeResources(R.color.light_blue);
        refreshlayout.setOnRefreshListener(listener);

        refreshlayout = (SwipeRefreshLayout)views[businessIndex].findViewById(R.id.refreshlayout);
        refreshLayouts[businessIndex] = refreshlayout;
        refreshlayout.setColorSchemeResources(R.color.light_blue);
        refreshlayout.setOnRefreshListener(listener);

    }


    public TaskRequest getTaskRequest(String type, int page) {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setType(type);
        taskRequest.setPage(page);
        return taskRequest;
    }

    public void orderListResponse(ArrayList<OrderResponse.OrderResponseData> orderResponseDatas, TaskRequest request) {
        TaskAdapter adapter = null;
        List<OrderResponse.OrderResponseData> datas = null;
        TextView state = null;
        ContentLoadingProgressBar progressBar = null;
        SwipeRefreshLayout refreshlayout = null;
        LoadMoreRecyclerView recycleView = null;
        boolean isUploading = false;
        int index = 0;
        if (TaskRequest.ALL.equals(request.getType())){
            index = allIndex;
        } else if (TaskRequest.PERSONAL.equals(request.getType())) {
            index = personalIndex;
        } else {
            index = businessIndex;
        }

        adapter = adapters[index];
        datas = adapter.getTaskData();
        isUploading = uploads[index];
        state = states[index];
        progressBar = progressBars[index];
        refreshlayout = refreshLayouts[index];
        recycleView = recyclerViews[index];

        if (isUploading) {
            if (orderResponseDatas.isEmpty()) {
                //无数据
                if (datas.isEmpty()) {
                    recycleView.loadMoreEnd();
                } else {
                    state.setVisibility(VISIBLE);
                }
            } else {
                datas.clear();
                datas.addAll(orderResponseDatas);
                state.setVisibility(INVISIBLE);
            }
        } else {
            datas.addAll(orderResponseDatas);
            state.setVisibility(INVISIBLE);
            if (orderResponseDatas.isEmpty()) {
                recycleView.loadMoreEnd();
            } else {
                recycleView.loadMoreComplete();
                pages[index] = request.getPage();
            }
        }
        refreshlayout.setRefreshing(false);
        progressBar.hide();
        adapter.notifyDataSetChanged();
    }

    /**
     * 网络连接失败或者请求失败的友好提示
     */
    public void loadingFailure(TaskRequest request) {
        List<OrderResponse.OrderResponseData> datas = null;
        TextView state = null;
        ContentLoadingProgressBar progressBar = null;
        SwipeRefreshLayout refreshlayout = null;
        LoadMoreRecyclerView recycleView = null;
        int index = allIndex;
        if (TaskRequest.ALL.equals(request.getType())){
            index = allIndex;
        } else if (TaskRequest.PERSONAL.equals(request.getType())) {
            index = personalIndex;
        } else {
            index = businessIndex;
        }

        datas = adapters[index].getTaskData();
        state = states[index];
        progressBar = progressBars[index];
        refreshlayout = refreshLayouts[index];
        recycleView = recyclerViews[index];

        if (datas.isEmpty()) {
            state.setVisibility(VISIBLE);
        }
        recycleView.loadMoreComplete();
        refreshlayout.setRefreshing(false);
        progressBar.hide();
    }

    public void refreshData() {
        progressBars[allIndex].show();
        callBack.getOrderList(getTaskRequest(TaskRequest.ALL, 1));
        progressBars[personalIndex].show();

        postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        callBack.getOrderList(getTaskRequest(TaskRequest.BUSINESS, 1));
                    }
                }
                , 200);

        progressBars[businessIndex].show();
        postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        callBack.getOrderList(getTaskRequest(TaskRequest.PERSONAL, 1));
                    }
                }
                , 400);
    }


    @Override
    public void onClick(View v) {
        String token = WindowHelper.getStringByPreference("token");
        switch (v.getId()) {
            case R.id.tv_my_task:
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                callBack.myTaskEnter();
                break;
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.iv_publish:
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                callBack.publishEnter();
                break;
        }
    }

}
