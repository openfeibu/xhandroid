package cn.flyexp.presenter.user;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.user.ChangePwdCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class ChangePwdPresenter extends BasePresenter implements ChangePwdCallback.RequestCallback{

    private ChangePwdCallback.ResponseCallback callback;

    public ChangePwdPresenter(ChangePwdCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestChangePwd(ChangePwdRequest request) {
        execute(ApiManager.getUserService().changePwdRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()){
                    case ResponseCode.RESPONSE_200:
                        callback.showDetail(R.string.changepwd_success);
                        callback.responseChangePwd(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_2006:
                        callback.showDetail(R.string.input_pwd_failure);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }
}
