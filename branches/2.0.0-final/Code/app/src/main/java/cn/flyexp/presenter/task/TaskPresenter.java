package cn.flyexp.presenter.task;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.task.TaskCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.PageRequest;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class TaskPresenter extends BasePresenter implements TaskCallback.RequestCallback {

    private TaskCallback.ResponseCallback callback;

    public TaskPresenter(TaskCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestTaskList(PageRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTaskService().taskListRequest(data), TaskResponse.class, new ObservableCallback<TaskResponse>() {
            @Override
            public void onSuccess(TaskResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTaskList(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }
}
