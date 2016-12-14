package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MessageResponse;

/**
 * Created by tanxinye on 2016/11/4.
 */
public interface MessageCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestMessage(MessageRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseMessage(MessageResponse response);
    }

}
