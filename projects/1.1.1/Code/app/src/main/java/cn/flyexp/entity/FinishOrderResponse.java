package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by txy on 2016/6/15 0015.
 */
public class FinishOrderResponse {
    @SerializedName("code")
    private int code;
    @SerializedName("description")
    private String description;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
