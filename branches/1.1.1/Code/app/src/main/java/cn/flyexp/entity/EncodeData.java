package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class EncodeData implements Serializable {

    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public EncodeData(String data) {
        this.data = data;
    }
}
