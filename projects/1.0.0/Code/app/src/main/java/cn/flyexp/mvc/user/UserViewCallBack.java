package cn.flyexp.mvc.user;

import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.LogRequest;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.RegisterVerifyCodeRequest;
import cn.flyexp.entity.ReportRequest;
import cn.flyexp.entity.ResetPwdVerifyCodeRequest;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/31 0031.
 */
public interface UserViewCallBack extends AbstractWindow.WindowCallBack {

    void mainEnter();

    void guideEnter();

    void registerEnter();

    void resetPwdEnter();

    void loginRequest(LoginRequest loginRequest);

    void registerRequest(RegisterRequest registerRequest);

    void registerInfoEnter(RegisterRequest registerRequest);

    void resetPwdRequest(ResetPwdRequest resetPwdRequest);

    void vercodeRegisterRequest(RegisterVerifyCodeRequest registerVerifyCodeRequest);

    void vercodeResetPwdRequest(ResetPwdVerifyCodeRequest resetPwdVerifyCodeRequest);

    void imageVerifyRequest();

    void clientVerifyRequest(ClientVerifyRequest clientVerifyRequest);

    void report(ReportRequest reportRequest);

    void getWebUrl(WebUrlRequest webUrlRequest);

    void update(UpdateRequest updateRequest);

    void logRequest(String logStr);
}
