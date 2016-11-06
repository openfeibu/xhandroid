package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by guo on 2016/10/13.
 */

public class AssnRequest implements Serializable{
    @SerializedName("token")
    String token;
    @SerializedName("page")
    int pgae;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPgae() {
        return pgae;
    }

    public void setPgae(int pgae) {
        this.pgae = pgae;
    }
}
