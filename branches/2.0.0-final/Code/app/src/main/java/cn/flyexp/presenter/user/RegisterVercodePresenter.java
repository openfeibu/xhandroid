package cn.flyexp.presenter.user;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.user.RegisterVercodeCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/12/2.
 */
public class RegisterVercodePresenter extends BasePresenter implements RegisterVercodeCallback.RequestCallback {

    private RegisterVercodeCallback.ResponseCallback callback;

    public RegisterVercodePresenter(RegisterVercodeCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestRegister(RegisterRequest request) {
        execute(ApiManager.getInstance().getUserService().registerRequest(request), TokenResponse.class, new ObservableCallback<TokenResponse>() {

            @Override
            public void onSuccess(TokenResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseRegister(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }

    @Override
    public void requestUploadAvatar(MultipartBody multipartBody) {
        String data = GsonUtil.getInstance().encodeJson("");
        execute(ApiManager.getInstance().getUserService().uploadRegisterAvatarRequest(multipartBody,data), ImgUrlResponse.class, new ObservableCallback<ImgUrlResponse>() {

            @Override
            public void onSuccess(ImgUrlResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseUploadAvatar(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }

    @Override
    public void requestSmscode(SmsCodeRequest request) {
        execute(ApiManager.getInstance().getUserService().registerVercodeRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse response) {
            }

        });
    }
}
