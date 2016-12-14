package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.AssnJoinCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/11/2.
 */
public class AssnJoinPresenter extends BasePresenter implements AssnJoinCallback.RequestCallback {

    private AssnJoinCallback.ResponseCallback callback;

    public AssnJoinPresenter(AssnJoinCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAssnJoin(AssnJoinRequest request) {
        execute(ApiManager.getAssnService().assnJoinRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnJoin(response);
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
