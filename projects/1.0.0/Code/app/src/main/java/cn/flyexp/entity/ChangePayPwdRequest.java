package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/24 0024.
 */
public class ChangePayPwdRequest implements Serializable {

    @SerializedName("token")
    private String token;

    @SerializedName("old_paypassword")
    private String old_paypassword;

    @SerializedName("new_paypassword")
    private String new_paypassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOld_paypassword() {
        return old_paypassword;
    }

    public void setOld_paypassword(String old_paypassword) {
        this.old_paypassword = old_paypassword;
    }

    public String getNew_paypassword() {
        return new_paypassword;
    }

    public void setNew_paypassword(String new_paypassword) {
        this.new_paypassword = new_paypassword;
    }
}
