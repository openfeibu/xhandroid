package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by txy on 2016/7/31 0031.
 */
public class UserCountRequset implements Serializable {

    @SerializedName("data")
    private HashMap<String, Integer> data;

    public HashMap<String, Integer> getData() {
        return data;
    }

    public void setData(HashMap<String, Integer> data) {
        this.data = data;
    }

    public UserCountRequset(HashMap<String, Integer> data) {
        this.data = data;
    }
}
