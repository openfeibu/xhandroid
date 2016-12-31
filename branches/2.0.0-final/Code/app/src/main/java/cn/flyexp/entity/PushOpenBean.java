package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class PushOpenBean {
    @SerializedName("open")
    private String open;
    @SerializedName("data")
    private String data;

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
