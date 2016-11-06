package cn.flyexp.mvc.topic;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.MyCommentRequest;
import cn.flyexp.entity.MyTopicRequest;
import cn.flyexp.entity.PicBrowserBean;
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

    public void publishEnter();

    public void picBrowserEnter(PicBrowserBean picBrowserBean);

    public void detailEnter(TopicResponseData responseData);

    public void searchEnter();

    public void detailEnter(int topId);

    public void uploadImageTopic(String token, List<File> files);

    /**
     * 请求数据
     */

    public void getTopicList(TopicListRequest topicListRequest);

    public void getTopic(TopicRequest topicRequest);

    public void submitTopic(TopicPublishRequest topicPublishRequest);

    public void getComment(TopicCommentRequest topicCommentRequest);

    public void commentTopic(CommentRequest commentRequest);

    public void searchTopic(TopicSearchRequest topicSearchRequest);

    public void thumbUp(ThumbUpRequest thumbUpRequest);

    public void getMyComment(MyCommentRequest myCommentRequest);

    public void getMyTopic(MyTopicRequest myTopicRequest);

    public void deleteComment(DeleteCommentRequest deleteCommentRequest);

    public void deleteTopic(DeleteTopicRequest deleteTopicRequest);
}
