package cn.flyexp.window.task;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import cn.flyexp.view.RefreshLayout;
import cn.flyexp.window.BaseWindow;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

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
    private TaskPresenter taskPresenter;
    private TaskView[] taskViews = new TaskView[3];

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
//        readyTask();
    }

    private void initView() {
//        taskAdapter = new TaskAdapter(getContext(), datas);
//        taskAdapter.setOnItemClickLinstener(new TaskAdapter.OnItemClickLinstener() {
//            @Override
//            public void onItemClickLinstener(View view, int position) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("taskdetail", datas.get(position));
//                openWindow(WindowIDDefine.WINDOW_TASK_DETAIL, bundle);
//            }
//        });
        for (int i = 0; i < 3; i++) {
            final TaskView taskView = new TaskView();
            ArrayList<TaskResponse.TaskResponseData> datas = new ArrayList<>();
            TaskAdapter taskAdapter = new TaskAdapter(getContext(), datas);
            taskView.setDatas(datas);
            taskView.setTaskAdapter(taskAdapter);
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_task, null);
            LoadMoreRecyclerView loadMoreRecyclerView = (LoadMoreRecyclerView) layout.findViewById(R.id.rv_task);
            loadMoreRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            loadMoreRecyclerView.setAdapter(taskAdapter);
            loadMoreRecyclerView.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
                @Override
                public void onLoadMore() {

                }
            });

            RefreshLayout refreshLayout = (RefreshLayout) layout.findViewById(R.id.layout_refresh);
            refreshLayout.setPtrHandler(new PtrDefaultHandler() {
                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    taskView.setRefresh(true);
                    taskView.setPage(taskView.getPage() + 1);
                }
            });
            taskView.setRefreshLayout(refreshLayout);
            taskView.setRecyclerView(loadMoreRecyclerView);
            taskView.setLayout(layout);
            taskViews[i] = taskView;
        }
        vpTask.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return taskViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(taskViews[position].getLayout());
                return taskViews[position].getLayout();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(taskViews[position].getLayout());
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return title[position];
            }
        });
        tabLayout.setupWithViewPager(vpTask);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void readyTask() {
        taskPresenter.requestTaskList(new PageRequest(taskViews[0].getPage()));
    }

    @OnClick({R.id.img_publish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_publish:
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
    }

    @Override
    public void requestFinish() {
    }

    @Override
    public void responseTaskList(TaskResponse response) {
        taskViews[0].getDatas().addAll(response.getData());
        taskViews[0].getTaskAdapter().notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_TASK_REFRESH) {
            readyTask();
        }
    }


    class TaskView {

        private boolean isRefresh;
        private int page = 1;
        private View layout;
        private LoadMoreRecyclerView recyclerView;
        private RefreshLayout refreshLayout;
        private ArrayList<TaskResponse.TaskResponseData> datas;
        private TaskAdapter taskAdapter;

        public ArrayList<TaskResponse.TaskResponseData> getDatas() {
            return datas;
        }

        public View getLayout() {
            return layout;
        }

        public boolean isRefresh() {
            return isRefresh;
        }

        public void setRefresh(boolean refresh) {
            isRefresh = refresh;
        }

        public void setLayout(View layout) {
            this.layout = layout;
        }

        public LoadMoreRecyclerView getRecyclerView() {
            return recyclerView;
        }

        public void setRecyclerView(LoadMoreRecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        public RefreshLayout getRefreshLayout() {
            return refreshLayout;
        }

        public void setRefreshLayout(RefreshLayout refreshLayout) {
            this.refreshLayout = refreshLayout;
        }

        public TaskAdapter getTaskAdapter() {
            return taskAdapter;
        }

        public void setTaskAdapter(TaskAdapter taskAdapter) {
            this.taskAdapter = taskAdapter;
        }

        public void setDatas(ArrayList<TaskResponse.TaskResponseData> datas) {
            this.datas = datas;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }
}
