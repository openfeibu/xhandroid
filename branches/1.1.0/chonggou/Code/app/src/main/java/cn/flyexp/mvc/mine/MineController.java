package cn.flyexp.mvc.mine;

import android.os.Message;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.CertificationRequest;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.IntegralRecordResponse;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.entity.MyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.TaInfoRequest;
import cn.flyexp.entity.TaInfoResponse;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.push.XGPush;
import cn.flyexp.mvc.main.MainWindow;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.UploadFileHelper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by guo on 2016/6/21.
 * Modify by txy on 2016/8/1.
 */
public class MineController extends AbstractController implements MineViewCallBack, NotifyManager.Notify {

    public MineWindow mineWindow;
    private MyInfoWindow myInfoWindow;
    private SettingWindow settingWindow;
    private FeedbackWindow feedbackWindow;
    private AboutWindow aboutWindow;
    private TaWindow taWindow;
    private CutAvatarWindow cutAvatarWindow;
    private InvitaionWindow invitaionWindow;
    private ChangePhoneWindow changePhoneWindow;
    private ChangePwdWindow changePwdWindow;
    private CertificationWindow certificationWindow;
    private IntegralWindow integralWindow;
    private CollectionShopWindow collectionShopWindow;
    private IntegralRecordWindow integralRecordWindow;
    private MessageWindow messageWindow;
    private MineModel mineModel;

    public MineController() {
        super();
        mineModel = new MineModel();
        mineWindow = new MineWindow(this);
        mineWindow.request();
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.MYINFO_OPEN) {
            myInfoWindow = new MyInfoWindow(this);
            myInfoWindow.initData((MyInfoResponse.MyInfoResponseData) mes.obj);
            myInfoWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.SETTING_OPEN) {
            settingWindow = new SettingWindow(this);
            settingWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.FEEDBACK_OPEN) {
            feedbackWindow = new FeedbackWindow(this);
            feedbackWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.ABOUT_OPEN) {
            aboutWindow = new AboutWindow(this);
            aboutWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.TA_OPEN) {
            taWindow = new TaWindow(this);
            taWindow.getTaInfoRequset((String) mes.obj);
            taWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.CUTHEADPIC_OPEN) {
            cutAvatarWindow = new CutAvatarWindow(this);
            cutAvatarWindow.setImage((String) mes.obj);
            cutAvatarWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.INVITATION_OPEN) {
            invitaionWindow = new InvitaionWindow(this);
            invitaionWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.CHANGEPHONE_OPEN) {
            changePhoneWindow = new ChangePhoneWindow(this);
            changePhoneWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.CHANGEPWD_OPEN) {
            changePwdWindow = new ChangePwdWindow(this);
            changePwdWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.CERTIFICATION_OPEN) {
            certificationWindow = new CertificationWindow(this);
            certificationWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.INTEGRAL_OPEN) {
            integralWindow = new IntegralWindow(this);
            integralWindow.initIntegral((MyInfoResponse.MyInfoResponseData) mes.obj);
            integralWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.COLLECTION_SHOP_OPEN) {
            collectionShopWindow = new CollectionShopWindow(this);
            collectionShopWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.INTEGRAL_RECORD_OPEN) {
            integralRecordWindow = new IntegralRecordWindow(this);
            integralRecordWindow.showWindow(true);
        } else if (mes.what == MessageIDDefine.MESSAGE_OPEN) {
            messageWindow = new MessageWindow(this);
            messageWindow.showWindow(true);
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
        registerMessage(MessageIDDefine.INTEGRAL_RECORD_OPEN, this);
        registerMessage(MessageIDDefine.MESSAGE_OPEN, this);
        registerNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH, this);
        registerNotify(NotifyIDDefine.PAY_PWD_RESULT, this);
        registerNotify(NotifyIDDefine.SHARE_SUCCESS, this);
        registerNotify(NotifyIDDefine.NOTIFY_MYTASK_REFRESH, this);
        registerNotify(NotifyIDDefine.NOTIFY_MESSAGE_REFRESH, this);
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_MINE_REFRESH) {
            mineWindow.request();
        } else if (mes.what == NotifyIDDefine.SHARE_SUCCESS) {
            String token = WindowHelper.getStringByPreference("token");
            share(token);
        } else if (mes.what == NotifyIDDefine.NOTIFY_MYTASK_REFRESH) {
            mineWindow.remindMyTask();
        } else if (mes.what == NotifyIDDefine.NOTIFY_MESSAGE_REFRESH) {
            mineWindow.remindMessage();
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
    public void myAssnEnter() {
        sendMessage(MessageIDDefine.MY_ASSN_OPEN);
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
    public void messageEnter() {
        sendMessage(MessageIDDefine.MESSAGE_OPEN);
    }

    @Override
    public void integralRecordRequest(IntegralRecordRequest integralRecordRequest) {
        if (integralRecordRequest == null) {
            return;
        }
        Call<EncodeData> call = service.integralRecord(integralRecordRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    IntegralRecordResponse integralRecordResponse = GsonUtil.fromEncodeJson(response.body().getData(), IntegralRecordResponse.class);
                    int code = integralRecordResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            integralRecordWindow.responseData(integralRecordResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(integralRecordWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(integralRecordResponse.getDetail());
                            break;
                    }
                } else {
                    integralRecordWindow.responseNoneData();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                integralRecordWindow.responseNoneData();
            }
        });
    }

    @Override
    public void uploadImageCertifi(CertificationRequest certificationRequest, ArrayList<File> file) {
        if (file == null || certificationRequest == null) {
            return;
        }
        MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(file);
        Call<EncodeData> call = service.uploadImageCertifi(multipartBody, GsonUtil.toEncodeJson(certificationRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                certificationWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    UploadImageResponse uploadImageAssnResponse = GsonUtil.fromEncodeJson(response.body()
                            .getData(), UploadImageResponse.class);
                    int code = uploadImageAssnResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("上传成功，请等待结果");
                            certificationWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(certificationWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(uploadImageAssnResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                certificationWindow.response();
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
                            integralWindow.loadUrl(webUrlResponse.getUrl());
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
    public void logout(LogoutRequest logoutRequest) {
        if (logoutRequest == null) {
            return;
        }
        Call<EncodeData> call = service.logout(logoutRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            MainWindow.vp_main.setCurrentItem(0);
                            WindowHelper.showToast("注销成功");
                            settingWindow.hideWindow(false);
                            XGPush.unbundingPush();
                            logout(settingWindow);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(mineWindow);
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
    public void taInfo(TaInfoRequest taInfoRequest) {
        if (taInfoRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(taInfoRequest);
        Call<EncodeData> call = service.getOthersInfo(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TaInfoResponse taInfoResponse = GsonUtil.fromEncodeJson(response.body().getData(), TaInfoResponse.class);
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
                            WindowHelper.showToast(taInfoResponse.getDetail());
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
    public void getMyInfo(MyInfoRequest myInfoRequest) {
        final String data = GsonUtil.toEncodeJson(myInfoRequest);
        Call<EncodeData> respond = service.getMyProile(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyInfoResponse myInfoResponse = GsonUtil.fromEncodeJson(response.body().getData(), MyInfoResponse.class);
                    int code = myInfoResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            mineWindow.responseData(myInfoResponse.getData());
                            WindowHelper.putStringByPreference(SharedPrefs.KET_OPENID, myInfoResponse.getData().getOpenid());
                            String push = WindowHelper.getStringByPreference(SharedPrefs.KET_PUSH);
                            if (push.equals(SharedPrefs.VALUE_XMPUSH)) {
                                MiPushClient.setUserAccount(getContext(), myInfoResponse.getData().getOpenid(), null);
                            } else if (push.equals(SharedPrefs.VALUE_XGPUSH)) {
                                XGPush.registerPush(myInfoResponse.getData().getOpenid());
                            }
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(mineWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(myInfoResponse.getDetail());
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
    public void changePwd(ChangePwdRequest changePwdRequest) {
        if (changePwdRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changePwd(changePwdRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                changePwdWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("修改成功，请用新密码登录");
                            changePwdWindow.hideWindow(true);
                            loginWindowEnter();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(mineWindow);
                            break;
                        case ResponseCode.RESPONSE_2006:
                            WindowHelper.showToast("旧密码错误，请重试");
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
                changePwdWindow.response();
            }
        });
    }

    @Override
    public void changeMyInfo(ChangeMyInfoRequest changeMyInfoRequest) {
        if (changeMyInfoRequest == null) {
            return;
        }
        Call<EncodeData> call = service.changeMyInfo(changeMyInfoRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            mineWindow.request();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(mineWindow);
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
    public void uploadImageAvatar(String token, List<File> file) {
        if (file == null || token == null || token.equals("")) {
            return;
        }
        MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(file);
        Call<EncodeData> call = service.uploadImageAvatar(multipartBody, GsonUtil.toEncodeJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body()
                            .getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("上传成功");
                            cutAvatarWindow.hideWindow(true);
                            mineWindow.request();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(cutAvatarWindow);
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
    public void feedback(FeedbackRequest feedbackRequest) {
        if (feedbackRequest == null) {
            return;
        }
        Call<EncodeData> call = service.feedback(feedbackRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                feedbackWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("谢谢你的宝贵的建议 ");
                            feedbackWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(feedbackWindow);
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
                feedbackWindow.response();
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
                aboutWindow.response();
                if (response.body() != null && response.isSuccess()) {
                    UpdateResponse updateResponse = GsonUtil.fromEncodeJson(response.body().getData(), UpdateResponse.class);
                    if (updateResponse == null) {
                        return;
                    }
                    int code = updateResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            aboutWindow.responseData(updateResponse.getData());
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
                aboutWindow.response();
            }
        });
    }

    @Override
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

    @Override
    public void getMessageList(MessageRequest messageRequest) {
        if (messageRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(messageRequest, MessageRequest.class);
        Call<EncodeData> respond = service.getMessageList(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MessageResponse messageResponse = GsonUtil.fromEncodeJson(response.body().getData(), MessageResponse.class);
                    int code = messageResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            messageWindow.messageResponse(messageResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(messageWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(messageResponse.getDetail());
                            break;
                    }
                } else {
                    messageWindow.loadingFailure();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                messageWindow.loadingFailure();
            }
        });
    }

}
