package cn.flyexp.api;

import cn.flyexp.entity.TaskCancelRequest;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.TaskCompleteRequest;
import cn.flyexp.entity.TaskFinishRequest;
import cn.flyexp.entity.TaskPublishRequest;
import cn.flyexp.entity.TaskReportRequest;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tanxinye on 2016/10/27.
 */
public interface TaskService {

    @GET("order/getRecommendOrders/")
    Observable<EncodeData> recommendTaskRequest(@Query("data") String data);

    @GET("order/getOrderList/")
    Observable<EncodeData> taskListRequest(@Query("data") String data);

    @GET("order/getMyOrder/")
    Observable<EncodeData> myOrderRequest(@Query("data") String data);

    @GET("order/getMyWork/")
    Observable<EncodeData> myTaskRequest(@Query("data") String data);

    @POST("order/claimOrder/")
    Observable<EncodeData> taskClaimRequest(@Body TaskClaimRequest request);

    @POST("accusation/")
    Observable<EncodeData> taskReportRequest(@Body TaskReportRequest request);

    @POST("order/confirmFinishWork/")
    Observable<EncodeData> taskCompleteRequest(@Body TaskCompleteRequest request);

    @POST("order/finishWork/")
    Observable<EncodeData> taskFinishRequest(@Body TaskFinishRequest request);

    @POST("order/askCancel/")
    Observable<EncodeData> taskCancelRequest(@Body TaskCancelRequest request);

    @POST("order/createOrder/")
    Observable<EncodeData> taskPublishRequest(@Body TaskPublishRequest request);
}
