package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guo on 2016/5/25.
 */
public class RegisterRequest {
    @SerializedName("mobile_no")
    private String mobile_no;
    @SerializedName("password")
    private String password;
    @SerializedName("sms_code")
    private String sms_code;
    @SerializedName("mid")
    private String mid;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("gender")
    private int gender;
    @SerializedName("enrollment_year")
    private int enrollment_year;


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

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

    public int getEnrollment_year() {
        return enrollment_year;
    }

    public void setEnrollment_year(int enrollment_year) {
        this.enrollment_year = enrollment_year;
    }
}
