package cn.flyexp.mvc.wallet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.ChangeAliPayRequest;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.entity.MyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.TaInfoRequest;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WithdrawalRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/12 0012.
 */
public interface WalletViewCallBack extends AbstractWindow.WindowCallBack {

    void walletDetailEnter();

    void withdrawalEnter();

    void withdrawalRequest(WithdrawalRequest withdrawalRequst);

    void payAccountEnter();

    void payBindEnter();

    void changePayAccount();

    void setPayPwdEnter();

    void payPwdInfoEnter();

    void changePayPwdEnter();

    void resetPayPwdEnter();

    void getWallet(String token);

    void getWalletDetail(WalletDetailRequest walletDetailRequest);

    void bindAlipay(BindAlipayRequest bindAlipayRequest);

    void vercodeChangePayAccount(String token);

    void changeAliPay(ChangeAliPayRequest changeAliPayRequest);

    void getAlipayInfo(String token);

    void setPayPwd(SetPayPwdRequest setPayPwdRequest);

    void changePayPwd(ChangePayPwdRequest changePayPwdRequest);

    void vercodeResetPayPwd(String tokenVercode);

    void resetPayPwd(ResetPayPwdRequest resetPayPwdRequest);

    void payPwdResult(String pwd, int requestCode);

    void verifiPayPwdEnter(int payResultWithdrawal);

}
