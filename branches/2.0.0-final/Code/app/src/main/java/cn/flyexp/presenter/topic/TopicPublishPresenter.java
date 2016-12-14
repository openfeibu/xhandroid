package cn.flyexp.presenter.topic;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.topic.TopicPublishCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/11/18.
 */
public class TopicPublishPresenter extends BasePresenter implements TopicPublishCallback.RequestCallback {

    private TopicPublishCallback.ResponseCallback callback;

    public TopicPublishPresenter(TopicPublishCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestUploadImageTopic(MultipartBody multipartBody, TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTopicService().uploadImageTopicRequest(multipartBody, data), ImgUrlResponse.class, new ObservableCallback<ImgUrlResponse>() {
            @Override
            public void onSuccess(ImgUrlResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseUploadImageTopic(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }

    @Override
    public void requestTopicPublish(TopicPublishRequest request) {
        execute(ApiManager.getTopicService().topicPublishRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTopicPublish(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }
}
