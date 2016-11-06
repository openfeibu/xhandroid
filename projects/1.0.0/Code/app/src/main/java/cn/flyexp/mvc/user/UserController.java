package cn.flyexp.mvc.user;

import android.os.Message;
import android.util.Log;

import cn.flyexp.R;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.ClientVerifyResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.ImageVerifyResponse;
import cn.flyexp.entity.LogRequest;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.LoginResponse;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.RegisterVerifyCodeRequest;
import cn.flyexp.entity.ReportRequest;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.ResetPwdVerifyCodeRequest;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.util.LogUtil;


/**
 * Created by guo on 2016/6/21.
 * Modify by txy on 2016/8/1.
 */
public class UserController extends AbstractController implements UserViewCallBack, UserModelCallBack {

    private UserModel userModel;
    private LoginWindow loginWindow;
    private SplashWindow splashWindow;
    private GuideWindow guideWindow;
    private PictureBrowserWindow pictureBrowserWindow;
    private RegisterWindow registerWindow;
    private ResetPwdWindow resetPwdWindow;
    private ReportWindow reportWindow;
    private RegisterInfoWindow registerInfoWindow;
    private WebWindow webWindow;

    public UserController() {
        super();
        userModel = new UserModel(this);
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.SPLASH_OPEN) {
            splashWindow = new SplashWindow(this);
            splashWindow.showWindow(false, false);
        } else if (mes.what == MessageIDDefine.GUIDE_OPEN) {
            guideWindow = new GuideWindow(this);
            guideWindow.showWindow(false, false);
        } else if (mes.what == MessageIDDefine.PIC_BROWSER_OPEN) {
            pictureBrowserWindow = new PictureBrowserWindow(this);
            pictureBrowserWindow.initData((PicBrowserBean) mes.obj);
            pictureBrowserWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.LOGIN_OPEN) {
            loginWindow = new LoginWindow(this);
            loginWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.REGISTER_OPEN) {
            registerWindow = new RegisterWindow(this);
            registerWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.FORGETPWD_OPEN) {
            resetPwdWindow = new ResetPwdWindow(this);
            resetPwdWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.REPORT_OPEN) {
            reportWindow = new ReportWindow(this);
            reportWindow.setOrderId(mes.arg1);
            reportWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.REGISTER_INFO_OPEN) {
            registerInfoWindow = new RegisterInfoWindow(this);
            registerInfoWindow.initRequest((RegisterRequest) mes.obj);
            registerInfoWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.WEB_OPEN) {
            webWindow = new WebWindow(this);
            if (mes.arg1 == 0) {
                webWindow.getWebUrl((String[]) mes.obj);
            } else if (mes.arg1 == 1) {
                webWindow.loadUrl((String[]) mes.obj);
            }
            webWindow.showWindow(true, true);
        }
    }


    protected void registerMessages() {
        registerMessage(MessageIDDefine.SPLASH_OPEN, this);
        registerMessage(MessageIDDefine.GUIDE_OPEN, this);
        registerMessage(MessageIDDefine.PIC_BROWSER_OPEN, this);
        registerMessage(MessageIDDefine.LOGIN_OPEN, this);
        registerMessage(MessageIDDefine.REGISTER_OPEN, this);
        registerMessage(MessageIDDefine.FORGETPWD_OPEN, this);
        registerMessage(MessageIDDefine.REPORT_OPEN, this);
        registerMessage(MessageIDDefine.REGISTER_INFO_OPEN, this);
        registerMessage(MessageIDDefine.WEB_OPEN, this);
    }

    @Override
    public void mainEnter() {
        sendMessage(MessageIDDefine.MAIN_OPEN);
    }

    @Override
    public void guideEnter() {
        sendMessage(MessageIDDefine.GUIDE_OPEN);
    }

    @Override
    public void registerEnter() {
        sendMessage(MessageIDDefine.REGISTER_OPEN);
    }

    @Override
    public void resetPwdEnter() {
        sendMessage(MessageIDDefine.FORGETPWD_OPEN);
    }

    @Override
    public void loginRequest(LoginRequest loginRequest) {
        userModel.loginRequest(loginRequest);
    }

    @Override
    public void registerRequest(RegisterRequest registerRequest) {
        userModel.registerRequest(registerRequest);
    }

    @Override
    public void registerInfoEnter(RegisterRequest registerRequest) {
        sendMessage(MessageIDDefine.REGISTER_INFO_OPEN, registerRequest);
    }

    @Override
    public void resetPwdRequest(ResetPwdRequest resetPwdRequest) {
        userModel.resetPwdRequest(resetPwdRequest);
    }

    @Override
    public void vercodeRegisterRequest(RegisterVerifyCodeRequest registerVerifyCodeRequest) {
        userModel.sendRegisterSMS(registerVerifyCodeRequest);
    }

    @Override
    public void vercodeResetPwdRequest(ResetPwdVerifyCodeRequest resetPwdVerifyCodeRequest) {
        userModel.sendResetPasswordSMS(resetPwdVerifyCodeRequest);
    }

    @Override
    public void imageVerifyRequest() {
        userModel.getImageVerifyCode();
    }

    @Override
    public void clientVerifyRequest(ClientVerifyRequest clientVerifyRequest) {
        userModel.clientVerify(clientVerifyRequest);
    }

    @Override
    public void report(ReportRequest reportRequest) {
        userModel.report(reportRequest);
    }

    @Override
    public void getWebUrl(WebUrlRequest webUrlRequest) {
        userModel.getWebUrl(webUrlRequest);
    }

    @Override
    public void update(UpdateRequest updateRequest) {
        userModel.update(updateRequest);
    }

    @Override
    public void logRequest(String logStr) {
        userModel.logRequest(logStr);
    }

    @Override
    public void loginResponse(LoginResponse loginResponse) {
        loginWindow.response();
        if (loginResponse == null) {
            return;
        }
        int code = loginResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                loginWindow.putStringByPreference("token", loginResponse.getToken());
                loginWindow.hideWindow(true);
                sendNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
                sendNotify(NotifyIDDefine.NOTIFY_MESSAGE_REFRESH);
                break;
            case ResponseCode.RESPONSE_2001:
                loginWindow.showToast("登录失败，请重试~");
                break;
            case ResponseCode.RESPONSE_2005:
                loginWindow.vertifyLogin();
                break;
            case ResponseCode.RESPONSE_2006:
                loginWindow.showToast("账号或密码错误，请重新输入");
                break;
            case ResponseCode.RESPONSE_2007:
                loginWindow.showToast("账号已被封号，请联系客服");
                break;
            case ResponseCode.RESPONSE_110:
                loginWindow.showToast(loginResponse.getDetail());
                break;
        }
    }

    @Override
    public void registerResponse(CommonResponse commonResponse) {
        registerInfoWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                String mid = registerInfoWindow.getStringByPreference("mid");
                String device_token = registerInfoWindow.getStringByPreference("device_token");
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setMobile_no(registerInfoWindow.getRegisterRequest().getMobile_no());
                loginRequest.setPassword(registerInfoWindow.getRegisterRequest().getPassword());
                loginRequest.setVerify_code("0");
                loginRequest.setMid(mid);
                loginRequest.setPlatform("and");
                loginRequest.setDevice_token(device_token.equals("") ? "0" : device_token);
                userModel.registerLoginRequest(loginRequest);
                break;
            case ResponseCode.RESPONSE_2002:
                registerInfoWindow.showToast("手机号码已注册");
                break;
            case ResponseCode.RESPONSE_2004:
                registerInfoWindow.showToast("验证码失效，请重新获取");
                break;
            case ResponseCode.RESPONSE_110:
                registerInfoWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void registerLoginResponse(LoginResponse loginResponse) {
        if (loginResponse == null) {
            return;
        }
        int code = loginResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                registerInfoWindow.showToast("注册成功");
                loginWindow.putStringByPreference("token", loginResponse.getToken());
                sendNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
                sendNotify(NotifyIDDefine.NOTIFY_MESSAGE_REFRESH);
                loginWindow.hideWindow(true);
                registerWindow.hideWindow(true);
                registerInfoWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                loginWindow.showToast("登录失败，请重试~");
                break;
            case ResponseCode.RESPONSE_2005:
                loginWindow.vertifyLogin();
                break;
            case ResponseCode.RESPONSE_2006:
                loginWindow.showToast("账号或密码错误，请重新输入");
                break;
            case ResponseCode.RESPONSE_2007:
                loginWindow.showToast("账号已被封号，请联系客服");
                break;
            case ResponseCode.RESPONSE_110:
                loginWindow.showToast(loginResponse.getDetail());
                break;
        }
    }

    @Override
    public void updateResponse(UpdateResponse updateResponse) {
        if (updateResponse == null) {
            return;
        }
        int code = updateResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                splashWindow.responseData(updateResponse.getData());
                break;
            case ResponseCode.RESPONSE_110:
                splashWindow.showToast(updateResponse.getDetail());
                break;
        }
    }

    @Override
    public void logResponse(boolean isLog) {
        if (isLog && splashWindow != null) {
            splashWindow.logResponse();
        }
    }


    @Override
    public void resetPwdResponse(CommonResponse commonResponse) {
        resetPwdWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                resetPwdWindow.showToast("密码找回成功，请用新密码登录");
                resetPwdWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2003:
                resetPwdWindow.showToast("手机号未被注册");
                break;
            case ResponseCode.RESPONSE_2004:
                resetPwdWindow.showToast("验证码失效，请重新获取");
                break;
            case ResponseCode.RESPONSE_110:
                resetPwdWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void imageVerifyResponse(ImageVerifyResponse imageVerifyResponse) {
        loginWindow.response();
        if (imageVerifyResponse == null) {
            return;
        }
        int code = imageVerifyResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                loginWindow.imageVerifyResponse(imageVerifyResponse);
                break;
            case ResponseCode.RESPONSE_110:
                loginWindow.showToast(imageVerifyResponse.getDetail());
                break;
        }
    }

    @Override
    public void vercodeRegisterResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        if (code != ResponseCode.RESPONSE_200) {
            registerWindow.showAlertDialog(commonResponse.getDetail(), getContext().getResources().getString(R.string.comfirm), null);
        }
    }

    @Override
    public void vercodeResetPwdResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_110:
                resetPwdWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void clientVerifyResponse(ClientVerifyResponse clientVerifyResponse) {
        if (clientVerifyResponse == null) {
            return;
        }
        int code = clientVerifyResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                splashWindow.putStringByPreference("mid", clientVerifyResponse.getMid());
                break;
            case ResponseCode.RESPONSE_110:
                splashWindow.showToast(clientVerifyResponse.getDetail());
                break;
        }
    }

    @Override
    public void reportResponse(CommonResponse commonResponse) {
        reportWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                reportWindow.showToast("请等待结果");
                reportWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_110:
                reportWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void webUrlResponse(WebUrlResponse webUrlResponse) {
        if (webUrlResponse == null) {
            return;
        }
        int code = webUrlResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                webWindow.loadUrl(new String[]{webUrlResponse.getUrl()});
                break;
            case ResponseCode.RESPONSE_110:
                webWindow.showToast(webUrlResponse.getDetail());
                break;
        }

    }


}
