package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by txy on 2016/7/25 0025.
 */
public class AdRequest {

    @SerializedName("time")
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
