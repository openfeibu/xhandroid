package cn.flyexp.mvc.mine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.entity.CertificationRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.MyInfoRequest;
import cn.flyexp.entity.TaInfoRequest;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.WebUrlRequest;
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

    void myAssnEnter();

    void changePhoneEnter();

    void changePwdEnter();

    void certificationEnter();

    void shopDetailEnter();

    void cutAvatarPicEnter(String imgPath);

    void logout(LogoutRequest logoutRequest);

    void taInfo(TaInfoRequest taInfoRequest);

    void getMyInfo(MyInfoRequest myProileRequest);

    void changePwd(ChangePwdRequest changePwdRequest);

    void changeMyInfo(ChangeMyInfoRequest myInfoRequest);

    void uploadImageAvatar(String token, List<File> avatar);

    void feedback(FeedbackRequest feedbackRequest);

    void update(UpdateRequest updateRequest);

    void share(String token);

    void integralRecordEnter();

    void integralRecordRequest(IntegralRecordRequest integralRecordRequest);

    void uploadImageCertifi(CertificationRequest certificationRequest, ArrayList<File> files);

    void integralEnter(MyInfoResponse.MyInfoResponseData responseData);

    void getWebUrl(WebUrlRequest webUrlRequest);

    void messageEnter();

     void getMessageList(MessageRequest messageRequest);
}
