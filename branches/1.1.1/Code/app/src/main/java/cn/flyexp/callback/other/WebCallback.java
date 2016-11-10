package cn.flyexp.callback.other;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.ClienVerifyResponse;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface WebCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestWebUrl(WebUrlRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseWebUrl(WebUrlResponse response);
    }

}
