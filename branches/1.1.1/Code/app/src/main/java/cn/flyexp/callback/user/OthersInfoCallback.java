package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.OthersInfoRequest;
import cn.flyexp.entity.OthersInfoResponse;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface OthersInfoCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestOthersInfo(OthersInfoRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseOthersInfo(OthersInfoResponse response);
    }

}
