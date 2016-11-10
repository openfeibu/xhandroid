package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnQuitRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("association_id")
    private int association_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAssociation_id() {
        return association_id;
    }

    public void setAssociation_id(int association_id) {
        this.association_id = association_id;
    }
}
