package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guo on 2016/5/25.
 * Modify by txy on 2016/8/1.
 */
public class LoginRequest {
    @SerializedName("mobile_no")
    private String mobile_no;
    @SerializedName("password")
    private String password;
    @SerializedName("mid")
    private String mid;
    @SerializedName("verify_code")
    private String verify_code;
    @SerializedName("device_token")
    private String device_token;
    @SerializedName("platform")
    private String platform;

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
