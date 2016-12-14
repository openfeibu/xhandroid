package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class OthersInfoRequest extends BaseRequest {

    @SerializedName("openid")
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
