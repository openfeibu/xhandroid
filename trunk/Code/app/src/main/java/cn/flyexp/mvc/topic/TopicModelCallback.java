package cn.flyexp.mvc.topic;

import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.MyCommentResponse;
import cn.flyexp.entity.MyTopicResponse;
import cn.flyexp.entity.TopicCommentResponse;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TopicResponse;
import cn.flyexp.entity.UploadImageResponse;

/**
 * Created by guo on 2016/7/24.
 */
public interface TopicModelCallback {

    public void topicResponse(TopicResponse topicResponse);

    public void getTopicList(TopicListResponse topicResponse);

    public void uploadImageResponse(UploadImageResponse uploadImageResponse);

    public void topicPublishResponse(CommonResponse commonResponse);

    public void commentListResponse(TopicCommentResponse topicCommentResponse);

    public void searchTopicResponse(TopicListResponse topicResponse);

    public void commentResponse(CommonResponse commonResponse);

    public void thumbUpResponse(CommonResponse commonResponse);

    public void myCommentResponse(MyCommentResponse myCommentResponse);

    public void myTopicResponse(MyTopicResponse myTopicResponse);

    public void deleteCommentResponse(CommonResponse commonResponse);

    public void deleteTopicResponse(CommonResponse commonResponse);
}
