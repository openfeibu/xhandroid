package cn.flyexp.presenter.topic;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.topic.MyTopicCallback;
import cn.flyexp.callback.topic.TopicCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommentResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by tanxinye on 2016/11/21.
 */
public class MyTopicPresenter extends BasePresenter implements MyTopicCallback.RequestCallback {

    private MyTopicCallback.ResponseCallback callback;

    public MyTopicPresenter(MyTopicCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMyTopicList(TopicListRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTopicService().myTopicListRequest(data), TopicListResponse.class, new ObservableCallback<TopicListResponse>() {
            @Override
            public void onSuccess(TopicListResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyTopicList(response);
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
        execute(ApiManager.getTopicService().commentRequest(request), CommentResponse.class, new ObservableCallback<CommentResponse>() {
            @Override
            public void onSuccess(CommentResponse response) {
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

    @Override
    public void requestDeleteComment(DeleteCommentRequest request) {
        execute(ApiManager.getTopicService().deleteCommentRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseDeleteComment(response);
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
    public void requestDeleteTopic(DeleteTopicRequest request) {
        execute(ApiManager.getTopicService().deleteTopicRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseDeleteTopic(response);
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
