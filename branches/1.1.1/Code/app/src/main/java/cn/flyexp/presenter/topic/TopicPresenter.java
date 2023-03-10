package cn.flyexp.presenter.topic;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.topic.TopicCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class TopicPresenter extends BasePresenter implements TopicCallback.RequestCallback {

    private TopicCallback.ResponseCallback callback;

    public TopicPresenter(TopicCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestTopicList(TopicListRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTopicService().topicListRequest(data), TopicListResponse.class, new ObservableCallback<TopicListResponse>() {
            @Override
            public void onSuccess(TopicListResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTopicList(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }

    @Override
    public void requestComment(CommentRequest request) {
        execute(ApiManager.getTopicService().commentRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseComment(response);
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
    public void requestThumbUp(ThumbUpRequest request) {
        execute(ApiManager.getTopicService().thumbUpRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseThumbUp(response);
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
