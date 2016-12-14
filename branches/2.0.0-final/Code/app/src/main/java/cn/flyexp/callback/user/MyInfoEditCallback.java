package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface MyInfoEditCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestChangeMyInfo(ChangeMyInfoRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseChangeMyInfo(BaseResponse response);
    }

}
