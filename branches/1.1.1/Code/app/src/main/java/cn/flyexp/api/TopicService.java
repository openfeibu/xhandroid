package cn.flyexp.api;

import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.ThumbUpRequest;
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

    @GET("topic/getTopicList/")
    Observable<EncodeData> topicListRequest(@Query("data") String data);
}
