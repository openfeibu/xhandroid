package cn.flyexp.mvc.topic;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.MyCommentRequest;
import cn.flyexp.entity.MyTopicRequest;
import cn.flyexp.entity.MyTopicResponseNew;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.ReplyListResponse;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicCommentRequest;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.entity.TopicSearchRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/12 0012.
 */
public interface TopicViewCallBack extends AbstractWindow.WindowCallBack {

    void publishEnter();

    void picBrowserEnter(PicBrowserBean picBrowserBean);

    void replyEnter(ReplyListResponse.ReplyListResponseData replyListResponseData);

    void replyListEnter();

    void uploadImageTopic(String token, List<File> files);

    /**
     * 请求数据
     */

    void getTopicList(TopicListRequest topicListRequest);

    void getTopic(TopicRequest topicRequest);

    void submitTopic(TopicPublishRequest topicPublishRequest);

    void commentTopic(CommentRequest commentRequest);

    void thumbUp(ThumbUpRequest thumbUpRequest);

    void getMyTopic(MyTopicRequest myTopicRequest);

    void deleteComment(DeleteCommentRequest deleteCommentRequest);

    void deleteTopic(DeleteTopicRequest deleteTopicRequest);

    void myCommentTopic(CommentRequest commentRequest);

    void mydeleteComment(DeleteCommentRequest deleteCommentRequest);

    void replyListRequest(String token);

    void replyRequest(CommentRequest commentRequest);
}
