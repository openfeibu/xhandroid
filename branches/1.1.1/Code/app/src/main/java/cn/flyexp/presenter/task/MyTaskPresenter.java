package cn.flyexp.presenter.task;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.task.MyTaskCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TokenPageRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MyTaskPresenter extends BasePresenter implements MyTaskCallback.RequestCallback {

    private MyTaskCallback.ResponseCallback callback;

    public MyTaskPresenter(MyTaskCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMyTask(TokenPageRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTaskService().myTaskRequest(data), MyTaskResponse.class, new ObservableCallback<MyTaskResponse>() {
            @Override
            public void onSuccess(MyTaskResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyTask(response);
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
    public void requestMyOrder(TokenPageRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTaskService().myOrderRequest(data), MyTaskResponse.class, new ObservableCallback<MyTaskResponse>() {
            @Override
            public void onSuccess(MyTaskResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyOrder(response);
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
