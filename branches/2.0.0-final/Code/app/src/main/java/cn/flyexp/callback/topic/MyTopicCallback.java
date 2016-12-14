package cn.flyexp.callback.topic;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommentResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;

/**
 * Created by tanxinye on 2016/11/21.
 */
public interface MyTopicCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestMyTopicList(TopicListRequest request);

        void requestComment(CommentRequest request);

        void requestThumbUp(ThumbUpRequest request);

        void requestDeleteComment(DeleteCommentRequest request);

        void requestDeleteTopic(DeleteTopicRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseMyTopicList(TopicListResponse response);

        void responseComment(CommentResponse response);

        void responseThumbUp(BaseResponse response);

        void responseDeleteComment(BaseResponse response);

        void responseDeleteTopic(BaseResponse response);
    }

}
