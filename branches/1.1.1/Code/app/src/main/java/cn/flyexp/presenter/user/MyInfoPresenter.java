package cn.flyexp.presenter.user;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.user.MyInfoCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class MyInfoPresenter extends BasePresenter implements MyInfoCallback.RequestCallback {

    private MyInfoCallback.ResponseCallback callback;

    public MyInfoPresenter(MyInfoCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestChangeMyInfo(ChangeMyInfoRequest request) {
        execute(ApiManager.getUserService().changeMyInfoRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()){
                    case ResponseCode.RESPONSE_200:
                        callback.responseChangeMyInfo(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }
}
