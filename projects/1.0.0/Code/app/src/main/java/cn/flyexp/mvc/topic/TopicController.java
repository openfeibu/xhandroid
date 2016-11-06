package cn.flyexp.mvc.topic;

import android.os.Message;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.MyCommentRequest;
import cn.flyexp.entity.MyCommentResponse;
import cn.flyexp.entity.MyTopicRequest;
import cn.flyexp.entity.MyTopicResponse;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicCommentRequest;
import cn.flyexp.entity.TopicCommentResponse;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.entity.TopicResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.entity.TopicSearchRequest;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;


/**
 * Created by txy on 2016/6/7.
 * Modify by txy on 2016/7/30.
 */
public class TopicController extends AbstractController implements TopicViewCallBack, TopicModelCallback {

    public TopicWindow topicWindow = null;
    private MyTopicWindow myTopicWindow = null;
    private TopicPublishWindow topicPublishWindow = null;
    private TopicDetailWindow topicDetailWindow = null;
    private TopicSearchWindow topicSearchWindow = null;
    private TopicModel topicModel = null;

    public TopicController() {
        super();
        topicModel = new TopicModel(this);
        topicWindow = new TopicWindow(this);
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.TOPIC_PUBLISH_OPEN) {
            topicPublishWindow = new TopicPublishWindow(this);
            topicPublishWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.TOPIC_DETAIL_OPEN) {
            topicDetailWindow = new TopicDetailWindow(this);
            if (mes.arg1 == -1) {
                topicDetailWindow.responseData((TopicResponseData) mes.obj);
            } else {
                topicDetailWindow.detailRequest(mes.arg1);
            }
            topicDetailWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.MYTOPIC_OPEN) {
            myTopicWindow = new MyTopicWindow(this);
            myTopicWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.TOPIC_SEARCH_OPEN) {
            topicSearchWindow = new TopicSearchWindow(this);
            topicSearchWindow.showWindow(true, true);
        }
    }

    protected void registerMessages() {
        registerMessage(MessageIDDefine.TOPIC_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.TOPIC_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.TOPIC_SEARCH_OPEN, this);
        registerMessage(MessageIDDefine.MYTOPIC_OPEN, this);
    }


    @Override
    public void publishEnter() {
        sendMessage(MessageIDDefine.TOPIC_PUBLISH_OPEN);
    }

    @Override
    public void picBrowserEnter(PicBrowserBean picBrowserBean) {
        sendMessage(MessageIDDefine.PIC_BROWSER_OPEN, picBrowserBean);
    }

    @Override
    public void detailEnter(TopicResponseData data) {
        Message message = Message.obtain();
        message.what = MessageIDDefine.TOPIC_DETAIL_OPEN;
        message.obj = data;
        message.arg1 = -1;
        sendMessage(message);
    }

    @Override
    public void searchEnter() {
        sendMessage(MessageIDDefine.TOPIC_SEARCH_OPEN);
    }

    @Override
    public void detailEnter(int topicId) {
        sendMessage(MessageIDDefine.TOPIC_DETAIL_OPEN, topicId);
    }

    @Override
    public void uploadImageTopic(String token, List<File> files) {
        topicModel.uploadImageTopic(token, files);
    }

    @Override
    public void getTopicList(TopicListRequest topicRequest) {
        topicModel.getTopicList(topicRequest);
    }

    @Override
    public void getTopic(TopicRequest topicRequest) {
        topicModel.getTopic(topicRequest);
    }


    @Override
    public void submitTopic(TopicPublishRequest complaintPublishRequest) {
        topicModel.createTopic(complaintPublishRequest);
    }

    @Override
    public void getComment(TopicCommentRequest complaintCommentRequest) {
        topicModel.getTopicCommentsList(complaintCommentRequest);
    }

    @Override
    public void commentTopic(CommentRequest commentRequest) {
        topicModel.commentTopic(commentRequest);
    }

    @Override
    public void searchTopic(TopicSearchRequest topicSearchRequest) {
        topicModel.searchTopic(topicSearchRequest);
    }

    @Override
    public void thumbUp(ThumbUpRequest thumbUpRequest) {
        String token = topicWindow.getStringByPreference("token");
        if (token.equals("")) {
            loginWindowEnter();
            return;
        }
        thumbUpRequest.setToken(token);
        topicModel.thumbUp(thumbUpRequest);
    }

    @Override
    public void getMyComment(MyCommentRequest myCommentRequest) {
        topicModel.myCommentList(myCommentRequest);
    }

    @Override
    public void getMyTopic(MyTopicRequest myTopicRequest) {
        topicModel.myTopicList(myTopicRequest);
    }

    @Override
    public void deleteComment(DeleteCommentRequest deleteCommentRequest) {
        String token = myTopicWindow.getStringByPreference("token");
        if (token.equals("")) {
            loginWindowEnter();
            return;
        }
        deleteCommentRequest.setToken(token);
        topicModel.deleteComment(deleteCommentRequest);
    }

    @Override
    public void deleteTopic(DeleteTopicRequest deleteTopicRequest) {
        String token = myTopicWindow.getStringByPreference("token");
        if (token.equals("")) {
            loginWindowEnter();
            return;
        }
        deleteTopicRequest.setToken(token);
        topicModel.deleteTopic(deleteTopicRequest);
    }

    @Override
    public void topicResponse(TopicResponse topicResponse) {
        if (topicResponse == null) {
            return;
        }
        int code = topicResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicDetailWindow.responseData(topicResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicWindow.showToast(topicResponse.getDetail());
                break;
        }
    }

    @Override
    public void getTopicList(TopicListResponse topicResponse) {
        if (topicResponse == null) {
            topicWindow.loadingFailure();
            return;
        }
        int code = topicResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicWindow.topicResponse(topicResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicWindow.showToast(topicResponse.getDetail());
                break;
        }
    }

    @Override
    public void uploadImageResponse(UploadImageResponse uploadImageResponse) {
        if (uploadImageResponse == null) {
            return;
        }
        int code = uploadImageResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicPublishWindow.uploadImageResponse(uploadImageResponse);
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicPublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicPublishWindow.showToast(uploadImageResponse.getDetail());
                break;
        }
    }

    @Override
    public void topicPublishResponse(CommonResponse commonResponse) {
        topicPublishWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicPublishWindow.showToast("发布成功");
                topicPublishWindow.hideWindow(true);
                topicWindow.refreshTopic();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicPublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicPublishWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void commentListResponse(TopicCommentResponse topicCommentResponse) {
        if (topicCommentResponse == null) {
            return;
        }
        int code = topicCommentResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicDetailWindow.commentListResponse(topicCommentResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicDetailWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicDetailWindow.showToast(topicCommentResponse.getDetail());
                break;
        }
    }

    @Override
    public void searchTopicResponse(TopicListResponse topicResponse) {
        if (topicResponse == null || topicResponse.getData() == null) {
            return;
        }
        int code = topicResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicSearchWindow.searchTopicResponse(topicResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicSearchWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicSearchWindow.showToast(topicResponse.getDetail());
                break;
        }
    }

    @Override
    public void commentResponse(CommonResponse commonResponse) {
        topicDetailWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicDetailWindow.commentRefresh();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicDetailWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicDetailWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void thumbUpResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                topicWindow.thumbUpResponse();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(topicWindow);
                break;
            case ResponseCode.RESPONSE_110:
                topicWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void myCommentResponse(MyCommentResponse myCommentResponse) {
        if (myCommentResponse == null) {
            return;
        }
        int code = myCommentResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myTopicWindow.responseCommentData(myCommentResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myTopicWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myTopicWindow.showToast(myCommentResponse.getDetail());
                break;
        }
    }

    @Override
    public void myTopicResponse(MyTopicResponse myTopicResponse) {
        if (myTopicResponse == null) {
            return;
        }
        int code = myTopicResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myTopicWindow.responseTopicData(myTopicResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myTopicWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myTopicWindow.showToast(myTopicResponse.getDetail());
                break;
        }
    }

    @Override
    public void deleteCommentResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myTopicWindow.deleteComment();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myTopicWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myTopicWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void deleteTopicResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myTopicWindow.deleteTopic();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myTopicWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myTopicWindow.showToast(commonResponse.getDetail());
                break;
        }
    }
}
