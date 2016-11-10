package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CertificationRequest;
import cn.flyexp.entity.OthersInfoRequest;
import cn.flyexp.entity.OthersInfoResponse;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface CertificationCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestCertification(MultipartBody multipartBody,CertificationRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseCertification(BaseResponse response);
    }

}
