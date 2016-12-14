package cn.flyexp.callback.mine;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/11/4.
 */
public interface ShareCallback  {

    interface RequestCallback extends BaseRequestCallback {
        void requestShare(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseShare(BaseResponse response);
    }
}
