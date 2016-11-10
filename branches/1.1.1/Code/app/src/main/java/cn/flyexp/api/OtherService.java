package cn.flyexp.api;

import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.CrashRequest;
import cn.flyexp.entity.TokenRequest;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface OtherService {

    @GET("version/")
    Observable<EncodeData> updateRequest(@Query("data") String data);

    @GET("getWebUrl/")
    Observable<EncodeData> webUrlRequest(@Query("data") String data);

    @POST("reportCrash/")
    Observable<EncodeData> crashRequest(@Body CrashRequest request);

    @GET("home/getADList/")
    Observable<EncodeData> adRequest(@Query("data") String data);

    @GET("home/getExtra/")
    Observable<EncodeData> extraRequest(@Query("data") String data);

    @GET("integral/integral_share/")
    Observable<EncodeData> shareRequest(@Body TokenRequest request);

}
