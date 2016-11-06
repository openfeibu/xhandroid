package cn.flyexp.mvc.topic;

import java.io.File;
import java.util.List;

import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.MyCommentRequest;
import cn.flyexp.entity.MyCommentResponse;
import cn.flyexp.entity.MyTopicRequest;
import cn.flyexp.entity.MyTopicResponse;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicCommentRequest;
import cn.flyexp.entity.TopicCommentResponse;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.TopicResponse;
import cn.flyexp.entity.TopicSearchRequest;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.UploadFileHelper;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/6/7.
 */
public class TopicModel {

    private TopicModelCallback callBack;
    private NetWorkService service;

    public TopicModel(TopicModelCallback callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void getTopicList(TopicListRequest topicRequest) {
        if (topicRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(topicRequest, TopicListRequest.class);
        Call<EncodeData> call = service.getTopicList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TopicListResponse topicResponse = GsonUtil.fromJson(response.body().getData(), TopicListResponse.class);
                    callBack.getTopicList(topicResponse);
                } else {
                    callBack.getTopicList(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.getTopicList(null);
            }
        });
    }

    public void getTopic(TopicRequest topicRequest) {
        if (topicRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(topicRequest, TopicRequest.class);
        Call<EncodeData> call = service.getTopic(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TopicResponse topicResponse = GsonUtil.fromJson(response.body().getData(), TopicResponse.class);
                    callBack.topicResponse(topicResponse);
                } else {
                    callBack.topicResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.topicResponse(null);
            }
        });
    }

    public void uploadImageTopic(String token, List<File> files) {
        if (files == null || token == null || token.equals("")) {
            return;
        }
        MultipartBody body = UploadFileHelper.uploadMultipartFile(files);
        Call<EncodeData> call = service.uploadImageTopic(body, GsonUtil.toJson(new TokenToJson(token)));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    UploadImageResponse uploadImageResponse = GsonUtil.fromJson(response.body().getData(), UploadImageResponse.class);
                    callBack.uploadImageResponse(uploadImageResponse);
                } else {
                    callBack.uploadImageResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.uploadImageResponse(null);
            }
        });

    }

    public void getTopicCommentsList(TopicCommentRequest topicCommentRequest) {
        if (topicCommentRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(topicCommentRequest, TopicCommentRequest.class);
        Call<EncodeData> respond = service.getTopicCommentsList(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TopicCommentResponse topicCommentResponse = (TopicCommentResponse) GsonUtil.fromJson(response.body().getData(), TopicCommentResponse.class);
                    callBack.commentListResponse(topicCommentResponse);
                } else {
                    callBack.commentListResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.commentListResponse(null);
            }
        });
    }

    public void searchTopic(TopicSearchRequest topicSearchRequest) {
        if (topicSearchRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(topicSearchRequest, TopicSearchRequest.class);
        Call<EncodeData> respond = service.searchTopic(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TopicListResponse topicResponse = (TopicListResponse) GsonUtil.fromJson(response.body().getData(), TopicListResponse.class);
                    callBack.searchTopicResponse(topicResponse);
                } else {
                    callBack.searchTopicResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.searchTopicResponse(null);
            }
        });
    }


    public void createTopic(TopicPublishRequest topicPublishRequest) {
        if (topicPublishRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createTopic(topicPublishRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.topicPublishResponse(commonResponse);
                } else {
                    callBack.topicPublishResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.topicPublishResponse(null);
            }
        });
    }

    public void commentTopic(CommentRequest commentRequest) {
        if (commentRequest == null) {
            return;
        }
        GsonUtil.toJson(commentRequest);
        Call<EncodeData> call = service.commentTopic(commentRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.commentResponse(commonResponse);
                } else {
                    callBack.commentResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.commentResponse(null);
            }
        });

    }

    public void thumbUp(ThumbUpRequest thumbUpRequest) {
        if (thumbUpRequest == null) {
            return;
        }
        Call<EncodeData> call = service.thumbUp(thumbUpRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.thumbUpResponse(commonResponse);
                } else {
                    callBack.thumbUpResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.thumbUpResponse(null);
            }
        });

    }


    public void myCommentList(MyCommentRequest myCommentRequest) {
        if (myCommentRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(myCommentRequest, MyCommentRequest.class);
        Call<EncodeData> call = service.myCommentList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyCommentResponse myCommentResponse = GsonUtil.fromJson(response.body().getData(), MyCommentResponse.class);
                    callBack.myCommentResponse(myCommentResponse);
                } else {
                    callBack.myCommentResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.myCommentResponse(null);
            }
        });
    }

    public void myTopicList(MyTopicRequest myTopicRequest) {
        if (myTopicRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(myTopicRequest, MyTopicRequest.class);
        Call<EncodeData> call = service.getMyTopicList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyTopicResponse myTopicResponse = GsonUtil.fromJson(response.body().getData(), MyTopicResponse.class);
                    callBack.myTopicResponse(myTopicResponse);
                } else {
                    callBack.myTopicResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.myTopicResponse(null);
            }
        });
    }

    public void deleteComment(DeleteCommentRequest deleteCommentRequest) {
        if (deleteCommentRequest == null) {
            return;
        }
        Call<EncodeData> call = service.deleteComment(deleteCommentRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.deleteCommentResponse(commonResponse);
                } else {
                    callBack.deleteCommentResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.deleteCommentResponse(null);
            }
        });
    }

    public void deleteTopic(DeleteTopicRequest deleteTopicRequest) {
        if (deleteTopicRequest == null) {
            return;
        }
        Call<EncodeData> call = service.deleteTopic(deleteTopicRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.deleteTopicResponse(commonResponse);
                } else {
                    callBack.deleteTopicResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.deleteTopicResponse(null);
            }
        });
    }
}
