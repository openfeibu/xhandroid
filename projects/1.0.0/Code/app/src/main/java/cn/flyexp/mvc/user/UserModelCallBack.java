package cn.flyexp.mvc.user;

import cn.flyexp.entity.ClientVerifyResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.ImageVerifyResponse;
import cn.flyexp.entity.LoginResponse;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.WebUrlResponse;

/**
 * Created by txy on 2016/8/1 0001.
 */
public interface UserModelCallBack {

    void loginResponse(LoginResponse loginResponse);

    void registerResponse(CommonResponse commonResponse);

    void resetPwdResponse(CommonResponse commonResponse);

    void imageVerifyResponse(ImageVerifyResponse imageVerifyResponse);

    void vercodeRegisterResponse(CommonResponse commonResponse);

    void vercodeResetPwdResponse(CommonResponse commonResponse);

    void clientVerifyResponse(ClientVerifyResponse clientVerifyResponse);

    void reportResponse(CommonResponse commonResponse);

    void webUrlResponse(WebUrlResponse webUrlResponse);

    void registerLoginResponse(LoginResponse loginResponse);

    void updateResponse(UpdateResponse updateResponse);

    void logResponse(boolean isLog);
}
