package cn.flyexp.mvc.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;

import java.io.File;
import java.util.Date;

import cn.flyexp.R;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.ClientVerifyResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.ImageVerifyRequest;
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
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by guo on 2016/6/21.
 * Modify by txy on 2016/8/1.
 */
public class UserController extends AbstractController implements UserViewCallBack {

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
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.SPLASH_OPEN) {
            splashWindow = new SplashWindow(this);
            splashWindow.showWindow(false);
        } else if (mes.what == MessageIDDefine.GUIDE_OPEN) {
            guideWindow = new GuideWindow(this);
            guideWindow.showWindow(false);
        } else if (mes.what == MessageIDDefine.PIC_BROWSER_OPEN) {
            pictureBrowserWindow = new PictureBrowserWindow(this);
            pictureBrowserWindow.initData((PicBrowserBean) mes.obj);
            pictureBrowserWindow.showWindow();
        } else if (mes.what == MessageIDDefine.LOGIN_OPEN) {
            loginWindow = new LoginWindow(this);
            loginWindow.showWindow();
        } else if (mes.what == MessageIDDefine.REGISTER_OPEN) {
            registerWindow = new RegisterWindow(this);
            registerWindow.showWindow();
        } else if (mes.what == MessageIDDefine.FORGETPWD_OPEN) {
            resetPwdWindow = new ResetPwdWindow(this);
            resetPwdWindow.showWindow();
        } else if (mes.what == MessageIDDefine.REPORT_OPEN) {
            reportWindow = new ReportWindow(this);
            reportWindow.setOrderId(mes.arg1);
            reportWindow.showWindow();
        } else if (mes.what == MessageIDDefine.REGISTER_INFO_OPEN) {
            registerInfoWindow = new RegisterInfoWindow(this);
            registerInfoWindow.initRequest((RegisterRequest) mes.obj);
            registerInfoWindow.showWindow();
        } else if (mes.what == MessageIDDefine.WEB_OPEN) {
            webWindow = new WebWindow(this);
            webWindow.initData((WebBean)mes.obj);
            webWindow.showWindow();
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
        sendMessage(MessageIDDefine.MAIN_OPEN_4_INIT);
        guideWindow = null;
        splashWindow = null;
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
        if (loginRequest == null) {
            return;
        }
        Call<EncodeData> call = service.login(loginRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    LoginResponse loginResponse = GsonUtil.fromEncodeJson(response.body().getData(), LoginResponse.class);
                    int code = loginResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.putStringByPreference("token", loginResponse.getToken());
                            loginWindow.hideWindow(true);
                            sendNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            WindowHelper.showToast("登录失败，请重试~");
                            break;
                        case ResponseCode.RESPONSE_2005:
                            loginWindow.vertifyLogin();
                            break;
                        case ResponseCode.RESPONSE_2006:
                            WindowHelper.showToast("账号或密码错误，请重新输入");
                            break;
                        case ResponseCode.RESPONSE_2007:
                            WindowHelper.showToast("账号已被封号，请联系客服");
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(loginResponse.getDetail());
                            break;
                    }
                } else {
                }
                loginWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                loginWindow.response();
            }
        });
    }

    @Override
    public void registerRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return;
        }
        Call<EncodeData> call = service.submitUserRegister(registerRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            String mid = WindowHelper.getStringByPreference("mid");
                            String device_token = WindowHelper.getStringByPreference("device_token");
                            LoginRequest loginRequest = new LoginRequest();
                            loginRequest.setMobile_no(registerInfoWindow.getRegisterRequest().getMobile_no());
                            loginRequest.setPassword(registerInfoWindow.getRegisterRequest().getPassword());
                            loginRequest.setVerify_code("0");
                            loginRequest.setMid(mid);
                            loginRequest.setPlatform("and");
                            loginRequest.setDevice_token(device_token.equals("") ? "0" : device_token);
                            if (loginRequest == null) {
                                return;
                            }
                            Call<EncodeData> logincall = service.login(loginRequest);
                            logincall.enqueue(new Callback<EncodeData>() {
                                @Override
                                public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                                    if (response.body() != null && response.isSuccess()) {
                                        LoginResponse loginResponse = GsonUtil.fromEncodeJson(response.body().getData(), LoginResponse.class);
                                        int code = loginResponse.getCode();
                                        switch (code) {
                                            case ResponseCode.RESPONSE_200:
                                                WindowHelper.showToast("注册成功");
                                                WindowHelper.putStringByPreference("token", loginResponse.getToken());
                                                sendNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
                                                sendNotify(NotifyIDDefine.NOTIFY_MESSAGE_REFRESH);
                                                loginWindow.hideWindow(true);
                                                registerWindow.hideWindow(true);
                                                registerInfoWindow.hideWindow(true);
                                                break;
                                            case ResponseCode.RESPONSE_2001:
                                                WindowHelper.showToast("登录失败，请重试~");
                                                break;
                                            case ResponseCode.RESPONSE_2005:
                                                loginWindow.vertifyLogin();
                                                break;
                                            case ResponseCode.RESPONSE_2006:
                                                WindowHelper.showToast("账号或密码错误，请重新输入");
                                                break;
                                            case ResponseCode.RESPONSE_2007:
                                                WindowHelper.showToast("账号已被封号，请联系客服");
                                                break;
                                            case ResponseCode.RESPONSE_110:
                                                WindowHelper.showToast(loginResponse.getDetail());
                                                break;
                                        }
                                    } else {
                                    }
                                }

                                @Override
                                public void onFailure(Call<EncodeData> call, Throwable t) {
                                }
                            });
                            break;
                        case ResponseCode.RESPONSE_2002:
                            WindowHelper.showToast("手机号码已注册");
                            break;
                        case ResponseCode.RESPONSE_2004:
                            WindowHelper.showToast("验证码失效，请重新获取");
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                registerInfoWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                registerInfoWindow.response();
            }
        });
    }

    @Override
    public void registerInfoEnter(RegisterRequest registerRequest) {
        sendMessage(MessageIDDefine.REGISTER_INFO_OPEN, registerRequest);
    }

    @Override
    public void resetPwdRequest(ResetPwdRequest resetPwdRequest) {
        if (resetPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.resetPassword(resetPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("密码找回成功，请用新密码登录");
                            resetPwdWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2003:
                            WindowHelper.showToast("手机号未被注册");
                            break;
                        case ResponseCode.RESPONSE_2004:
                            WindowHelper.showToast("验证码失效，请重新获取");
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                resetPwdWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                resetPwdWindow.response();
            }
        });
    }

    @Override
    public void vercodeRegisterRequest(RegisterVerifyCodeRequest registerVerifyCodeRequest) {
        if (registerVerifyCodeRequest == null) {
            return;
        }
        Call<EncodeData> call = service.sendRegisterSMS(registerVerifyCodeRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    if (code != ResponseCode.RESPONSE_200) {
                        WindowHelper.showAlertDialog(commonResponse.getDetail(), getContext().getResources().getString(R.string.comfirm), null);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void vercodeResetPwdRequest(ResetPwdVerifyCodeRequest resetPwdVerifyCodeRequest) {
        if (resetPwdVerifyCodeRequest == null) {
            return;
        }
        Call<EncodeData> call = service.sendResetPasswordSMS(resetPwdVerifyCodeRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void imageVerifyRequest() {
        Call<EncodeData> call = service.getVerifyImageURL(GsonUtil.toEncodeJson(new ImageVerifyRequest(DateUtil.long2Date(new Date().getTime()))));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    ImageVerifyResponse imageVerifyResponse = GsonUtil.fromEncodeJson(response.body().getData(), ImageVerifyResponse.class);
                    int code = imageVerifyResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            loginWindow.imageVerifyResponse(imageVerifyResponse);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(imageVerifyResponse.getDetail());
                            break;
                    }
                } else {
                    loginWindow.response();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                loginWindow.response();
            }
        });
    }

    @Override
    public void clientVerifyRequest(ClientVerifyRequest clientVerifyRequest) {
        if (clientVerifyRequest == null) {
            return;
        }
        Call<EncodeData> call = service.verify(clientVerifyRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    ClientVerifyResponse clientVerifyResponse = GsonUtil.fromEncodeJson(response.body().getData(), ClientVerifyResponse.class);
                    int code = clientVerifyResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.putStringByPreference("mid", clientVerifyResponse.getMid());
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(clientVerifyResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void report(ReportRequest reportRequest) {
        if (reportRequest == null) {
            return;
        }
        Call<EncodeData> call = service.report(reportRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("请等待结果");
                            reportWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                reportWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                reportWindow.response();
            }
        });

    }

    @Override
    public void getWebUrl(WebUrlRequest webUrlRequest) {
        if (webUrlRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(webUrlRequest, WebUrlRequest.class);
        Call<EncodeData> call = service.getWebUrl(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    WebUrlResponse webUrlResponse = GsonUtil.fromEncodeJson(response.body().getData(), WebUrlResponse.class);
                    int code = webUrlResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            webWindow.loadUrl(webUrlResponse.getUrl());
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(webUrlResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void update(UpdateRequest updateRequest) {
        if (updateRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(updateRequest);
        Call<EncodeData> call = service.update(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UpdateResponse updateResponse = GsonUtil.fromEncodeJson(response.body().getData(), UpdateResponse.class);
                    int code = updateResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            responseData(updateResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(updateResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void logRequest(String log) {
        if (log.equals("")) {
            return;
        }
        LogRequest logRequest = GsonUtil.fromEncodeJson(log, LogRequest.class);
        service.crash(logRequest).enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.isSuccess() && response != null) {
                    logResponse(true);
                } else {
                    logResponse(false);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                logResponse(false);
            }
        });
    }

    @Override
    public void picBrowserEnter(PicBrowserBean picBrowserBean) {
        Message message = Message.obtain();
        message.obj = picBrowserBean;
        message.what = MessageIDDefine.PIC_BROWSER_OPEN;
        sendMessage(message);
    }


    public void responseData(final UpdateResponse.UpdateResponseData data) {
        if (data.getCompulsion() == 1) {
            WindowHelper.showAlertDialog(data.getDetail(), "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity) getContext()).finish();
                }
            }, "前往下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse(data.getDownload());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }
            });
        }
    }

    public void logResponse(boolean isLog) {
        if (isLog) {
            File dir = new File(CommonUtil.getFilePath(getContext()) + "/carsh/carsh.log");
            if (dir.exists()) {
                dir.delete();
            }
        }
    }


}
