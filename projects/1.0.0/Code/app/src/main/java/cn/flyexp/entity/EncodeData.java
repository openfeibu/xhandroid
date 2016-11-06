package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guo on 2016/6/13.
 */
public class EncodeData {
    @SerializedName("data")
    private String data;

    public EncodeData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
