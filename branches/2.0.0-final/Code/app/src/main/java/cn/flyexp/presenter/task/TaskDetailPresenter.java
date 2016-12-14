package cn.flyexp.presenter.task;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.task.TaskDetailCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class TaskDetailPresenter extends BasePresenter implements TaskDetailCallback.RequestCallback {

    private TaskDetailCallback.ResponseCallback callback;

    public TaskDetailPresenter(TaskDetailCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestTaskClaim(TaskClaimRequest request) {
        execute(ApiManager.getTaskService().taskClaimRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTaskClaim(response);
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
