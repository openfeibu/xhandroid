package cn.flyexp.presenter.task;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.task.TaskPublishCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DataResponse;
import cn.flyexp.entity.TaskPublishRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/11/8.
 */
public class TaskPublishPresenter extends BasePresenter implements TaskPublishCallback.RequestCallback {

    private TaskPublishCallback.ResponseCallback callback;

    public TaskPublishPresenter(TaskPublishCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestTaskPublish(TaskPublishRequest request) {
        execute(ApiManager.getTaskService().taskPublishRequest(request), DataResponse.class, new ObservableCallback<DataResponse>() {
            @Override
            public void onSuccess(DataResponse response) {
                switch (response.getCode()){
                    case ResponseCode.RESPONSE_200:
                        callback.responseTaskPublish(response);
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
