package cn.flyexp.callback.other;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ClienVerifyResponse;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.CrashRequest;

/**
 * Created by tanxinye on 2016/10/23.
 */
public interface SplashCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestClientVerify(ClientVerifyRequest request);

        void reqeustCrash(CrashRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseClientVerify(ClienVerifyResponse response);

        void responseCrash();
    }

}
