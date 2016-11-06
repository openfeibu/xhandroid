package cn.flyexp.mvc.mine;

import android.os.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.AssnJoinRequset;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.ChangeAliPayRequest;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.util.Constants;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.IntegralRecordResponse;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.entity.MyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.TaInfoRequest;
import cn.flyexp.entity.TaInfoResponse;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.entity.WalletResponse;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.entity.WithdrawalRequest;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.XGPush;
import cn.flyexp.mvc.main.MainWindow;


/**
 * Created by guo on 2016/6/21.
 * Modify by txy on 2016/8/1.
 */
public class MineController extends AbstractController implements MineViewCallBack, MineModelCallBack, NotifyManager.Notify {

    public MineWindow mineWindow;
    private MyInfoWindow myInfoWindow;
    private SettingWindow settingWindow;
    private FeedbackWindow feedbackWindow;
    private AboutWindow aboutWindow;
    private TaWindow taWindow;
    private MineModel mineModel;
    private CutAvatarWindow cutAvatarWindow;
    private InvitaionWindow invitaionWindow;
    private ChangePhoneWindow changePhoneWindow;
    private ChangePwdWindow changePwdWindow;
    private CertificationWindow certificationWindow;
    private IntegralWindow integralWindow;
    private CollectionShopWindow collectionShopWindow;
    private AssnJoinWindow assnJoinWindow;
    private IntegralRecordWindow integralRecordWindow;
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

    public MineController() {
        super();
        mineModel = new MineModel(this);
        mineWindow = new MineWindow(this);
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.MYINFO_OPEN) {
            myInfoWindow = new MyInfoWindow(this);
            myInfoWindow.initData((MyInfoResponse.MyInfoResponseData) mes.obj);
            myInfoWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.SETTING_OPEN) {
            settingWindow = new SettingWindow(this);
            settingWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.FEEDBACK_OPEN) {
            feedbackWindow = new FeedbackWindow(this);
            feedbackWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ABOUT_OPEN) {
            aboutWindow = new AboutWindow(this);
            aboutWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.TA_OPEN) {
            taWindow = new TaWindow(this);
            taWindow.getTaInfoRequset((String) mes.obj);
            taWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.CUTHEADPIC_OPEN) {
            cutAvatarWindow = new CutAvatarWindow(this);
            cutAvatarWindow.setImage((String) mes.obj);
            cutAvatarWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.INVITATION_OPEN) {
            invitaionWindow = new InvitaionWindow(this);
            invitaionWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.CHANGEPHONE_OPEN) {
            changePhoneWindow = new ChangePhoneWindow(this);
            changePhoneWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.CHANGEPWD_OPEN) {
            changePwdWindow = new ChangePwdWindow(this);
            changePwdWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.CERTIFICATION_OPEN) {
            certificationWindow = new CertificationWindow(this);
            certificationWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.INTEGRAL_OPEN) {
            integralWindow = new IntegralWindow(this);
            integralWindow.initIntegral((MyInfoResponse.MyInfoResponseData) mes.obj);
            integralWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.COLLECTION_SHOP_OPEN) {
            collectionShopWindow = new CollectionShopWindow(this);
            collectionShopWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_JOIN_OPEN) {
            assnJoinWindow = new AssnJoinWindow(this);
            assnJoinWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.INTEGRAL_RECORD_OPEN) {
            integralRecordWindow = new IntegralRecordWindow(this);
            integralRecordWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.WALLET_OPEN) {
            walletWindow = new WalletWindow(this);
            walletWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.WALLET_DETAIL_OPEN) {
            walletDetailWindow = new WalletDetailWindow(this);
            walletDetailWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.WITHDRAWAL_OPEN) {
            withdrawalWindow = new WithdrawalWindow(this);
            withdrawalWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.PAYACCOUNT_OPEN) {
            payAccountWindow = new PayAccountWindow(this);
            payAccountWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.PAYBIND_OPEN) {
            payBindWindow = new PayBindWindow(this);
            payBindWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.CHANGEPAYACCOUNT_OPEN) {
            changePayAccountWindow = new ChangePayAccountWindow(this);
            changePayAccountWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.PAYPWDINFO_OPEN) {
            payPwdInfoWindow = new PayPwdInfoWindow(this);
            payPwdInfoWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.SETPAYPWD_OPEN) {
            setPayPwdWindow = new SetPayPwdWindow(this);
            setPayPwdWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.RESETPAYPWD_OPEN) {
            resetPayPwdWindow = new ResetPayPwdWindow(this);
            resetPayPwdWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.CHANGEPAYPWD_OPEN) {
            changePayPwdWindow = new ChangePayPwdWindow(this);
            changePayPwdWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.VERIFIPAYPWD_OPEN) {
            verifiPayPwdWindow = new VerifiPayPwdWindow(this);
            verifiPayPwdWindow.setRequestCode(mes.arg1);
            verifiPayPwdWindow.showWindow(true, true);
        }

    }


    protected void registerMessages() {
        registerMessage(MessageIDDefine.MYINFO_OPEN, this);
        registerMessage(MessageIDDefine.SETTING_OPEN, this);
        registerMessage(MessageIDDefine.FEEDBACK_OPEN, this);
        registerMessage(MessageIDDefine.ABOUT_OPEN, this);
        registerMessage(MessageIDDefine.TA_OPEN, this);
        registerMessage(MessageIDDefine.CUTHEADPIC_OPEN, this);
        registerMessage(MessageIDDefine.INVITATION_OPEN, this);
        registerMessage(MessageIDDefine.CHANGEPHONE_OPEN, this);
        registerMessage(MessageIDDefine.CHANGEPWD_OPEN, this);
        registerMessage(MessageIDDefine.CERTIFICATION_OPEN, this);
        registerMessage(MessageIDDefine.INTEGRAL_OPEN, this);
        registerMessage(MessageIDDefine.COLLECTION_SHOP_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_JOIN_OPEN, this);
        registerMessage(MessageIDDefine.INTEGRAL_RECORD_OPEN, this);
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
        if (mes.what == NotifyIDDefine.NOTIFY_MINE_REFRESH) {
            mineWindow.refreshData();
        } else if (mes.what == NotifyIDDefine.PAY_PWD_RESULT && mes.arg1 == Constants.PAY_RESULT_WITHDRAWAL) {
            if (withdrawalWindow != null) {
                withdrawalWindow.payPwdResponse((String) mes.obj);
            }
        } else if (mes.what == NotifyIDDefine.SHARE_SUCCESS) {
            String token = mineWindow.getStringByPreference("token");
            share(token);
        }
    }

    @Override
    public void myTaskEnter() {
        sendMessage(MessageIDDefine.MYTASK_OPEN);
    }

    @Override
    public void myTopicEnter() {
        sendMessage(MessageIDDefine.MYTOPIC_OPEN);
    }

    @Override
    public void myInfoEnter(MyInfoResponse.MyInfoResponseData responseData) {
        sendMessage(MessageIDDefine.MYINFO_OPEN, responseData);
    }

    @Override
    public void settingEnter() {
        sendMessage(MessageIDDefine.SETTING_OPEN);
    }

    @Override
    public void shareEnter() {
        sendMessage(MessageIDDefine.INVITATION_OPEN);
    }

    @Override
    public void walletEnter() {
        sendMessage(MessageIDDefine.WALLET_OPEN);
    }

    @Override
    public void feedbeakEnter() {
        sendMessage(MessageIDDefine.FEEDBACK_OPEN);
    }

    @Override
    public void aboutEnter() {
        sendMessage(MessageIDDefine.ABOUT_OPEN);
    }

    @Override
    public void assnManageEnter() {
        sendMessage(MessageIDDefine.ASSN_MANAGE_OPEN);
    }

    @Override
    public void assnJoinEnter() {
        sendMessage(MessageIDDefine.ASSN_JOIN_OPEN);
    }

    @Override
    public void changePhoneEnter() {
        sendMessage(MessageIDDefine.CHANGEPHONE_OPEN);
    }

    @Override
    public void changePwdEnter() {
        sendMessage(MessageIDDefine.CHANGEPWD_OPEN);
    }

    @Override
    public void certificationEnter() {
        sendMessage(MessageIDDefine.CERTIFICATION_OPEN);
    }

    @Override
    public void shopDetailEnter() {
        sendMessage(MessageIDDefine.SHOP_DETAIL_OPEN);
    }

    @Override
    public void walletDetailEnter() {
        sendMessage(MessageIDDefine.WALLET_DETAIL_OPEN);
    }

    @Override
    public void cutAvatarPicEnter(String imgPath) {
        sendMessage(MessageIDDefine.CUTHEADPIC_OPEN, imgPath);
    }

    @Override
    public void integralRecordEnter() {
        sendMessage(MessageIDDefine.INTEGRAL_RECORD_OPEN);
    }

    @Override
    public void integralEnter(MyInfoResponse.MyInfoResponseData responseData) {
        sendMessage(MessageIDDefine.INTEGRAL_OPEN, responseData);
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
    public void getWallet(String token) {
        mineModel.getWallet(token);
    }

    @Override
    public void getWalletDetail(WalletDetailRequest walletDetailRequest) {
        mineModel.getWalletDetail(walletDetailRequest);
    }

    @Override
    public void bindAlipay(BindAlipayRequest bindAlipayRequest) {
        mineModel.bindAlipay(bindAlipayRequest);
    }

    @Override
    public void vercodeChangePayAccount(String token) {
        mineModel.changPayAccountVercode(token);
    }

    @Override
    public void changeAliPay(ChangeAliPayRequest changeAliPayRequest) {
        mineModel.changeAliPay(changeAliPayRequest);
    }

    @Override
    public void getAlipayInfo(String token) {
        mineModel.getAlipayInfo(token);
    }

    @Override
    public void setPayPwd(SetPayPwdRequest setPayPwdRequest) {
        mineModel.setPayPwd(setPayPwdRequest);
    }

    @Override
    public void changePayPwd(ChangePayPwdRequest changePayPwdRequest) {
        mineModel.changePayPwd(changePayPwdRequest);
    }

    @Override
    public void vercodeResetPayPwd(String tokenVercode) {
        mineModel.vercodeResetPayPwd(tokenVercode);
    }

    @Override
    public void resetPayPwd(ResetPayPwdRequest resetPayPwdRequest) {
        mineModel.resetPayPwd(resetPayPwdRequest);
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
    public void integralRecordRequest(IntegralRecordRequest integralRecordRequest) {
        mineModel.integralRecord(integralRecordRequest);
    }

    @Override
    public void uploadImageCertifi(String token, ArrayList<File> files) {
        mineModel.uploadImageCertifi(token, files);
    }

    @Override
    public void getWebUrl(WebUrlRequest webUrlRequest) {
        mineModel.getWebUrl(webUrlRequest);
    }

    @Override
    public void withdrawalRequest(WithdrawalRequest withdrawalRequst) {
        mineModel.withdrawl(withdrawalRequst);
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        mineModel.logout(logoutRequest);
    }

    @Override
    public void taInfo(TaInfoRequest taInfoRequest) {
        mineModel.getTaInfo(taInfoRequest);
    }

    @Override
    public void getMyInfo(MyInfoRequest myProileRequest) {
        mineModel.getMyInfo(myProileRequest);
    }

    @Override
    public void changePwd(ChangePwdRequest changePwdRequest) {
        mineModel.changePwd(changePwdRequest);
    }

    @Override
    public void changeMyInfo(ChangeMyInfoRequest myInfoRequest) {
        mineModel.changeMyInfo(myInfoRequest);
    }

    @Override
    public void uploadImageAvatar(String token, List<File> avatar) {
        mineModel.uploadImageAvatar(token, avatar);
    }

    @Override
    public void uploadImageAssn(String token, List<File> assn) {
        mineModel.uploadImageAssn(token, assn);
    }

    @Override
    public void assnJoin(AssnJoinRequset assnEnterRequset) {
        mineModel.join(assnEnterRequset);
    }

    @Override
    public void feedback(FeedbackRequest feedbackRequest) {
        mineModel.feedback(feedbackRequest);
    }

    @Override
    public void update(UpdateRequest updateRequest) {
        mineModel.update(updateRequest);
    }

    @Override
    public void share(String token) {
        mineModel.share(token);
    }

    @Override
    public void taInfoResponse(TaInfoResponse taInfoResponse) {
        if (taInfoResponse == null || taInfoResponse.getData() == null) {
            return;
        }
        int code = taInfoResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                taWindow.initData(taInfoResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(taWindow);
                break;
            case ResponseCode.RESPONSE_110:
                taWindow.showToast(taInfoResponse.getDetail());
                break;
        }
    }

    @Override
    public void getMyInfoResponse(MyInfoResponse myInfoResponse) {
        if (myInfoResponse == null) {
            return;
        }
        int code = myInfoResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                mineWindow.myProileResponse(myInfoResponse.getData());
                mineWindow.putStringByPreference("mine_openid", myInfoResponse.getData().getOpenid());
                XGPush.registerPush(myInfoResponse.getData().getOpenid());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(mineWindow);
                break;
            case ResponseCode.RESPONSE_110:
                mineWindow.showToast(myInfoResponse.getDetail());
                break;
        }
    }

    @Override
    public void logoutResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                MainWindow.vp_main.setCurrentItem(0);
                settingWindow.showToast("注销成功");
                settingWindow.hideWindow(false);
                XGPush.unbundingPush();
                logout(settingWindow);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(mineWindow);
                break;
            case ResponseCode.RESPONSE_110:
                mineWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void changePwdResponse(CommonResponse commonResponse) {
        changePwdWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                changePwdWindow.showToast("修改成功，请用新密码登录");
                changePwdWindow.hideWindow(true);
                loginWindowEnter();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(mineWindow);
                break;
            case ResponseCode.RESPONSE_2006:
                changePwdWindow.showToast("旧密码错误，请重试");
                break;
            case ResponseCode.RESPONSE_110:
                changePwdWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void changeInfoResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                mineWindow.refreshData();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(mineWindow);
                break;
            case ResponseCode.RESPONSE_110:
                mineWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void feedbackResponse(CommonResponse commonResponse) {
        feedbackWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                feedbackWindow.showToast("谢谢你的宝贵的建议 ");
                feedbackWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(feedbackWindow);
                break;
            case ResponseCode.RESPONSE_110:
                feedbackWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void asnnJoinResponse(CommonResponse commonResponse) {
        assnJoinWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnJoinWindow.showToast("申请入驻请求已提交，我们将尽快为您办理，请您耐心等待管理员的审核，一般情况下24小时内会反馈给您审核结果。");
                assnJoinWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnJoinWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnJoinWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void uploadImageAvatar(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                cutAvatarWindow.showToast("上传成功");
                cutAvatarWindow.hideWindow(true);
                mineWindow.refreshData();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(cutAvatarWindow);
                break;
            case ResponseCode.RESPONSE_110:
                cutAvatarWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void uploadImageAssn(UploadImageResponse uploadImageAssnResponse) {
        if (uploadImageAssnResponse == null) {
            return;
        }
        int code = uploadImageAssnResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnJoinWindow.assnJoinRequset(uploadImageAssnResponse.getUrl());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnJoinWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnJoinWindow.showToast(uploadImageAssnResponse.getDetail());
                break;
        }
    }

    @Override
    public void updateResponse(UpdateResponse updateResponse) {
        aboutWindow.response();
        if (updateResponse == null) {
            return;
        }
        int code = updateResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                aboutWindow.responseData(updateResponse.getData());
                break;
            case ResponseCode.RESPONSE_110:
                aboutWindow.showToast(updateResponse.getDetail());
                break;
        }
    }

    @Override
    public void integralRecordResponse(IntegralRecordResponse integralRecordResponse) {
        if (integralRecordResponse == null) {
            integralRecordWindow.responseNoneData();
            return;
        }
        int code = integralRecordResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                integralRecordWindow.responseData(integralRecordResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(integralRecordWindow);
                break;
            case ResponseCode.RESPONSE_110:
                integralRecordWindow.showToast(integralRecordResponse.getDetail());
                break;
        }
    }

    @Override
    public void uploadImageCertifi(UploadImageResponse uploadImageAssnResponse) {
        certificationWindow.response();
        if (uploadImageAssnResponse == null) {
            return;
        }
        int code = uploadImageAssnResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                certificationWindow.showToast("上传成功，请等待结果");
                certificationWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(certificationWindow);
                break;
            case ResponseCode.RESPONSE_110:
                certificationWindow.showToast(uploadImageAssnResponse.getDetail());
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
                integralWindow.loadUrl(webUrlResponse.getUrl());
                break;
            case ResponseCode.RESPONSE_110:
                integralWindow.showToast(webUrlResponse.getDetail());
                break;
        }

    }

    @Override
    public void withdrawlResponse(CommonResponse commonResponse) {
        withdrawalWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                withdrawalWindow.showToast("您的提现申请已提交，我们会尽快给您转账，请您耐心等待！");
                withdrawalWindow.hideWindow(true);
                walletWindow.refreshData();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(withdrawalWindow);
                break;
            case ResponseCode.RESPONSE_110:
                withdrawalWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void walletResponse(WalletResponse walletResponse) {
        if (walletResponse == null) {
            return;
        }
        int code = walletResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                walletWindow.responseData(walletResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(walletWindow);
                break;
            case ResponseCode.RESPONSE_110:
                walletWindow.showToast(walletResponse.getDetail());
                break;
        }
    }

    @Override
    public void walletDetailResponse(WalletDetailResponse walletDetailResponse) {
        if (walletDetailResponse == null) {
            return;
        }
        int code = walletDetailResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                walletDetailWindow.responseData(walletDetailResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(walletDetailWindow);
                break;
            case ResponseCode.RESPONSE_110:
                walletDetailWindow.showToast(walletDetailResponse.getDetail());
                break;
        }
    }

    @Override
    public void bindAlipayResponse(CommonResponse commonResponse) {
        payBindWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                payBindWindow.showToast("绑定成功");
                payBindWindow.hideWindow(true);
                walletWindow.refreshData();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(payBindWindow);
                break;
            case ResponseCode.RESPONSE_110:
                payBindWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void changeAliPayResponse(CommonResponse commonResponse) {
        changePayAccountWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                changePayAccountWindow.showToast("更换成功");
                changePayAccountWindow.hideWindow(true);
                walletWindow.refreshData();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(changePayAccountWindow);
                break;
            case ResponseCode.RESPONSE_110:
                changePayAccountWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void alipayInfoResponse(AlipayInfoResponse alipayInfoResponse) {
        if (alipayInfoResponse == null) {
            return;
        }
        int code = alipayInfoResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                payAccountWindow.responseData(alipayInfoResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(payAccountWindow);
                break;
            case ResponseCode.RESPONSE_110:
                payAccountWindow.showToast(alipayInfoResponse.getDetail());
                break;
        }
    }

    @Override
    public void setPayPwdResponse(CommonResponse commonResponse) {
        setPayPwdWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                setPayPwdWindow.showToast("设置成功");
                setPayPwdWindow.hideWindow(true);
                mineWindow.refreshData();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(setPayPwdWindow);
                break;
            case ResponseCode.RESPONSE_110:
                setPayPwdWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void changePayPwdResponse(CommonResponse commonResponse) {
        changePayPwdWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                changePayPwdWindow.showToast("更换成功");
                changePayPwdWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(changePayPwdWindow);
                break;
            case ResponseCode.RESPONSE_110:
                changePayPwdWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void resetPayPwdResponse(CommonResponse commonResponse) {
        resetPayPwdWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                resetPayPwdWindow.showToast("重设成功，请用新密码登录");
                resetPayPwdWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(resetPayPwdWindow);
                break;
            case ResponseCode.RESPONSE_110:
                resetPayPwdWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

}
