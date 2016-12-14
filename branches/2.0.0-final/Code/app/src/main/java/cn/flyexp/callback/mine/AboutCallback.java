package cn.flyexp.callback.mine;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.ClienVerifyResponse;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;

/**
 * Created by tanxinye on 2016/10/23.
 */
public interface AboutCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestUpdate(UpdateRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseUpdate(UpdateResponse response);
    }

}
