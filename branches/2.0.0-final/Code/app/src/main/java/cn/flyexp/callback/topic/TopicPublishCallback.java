package cn.flyexp.callback.topic;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.entity.TopicPublishRequest;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/11/18.
 */
public interface TopicPublishCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestUploadImageTopic(MultipartBody multipartBody, TokenRequest request);

        void requestTopicPublish(TopicPublishRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseUploadImageTopic(ImgUrlResponse response);

        void responseTopicPublish(BaseResponse response);
    }
}
