package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tanxinye on 2016/10/2.
 */
public class AssnActivityRequest implements Serializable {

    @SerializedName("page")
    private int page;

    @SerializedName("last_time")
    private String last_time;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }
}
