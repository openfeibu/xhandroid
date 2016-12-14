package cn.flyexp.api;

import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.ChangePwdRequest;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tanxinye on 2016/10/23.
 */
public interface UserService {

    @POST("user/login/")
    Observable<EncodeData> loginRequest(@Body LoginRequest request);

    @POST("user/register/")
    Observable<EncodeData> registerRequest(@Body RegisterRequest request);

    @POST("user/uploadImage/")
    Observable<EncodeData> uploadRegisterAvatarRequest(@Body MultipartBody multipartBody, @Query("data") String data);

    @POST("user/sendRegisterSMS/")
    Observable<EncodeData> registerVercodeRequest(@Body SmsCodeRequest request);

    @POST("user/sendResetPasswordSMS/")
    Observable<EncodeData> resetPwdVercodeRequest(@Body SmsCodeRequest request);

    @GET("user/getVerifyImageURL/")
    Observable<EncodeData> imgVercodeRequest(@Query("data") String data);

    @GET("user/sendChangeAliSMS/")
    Observable<EncodeData> changeAlipayRequest(@Query("data") String data);

    @POST("verify/")
    Observable<EncodeData> clientVerifyRequest(@Body ClientVerifyRequest request);

    @POST("user/resetPassword/")
    Observable<EncodeData> resetPwdRequest(@Body ResetPwdRequest request);

    @POST("user/changeUserInfo/")
    Observable<EncodeData> changeMyInfoRequest(@Body ChangeMyInfoRequest request);

    @POST("user/changePassword/")
    Observable<EncodeData> changePwdRequest(@Body ChangePwdRequest request);

    @GET("user/getOthersInfo/")
    Observable<EncodeData> othersRequest(@Query("data") String data);

    @POST("user/realNameAuth/")
    Observable<EncodeData> certificationRequest(@Body MultipartBody multipartBody, @Query("data") String data);

    @POST("user/uploadAvatarFile/")
    Observable<EncodeData> uploadAvatarRequest(@Body MultipartBody multipartBody, @Query("data") String data);

    @GET("user/getMyInfo/")
    Observable<EncodeData> myInfoRequest(@Query("data") String data);

    @POST("user/logout/")
    Observable<EncodeData> logoutRequest(@Body TokenRequest request);

    @POST("feedback/")
    Observable<EncodeData> feedbackRequest(@Body FeedbackRequest request);

    @GET("message/getMessageList/")
    Observable<EncodeData> messageRequest(@Query("data") String data);
}
