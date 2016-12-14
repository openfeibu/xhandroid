package cn.flyexp.window.task;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.TaskAdapter;
import cn.flyexp.callback.task.TaskCallback;
import cn.flyexp.entity.PageRequest;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.task.TaskPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class TaskWindow extends BaseWindow implements TaskCallback.ResponseCallback, NotifyManager.Notify {

    @InjectView(R.id.tablayout)
    TabLayout tabLayout;
    @InjectView(R.id.vp_task)
    ViewPager vpTask;

    private String[] title = new String[]{getContext().getResources().getString(R.string.task_all),
            getContext().getResources().getString(R.string.task_alumnus), getContext().getResources().getString(R.string.task_business)};
    private int allpage = 1;
    private ArrayList<TaskResponse.TaskResponseData> datas = new ArrayList<>();
    private TaskPresenter taskPresenter;
    private TaskAdapter taskAdapter;
    private boolean isRefresh;
    private View[] layouts = new View[3];
    private SwipeRefreshLayout refreshLayout;
    private LoadMoreRecyclerView rvTask;

    @Override
    protected int getLayoutId() {
        return R.layout.window_task;
    }

    public TaskWindow() {
        taskPresenter = new TaskPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TASK_REFRESH, this);
        initView();
    }

    @Override
    public void onStart() {
        readyTask();
    }

    @Override
    public void onRenew() {
        isRefresh = true;
        readyTask();
    }

    private void initView() {
        taskAdapter = new TaskAdapter(getContext(), datas);
        taskAdapter.setOnItemClickLinstener(new TaskAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskdetail", datas.get(position));
                openWindow(WindowIDDefine.WINDOW_TASK_DETAIL, bundle);
            }
        });
        vpTask.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return layouts.length;
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
        tabLayout.setupWithViewPager(vpTask);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < 3; i++) {
            layouts[i] = LayoutInflater.from(getContext()).inflate(R.layout.layout_task, null);
            refreshLayout = (SwipeRefreshLayout) layouts[i].findViewById(R.id.layout_refresh);
            refreshLayout.setColorSchemeResources(R.color.light_blue);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    allpage = 1;
                    isRefresh = true;
                    readyTask();
                }
            });
            rvTask = (LoadMoreRecyclerView) layouts[i].findViewById(R.id.rv_task);
            rvTask.setAdapter(taskAdapter);
            rvTask.setLayoutManager(new LinearLayoutManager(getContext()));
            rvTask.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
                @Override
                public void onLoadMore() {
                    allpage++;
                    readyTask();
                }
            });
        }
    }

    private void readyTask() {
        taskPresenter.requestTaskList(new PageRequest(allpage));
    }

    @OnClick({R.id.fab_publish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_publish:
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                    return;
                }
                int auth = SharePresUtil.getInt(SharePresUtil.KEY_AUTH);
                if (auth == 0) {
                    showToast(R.string.go_auth);
                    openWindow(WindowIDDefine.WINDOW_CERTIFITION);
                } else if (auth == 2) {
                    showToast(R.string.authing);
                } else if (auth == 1) {
                    openWindow(WindowIDDefine.WINDOW_TASK_PUBLISH);
                }
                break;
        }
    }

    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

    @Override
    public void requestFailure() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void requestFinish() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void responseTaskList(TaskResponse response) {
        if (isRefresh) {
            datas.clear();
            isRefresh = false;
        }
        datas.addAll(response.getData());
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_TASK_REFRESH) {
            isRefresh = true;
            readyTask();
        }
    }
}
