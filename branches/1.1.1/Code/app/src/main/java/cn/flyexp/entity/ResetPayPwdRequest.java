package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class ResetPayPwdRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("pay_password")
    private String pay_password;

    @SerializedName("sms_code")
    private String sms_code;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }
}
