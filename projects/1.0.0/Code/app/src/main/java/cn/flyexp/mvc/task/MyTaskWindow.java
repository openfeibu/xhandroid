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
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.MyOrderAdapter;
import cn.flyexp.adapter.MyTaskAdapter;
import cn.flyexp.entity.MyTaskRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;


/**
 * Created by txy on 2016/6/6.
 */
public class MyTaskWindow extends AbstractWindow implements View.OnClickListener {

    private TaskViewCallBack callBack;
    private ViewPager vp_mytask;
    private View[] views;
    private RadioButton btn_task;
    private RadioButton btn_order;
    private ArrayList<MyTaskResponse.MyTaskResponseData> taskData = new ArrayList<MyTaskResponse.MyTaskResponseData>();
    private ArrayList<MyTaskResponse.MyTaskResponseData> orderData = new ArrayList<MyTaskResponse.MyTaskResponseData>();
    private MyTaskAdapter taskAdapter;
    private MyOrderAdapter orderAdapter;
    private LoadMoreRecyclerView taskRecyclerView;
    private LoadMoreRecyclerView orderRecyclerView;
    private int taskPage = 1;
    private int orderPage = 1;
    private boolean isResponse = true;
    private ContentLoadingProgressBar orderProgressBar;
    private ContentLoadingProgressBar taskProgressBar;
    private TextView orderState;
    private TextView taskState;
    private TabLayout taskTab;
    private TabLayout orderTab;
    private ViewPager orderViewPager;
    private ViewPager taskViewPager;

    public MyTaskWindow(TaskViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getMyTaskList(getMyTaskRequest());
        callBack.getMyOrderList(getMyTaskRequest());
    }

    private void initView() {
        setContentView(R.layout.window_mytask);
        findViewById(R.id.iv_back).setOnClickListener(this);
        btn_task = (RadioButton) findViewById(R.id.btn_task);
        btn_order = (RadioButton) findViewById(R.id.btn_order);
        btn_task.setOnClickListener(this);
        btn_order.setOnClickListener(this);

        views = new View[2];
        views[0] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview, null);
        views[1] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview, null);

        taskProgressBar = (ContentLoadingProgressBar) views[0].findViewById(R.id.progressBar);
        orderProgressBar = (ContentLoadingProgressBar) views[1].findViewById(R.id.progressBar);
        taskProgressBar.show();
        orderProgressBar.show();

        taskProgressBar.hide();
        orderProgressBar.hide();

        taskState = (TextView) views[0].findViewById(R.id.tv_state);
        orderState = (TextView) views[1].findViewById(R.id.tv_state);
        taskRecyclerView = (LoadMoreRecyclerView) views[0].findViewById(R.id.recyclerView);
        orderRecyclerView = (LoadMoreRecyclerView) views[1].findViewById(R.id.recyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new MyTaskAdapter(getContext(), taskData);
        orderAdapter = new MyOrderAdapter(getContext(), orderData);
        taskAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.myTaskDetailEnter(taskData.get(position));
            }
        });
        orderAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.myOrderDetailEnter(orderData.get(position));
            }
        });
        taskRecyclerView.setAdapter(taskAdapter);
        orderRecyclerView.setAdapter(orderAdapter);
        taskRecyclerView.setItemAnimator(new DefaultItemAnimator());
        orderRecyclerView.setItemAnimator(new DefaultItemAnimator());

        taskRecyclerView.setVisibility(VISIBLE);
        orderRecyclerView.setVisibility(VISIBLE);

        taskRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        taskRecyclerView.setFootEndView("没有更多任务了~");
        taskRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                taskPage++;
                callBack.getMyTaskList(getMyTaskRequest());
            }
        });

        orderRecyclerView.setFootLoadingView(ProgressView.BallPulse);
        orderRecyclerView.setFootEndView("没有更多订单了~");
        orderRecyclerView.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                orderPage++;
                callBack.getMyOrderList(getMyOrderRequest());
            }
        });

        vp_mytask = (ViewPager) findViewById(R.id.vp_mytask);
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
        });
        vp_mytask.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btn_task.setChecked(true);
                    btn_order.setChecked(false);
                } else {
                    btn_task.setChecked(false);
                    btn_order.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void refreshMyTask() {
        taskData.clear();
        callBack.getMyTaskList(getMyTaskRequest());
    }

    public void refreshMyOrder() {
        orderData.clear();
        callBack.getMyOrderList(getMyTaskRequest());
    }

    public MyTaskRequest getMyTaskRequest() {
        String token = getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return null;
        }
        MyTaskRequest myTaskRequest = new MyTaskRequest();
        myTaskRequest.setToken(token);
        myTaskRequest.setPage(taskPage);
        return myTaskRequest;
    }

    public MyTaskRequest getMyOrderRequest() {
        String token = getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return null;
        }
        MyTaskRequest myTaskRequest = new MyTaskRequest();
        myTaskRequest.setToken(token);
        myTaskRequest.setPage(orderPage);
        return myTaskRequest;
    }

    public void getMyTaskListResponse(ArrayList<MyTaskResponse.MyTaskResponseData> myTaskData) {
        taskProgressBar.hide();
        taskState.setVisibility(View.GONE);
        if (myTaskData.size() == 0) {
            taskRecyclerView.loadMoreEnd();
        } else {
            taskRecyclerView.loadMoreComplete();
        }

        taskData.addAll(myTaskData);
        if (taskData.size() == 0) {
            taskState.setText("暂无任务");
            taskState.setVisibility(View.VISIBLE);
            taskRecyclerView.setVisibility(View.GONE);
        } else {
            taskRecyclerView.setVisibility(View.VISIBLE);
            taskAdapter.notifyDataSetChanged();
        }
        isResponse = true;
    }

    public void getMyOrderListResponse(ArrayList<MyTaskResponse.MyTaskResponseData> myOrderData) {
        orderProgressBar.hide();
        orderState.setVisibility(View.GONE);
        if (myOrderData.size() == 0) {
            orderRecyclerView.loadMoreEnd();
        } else {
            orderRecyclerView.loadMoreComplete();
        }

        orderData.addAll(myOrderData);
        if (orderData.size() == 0) {
            orderState.setText("暂无订单");
            orderState.setVisibility(View.VISIBLE);
            orderRecyclerView.setVisibility(View.GONE);
        } else {
            orderRecyclerView.setVisibility(View.VISIBLE);
            orderAdapter.notifyDataSetChanged();
        }
        isResponse = true;
    }

    public void taskLoadingFailure() {
        taskProgressBar.hide();
        orderState.setText("数据加载失败...");
        orderState.setVisibility(View.VISIBLE);
        taskRecyclerView.setVisibility(View.GONE);
        taskRecyclerView.loadMoreComplete();
        isResponse = true;
    }

    public void orderLoadingFailure() {
        orderProgressBar.hide();
        taskState.setText("数据加载失败...");
        taskState.setVisibility(View.VISIBLE);
        orderRecyclerView.setVisibility(View.GONE);
        orderRecyclerView.loadMoreComplete();
        isResponse = true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_task:
                vp_mytask.setCurrentItem(0);
                break;
            case R.id.btn_order:
                vp_mytask.setCurrentItem(1);
                break;
        }
    }
}
