package cn.flyexp.mvc.assn;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.AssnActivityPublishRequest;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnExamineRequest;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.entity.AssnMemberRequest;
import cn.flyexp.entity.AssnMemberResponse;
import cn.flyexp.entity.AssnRequest;
import cn.flyexp.entity.AssnResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.DeleteMemberRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.MyAssnResponse;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.AssnProfilePublishRequest;
import cn.flyexp.entity.AssnProfileRequest;
import cn.flyexp.entity.AssnProfileResponse;
import cn.flyexp.entity.SetMemberLevelRequest;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.UploadFileHelper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class AssnController extends AbstractController implements AssnViewCallBack, NotifyManager.Notify {

    private AssnWindow assnWindow;
    private AssnActivityDetailWindow assnActivityDetailWindow;
    private AssnIntroduceWindow assnIntroduceWindow;
    private AssnManageWindow assnManageWindow;
    private AssnIntroducePublishWindow assnIntroducePublishWindow;
    private AssnNoticePublishWindow assnNoticePublishWindow;
    private AssnActivityPublishWindow assnActivityPublishWindow;
    private MyAssnWindow myAssnWindow;
    private MyAssnDetailWindow myAssnDetailWindow;
    private AssnDetailWindow assnDetailWindow;
    private AssnJoinWindow assnJoinWindow;
    private MyAssnActivityWindow myAssnActivityWindow;
    private AssnExamineWindow assnExamineWindow;
    private AssnExamineDetailWindow assnExamineDetailWindow;
    private final AssnModel assnModel;
    private AssnMemberWindow assnMemberWindow;

    public AssnController() {
        super();
        assnModel = new AssnModel();
    }

    @Override
    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.ASSN_OPEN) {
            assnWindow = new AssnWindow(this);
            assnWindow.showWindow();
            assnWindow.requsetAssnActi();
        } else if (mes.what == MessageIDDefine.ASSN_ACTIVITY_DETAIL_OPEN) {
            assnActivityDetailWindow = new AssnActivityDetailWindow(this);
            assnActivityDetailWindow.initData((AssnActivityResponse.AssnActivityResponseData) mes.obj);
            assnActivityDetailWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_INTRODUCE_OPEN) {
            assnIntroduceWindow = new AssnIntroduceWindow(this);
            assnIntroduceWindow.introduceRequest(mes.arg1);
            assnIntroduceWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_MANAGE_OPEN) {
            assnManageWindow = new AssnManageWindow(this);
            assnManageWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_INTRODUCE_PUBLISH_OPEN) {
            assnIntroducePublishWindow = new AssnIntroducePublishWindow(this);
            assnIntroducePublishWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_NOTICE_PUBLISH_OPEN) {
            assnNoticePublishWindow = new AssnNoticePublishWindow(this);
            assnNoticePublishWindow.setAid(mes.arg1);
            assnNoticePublishWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_ACTIVITY_PUBLISH_OPEN) {
            assnActivityPublishWindow = new AssnActivityPublishWindow(this);
            assnActivityPublishWindow.setAid(mes.arg1);
            assnActivityPublishWindow.showWindow();
        } else if (mes.what == MessageIDDefine.MY_ASSN_OPEN) {
            myAssnWindow = new MyAssnWindow(this);
            myAssnWindow.showWindow();
        } else if (mes.what == MessageIDDefine.MYASSN_DETAIL_OPEN) {
            myAssnDetailWindow = new MyAssnDetailWindow(this);
            myAssnDetailWindow.init(mes.arg1, mes.arg2);
            myAssnDetailWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_DETAIL_OPEN) {
            assnDetailWindow = new AssnDetailWindow(this);
            assnDetailWindow.requestData(mes.arg1);
            assnDetailWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_JOIN_OPEN) {
            assnJoinWindow = new AssnJoinWindow(this);
            assnJoinWindow.initAid(mes.arg1);
            assnJoinWindow.showWindow();
        } else if (mes.what == MessageIDDefine.MYASSN_ACTIVITY_OPEN) {
            myAssnActivityWindow = new MyAssnActivityWindow(this);
            myAssnActivityWindow.setAid(mes.arg1, mes.arg2);
            myAssnActivityWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_EXAMINE_OPEN) {
            assnExamineWindow = new AssnExamineWindow(this);
            assnExamineWindow.requestMemberList(mes.arg1);
            assnExamineWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_EXAMINE_DETAIL_OPEN) {
            assnExamineDetailWindow = new AssnExamineDetailWindow(this);
            assnExamineDetailWindow.initData((AssnExamineListResponse.AssnExamineListResponseData) mes.obj);
            assnExamineDetailWindow.showWindow();
        } else if (mes.what == MessageIDDefine.ASSN_MEMBER_OPEN) {
            assnMemberWindow = new AssnMemberWindow(this);
            assnMemberWindow.init(mes.getData());
            assnMemberWindow.showWindow();
        }
    }

    @Override
    protected void registerMessages() {
        registerMessage(MessageIDDefine.ASSN_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_INTRODUCE_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_ACTIVITY_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_MANAGE_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_INTRODUCE_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_NOTICE_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_ACTIVITY_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.MY_ASSN_OPEN, this);
        registerMessage(MessageIDDefine.MYASSN_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_JOIN_OPEN, this);
        registerMessage(MessageIDDefine.MYASSN_ACTIVITY_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_EXAMINE_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_EXAMINE_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_MEMBER_OPEN, this);
        registerNotify(NotifyIDDefine.NOTIFY_ASSN_MENBER_REFRESH, this);
        registerNotify(NotifyIDDefine.NOTIFY_ASSN_ACT_REFRESH, this);
        registerNotify(NotifyIDDefine.NOTIFY_ASSN_NOTICE_REFRESH, this);
    }

    @Override
    public void onNotify(Message mes) {
    }

    private int getAidByNotify(String data) {
        int aid = -1;
        try {
            JSONObject jsonObject = new JSONObject(data);
            aid = jsonObject.getInt("aid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return aid;
    }

    @Override
    public void activityDetailEnter(AssnActivityResponse.AssnActivityResponseData
                                            assnActivityResponseData) {
        sendMessage(MessageIDDefine.ASSN_ACTIVITY_DETAIL_OPEN, assnActivityResponseData);
    }

    @Override
    public void introduceEnter(int aid) {
        sendMessage(MessageIDDefine.ASSN_INTRODUCE_OPEN, aid);
    }

    @Override
    public void introducePublishEnter() {
        sendMessage(MessageIDDefine.ASSN_INTRODUCE_PUBLISH_OPEN);
    }

    @Override
    public void noticePublishEnter(int aid) {
        sendMessage(MessageIDDefine.ASSN_NOTICE_PUBLISH_OPEN, aid);
    }

    @Override
    public void picBrowserEnter(PicBrowserBean picBrowserBean) {
        sendMessage(MessageIDDefine.PIC_BROWSER_OPEN, picBrowserBean);
    }

    @Override
    public void myAssnDetailEnter(int aid, int level) {
        Message message = Message.obtain();
        message.what = MessageIDDefine.MYASSN_DETAIL_OPEN;
        message.arg1 = aid;
        message.arg2 = level;
        sendMessage(message);
    }

    @Override
    public void assnJoinEnter(int aid) {
        sendMessage(MessageIDDefine.ASSN_JOIN_OPEN, aid);
    }

    @Override
    public void myAssnActivityEnter(int aid, int level) {
        Message message = Message.obtain();
        message.what = MessageIDDefine.MYASSN_ACTIVITY_OPEN;
        message.arg1 = aid;
        message.arg2 = level;
        sendMessage(message);
    }

    @Override
    public void assnExamineEnter(int aid) {
        sendMessage(MessageIDDefine.ASSN_EXAMINE_OPEN, aid);
    }

    @Override
    public void assnMemberEnter(Bundle bundle) {
        Message msg = Message.obtain();
        msg.what = MessageIDDefine.ASSN_MEMBER_OPEN;
        msg.setData(bundle);
        sendMessage(msg);
    }

    @Override
    public void assnExamineDetailEnter(AssnExamineListResponse.AssnExamineListResponseData data) {
        sendMessage(MessageIDDefine.ASSN_EXAMINE_DETAIL_OPEN, data);
    }

    @Override
    public void activityPublishEnter(int aid) {
        sendMessage(MessageIDDefine.ASSN_ACTIVITY_PUBLISH_OPEN, aid);
    }

    @Override
    public void assnDetailEnter(int aid) {
        sendMessage(MessageIDDefine.ASSN_DETAIL_OPEN, aid);
    }

    @Override
    public void submitNotice(AssnNoticePublishRequest assnNoticePublishRequest) {
        if (assnNoticePublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.releaseNotice(GsonUtil.toEncodeJson(assnNoticePublishRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("发布成功");
                            assnNoticePublishWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnNoticePublishWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                assnNoticePublishWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                assnNoticePublishWindow.response();
            }
        });
    }

    @Override
    public void submitActivity(AssnActivityPublishRequest assnActivityPublishRequest) {
        if (assnActivityPublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createActivity(assnActivityPublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse =
                            GsonUtil.fromEncodeJson(response.body()
                                    .getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("发布成功");
                            assnActivityPublishWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnActivityPublishWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                assnActivityPublishWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                assnActivityPublishWindow.response();
            }
        });
    }

    @Override
    public void submitProfile(AssnProfilePublishRequest assnProfilePublishRequest) {
        if (assnProfilePublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.setProfile(assnProfilePublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("发布成功");
                            assnIntroducePublishWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnIntroducePublishWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                assnIntroducePublishWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                assnIntroducePublishWindow.response();
            }
        });
    }

    @Override
    public void getAssnIntroduce(AssnProfileRequest assnProfileRequest, final int which) {
        if (assnProfileRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(assnProfileRequest);
        Call<EncodeData> respond = service.getProfile(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnProfileResponse assnProfileResponse = GsonUtil
                            .fromEncodeJson(response.body().getData(), AssnProfileResponse.class);
                    //which 1代表编辑简介的返回码
                    int code = assnProfileResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            if (which == 0) {
                                assnIntroduceWindow.profileResponse(assnProfileResponse.getData());
                            } else if (which == 1) {
                                assnIntroducePublishWindow.profileResponse(assnProfileResponse.getData());
                                assnIntroducePublishWindow.response();
                            } else if (which == 2) {
                                assnManageWindow.profileResponse(assnProfileResponse.getData());
                            }
                            break;
                        case ResponseCode.RESPONSE_2001:
                            if (which == 0) {
                                againLogin(assnIntroduceWindow);
                            } else if (which == 1) {
                                againLogin(assnIntroducePublishWindow);
                                assnIntroducePublishWindow.response();
                            } else if (which == 2) {
                                againLogin(assnManageWindow);
                            }
                            break;
                        case ResponseCode.RESPONSE_110:
                            if (which == 0) {
                                WindowHelper.showToast(assnProfileResponse.getDetail());
                            } else if (which == 1) {
                                WindowHelper.showToast(assnProfileResponse.getDetail());
                                assnIntroducePublishWindow.response();
                            } else if (which == 2) {
                                WindowHelper.showToast(assnProfileResponse.getDetail());
                            }
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
    public void uploadImageActi(String token, List<File> files) {
        MultipartBody body = UploadFileHelper.uploadMultipartFile(files);
        Call<EncodeData> call = service.uploadImageActi(body, GsonUtil.toEncodeJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UploadImageResponse uploadImageResponse = GsonUtil
                            .fromEncodeJson(response.body()
                                    .getData(), UploadImageResponse.class);
                    int code = uploadImageResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            assnActivityPublishWindow.uploadImageResponse(uploadImageResponse.getUrl());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnActivityPublishWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(uploadImageResponse.getDetail());
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
    public void getMyAssnList(String token) {
        Call<EncodeData> call = service.getMyAssociations(GsonUtil.toEncodeJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyAssnResponse myAssnResponse = GsonUtil.fromEncodeJson(response.body().getData(), MyAssnResponse
                            .class);
                    int code = myAssnResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myAssnWindow.reponseData(myAssnResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(myAssnResponse.getDetail());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void getMyAssnDetails(AssnDetailRequest myAssnDetailRequest) {
        Call<EncodeData> call = service.getAssociationsDetails(GsonUtil.toEncodeJson(myAssnDetailRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnDetailResponse assnDetailResponse = GsonUtil.fromEncodeJson(response.body().getData(), AssnDetailResponse.class);
                    int code = assnDetailResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myAssnDetailWindow.myAssnDetailResponse(assnDetailResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnDetailResponse.getDetail());
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
    public void getMemberListByMyAssn(AssnMemberRequest assnMemberRequest) {
        Call<EncodeData> call = service.getAssociationMember(GsonUtil.toEncodeJson(assnMemberRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnMemberResponse assnMemberResponse = GsonUtil.fromEncodeJson(response.body().getData(), AssnMemberResponse
                            .class);
                    int code = assnMemberResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myAssnDetailWindow.memberListResponse(assnMemberResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnMemberResponse.getDetail());
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
    public void setMemberLevel(SetMemberLevelRequest setMemberLevelRequest) {
        Call<EncodeData> call = service.updateMemberLevel(GsonUtil.toEncodeJson(setMemberLevelRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("设置成功");
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnDetailWindow);
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
    public void deleteMember(DeleteMemberRequest deleteMemberRequest) {
        Call<EncodeData> call = service.deleteMember(GsonUtil.toEncodeJson(deleteMemberRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("已踢出社团");
                            myAssnDetailWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnDetailWindow);
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
    public void assnExamine(AssnExamineRequest assnExamineRequest) {
        Call<EncodeData> call = service.checkMember(GsonUtil.toEncodeJson(assnExamineRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("操作成功");
                            assnExamineWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnExamineWindow);
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
    public void assnEnter() {
        sendMessage(MessageIDDefine.ASSN_OPEN);
    }

    @Override
    public void getMemberList(AssnMemberRequest assnMemberRequest) {
        Call<EncodeData> call = service.getAssociationMember(GsonUtil.toEncodeJson(assnMemberRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnMemberResponse assnMemberResponse = GsonUtil.fromEncodeJson(response.body().getData(), AssnMemberResponse
                            .class);
                    int code = assnMemberResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            assnMemberWindow.memberResponse(assnMemberResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnMemberWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnMemberResponse.getDetail());
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
    public void getMyAssnActivity(MyAssnActivityRequest myAssnActivityRequest) {
        Call<EncodeData> call = service.getAssociationActivity(GsonUtil.toEncodeJson(myAssnActivityRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnActivityResponse myAssnActivityResponse = GsonUtil.fromEncodeJson(response.body().getData(), AssnActivityResponse
                            .class);
                    int code = myAssnActivityResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myAssnDetailWindow.myAssnActiResponse(myAssnActivityResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnActivityWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(myAssnActivityResponse.getDetail());
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
    public void assnJoin(AssnJoinRequest assnJoinRequest) {
        Call<EncodeData> call = service.joinAssociationMember(assnJoinRequest);
        call.enqueue(new Callback<EncodeData>() {

            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("申请成功");
                            assnJoinWindow.hideWindow(true);
                            setUIData(AbstractWindow.WindowCallBack.UIDataKeysDef.JOINASSN_CAUSE, "");
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnJoinWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {

            }
        });
    }

    @Override
    public void getAssociations(AssnRequest request) {
        Call<EncodeData> call = service.getAssociations(GsonUtil.toEncodeJson(request));
        call.enqueue(new Callback<EncodeData>() {

            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnResponse assnResponse = GsonUtil.fromEncodeJson(response.body().getData(), AssnResponse.class);
                    int code = assnResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            assnWindow.assnDataResponse(assnResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnActivityWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnResponse.getDetail());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {

            }
        });
    }

    @Override
    public void getAssnDetails(AssnDetailRequest assnDetailRequest) {
        Call<EncodeData> call = service.getAssociationsDetails(GsonUtil.toEncodeJson(assnDetailRequest));
        call.enqueue(new Callback<EncodeData>() {

            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnDetailResponse assnDetailResponse = GsonUtil.fromEncodeJson(response.body().getData(), AssnDetailResponse.class);
                    int code = assnDetailResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            assnDetailWindow.responseData(assnDetailResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnDetailResponse.getDetail());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });

    }

    @Override
    public void examineMembarList(AssnExamineListRequest assnExamineRequest) {
        Call<EncodeData> call = service.checkMemberList(GsonUtil.toEncodeJson(assnExamineRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnExamineListResponse assnExamineListResponse =
                            GsonUtil.fromEncodeJson(response.body().getData(), AssnExamineListResponse
                                    .class);
                    int code = assnExamineListResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            assnExamineWindow.examineListResponse(assnExamineListResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnExamineWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnExamineListResponse.getDetail());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void examineMembarListByMyAssn(AssnExamineListRequest assnExamineListRequest) {
        Call<EncodeData> call = service.checkMemberList(GsonUtil.toEncodeJson(assnExamineListRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnExamineListResponse assnExamineListResponse =
                            GsonUtil.fromEncodeJson(response.body().getData(), AssnExamineListResponse
                                    .class);
                    int code = assnExamineListResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myAssnDetailWindow.examineListResponse(assnExamineListResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnExamineListResponse.getDetail());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void assnQuit(AssnQuitRequest assnQuitRequest) {
        Call<EncodeData> call = service.quitAssociation(GsonUtil.toEncodeJson(assnQuitRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse =
                            GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("已退出社团");
                            myAssnDetailWindow.hideWindow(true);
                            myAssnWindow.readMyAssnList();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myAssnDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                    assnWindow.activityLoadingFailure();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                assnWindow.activityLoadingFailure();
            }
        });
    }

    @Override
    public void getAssnActivity(AssnActivityRequest assnActivityRequest) {
        String data = GsonUtil.toEncodeJson(assnActivityRequest);
        Call<EncodeData> call = service.getActivityList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnActivityResponse assnActivityResponse =
                            GsonUtil.fromEncodeJson(response.body().getData(), AssnActivityResponse
                                    .class);
                    int code = assnActivityResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            assnWindow.assnActivityResponse(assnActivityResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(assnWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnActivityResponse.getDetail());
                            break;
                    }
                } else {
                    assnWindow.activityLoadingFailure();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                assnWindow.activityLoadingFailure();
            }
        });
    }


}
