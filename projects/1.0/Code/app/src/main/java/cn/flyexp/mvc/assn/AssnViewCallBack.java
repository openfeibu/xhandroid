package cn.flyexp.mvc.assn;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.AssnActivityPublishRequest;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoPublishRequest;
import cn.flyexp.entity.AssnInfoRequest;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.AssnProfilePublishRequest;
import cn.flyexp.entity.AssnProfileRequest;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/21 0021.
 */
public interface AssnViewCallBack extends AbstractWindow.WindowCallBack {

    public void picBrowserEnter(PicBrowserBean picBrowserBean);

    public void infoDetailEnter(AssnInfoResponse.AssnInfoResponseData assnInfoResponseData);

    public void activityDetailEnter(AssnActivityResponse.AssnActivityResponseData assnActivityResponseData);

    public void introduceEnter(int aid);

    public void introducePublishEnter();

    public void noticePublishEnter();

    public void infoPublishEnter();

    public void activityPublishEnter();

    public void submitNotice(AssnNoticePublishRequest assnNoticePublishRequest);

    public void submitInfo(AssnInfoPublishRequest assnInfoPublishRequest);

    public void submitActivity(AssnActivityPublishRequest assnActivityPublishRequest);

    public void submitProfile(AssnProfilePublishRequest assnProfilePublishRequest);

    public void getAssnActivity(AssnActivityRequest assnActivityRequest);

    public void getAssnInfo(AssnInfoRequest assnInfoRequest);

    public void getAssnIntroduce(AssnProfileRequest assnProfileRequest,int which);

    public void uploadImageInfo(String token, List<File> files);

    public void uploadImageActi(String token, List<File> files);

}
