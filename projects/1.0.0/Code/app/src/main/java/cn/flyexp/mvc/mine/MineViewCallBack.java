package cn.flyexp.mvc.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.entity.AssnJoinRequset;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.ChangeAliPayRequest;
import cn.flyexp.entity.ChangePayAccountVCRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.MyInfoRequest;
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
public interface MineViewCallBack extends AbstractWindow.WindowCallBack {

    void myTaskEnter();

    void myTopicEnter();

    void myInfoEnter(MyInfoResponse.MyInfoResponseData responseData);

    void settingEnter();

    void shareEnter();

    void walletEnter();

    void feedbeakEnter();

    void aboutEnter();

    void assnManageEnter();

    void assnJoinEnter();

    void changePhoneEnter();

    void changePwdEnter();

    void certificationEnter();

    void shopDetailEnter();

    void walletDetailEnter();

    void cutAvatarPicEnter(String imgPath);

    void logout(LogoutRequest logoutRequest);

    void taInfo(TaInfoRequest taInfoRequest);

    void getMyInfo(MyInfoRequest myProileRequest);

    void changePwd(ChangePwdRequest changePwdRequest);

    void changeMyInfo(ChangeMyInfoRequest myInfoRequest);

    void uploadImageAvatar(String token, List<File> avatar);

    void uploadImageAssn(String token, List<File> assn);

    void assnJoin(AssnJoinRequset assnJoinRequset);

    void feedback(FeedbackRequest feedbackRequest);

    void update(UpdateRequest updateRequest);

    void share(String token);

    void integralRecordEnter();

    void integralRecordRequest(IntegralRecordRequest integralRecordRequest);

    void uploadImageCertifi(String token, ArrayList<File> files);

    void integralEnter(MyInfoResponse.MyInfoResponseData responseData);

    void getWebUrl(WebUrlRequest webUrlRequest);

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
