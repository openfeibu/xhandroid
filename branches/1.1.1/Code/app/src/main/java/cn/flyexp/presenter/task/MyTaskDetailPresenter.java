package cn.flyexp.presenter.task;

import cn.flyexp.api.ApiManager;
import cn.flyexp.api.TaskService;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.task.MyTaskDetailCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskCancelRequest;
import cn.flyexp.entity.TaskCompleteRequest;
import cn.flyexp.entity.TaskFinishRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/11/6.
 */
public class MyTaskDetailPresenter extends BasePresenter implements MyTaskDetailCallback.RequestCallback {

    private MyTaskDetailCallback.ResponseCallback callback;
    private TaskService taskService;

    public MyTaskDetailPresenter(MyTaskDetailCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
        taskService = ApiManager.getTaskService();
    }

    @Override
    public void requestTaskCancel(TaskCancelRequest request) {
        execute(taskService.taskCancelRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTaskCancel(response);
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

    @Override
    public void requestTaskFinish(TaskFinishRequest request) {
        execute(taskService.taskFinishRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTaskFinish(response);
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

    @Override
    public void requestTaskComplete(TaskCompleteRequest request) {
        execute(taskService.taskCompleteRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTaskComplete(response);
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
