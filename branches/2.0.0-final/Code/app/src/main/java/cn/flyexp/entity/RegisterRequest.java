package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class RegisterRequest extends BaseRequest {

    @SerializedName("mobile_no")
    private String mobile_no;
    @SerializedName("password")
    private String password;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("gender")
    private int gender;
    @SerializedName("sms_code")
    private String sms_code;
    @SerializedName("device_token")
    private String device_token;
    @SerializedName("platform")
    private String platform;
    @SerializedName("push_server")
    private String push_server;
    @SerializedName("avatar_url")
    private String avatar_url;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public String getPush_server() {
        return push_server;
    }

    public void setPush_server(String push_server) {
        this.push_server = push_server;
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

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
