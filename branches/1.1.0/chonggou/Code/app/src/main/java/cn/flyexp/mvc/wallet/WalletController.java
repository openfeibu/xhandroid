package cn.flyexp.mvc.wallet;

import android.os.Message;

import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.ChangeAliPayRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.entity.WalletResponse;
import cn.flyexp.entity.WithdrawalRequest;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.constants.Config;
import cn.flyexp.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by guo on 2016/6/21.
 * Modify by txy on 2016/8/1.
 */
public class WalletController extends AbstractController implements WalletViewCallBack, NotifyManager.Notify {

    private WalletWindow walletWindow;
    private WalletDetailWindow walletDetailWindow;
    private WithdrawalWindow withdrawalWindow;
    private PayAccountWindow payAccountWindow;
    private PayBindWindow payBindWindow;
    private ChangePayAccountWindow changePayAccountWindow;
    private PayPwdInfoWindow payPwdInfoWindow;
    private SetPayPwdWindow setPayPwdWindow;
    private ResetPayPwdWindow resetPayPwdWindow;
    private ChangePayPwdWindow changePayPwdWindow;
    private VerifiPayPwdWindow verifiPayPwdWindow;

    public WalletController() {
        super();
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.WALLET_OPEN) {
            walletWindow = new WalletWindow(this);
            walletWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.WALLET_DETAIL_OPEN) {
            walletDetailWindow = new WalletDetailWindow(this);
            walletDetailWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.WITHDRAWAL_OPEN) {
            withdrawalWindow = new WithdrawalWindow(this);
            withdrawalWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.PAYACCOUNT_OPEN) {
            payAccountWindow = new PayAccountWindow(this);
            payAccountWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.PAYBIND_OPEN) {
            payBindWindow = new PayBindWindow(this);
            payBindWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.CHANGEPAYACCOUNT_OPEN) {
            changePayAccountWindow = new ChangePayAccountWindow(this);
            changePayAccountWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.PAYPWDINFO_OPEN) {
            payPwdInfoWindow = new PayPwdInfoWindow(this);
            payPwdInfoWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.SETPAYPWD_OPEN) {
            setPayPwdWindow = new SetPayPwdWindow(this);
            setPayPwdWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.RESETPAYPWD_OPEN) {
            resetPayPwdWindow = new ResetPayPwdWindow(this);
            resetPayPwdWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.CHANGEPAYPWD_OPEN) {
            changePayPwdWindow = new ChangePayPwdWindow(this);
            changePayPwdWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.VERIFIPAYPWD_OPEN) {
            verifiPayPwdWindow = new VerifiPayPwdWindow(this);
            verifiPayPwdWindow.setRequestCode(mes.arg1);
            verifiPayPwdWindow.showWindow(true);
        }

    }


    protected void registerMessages() {
        registerMessage(MessageIDDefine.WALLET_OPEN, this);
        registerMessage(MessageIDDefine.WALLET_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.WITHDRAWAL_OPEN, this);
        registerMessage(MessageIDDefine.PAYACCOUNT_OPEN, this);
        registerMessage(MessageIDDefine.PAYBIND_OPEN, this);
        registerMessage(MessageIDDefine.CHANGEPAYACCOUNT_OPEN, this);
        registerMessage(MessageIDDefine.PAYPWDINFO_OPEN, this);
        registerMessage(MessageIDDefine.SETPAYPWD_OPEN, this);
        registerMessage(MessageIDDefine.RESETPAYPWD_OPEN, this);
        registerMessage(MessageIDDefine.CHANGEPAYPWD_OPEN, this);
        registerMessage(MessageIDDefine.VERIFIPAYPWD_OPEN, this);
        registerNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH, this);
        registerNotify(NotifyIDDefine.PAY_PWD_RESULT, this);
        registerNotify(NotifyIDDefine.SHARE_SUCCESS, this);
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.PAY_PWD_RESULT && mes.arg1 == NotifyIDDefine.RESULT_PAY_WITHDRAWAL) {
            if (withdrawalWindow != null) {
                withdrawalWindow.payPwdResponse((String) mes.obj);
            }
        }
    }

    @Override
    public void walletDetailEnter() {
        sendMessage(MessageIDDefine.WALLET_DETAIL_OPEN);
    }

    @Override
    public void withdrawalEnter() {
        sendMessage(MessageIDDefine.WITHDRAWAL_OPEN);
    }

    @Override
    public void payAccountEnter() {
        sendMessage(MessageIDDefine.PAYACCOUNT_OPEN);
    }

    @Override
    public void payBindEnter() {
        sendMessage(MessageIDDefine.PAYBIND_OPEN);
    }

    @Override
    public void changePayAccount() {
        sendMessage(MessageIDDefine.CHANGEPAYACCOUNT_OPEN);
    }

    @Override
    public void setPayPwdEnter() {
        sendMessage(MessageIDDefine.SETPAYPWD_OPEN);
    }

    @Override
    public void payPwdInfoEnter() {
        sendMessage(MessageIDDefine.PAYPWDINFO_OPEN);
    }

    @Override
    public void changePayPwdEnter() {
        sendMessage(MessageIDDefine.CHANGEPAYPWD_OPEN);
    }

    @Override
    public void resetPayPwdEnter() {
        sendMessage(MessageIDDefine.RESETPAYPWD_OPEN);
    }

    @Override
    public void payPwdResult(String pwd, int requestCode) {
        Message message = Message.obtain();
        message.what = NotifyIDDefine.PAY_PWD_RESULT;
        message.obj = pwd;
        message.arg1 = requestCode;
        sendNotify(message);
    }

    @Override
    public void verifiPayPwdEnter(int requestCode) {
        sendMessage(MessageIDDefine.VERIFIPAYPWD_OPEN, requestCode);
    }

    @Override
    public void getWallet(String token) {
        String data = GsonUtil.toEncodeJson(new TokenToJson(token));
        Call<EncodeData> call = service.getWallet(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    WalletResponse walletResponse = GsonUtil.fromEncodeJson(response.body().getData(), WalletResponse.class);
                    int code = walletResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            walletWindow.responseData(walletResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(walletWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(walletResponse.getDetail());
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
    public void getWalletDetail(WalletDetailRequest walletDetailRequest) {
        String data = GsonUtil.toEncodeJson(walletDetailRequest, WalletDetailRequest.class);
        Call<EncodeData> call = service.getWalletDetail(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    WalletDetailResponse walletDetailResponse = GsonUtil.fromEncodeJson(response.body().getData(), WalletDetailResponse.class);
                    int code = walletDetailResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            walletDetailWindow.responseData(walletDetailResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(walletDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(walletDetailResponse.getDetail());
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
    public void bindAlipay(BindAlipayRequest bindAlipayRequest) {
        if (bindAlipayRequest == null) {
            return;
        }
        Call<EncodeData> call = service.bindAlipay(bindAlipayRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                payBindWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("绑定成功");
                            payBindWindow.hideWindow(true);
                            walletWindow.refreshData();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(payBindWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                payBindWindow.response();
            }
        });
    }

    @Override
    public void vercodeChangePayAccount(String token) {
        String data = GsonUtil.toEncodeJson(new TokenToJson(token));
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

    @Override
    public void changeAliPay(ChangeAliPayRequest changeAliPayRequest) {
        if (changeAliPayRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changeAlipay(changeAliPayRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                changePayAccountWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("更换成功");
                            changePayAccountWindow.hideWindow(true);
                            walletWindow.refreshData();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(changePayAccountWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                changePayAccountWindow.response();
            }
        });
    }

    @Override
    public void getAlipayInfo(String token) {
        if (token == null || token.equals("")) {
            return;
        }
        String data = GsonUtil.toEncodeJson(new TokenToJson(token));
        Call<EncodeData> call = service.getAlipayInfo(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AlipayInfoResponse alipayInfoResponse = GsonUtil.fromEncodeJson(response.body().getData(), AlipayInfoResponse.class);
                    int code = alipayInfoResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            payAccountWindow.responseData(alipayInfoResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(payAccountWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(alipayInfoResponse.getDetail());
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
    public void setPayPwd(SetPayPwdRequest setPayPwdRequest) {
        if (setPayPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.setPayPassword(setPayPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                setPayPwdWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("设置成功");
                            setPayPwdWindow.hideWindow(true);
                            sendNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(setPayPwdWindow);
                            break;
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
    public void changePayPwd(ChangePayPwdRequest changePayPwdRequest) {
        if (changePayPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changePayPwd(changePayPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                changePayPwdWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("更换成功");
                            changePayPwdWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(changePayPwdWindow);
                            break;
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
    public void vercodeResetPayPwd(String token) {
        String data = GsonUtil.toEncodeJson(new TokenToJson(token));
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

    @Override
    public void resetPayPwd(ResetPayPwdRequest resetPayPwdRequest) {
        if (resetPayPwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.resetPayPwd(resetPayPwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                resetPayPwdWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("重设成功，请用新密码登录");
                            resetPayPwdWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(resetPayPwdWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                resetPayPwdWindow.response();
            }
        });
    }

    @Override
    public void withdrawalRequest(WithdrawalRequest withdrawalRequst) {
        if (withdrawalRequst == null) {
            return;
        }
        Call<EncodeData> call = service.withdrawl(withdrawalRequst);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                withdrawalWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("您的提现申请已提交，我们会尽快给您转账，请您耐心等待！");
                            withdrawalWindow.hideWindow(true);
                            walletWindow.refreshData();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(withdrawalWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                withdrawalWindow.response();
            }
        });
    }


}
