package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by guo on 2016/7/22.
 * Modifity by txy on 2016/7/30
 */
public class TopicListRequest implements Serializable {

    @SerializedName("page")
    private int page;

    @SerializedName("token")
    private String token;

    @SerializedName("last_time")
    private String last_time;

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

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
