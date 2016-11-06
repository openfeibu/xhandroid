package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by guo on 2016/5/25.
 * Modify by txy on 2016/8/1.
 */
public class LogoutRequest implements Serializable{
    @SerializedName("token")
    private String token;

    public LogoutRequest(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
