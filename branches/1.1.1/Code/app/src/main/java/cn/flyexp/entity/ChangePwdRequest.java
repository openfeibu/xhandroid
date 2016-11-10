package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class ChangePwdRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("password")
    private String password;

    @SerializedName("new_password")
    private String new_password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }
}
