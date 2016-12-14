package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.ChangePwdRequest;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface ChangePwdCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestChangePwd(ChangePwdRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseChangePwd(BaseResponse response);
    }

}
