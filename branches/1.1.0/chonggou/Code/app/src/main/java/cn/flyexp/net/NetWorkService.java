package cn.flyexp.net;


import cn.flyexp.entity.AssnActivityPublishRequest;
import cn.flyexp.entity.AssnJoinRequest;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.CancelTaskRequest;
import cn.flyexp.entity.ChangeAliPayRequest;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.FinishWorkRequest;
import cn.flyexp.entity.IntegralRecordRequest;
import cn.flyexp.entity.LogRequest;
import cn.flyexp.entity.RecommendOrderRequest;
import cn.flyexp.entity.RegisterVerifyCodeRequest;
import cn.flyexp.entity.ReportRequest;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.ResetPwdVerifyCodeRequest;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TokenToJson;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.LogoutRequest;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.OrderCreateRequest;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.AssnProfilePublishRequest;
import cn.flyexp.entity.WithdrawalRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by guo on 2016/5/24.
 * Modified by txy on 2016/8/20.
 */
public interface NetWorkService {

    //getOrderListf
    @GET("order/getOrderList/")
    Call<EncodeData> getOrderList(@Query("data") String data);

    //createOrder
    @POST("order/createOrder/")
    Call<EncodeData> createOrder(@Body OrderCreateRequest request);

    //claim
    @POST("order/claimOrder/")
    Call<EncodeData> claimOrder(@Body TaskClaimRequest request);

    //getMyOrder
    @GET("order/getMyOrder/")
    Call<EncodeData> myOrder(@Query("data") String data);

    //getMyWork
    @GET("order/getMyWork/")
    Call<EncodeData> myTask(@Query("data") String data);

    //getMyWork
    @POST("order/confirmFinishWork/")
    Call<EncodeData> confirmFinishWork(@Body FinishOrderRequest request);

    //finishWork
    @POST("order/finishWork/")
    Call<EncodeData> finishWork(@Body FinishWorkRequest request);

    @POST("order/askCancel/")
    Call<EncodeData> cancelTask(@Body CancelTaskRequest request);

    /**
     * 用户中心模块
     *
     * @param request
     * @param
     */
    //**regester
    @POST("user/register/")
    Call<EncodeData> submitUserRegister(@Body RegisterRequest request);

    //**login
    @POST("user/login/")
    Call<EncodeData> login(@Body LoginRequest request);

    //loginout
    @POST("user/logout/")
    Call<EncodeData> logout(@Body LogoutRequest request);

    //changePassword
    @POST("user/changePassword/")
    Call<EncodeData> changePwd(@Body ChangePwdRequest request);

    @POST("user/changeUserInfo/")
    Call<EncodeData> changeMyInfo(@Body ChangeMyInfoRequest request);

    @POST("user/sendResetPasswordSMS/")
    Call<EncodeData> sendResetPasswordSMS(@Body ResetPwdVerifyCodeRequest request);

    @POST("user/sendRegisterSMS/")
    Call<EncodeData> sendRegisterSMS(@Body RegisterVerifyCodeRequest request);

    @GET("user/getVerifyImageURL/")
    Call<EncodeData> getVerifyImageURL(@Query("data") String data);

    //resetPassword
    @POST("user/resetPassword/")
    Call<EncodeData> resetPassword(@Body ResetPwdRequest request);

    //verify
    @POST("verify/")
    Call<EncodeData> verify(@Body ClientVerifyRequest request);

    @POST("feedback/")
    Call<EncodeData> feedback(@Body FeedbackRequest request);

    @POST("accusation/")
    Call<EncodeData> report(@Body ReportRequest request);

    /**
     * 话题网络
     */
    //获取话题列表
    @GET("topic/getTopicList/")
    Call<EncodeData> getTopicList(@Query("data") String data);

    @GET("topic/getTopic/")
    Call<EncodeData> getTopic(@Query("data") String data);

    @POST("topic/createTopic")
    Call<EncodeData> createTopic(@Body TopicPublishRequest request);

    @POST("topic/deleteTopic/")
    Call<EncodeData> deleteTopic(@Body DeleteTopicRequest request);

    @POST("topic/comment/")
    Call<EncodeData> commentTopic(@Body CommentRequest request);

    @GET("topic/getMyComment/")
    Call<EncodeData> myCommentList(@Query("data") String data);

    @GET("topic/getMyTopic/")
    Call<EncodeData> getMyTopicList(@Query("data") String data);

    @POST("topic/deleteComment/")
    Call<EncodeData> deleteComment(@Body DeleteCommentRequest request);

    @GET("getNewTopicNotifications/")
    Call<EncodeData> getNewTopicNotifications(@Query("data") String data);

    @POST("topic/thumbUp/")
    Call<EncodeData> thumbUp(@Body ThumbUpRequest request);

    @POST("topic/uploadImage/")
    Call<EncodeData> uploadImageTopic(@Body MultipartBody multipartBody, @Query("data") String token);

    /**
     * 校园
     */
    @GET("home/getADList/")
    Call<EncodeData> getAdList(@Query("data") String data);

    @GET("order/getRecommendOrders/")
    Call<EncodeData> getRecommendOrders(@Query("data") String data);


    /**
     * 社团
     */
    @GET("association/getActivityList/")
    Call<EncodeData> getActivityList(@Query("data") String data);

    @GET("association/getHotActivities/")
    Call<EncodeData> getHotActivities(@Query("data") String data);

    @POST("association/createMessage/")
    Call<EncodeData> createMessage(@Body AssnNoticePublishRequest request);

    @GET("association/releaseNotice/")
    Call<EncodeData> releaseNotice(@Query("data") String data);

    @POST("association/createActivity/")
    Call<EncodeData> createActivity(@Body AssnActivityPublishRequest request);

    @POST("association/setProfile/")
    Call<EncodeData> setProfile(@Body AssnProfilePublishRequest request);

    @GET("association/getProfile/")
    Call<EncodeData> getProfile(@Query("data") String data);

    @POST("association/uploadActivityImageFiles/")
    Call<EncodeData> uploadImageActi(@Body MultipartBody multipartBody, @Query("data") String token);

    @GET("association/getMyAssociations/")
    Call<EncodeData> getMyAssociations(@Query("data") String token);

    @GET("association/getAssociationsDetails/")
    Call<EncodeData> getAssociationsDetails(@Query("data") String token);

    @GET("association/getAssociationActivity/")
    Call<EncodeData> getAssociationActivity(@Query("data") String token);

    @GET("association/getAssociationMember/")
    Call<EncodeData> getAssociationMember(@Query("data") String token);

    @GET("association/getAssociations/")
    Call<EncodeData> getAssociations(@Query("data") String token);

    @GET("association/checkMemberList/")
    Call<EncodeData> checkMemberList(@Query("data") String token);

    @GET("association/quitAssociation/")
    Call<EncodeData> quitAssociation(@Query("data") String token);

    @GET("association/updateMemberLevel/")
    Call<EncodeData> updateMemberLevel(@Query("data") String token);

    @GET("association/deleteMember/")
    Call<EncodeData> deleteMember(@Query("data") String token);

    @GET("association/checkMember/")
    Call<EncodeData> checkMember(@Query("data") String token);

    @POST("association/joinAssociationMember/")
    Call<EncodeData> joinAssociationMember(@Body AssnJoinRequest request);

    /**
     * 纸条
     */
    @GET("message/getMessageList/")
    Call<EncodeData> getMessageList(@Query("data") String data);


    /**
     * 我的
     */
    @GET("user/getMyInfo/")
    Call<EncodeData> getMyProile(@Query("data") String data);

    @GET("user/getOthersInfo/")
    Call<EncodeData> getOthersInfo(@Query("data") String data);

    @POST("user/uploadAvatarFile/")
    Call<EncodeData> uploadImageAvatar(@Body MultipartBody multipartBody, @Query("data") String token);

    @GET("version/")
    Call<EncodeData> update(@Query("data") String data);

    @POST("user/realNameAuth/")
    Call<EncodeData> uploadImageCertifi(@Body MultipartBody multipartBody, @Query("data") String token);

    /**
     * 校园淘
     */
    @GET("integral/integral_share/")
    Call<EncodeData> share(@Body TokenToJson token);

    @GET("home/getExtra/")
    Call<EncodeData> campusNo(@Query("data") String data);

    @POST("integral/integral_list")
    Call<EncodeData> integralRecord(@Body IntegralRecordRequest request);


    /**
     * 支付
     */
    @POST("user/withdrawalsApply/")
    Call<EncodeData> withdrawl(@Body WithdrawalRequest request);

    @POST("user/bindAlipay/")
    Call<EncodeData> bindAlipay(@Body BindAlipayRequest request);

    @GET("user/getWallet/")
    Call<EncodeData> getWallet(@Query("data") String data);

    @GET("user/walletAccount/")
    Call<EncodeData> getWalletDetail(@Query("data") String data);

    @GET("user/getAlipayInfo/")
    Call<EncodeData> getAlipayInfo(@Query("data") String data);

    @POST("user/changeAlipay/")
    Call<EncodeData> changeAlipay(@Body ChangeAliPayRequest request);

    @GET("user/sendChangeAliSMS/")
    Call<EncodeData> getChangeAliPayVercord(@Query("data") String data);

    @GET("user/sendResetPayPasswordSMS/")
    Call<EncodeData> sendResetPayPasswordSMS(@Query("data") String data);

    @POST("user/setPayPassword/")
    Call<EncodeData> setPayPassword(@Body SetPayPwdRequest request);

    @POST("user/changePayPassword/")
    Call<EncodeData> changePayPwd(@Body ChangePayPwdRequest request);

    @POST("user/resetPayPassword/")
    Call<EncodeData> resetPayPwd(@Body ResetPayPwdRequest request);

    /**
     * 获取网页界面url
     */
    @GET("getWebUrl/")
    Call<EncodeData> getWebUrl(@Query("data") String data);

    @POST("reportCrash/")
    Call<EncodeData> crash(@Body LogRequest request);

}
