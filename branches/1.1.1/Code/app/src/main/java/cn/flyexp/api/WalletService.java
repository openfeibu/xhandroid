package cn.flyexp.api;

import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.ChangeAlipayRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.WithdrawalRequest;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface WalletService {


    @POST("user/changeAlipay/")
    Observable<EncodeData> changeAlipayRequest(@Body ChangeAlipayRequest request);

    @POST("user/changePayPassword/")
    Observable<EncodeData> changePayPwdRequest(@Body ChangePayPwdRequest request);

    @GET("user/getAlipayInfo/")
    Observable<EncodeData> alipayInfoRequest(@Query("data") String data);

    @POST("user/bindAlipay/")
    Observable<EncodeData> bindAlipayRequest(@Body BindAlipayRequest request);

    @POST("user/resetPayPassword/")
    Observable<EncodeData> resetPayPwdRequest(@Body ResetPayPwdRequest request);

    @GET("user/sendResetPayPasswordSMS/")
    Observable<EncodeData> resetPayPwdVerCodeRequest(@Query("data") String data);

    @POST("user/setPayPassword/")
    Observable<EncodeData> setPayPwdRequest(@Body SetPayPwdRequest request);

    @GET("user/walletAccount/")
    Observable<EncodeData> walletDetailRequest(@Query("data") String data);

    @GET("user/getWallet/")
    Observable<EncodeData> walletInfoRequest(@Query("data") String data);

    @POST("user/withdrawalsApply/")
    Observable<EncodeData> withdrawlRequest(@Body WithdrawalRequest request);
}
