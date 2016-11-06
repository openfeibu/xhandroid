package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tanxinye on 2016/10/4.
 */
public class LastTimeRequest implements Serializable{

    @SerializedName("last_time")
    private String last_time;

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public LastTimeRequest(String last_time) {
        this.last_time = last_time;
    }

}
