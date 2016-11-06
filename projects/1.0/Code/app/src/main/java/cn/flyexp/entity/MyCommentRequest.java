package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/20 0020.
 */
public class MyCommentRequest implements Serializable {

    @SerializedName("token")
    private String token;

    @SerializedName("page")
    private int page;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
