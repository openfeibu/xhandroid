package cn.flyexp.mvc.assn;

import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.entity.AssnProfileResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.UploadImageResponse;

/**
 * Created by txy on 2016/7/25 0025.
 */
public interface AssnModelCallBack {

    public void assnActivityListResponse(AssnActivityResponse socialActivityResponse);

    public void assnInfoListResponse(AssnInfoResponse socialInfoResponse);

    public void assnInfoPublishResponse(CommonResponse commonResponse);

    public void assnNoticePublishResponse(CommonResponse commonResponse);

    public void assnActivityPublishResponse(CommonResponse commonResponse);

    public void assnProfilePublishResponse(CommonResponse commonResponse);

    public void assnProfileResponse(AssnProfileResponse socialProfileResponse, int which);

    public void uploadInfoImageResponse(UploadImageResponse uploadImageResponse);

    public void uploadActiImageResponse(UploadImageResponse uploadImageResponse);

}
