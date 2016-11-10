package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CertificationRequest;
import cn.flyexp.entity.TokenRequest;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface AvatarCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestUploadAvatar(MultipartBody multipartBody, TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseUploadAvatar(BaseResponse response);
    }

}
