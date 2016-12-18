package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenResponse;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/12/2.
 */
public interface RegisterVercodeCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestRegister(RegisterRequest request);

        void requestUploadAvatar(MultipartBody multipartBody);

        void requestSmscode(SmsCodeRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseRegister(TokenResponse response);

        void responseUploadAvatar(ImgUrlResponse response);
    }
}
