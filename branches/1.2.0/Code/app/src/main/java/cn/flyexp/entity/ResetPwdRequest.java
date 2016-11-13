package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/9.
 * Modify by txy on 2016/8/1.
 */
public class ResetPwdRequest implements Serializable{

    @SerializedName("mobile_no")
    private String mobile_no;
    @SerializedName("sms_code")
    private String sms_code;
    @SerializedName("password")
    private String password;

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
