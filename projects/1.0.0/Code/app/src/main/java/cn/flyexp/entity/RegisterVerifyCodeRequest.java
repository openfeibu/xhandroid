package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/28.
 * Modify by txy on 2016/8/1.
 */
public class RegisterVerifyCodeRequest implements Serializable{
    @SerializedName("mobile_no")
    private String mobile_no;

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }
}
