package cn.flyexp.mvc.assn;

import android.os.Bundle;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.AssnActivityPublishRequest;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnExamineRequest;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.entity.AssnMemberRequest;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.AssnProfilePublishRequest;
import cn.flyexp.entity.AssnProfileRequest;
import cn.flyexp.entity.AssnRequest;
import cn.flyexp.entity.DeleteMemberRequest;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.SetMemberLevelRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/21 0021.
 */
public interface AssnViewCallBack extends AbstractWindow.WindowCallBack {

    void picBrowserEnter(PicBrowserBean picBrowserBean);

    void activityDetailEnter(AssnActivityResponse.AssnActivityResponseData assnActivityResponseData);

    void introduceEnter(int aid);

    void introducePublishEnter();

    void noticePublishEnter(int aid);

    void activityPublishEnter(int aid);

    void myAssnDetailEnter(int aid, int level);

    void assnJoinEnter(int aid);

    void myAssnActivityEnter(int aid,int level);

    void assnExamineEnter(int aid);

    void assnMemberEnter(Bundle bundle);

    void assnExamineDetailEnter(AssnExamineListResponse.AssnExamineListResponseData data);

    void submitNotice(AssnNoticePublishRequest assnNoticePublishRequest);

    void submitActivity(AssnActivityPublishRequest assnActivityPublishRequest);

    void submitProfile(AssnProfilePublishRequest assnProfilePublishRequest);

    void getAssnActivity(AssnActivityRequest assnActivityRequest);

    void getAssnIntroduce(AssnProfileRequest assnProfileRequest, int which);

    void uploadImageActi(String token, List<File> files);

    void getMyAssnList(String token);

    void getMemberList(AssnMemberRequest assnMemberRequest);

    void getMyAssnActivity(MyAssnActivityRequest myAssnActivityRequest);

    void assnJoin(AssnJoinRequest requset);

    void getAssociations(AssnRequest request);

    void getAssnDetails(AssnDetailRequest request);

    void examineMembarList(AssnExamineListRequest assnExamineRequest);

    void examineMembarListByMyAssn(AssnExamineListRequest assnExamineListRequest);

    void assnQuit(AssnQuitRequest assnExitRequest);

    void assnDetailEnter(int aid);

    void getMyAssnDetails(AssnDetailRequest assnDetailRequest);

    void getMemberListByMyAssn(AssnMemberRequest assnMemberRequest);

    void setMemberLevel(SetMemberLevelRequest setMemberLevelRequest);

    void deleteMember(DeleteMemberRequest deleteMemberRequest);

    void assnExamine(AssnExamineRequest assnExamineRequest);

    void assnEnter();
}
