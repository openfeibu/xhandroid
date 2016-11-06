package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/6/10.
 * Modified by txy on 2016/8/17
 */
public class ClientVerifyResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("detail")
    private String detail;
    @SerializedName("mid")
    private String mid;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
