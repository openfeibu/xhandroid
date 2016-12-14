package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class LoginRequest extends BaseRequest {

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
    @SerializedName("push_server")
    private String push_server;

    public String getPush_server() {
        return push_server;
    }

    public void setPush_server(String push_server) {
        this.push_server = push_server;
    }

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
