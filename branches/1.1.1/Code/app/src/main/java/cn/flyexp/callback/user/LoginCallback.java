package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenResponse;
import cn.flyexp.entity.RegisterRequest;

/**
 * Created by tanxinye on 2016/10/23.
 */
public interface LoginCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestLogin(LoginRequest request);

        void requestRegister(RegisterRequest request);

        void requestSmscode(SmsCodeRequest request);

        void requestImgVercode();
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseLogin(TokenResponse response);

        void responseRegister(TokenResponse response);

        void responseImgVerCode(ImgUrlResponse response);

        void vertifyLogin();
    }

}
