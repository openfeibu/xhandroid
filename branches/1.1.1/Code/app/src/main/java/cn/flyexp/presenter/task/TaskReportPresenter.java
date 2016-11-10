package cn.flyexp.presenter.task;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.task.TaskReportCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskReportRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/11/7.
 */
public class TaskReportPresenter extends BasePresenter implements TaskReportCallback.RequestCallback {

    private TaskReportCallback.ResponseCallback callback;

    public TaskReportPresenter(TaskReportCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestTaskReport(TaskReportRequest request) {
        execute(ApiManager.getTaskService().taskReportRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTaskReport(response);
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
