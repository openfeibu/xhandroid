package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class SmsCodeRequest extends BaseRequest {

    @SerializedName("mobile_no")
    private String mobile_no;

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public SmsCodeRequest(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public SmsCodeRequest() {
    }
}
