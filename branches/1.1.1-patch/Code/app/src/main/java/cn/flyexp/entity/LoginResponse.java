package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guo on 2016/5/25.
 * Modify by txy on 2016/8/1.
 */
public class LoginResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("detail")
    private String detail;
    @SerializedName("token")
    private String token;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
