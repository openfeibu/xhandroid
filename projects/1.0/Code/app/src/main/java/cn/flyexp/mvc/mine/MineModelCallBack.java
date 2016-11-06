package cn.flyexp.mvc.mine;

import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.IntegralRecordResponse;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.TaInfoResponse;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.entity.WalletResponse;
import cn.flyexp.entity.WebUrlResponse;

/**
 * Created by txy on 2016/7/25 0025.
 */
public interface MineModelCallBack {

    void taInfoResponse(TaInfoResponse taProileResponse);

    void getMyInfoResponse(MyInfoResponse myProileResponse);

    void logoutResponse(CommonResponse commonResponse);

    void changePwdResponse(CommonResponse commonResponse);

    void changeInfoResponse(CommonResponse commonResponse);

    void feedbackResponse(CommonResponse commonResponse);

    void asnnJoinResponse(CommonResponse commonResponse);

    void uploadImageAvatar(CommonResponse commonResponse);

    void uploadImageAssn(UploadImageResponse uploadImageAssnResponse);

    void updateResponse(UpdateResponse updateResponse);

    void integralRecordResponse(IntegralRecordResponse integralRecordResponse);

    void uploadImageCertifi(UploadImageResponse uploadImageAssnResponse);

    void webUrlResponse(WebUrlResponse webUrlResponse);

    void withdrawlResponse(CommonResponse commonResponse);

    void walletResponse(WalletResponse walletResponse);

    void walletDetailResponse(WalletDetailResponse walletDetailResponse);

    void bindAlipayResponse(CommonResponse commonResponse);

    void changeAliPayResponse(CommonResponse commonResponse);

    void alipayInfoResponse(AlipayInfoResponse alipayInfoResponse);

    void setPayPwdResponse(CommonResponse commonResponse);

    void changePayPwdResponse(CommonResponse commonResponse);

    void resetPayPwdResponse(CommonResponse commonResponse);
}
