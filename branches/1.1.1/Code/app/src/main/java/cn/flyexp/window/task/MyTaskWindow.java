package cn.flyexp.window.task;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.MyTaskAdapter;
import cn.flyexp.callback.task.MyTaskCallback;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TokenPageRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.task.MyTaskPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MyTaskWindow extends BaseWindow implements MyTaskCallback.ResponseCallback, NotifyManager.Notify {

    @InjectView(R.id.tablayout)
    TabLayout tabLayout;
    @InjectView(R.id.vp_mytask)
    ViewPager vpMytask;

    private String[] title = new String[]{getContext().getResources().getString(R.string.mytask_receive),
            getContext().getResources().getString(R.string.mytask_send)};
    private View[] layouts = new View[2];
    private MyTaskPresenter myTaskPresenter;
    private int myTaskPage = 1;
    private int myOrderPage = 1;
    private ArrayList<MyTaskResponse.MyTaskResponseData> myTaskdatas = new ArrayList<>();
    private ArrayList<MyTaskResponse.MyTaskResponseData> myOrderdatas = new ArrayList<>();
    private MyTaskAdapter myTaskAdapter;
    private LoadMoreRecyclerView rvMyTask;
    private MyTaskAdapter myOrderAdapter;
    private LoadMoreRecyclerView rvMyOrder;
    private boolean isTaskRefresh;
    private boolean isOrderRefresh;
    private View myTaskHintLayout;
    private boolean isOnceTask = true;
    private boolean isOnceOrder = true;
    private View myOrderHintLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.window_mytask;
    }

    public MyTaskWindow() {
        myTaskPresenter = new MyTaskPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_MYTASK, this);

        initView();
        readyMyTask(true);
        readyMyTask(false);
    }

    private void initView() {
        initMyTaskView();
        initMyOrderView();
        vpMytask.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return title.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(layouts[position]);
                return layouts[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(layouts[position]);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });
        tabLayout.setupWithViewPager(vpMytask);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initMyTaskView() {
        layouts[0] = LayoutInflater.from(getContext()).inflate(R.layout.layout_mytask, null);
        myTaskHintLayout = layouts[0].findViewById(R.id.layout_mytaskhint);
        layouts[0].findViewById(R.id.btn_go).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getNotifyManager().notify(NotifyIDDefine.NOTIFY_MAIN_TASK);
                hideWindow(false);
            }
        });
        myTaskAdapter = new MyTaskAdapter(getContext(), myTaskdatas);
        myTaskAdapter.setOnItemClickLinstener(new MyTaskAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("mytask", true);
                bundle.putSerializable("mytaskDetail", myTaskdatas.get(position));
                openWindow(WindowIDDefine.WINDOW_MYTASK_DETAIL, bundle);
            }
        });
        rvMyTask = (LoadMoreRecyclerView) layouts[0].findViewById(R.id.loadmoreview);
        rvMyTask.setAdapter(myTaskAdapter);
        rvMyTask.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyTask.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                myTaskPage++;
                readyMyTask(true);
            }
        });

    }

    private void initMyOrderView() {
        layouts[1] = LayoutInflater.from(getContext()).inflate(R.layout.layout_myorder, null);
        myOrderHintLayout = layouts[1].findViewById(R.id.layout_myorderhint);
        layouts[1].findViewById(R.id.btn_publish).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openWindow(WindowIDDefine.WINDOW_TASK_PUBLISH);
            }
        });
        myOrderAdapter = new MyTaskAdapter(getContext(), myOrderdatas);
        myOrderAdapter.setOnItemClickLinstener(new MyTaskAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("mytask", false);
                bundle.putSerializable("mytaskDetail", myOrderdatas.get(position));
                openWindow(WindowIDDefine.WINDOW_MYTASK_DETAIL, bundle);
            }
        });
        rvMyOrder = (LoadMoreRecyclerView) layouts[1].findViewById(R.id.loadmoreview);
        rvMyOrder.setAdapter(myOrderAdapter);
        rvMyOrder.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyOrder.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                myOrderPage++;
                readyMyTask(false);
            }
        });
    }

    private void readyMyTask(boolean isTask) {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            TokenPageRequest tokenPageRequest = new TokenPageRequest();
            tokenPageRequest.setPage(isTask ? myTaskPage : myOrderPage);
            tokenPageRequest.setToken(token);
            if (isTask) {
                myTaskPresenter.requestMyTask(tokenPageRequest);
            } else {
                myTaskPresenter.requestMyOrder(tokenPageRequest);
            }
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
    public void responseMyTask(MyTaskResponse response) {
        if (isOnceTask && response.getData().size() == 0) {
            rvMyTask.setVisibility(GONE);
            myTaskHintLayout.setVisibility(VISIBLE);
            isOnceTask = false;
        }
        if (isTaskRefresh) {
            myTaskdatas.clear();
            isTaskRefresh = false;
        }
        myTaskdatas.addAll(response.getData());
        myTaskAdapter.notifyDataSetChanged();
    }

    @Override
    public void responseMyOrder(MyTaskResponse response) {
        if (isOnceOrder && response.getData().size() == 0) {
            rvMyOrder.setVisibility(GONE);
            myOrderHintLayout.setVisibility(VISIBLE);
            isOnceOrder = false;
        }
        if (isOrderRefresh) {
            myOrderdatas.clear();
            isOrderRefresh = false;
        }
        myOrderdatas.addAll(response.getData());
        myOrderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_MYTASK) {
            readyMyTask(true);
            readyMyTask(false);
            isTaskRefresh = true;
            isOrderRefresh = true;
        }
    }
}
