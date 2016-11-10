package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.entity.BaseResponse;

/**
 * Created by tanxinye on 2016/11/2.
 */
public interface AssnJoinCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAssnJoin(AssnJoinRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAssnJoin(BaseResponse response);
    }

}
