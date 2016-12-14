package cn.flyexp.presenter.user;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.api.UserService;
import cn.flyexp.callback.user.LoginCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class LoginPresenter extends BasePresenter implements LoginCallback.RequestCallback {

    private LoginCallback.ResponseCallback callback;

    public LoginPresenter(LoginCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestLogin(LoginRequest request) {
        execute(ApiManager.getInstance().getUserService().loginRequest(request), TokenResponse.class, new ObservableCallback<TokenResponse>() {

            @Override
            public void onSuccess(TokenResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseLogin(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.showDetail(R.string.login_failure);
                        break;
                    case ResponseCode.RESPONSE_2006:
                        callback.showDetail(R.string.login_info_error);
                        break;
                    case ResponseCode.RESPONSE_2007:
                        callback.showDetail(R.string.close_account);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }

}
