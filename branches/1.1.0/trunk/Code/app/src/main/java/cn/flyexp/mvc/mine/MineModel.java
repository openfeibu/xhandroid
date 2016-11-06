package cn.flyexp.mvc.mine;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.AssnJoinRequset;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.ChangeAliPayRequest;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.ChangePayAccountVCRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.IntegralRecordResponse;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.entity.MyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.TaInfoRequest;
import cn.flyexp.entity.TaInfoResponse;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletResponse;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.entity.WithdrawalRequest;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.UploadFileHelper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/6/6.
 */
public class MineModel {

    private MineModelCallBack callBack;
    private NetWorkService service;

    public MineModel(MineModelCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void getMyInfo(MyInfoRequest myInfoRequest) {
        if (myInfoRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(myInfoRequest);
        Call<EncodeData> respond = service.getMyProile(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyInfoResponse myProileResponse = GsonUtil.fromJson(response.body().getData(), MyInfoResponse.class);
                    callBack.getMyInfoResponse(myProileResponse);
                } else {
                    callBack.getMyInfoResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.getMyInfoResponse(null);
            }
        });
    }

    public void logout(LogoutRequest logoutRequest) {
        if (logoutRequest == null) {
            return;
        }
        Call<EncodeData> call = service.logout(logoutRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.logoutResponse(commonResponse);
                } else {
                    callBack.logoutResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.logoutResponse(null);
            }
        });
    }

    public void changePwd(ChangePwdRequest changePwdRequest) {
        if (changePwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changePwd(changePwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.changePwdResponse(commonResponse);
                } else {
                    callBack.changePwdResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.changePwdResponse(null);
            }
        });
    }

    public void changeMyInfo(ChangeMyInfoRequest changeMyInfoRequest) {
        if (changeMyInfoRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changeMyInfo(changeMyInfoRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.changeInfoResponse(commonResponse);
                } else {
                    callBack.changeInfoResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.changeInfoResponse(null);
            }
        });
    }

    public void getTaInfo(TaInfoRequest taInfoRequest) {
        if (taInfoRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(taInfoRequest);
        Call<EncodeData> call = service.getOthersInfo(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TaInfoResponse taInfoResponse = GsonUtil.fromJson(response.body().getData(), TaInfoResponse.class);
                    callBack.taInfoResponse(taInfoResponse);
                } else {
                    callBack.taInfoResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.taInfoResponse(null);
            }
        });
    }

    public void uploadImageAvatar(String token, List<File> file) {
        if (file == null || token == null || token.equals("")) {
            return;
        }
        MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(file);
        Call<EncodeData> call = service.uploadImageAvatar(multipartBody, GsonUtil.toJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body()
                            .getData(), CommonResponse.class);
                    callBack.uploadImageAvatar(commonResponse);
                } else {
                    callBack.uploadImageAvatar(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.uploadImageAvatar(null);
            }
        });

    }

    public void uploadImageAssn(String token, List<File> file) {
        if (file == null || token == null || token.equals("")) {
            return;
        }
        MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(file);
        Call<EncodeData> call = service.uploadImageJoin(multipartBody, GsonUtil.toJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UploadImageResponse uploadImageAssnResponse = GsonUtil.fromJson(response.body()
                            .getData(), UploadImageResponse.class);
                    callBack.uploadImageAssn(uploadImageAssnResponse);
                } else {
                    callBack.uploadImageAssn(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.uploadImageAssn(null);
            }
        });

    }


    public void join(AssnJoinRequset assnJoinRequset) {
        if (assnJoinRequset == null) {
            return;
        }
        Call<EncodeData> call = service.join(assnJoinRequset);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.asnnJoinResponse(commonResponse);
                } else {
                    callBack.asnnJoinResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.asnnJoinResponse(null);
            }
        });
    }

    public void feedback(FeedbackRequest feedbackRequest) {
        if (feedbackRequest == null) {
            return;
        }
        Call<EncodeData> call = service.feedback(feedbackRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.feedbackResponse(commonResponse);
                } else {
                    callBack.feedbackResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.feedbackResponse(null);
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

    public void share(String token) {
        Call<EncodeData> call = service.share(new TokenToJson(token));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    public void integralRecord(IntegralRecordRequest integralRecordRequest) {
        if (integralRecordRequest == null) {
            return;
        }
        Call<EncodeData> call = service.integralRecord(integralRecordRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    IntegralRecordResponse integralRecordResponse = GsonUtil.fromJson(response.body().getData(), IntegralRecordResponse.class);
                    callBack.integralRecordResponse(integralRecordResponse);
                } else {
                    callBack.integralRecordResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.integralRecordResponse(null);
            }
        });
    }

    public void uploadImageCertifi(String token, List<File> file) {
        if (file == null || token == null || token.equals("")) {
            return;
        }
        MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(file);
        Call<EncodeData> call = service.uploadImageCertifi(multipartBody, GsonUtil.toJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UploadImageResponse uploadImageAssnResponse = GsonUtil.fromJson(response.body()
                            .getData(), UploadImageResponse.class);
                    callBack.uploadImageCertifi(uploadImageAssnResponse);
                } else {
                    callBack.uploadImageCertifi(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.uploadImageCertifi(null);
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

    public void withdrawl(WithdrawalRequest withdrawalRequst) {
        if (withdrawalRequst == null) {
            return;
        }
        Call<EncodeData> call = service.withdrawl(withdrawalRequst);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.withdrawlResponse(commonResponse);
                } else {
                    callBack.withdrawlResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.withdrawlResponse(null);
            }
        });
    }

    public void getWallet(String token) {
        String data = GsonUtil.toJson(new TokenToJson(token));
        Call<EncodeData> call = service.getWallet(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    WalletResponse walletResponse = GsonUtil.fromJson(response.body().getData(), WalletResponse.class);
                    callBack.walletResponse(walletResponse);
                } else {
                    callBack.walletResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.walletResponse(null);
            }
        });
    }

    public void getWalletDetail(WalletDetailRequest walletDetailRequest) {
        if (walletDetailRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(walletDetailRequest, WalletDetailRequest.class);
        Call<EncodeData> call = service.getWalletDetail(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    WalletDetailResponse walletDetailResponse = GsonUtil.fromJson(response.body().getData(), WalletDetailResponse.class);
                    callBack.walletDetailResponse(walletDetailResponse);
                } else {
                    callBack.walletDetailResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.walletDetailResponse(null);
            }
        });
    }

    public void bindAlipay(BindAlipayRequest bindAlipayRequest) {
        if (bindAlipayRequest == null) {
            return;
        }
        Call<EncodeData> call = service.bindAlipay(bindAlipayRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.bindAlipayResponse(commonResponse);
                } else {
                    callBack.bindAlipayResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.bindAlipayResponse(null);
            }
        });
    }

    public void changPayAccountVercode(String token) {
        String data = GsonUtil.toJson(new TokenToJson(token));
        Call<EncodeData> call = service.getChangeAliPayVercord(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    public void changeAliPay(ChangeAliPayRequest changeAliPayRequest) {
        if (changeAliPayRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changeAlipay(changeAliPayRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.changeAliPayResponse(commonResponse);
                } else {
                    callBack.changeAliPayResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.changeAliPayResponse(null);
            }
        });
    }

    public void getAlipayInfo(String token) {
        if (token == null || token.equals("")) {
            return;
        }
        String data = GsonUtil.toJson(new TokenToJson(token));
        Call<EncodeData> call = service.getAlipayInfo(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AlipayInfoResponse alipayInfoResponse = GsonUtil.fromJson(response.body().getData(), AlipayInfoResponse.class);
                    callBack.alipayInfoResponse(alipayInfoResponse);
                } else {
                    callBack.alipayInfoResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.alipayInfoResponse(null);
            }
        });
    }


    public void setPayPwd(SetPayPwdRequest setPayPwdRequest) {
        if (setPayPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.setPayPassword(setPayPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.setPayPwdResponse(commonResponse);
                } else {
                    callBack.setPayPwdResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.setPayPwdResponse(null);
            }
        });
    }

    public void changePayPwd(ChangePayPwdRequest changePayPwdRequest) {
        if (changePayPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changePayPwd(changePayPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.changePayPwdResponse(commonResponse);
                } else {
                    callBack.changePayPwdResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.changePayPwdResponse(null);
            }
        });
    }

    public void vercodeResetPayPwd(String token) {
        String data = GsonUtil.toJson(new TokenToJson(token));
        Call<EncodeData> call = service.sendResetPayPasswordSMS(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    public void resetPayPwd(ResetPayPwdRequest resetPayPwdRequest) {
        if (resetPayPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.resetPayPwd(resetPayPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.resetPayPwdResponse(commonResponse);
                } else {
                    callBack.resetPayPwdResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.resetPayPwdResponse(null);
            }
        });
    }
}
