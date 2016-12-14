package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnActiPublishRequest;
import cn.flyexp.entity.BaseRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.TokenRequest;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/11/5.
 */
public interface AssnActiPublishCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestUploadImageActi(MultipartBody multipartBody, TokenRequest request);

        void requestAssnActiPublish(AssnActiPublishRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseUploadImageActi(ImgUrlResponse response);

        void responseAssnActiPublish(BaseResponse response);
    }
}
