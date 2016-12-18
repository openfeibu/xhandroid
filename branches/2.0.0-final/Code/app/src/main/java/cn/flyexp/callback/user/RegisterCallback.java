package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseRequest;
import cn.flyexp.entity.SmsCodeRequest;

/**
 * Created by tanxinye on 2016/12/18.
 */
public interface RegisterCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestSmscode(SmsCodeRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseSmscode(BaseRequest request);
    }
}
