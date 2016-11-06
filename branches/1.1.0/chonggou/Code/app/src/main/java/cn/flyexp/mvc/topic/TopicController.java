package cn.flyexp.mvc.topic;

import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import cn.flyexp.constants.Config;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.MyTopicRequest;
import cn.flyexp.entity.MyTopicResponse;
import cn.flyexp.entity.MyTopicResponseNew;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.ReplyListResponse;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.TopicResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.UploadFileHelper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by txy on 2016/6/7.
 * Modify by txy on 2016/7/30.
 */
public class TopicController extends AbstractController implements TopicViewCallBack, NotifyManager.Notify {

    public TopicWindow topicWindow = null;
    private MyTopicWindow myTopicWindow = null;
    private TopicPublishWindow topicPublishWindow = null;
    private ReplyListWindow replyListWindow;
    private ReplyWindow replyWindow;
    private final TopicModel topicModel;

    public TopicController() {
        super();
        topicModel = new TopicModel();
        topicWindow = new TopicWindow(this);
        topicWindow.requestTopic();
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.TOPIC_PUBLISH_OPEN) {
            topicPublishWindow = new TopicPublishWindow(this);
            topicPublishWindow.showWindow();
        } else if (mes.what == MessageIDDefine.MYTOPIC_OPEN) {
            myTopicWindow = new MyTopicWindow(this);
            myTopicWindow.showWindow();
        } else if (mes.what == MessageIDDefine.REPLY_OPEN) {
            replyWindow = new ReplyWindow(this);
            replyWindow.initData((ReplyListResponse.ReplyListResponseData)mes.obj);
            replyWindow.showWindow();
        } else if (mes.what == MessageIDDefine.REPLY_LIST_OPEN) {
            replyListWindow = new ReplyListWindow(this);
            replyListWindow.showWindow();
        }
    }

    @Override
    public void onNotify(Message mes) {
        if(mes.what == NotifyIDDefine.NOTIFY_TOPIC_REFRESH){
            topicWindow.remindTopic();
        }
    }


    protected void registerMessages() {
        registerMessage(MessageIDDefine.TOPIC_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.MYTOPIC_OPEN, this);
        registerMessage(MessageIDDefine.REPLY_OPEN, this);
        registerMessage(MessageIDDefine.REPLY_LIST_OPEN, this);
        registerNotify(NotifyIDDefine.NOTIFY_TOPIC_REFRESH, this);
    }

    @Override
    public void publishEnter() {
        sendMessage(MessageIDDefine.TOPIC_PUBLISH_OPEN);
    }

    @Override
    public void picBrowserEnter(PicBrowserBean picBrowserBean) {
        Message message = Message.obtain();
        message.obj = picBrowserBean;
        message.what = MessageIDDefine.PIC_BROWSER_OPEN;
        sendMessage(message);
    }

    @Override
    public void replyEnter(ReplyListResponse.ReplyListResponseData replyListResponseData) {
        sendMessage(MessageIDDefine.REPLY_OPEN, replyListResponseData);
    }

    @Override
    public void replyListEnter() {
        sendMessage(MessageIDDefine.REPLY_LIST_OPEN);
    }


    @Override
    public void uploadImageTopic(String token, List<File> files) {
        if (files == null || token == null || token.equals("")) {
            return;
        }
        MultipartBody body = UploadFileHelper.uploadMultipartFile(files);
        Call<EncodeData> call = service.uploadImageTopic(body, GsonUtil.toEncodeJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    topicPublishWindow.dismissProgressDialog();
                    UploadImageResponse uploadImageResponse = GsonUtil.fromEncodeJson(response.body().getData(), UploadImageResponse.class);
                    int code = uploadImageResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            topicPublishWindow.uploadImageResponse(uploadImageResponse);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(topicPublishWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(uploadImageResponse.getDetail());
                            break;
                    }
                } else {
                    topicPublishWindow.uploadImageResponseFail();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                topicPublishWindow.uploadImageResponseFail();
            }
        });
    }

    @Override
    public void getTopicList(TopicListRequest topicRequest) {
        if (topicRequest == null) {
            return;
        }
        topicRequest.setLast_time(topicModel.getTopicLastTime());
        String data = GsonUtil.toEncodeJson(topicRequest, TopicListRequest.class);
        Call<EncodeData> call = service.getTopicList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TopicListResponse topicResponse = GsonUtil.fromEncodeJson(response.body().getData(), TopicListResponse.class);
                    int code = topicResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            topicWindow.topicResponse(topicResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(topicWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(topicResponse.getDetail());
                            break;
                    }
                } else {
                    topicWindow.loadingFailure();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                topicWindow.loadingFailure();
            }
        });
    }


    @Override
    public void getTopic(TopicRequest topicRequest) {
        if (topicRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(topicRequest, TopicRequest.class);
        Call<EncodeData> call = service.getTopic(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TopicResponse topicResponse = GsonUtil.fromEncodeJson(response.body().getData(), TopicResponse.class);
                    int code = topicResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
//                            topicDetailWindow.responseData(topicResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(topicWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(topicResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void submitTopic(TopicPublishRequest topicPublishRequest) {
        if (topicPublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createTopic(topicPublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("发布成功");
                            topicPublishWindow.hideWindow(true);
                            setUIData(UIDataKeysDef.TOPIC_CONTENT, "");
                            topicWindow.refreshTopic();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(topicPublishWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                topicPublishWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                topicPublishWindow.response();
            }
        });
    }

    @Override
    public void commentTopic(CommentRequest commentRequest) {
        if (commentRequest == null) {
            return;
        }
        GsonUtil.toEncodeJson(commentRequest);
        Call<EncodeData> call = service.commentTopic(commentRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
//                            topicDetailWindow.commentRefresh();
                            topicWindow.setCommentComponentReset();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myTopicWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
//                topicDetailWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
//                topicDetailWindow.response();
            }
        });
    }

    @Override
    public void thumbUp(ThumbUpRequest thumbUpRequest) {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            loginWindowEnter();
            return;
        }
        thumbUpRequest.setToken(token);
        if (thumbUpRequest == null) {
            return;
        }
        Call<EncodeData> call = service.thumbUp(thumbUpRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            topicWindow.thumbUpResponse();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(topicWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void getMyTopic(MyTopicRequest myTopicRequest) {
        if (myTopicRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(myTopicRequest, MyTopicRequest.class);
        Call<EncodeData> call = service.getMyTopicList(data);

        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyTopicResponseNew myTopicResponse = GsonUtil.fromEncodeJson(response.body().getData(), MyTopicResponseNew.class);
                    int code = myTopicResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myTopicWindow.responseTopicData(myTopicResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myTopicWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(myTopicResponse.getDetail());
                            break;
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                myTopicWindow.loadingFailure();
            }
        });

//        call.enqueue(new Callback<EncodeData>() {
//            @Override
//            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
//                if (response.body() != null && response.isSuccess()) {
//                    MyTopicResponse myTopicResponse = GsonUtil.fromEncodeJson(response.body().getData(), MyTopicResponse.class);
//                    int code = myTopicResponse.getCode();
//                    switch (code) {
//                        case ResponseCode.RESPONSE_200:
//                            myTopicWindow.responseTopicData(myTopicResponse.getData());
//                            break;
//                        case ResponseCode.RESPONSE_2001:
//                            againLogin(myTopicWindow);
//                            break;
//                        case ResponseCode.RESPONSE_110:
//                            WindowHelper.showToast(myTopicResponse.getDetail());
//                            break;
//                    }
//                } else {
//                }
//            }
//
//            @Override
//            public void onFailure(Call<EncodeData> call, Throwable t) {
//            }
//        });
    }

    @Override
    public void deleteComment(DeleteCommentRequest deleteCommentRequest) {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            loginWindowEnter();
            return;
        }
        deleteCommentRequest.setToken(token);
        if (deleteCommentRequest == null) {
            return;
        }
        Call<EncodeData> call = service.deleteComment(deleteCommentRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    if (commonResponse.getCode() == 200) {
                        topicWindow.refreshTopic();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void deleteTopic(DeleteTopicRequest deleteTopicRequest) {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            loginWindowEnter();
            return;
        }
        deleteTopicRequest.setToken(token);
        if (deleteTopicRequest == null) {
            return;
        }
        Call<EncodeData> call = service.deleteTopic(deleteTopicRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myTopicWindow.deleteTopic();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myTopicWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void myCommentTopic(CommentRequest commentRequest) {
        if (commentRequest == null) {
            return;
        }
        GsonUtil.toEncodeJson(commentRequest);
        Call<EncodeData> call = service.commentTopic(commentRequest);
        call.enqueue(new Callback<EncodeData>() {

            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myTopicWindow.onCommentSubmitResponse();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myTopicWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void mydeleteComment(DeleteCommentRequest deleteCommentRequest) {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            loginWindowEnter();
            return;
        }
        deleteCommentRequest.setToken(token);
        if (deleteCommentRequest == null) {
            return;
        }
        Call<EncodeData> call = service.deleteComment(deleteCommentRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    if (commonResponse.getCode() == 200) {
                        myTopicWindow.deleteCommentResponse();
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void replyListRequest(String token) {
        Call<EncodeData> call = service.getNewTopicNotifications(GsonUtil.toEncodeJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    ReplyListResponse replyListResponse = GsonUtil.fromEncodeJson(response.body().getData(), ReplyListResponse.class);
                    if (replyListResponse.getCode() == 200) {
                        replyListWindow.responseData(replyListResponse.getData());
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void replyRequest(CommentRequest commentRequest) {
        Call<EncodeData> call = service.commentTopic(commentRequest);
        call.enqueue(new Callback<EncodeData>() {

            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            topicWindow.post(new Runnable() {
                                                 @Override
                                                 public void run() {
                                                     topicWindow.requestTopic();
                                                 }
                                             });
                            setUIData(UIDataKeysDef.REPLY_CONTENT, "");
                            replyWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(replyWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }
}
