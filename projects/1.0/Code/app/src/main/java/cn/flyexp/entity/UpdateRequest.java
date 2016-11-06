package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/19 0019.
 */
public class UpdateRequest implements Serializable {

    @SerializedName("platform")
    private String platform;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
