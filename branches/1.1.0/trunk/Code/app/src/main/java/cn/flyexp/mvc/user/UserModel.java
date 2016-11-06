package cn.flyexp.mvc.user;

import android.util.Log;

import java.util.Date;

import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.ClientVerifyResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.LogRequest;
import cn.flyexp.entity.RegisterVerifyCodeRequest;
import cn.flyexp.entity.ReportRequest;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.ImageVerifyRequest;
import cn.flyexp.entity.ImageVerifyResponse;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.LoginResponse;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.ResetPwdVerifyCodeRequest;
import cn.flyexp.entity.TaskRequest;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zlk on 2016/5/25.
 * Mofity by txy on 2016/8/1.
 */
public class UserModel {

    private UserModelCallBack callBack;
    private NetWorkService service;

    public UserModel(UserModelCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void loginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return;
        }
        Call<EncodeData> call = service.login(loginRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    LoginResponse loginResponse = GsonUtil.fromJson(response.body().getData(), LoginResponse.class);
                    callBack.loginResponse(loginResponse);
                } else {
                    callBack.loginResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.loginResponse(null);
            }
        });
    }

    public void getImageVerifyCode() {
        Call<EncodeData> call = service.getVerifyImageURL(GsonUtil.toJson(new ImageVerifyRequest(DateUtil.long2Date(new Date().getTime()))));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    ImageVerifyResponse imageVerifyResponse = GsonUtil.fromJson(response.body().getData(), ImageVerifyResponse.class);
                    Log.e("test", "梵蒂冈法国发过");
                    callBack.imageVerifyResponse(imageVerifyResponse);
                } else {
                    callBack.imageVerifyResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.imageVerifyResponse(null);
            }
        });
    }


    public void sendRegisterSMS(RegisterVerifyCodeRequest registerVerifyCodeRequest) {
        if (registerVerifyCodeRequest == null) {
            return;
        }
        Call<EncodeData> call = service.sendRegisterSMS(registerVerifyCodeRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.vercodeRegisterResponse(commonResponse);
                } else {
                    callBack.vercodeRegisterResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.vercodeRegisterResponse(null);
            }
        });
    }

    public void sendResetPasswordSMS(ResetPwdVerifyCodeRequest resetPwdVerifyCodeRequest) {
        if (resetPwdVerifyCodeRequest == null) {
            return;
        }
        Call<EncodeData> call = service.sendResetPasswordSMS(resetPwdVerifyCodeRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.vercodeResetPwdResponse(commonResponse);
                } else {
                    callBack.vercodeResetPwdResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.vercodeResetPwdResponse(null);
            }
        });
    }


    public void registerRequest(RegisterRequest registerRequest) {
        if (registerRequest == null) {
            return;
        }
        Call<EncodeData> call = service.submitUserRegister(registerRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.registerResponse(commonResponse);
                } else {
                    callBack.registerResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.registerResponse(null);
            }
        });
    }

    public void resetPwdRequest(ResetPwdRequest resetPwdRequest) {
        if (resetPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.resetPassword(resetPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.resetPwdResponse(commonResponse);
                } else {
                    callBack.resetPwdResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.resetPwdResponse(null);
            }
        });

    }

    public void clientVerify(ClientVerifyRequest clientVerifyRequest) {
        if (clientVerifyRequest == null) {
            return;
        }
        Call<EncodeData> call = service.verify(clientVerifyRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    ClientVerifyResponse clientVerifyResponse = GsonUtil.fromJson(response.body().getData(), ClientVerifyResponse.class);
                    callBack.clientVerifyResponse(clientVerifyResponse);
                } else {
                    callBack.clientVerifyResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.clientVerifyResponse(null);
            }
        });

    }

    public void report(ReportRequest reportRequest) {
        if (reportRequest == null) {
            return;
        }
        Call<EncodeData> call = service.report(reportRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.reportResponse(commonResponse);
                } else {
                    callBack.reportResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.reportResponse(null);
            }
        });

    }


    public void getWebUrl(WebUrlRequest webUrlRequest) {
        if (webUrlRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(webUrlRequest, WebUrlRequest.class);
        Call<EncodeData> call = service.getWebUrl(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    WebUrlResponse webUrlResponse = GsonUtil.fromJson(response.body().getData(), WebUrlResponse.class);
                    callBack.webUrlResponse(webUrlResponse);
                } else {
                    callBack.webUrlResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.webUrlResponse(null);
            }
        });

    }

    public void registerLoginRequest(LoginRequest loginRequest) {
        if (loginRequest == null) {
            return;
        }
        Call<EncodeData> call = service.login(loginRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    LoginResponse loginResponse = GsonUtil.fromJson(response.body().getData(), LoginResponse.class);
                    callBack.registerLoginResponse(loginResponse);
                } else {
                    callBack.registerLoginResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.registerLoginResponse(null);
            }
        });
    }

    public void update(UpdateRequest updateRequest) {
        if (updateRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(updateRequest);
        Call<EncodeData> call = service.update(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UpdateResponse updateResponse = GsonUtil.fromJson(response.body().getData(), UpdateResponse.class);
                    callBack.updateResponse(updateResponse);
                } else {
                    callBack.updateResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.updateResponse(null);
            }
        });
    }

    public void logRequest(String log) {
        if (log.equals("")) {
            return;
        }
        LogRequest logRequest = GsonUtil.fromJson(log,LogRequest.class);
        service.crash(logRequest).enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.isSuccess() && response != null) {
                    callBack.logResponse(true);
                } else {
                    callBack.logResponse(false);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.logResponse(false);
            }
        });
    }
}
