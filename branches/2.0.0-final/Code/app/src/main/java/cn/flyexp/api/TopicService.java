package cn.flyexp.api;

import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicPublishRequest;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tanxinye on 2016/10/26.
 */
public interface TopicService {

    @POST("topic/comment/")
    Observable<EncodeData> commentRequest(@Body CommentRequest request);

    @POST("topic/thumbUp/")
    Observable<EncodeData> thumbUpRequest(@Body ThumbUpRequest request);

    @GET("topic/getTopics/")
    Observable<EncodeData> topicListRequest(@Query("data") String data);

    @POST("topic/createTopic")
    Observable<EncodeData> topicPublishRequest(@Body TopicPublishRequest request);

    @POST("topic/uploadImage/")
    Observable<EncodeData> uploadImageTopicRequest(@Body MultipartBody multipartBody, @Query("data") String data);

    @POST("topic/deleteComment/")
    Observable<EncodeData> deleteCommentRequest(@Body DeleteCommentRequest request);

    @POST("topic/deleteTopic/")
    Observable<EncodeData> deleteTopicRequest(@Body DeleteTopicRequest request);

    @GET("topic/getMyTopics/")
    Observable<EncodeData> myTopicListRequest(@Query("data") String data);

    @GET("topic/getTopicCommentsList/")
    Observable<EncodeData> commentListRequest(@Query("data") String data);
}
