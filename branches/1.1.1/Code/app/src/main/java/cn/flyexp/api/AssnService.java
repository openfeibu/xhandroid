package cn.flyexp.api;

import cn.flyexp.entity.AssnActiPublishRequest;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.entity.EncodeData;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tanxinye on 2016/10/27.
 */
public interface AssnService {

    @GET("association/getHotActivities/")
    Observable<EncodeData> hotActiRequest(@Query("data") String data);

    @GET("association/getActivityList/")
    Observable<EncodeData> assnActiRequest(@Query("data") String data);

    @GET("association/getAssociations/")
    Observable<EncodeData> assnListRequest(@Query("data") String data);

    @GET("association/getAssociationsDetails/")
    Observable<EncodeData> assnDetailRequest(@Query("data") String data);

    @POST("association/joinAssociationMember/")
    Observable<EncodeData> assnJoinRequest(@Body AssnJoinRequest request);

    @GET("association/getMyAssociations/")
    Observable<EncodeData> myAssnRequest(@Query("data") String data);

    @GET("association/getAssociationActivity/")
    Observable<EncodeData> myAssnActivityRequest(@Query("data") String data);

    @GET("association/getAssociationMember/")
    Observable<EncodeData> assnMemberListRequest(@Query("data") String data);

    @GET("association/deleteMember/")
    Observable<EncodeData> deleteMemberRequest(@Query("data") String data);

    @GET("association/updateMemberLevel/")
    Observable<EncodeData> memberManageRequest(@Query("data") String data);

    @GET("association/quitAssociation/")
    Observable<EncodeData> assnQuitRequest(@Query("data") String data);

    @GET("association/checkMemberList/")
    Observable<EncodeData> assnExamineListRequest(@Query("data") String data);

    @GET("association/checkMember/")
    Observable<EncodeData> assnExamineRequest(@Query("data") String data);

    @GET("association/releaseNotice/")
    Observable<EncodeData> assnNoticePublishRequest(@Query("data") String data);

    @POST("association/uploadActivityImageFiles/")
    Observable<EncodeData> uploadImageActiRequest(@Body MultipartBody multipartBody, @Query("data") String token);

    @POST("association/createActivity/")
    Observable<EncodeData> assnActiPublishRequest(@Body AssnActiPublishRequest request);
}
