package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/1 0001.
 */
public class ImageVerifyRequest implements Serializable{
    
    @SerializedName("time")
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ImageVerifyRequest(String time) {
        this.time = time;
    }
}
