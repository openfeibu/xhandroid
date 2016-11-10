package cn.flyexp.callback.topic;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.PageRequest;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;

/**
 * Created by tanxinye on 2016/10/27.
 */
public interface TopicCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestTopicList(TopicListRequest request);

        void requestComment(CommentRequest request);

        void requestThumbUp(ThumbUpRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseTopicList(TopicListResponse response);

        void responseComment(BaseResponse response);

        void responseThumbUp(BaseResponse response);
    }

}
