package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.TokenRequest;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface MyInfoCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestUploadAvatar(MultipartBody multipartBody, TokenRequest request);

        void requestChangeMyInfo(ChangeMyInfoRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseUploadAvatar();

        void responseChangeMyInfo(BaseResponse response);
    }

}
