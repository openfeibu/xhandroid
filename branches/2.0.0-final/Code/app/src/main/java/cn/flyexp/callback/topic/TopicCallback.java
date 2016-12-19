package cn.flyexp.callback.topic;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;

/**
 * Created by huangju on 2016/12/17.
 */

public interface TopicCallback {

    interface RequestCallback extends BaseRequestCallback{
        void requestTopicList(TopicListRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseTopicList(TopicListResponse response);
    }
}
