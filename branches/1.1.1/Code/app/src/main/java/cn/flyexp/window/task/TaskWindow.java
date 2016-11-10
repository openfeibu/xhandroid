package cn.flyexp.window.task;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

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
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class TaskWindow extends BaseWindow implements TaskCallback.ResponseCallback, NotifyManager.Notify {

    @InjectView(R.id.rv_task)
    LoadMoreRecyclerView rvTask;

    private int taskPage = 1;
    private ArrayList<TaskResponse.TaskResponseData> datas = new ArrayList<>();
    private TaskPresenter taskPresenter;
    private TaskAdapter taskAdapter;
    private boolean isRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.window_task;
    }

    public TaskWindow() {
        taskPresenter = new TaskPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TASK_REFRESH, this);

        initView();
        readyTask();
    }

    private void initView() {
        taskAdapter = new TaskAdapter(getContext(), datas);
        taskAdapter.setOnItemClickLinstener(new TaskAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                if (datas.get(position).getStatus().equals("new")) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskdetail", datas.get(position));
                    openWindow(WindowIDDefine.WINDOW_TASK_DETAIL, bundle);
                }
            }
        });
        rvTask.setAdapter(taskAdapter);
        rvTask.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTask.setLayoutManager(layoutManager);
        rvTask.setHasFixedSize(false);
        rvTask.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                taskPage++;
                readyTask();
            }
        });
    }

    private void readyTask() {
        taskPresenter.requestTaskList(new PageRequest(taskPage));
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
