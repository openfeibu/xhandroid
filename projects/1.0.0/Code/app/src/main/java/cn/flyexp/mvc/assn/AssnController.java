package cn.flyexp.mvc.assn;

import android.os.Message;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.AssnActivityPublishRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoPublishRequest;
import cn.flyexp.entity.AssnInfoRequest;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.AssnProfilePublishRequest;
import cn.flyexp.entity.AssnProfileRequest;
import cn.flyexp.entity.AssnProfileResponse;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class AssnController extends AbstractController implements AssnViewCallBack,
        AssnModelCallBack {

    private AssnModel assnModel;
    private AssnWindow assnWindow;
    private AssnActivityDetailWindow assnActivityDetailWindow;
    private AssnIntroduceWindow assnIntroduceWindow;
    private AssnManageWindow assnManageWindow;
    private AssnIntroducePublishWindow assnIntroducePublishWindow;
    private AssnNoticePublishWindow assnNoticePublishWindow;
    private AssnInfoPublishWindow assnInfoPublishWindow;
    private AssnInfoDetailWindow assnInfoDetailWindow;
    private AssnActivityPublishWindow assnActivityPublishWindow;

    public AssnController() {
        super();
        assnModel = new AssnModel(this);
    }

    @Override
    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.ASSN_OPEN) {
            assnWindow = new AssnWindow(this);
            if (mes.obj == "intoActivity") {
                assnWindow.setActivity();
            }
            assnWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_ACTIVITY_DETAIL_OPEN) {
            assnActivityDetailWindow = new AssnActivityDetailWindow(this);
            assnActivityDetailWindow.initData((AssnActivityResponse.AssnActivityResponseData) mes.obj);
            assnActivityDetailWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_INTRODUCE_OPEN) {
            assnIntroduceWindow = new AssnIntroduceWindow(this);
            assnIntroduceWindow.introduceRequest(mes.arg1);
            assnIntroduceWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_MANAGE_OPEN) {
            assnManageWindow = new AssnManageWindow(this);
            assnManageWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_INTRODUCE_PUBLISH_OPEN) {
            assnIntroducePublishWindow = new AssnIntroducePublishWindow(this);
            assnIntroducePublishWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_NOTICE_PUBLISH_OPEN) {
            assnNoticePublishWindow = new AssnNoticePublishWindow(this);
            assnNoticePublishWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_INFO_PUBLISH_OPEN) {
            assnInfoPublishWindow = new AssnInfoPublishWindow(this);
            assnInfoPublishWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_ACTIVITY_PUBLISH_OPEN) {
            assnActivityPublishWindow = new AssnActivityPublishWindow(this);
            assnActivityPublishWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.ASSN_INFO_DETAIL_OPEN) {
            assnInfoDetailWindow = new AssnInfoDetailWindow(this);
            assnInfoDetailWindow.initDetail((AssnInfoResponse.AssnInfoResponseData) mes.obj);
            assnInfoDetailWindow.showWindow(true, true);
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
        registerMessage(MessageIDDefine.ASSN_INFO_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_ACTIVITY_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.ASSN_INFO_DETAIL_OPEN, this);
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
    public void noticePublishEnter() {
        sendMessage(MessageIDDefine.ASSN_NOTICE_PUBLISH_OPEN);
    }

    @Override
    public void infoPublishEnter() {
        sendMessage(MessageIDDefine.ASSN_INFO_PUBLISH_OPEN);
    }

    @Override
    public void picBrowserEnter(PicBrowserBean picBrowserBean) {
        sendMessage(MessageIDDefine.PIC_BROWSER_OPEN, picBrowserBean);
    }

    @Override
    public void infoDetailEnter(AssnInfoResponse.AssnInfoResponseData assnInfoResponseData) {
        sendMessage(MessageIDDefine.ASSN_INFO_DETAIL_OPEN, assnInfoResponseData);
    }

    @Override
    public void activityPublishEnter() {
        sendMessage(MessageIDDefine.ASSN_ACTIVITY_PUBLISH_OPEN);
    }

    @Override
    public void submitNotice(AssnNoticePublishRequest assnNoticePublishRequest) {
        assnModel.createMessage(assnNoticePublishRequest);
    }

    @Override
    public void submitInfo(AssnInfoPublishRequest assnInfoPublishRequest) {
        assnModel.createInformation(assnInfoPublishRequest);
    }

    @Override
    public void submitActivity(AssnActivityPublishRequest assnActivityPublishRequest) {
        assnModel.createActivity(assnActivityPublishRequest);
    }

    @Override
    public void submitProfile(AssnProfilePublishRequest assnProfilePublishRequest) {
        assnModel.setProfile(assnProfilePublishRequest);
    }

    @Override
    public void getAssnInfo(AssnInfoRequest assnInfoRequest) {
        assnModel.getInformationList(assnInfoRequest);
    }

    @Override
    public void getAssnIntroduce(AssnProfileRequest assnProfileRequest, int which) {
        assnModel.getProfile(assnProfileRequest, which);
    }

    @Override
    public void uploadImageInfo(String token, List<File> files) {
        assnModel.uploadImageInfo(token, files);
    }

    @Override
    public void uploadImageActi(String token, List<File> files) {
        assnModel.uploadImageActi(token, files);
    }


    @Override
    public void getAssnActivity(AssnActivityRequest assnActivityRequest) {
        assnModel.getActivityList(assnActivityRequest);
    }

    @Override
    public void assnInfoListResponse(AssnInfoResponse assnInfoResponse) {
        if (assnInfoResponse == null) {
            assnWindow.infoLoadingFailure();
            return;
        }
        int code = assnInfoResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnWindow.assnInfoResponse(assnInfoResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnWindow.showToast(assnInfoResponse.getDetail());
                break;
        }
    }

    @Override
    public void assnInfoPublishResponse(CommonResponse commonResponse) {
        assnInfoPublishWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnInfoPublishWindow.showToast("发布成功");
                assnInfoPublishWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnInfoPublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnInfoPublishWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void assnNoticePublishResponse(CommonResponse commonResponse) {
        assnNoticePublishWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnNoticePublishWindow.showToast("发布成功");
                assnNoticePublishWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnNoticePublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnNoticePublishWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void assnActivityPublishResponse(CommonResponse commonResponse) {
        assnActivityPublishWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnActivityPublishWindow.showToast("发布成功");
                assnActivityPublishWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnActivityPublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnActivityPublishWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void assnProfilePublishResponse(CommonResponse commonResponse) {
        assnIntroducePublishWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnIntroducePublishWindow.showToast("发布成功");
                assnIntroducePublishWindow.hideWindow(true);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnIntroducePublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnIntroducePublishWindow.showToast(commonResponse.getDetail());
                break;
        }

    }

    @Override
    public void assnProfileResponse(AssnProfileResponse assnProfileResponse, int which) {
        if (assnProfileResponse == null) {
            return;
        }
        //which 1代表编辑简介的返回码
        int code = assnProfileResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                if (which == 0) {
                    assnIntroduceWindow.profileResponse(assnProfileResponse.getData());
                } else if (which == 1) {
                    assnIntroducePublishWindow.profileResponse(assnProfileResponse.getData());
                    assnIntroducePublishWindow.response();
                } else if (which ==2) {
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
                    assnIntroduceWindow.showToast(assnProfileResponse.getDetail());
                } else if (which == 1) {
                    assnIntroducePublishWindow.showToast(assnProfileResponse.getDetail());
                    assnIntroducePublishWindow.response();
                } else if (which == 2) {
                    assnManageWindow.showToast(assnProfileResponse.getDetail());
                }
                break;
        }
    }

    @Override
    public void uploadInfoImageResponse(UploadImageResponse uploadImageResponse) {
        if (uploadImageResponse == null) {
            return;
        }
        int code = uploadImageResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnInfoPublishWindow.uploadImageResponse(uploadImageResponse.getUrl());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnInfoPublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnInfoPublishWindow.showToast(uploadImageResponse.getDetail());
                break;
        }
    }

    @Override
    public void uploadActiImageResponse(UploadImageResponse uploadImageResponse) {
        if (uploadImageResponse == null) {
            return;
        }
        int code = uploadImageResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnActivityPublishWindow.uploadImageResponse(uploadImageResponse.getUrl());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnActivityPublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnActivityPublishWindow.showToast(uploadImageResponse.getDetail());
                break;
        }
    }

    @Override
    public void assnActivityListResponse(AssnActivityResponse assnActivityResponse) {
        if (assnActivityResponse == null) {
            assnWindow.activityLoadingFailure();
            return;
        }
        int code = assnActivityResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                assnWindow.assnActivityResponse(assnActivityResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(assnWindow);
                break;
            case ResponseCode.RESPONSE_110:
                assnWindow.showToast(assnActivityResponse.getDetail());
                break;
        }
    }


}
