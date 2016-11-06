package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/7/27 0027.
 */
public class AssnNoticePublishRequest implements Serializable{

    @SerializedName("token")
    private String token;

    @SerializedName("notice")
    private String notice;

    @SerializedName("association_id")
    private int association_id;

    public int getAssociation_id() {
        return association_id;
    }

    public void setAssociation_id(int association_id) {
        this.association_id = association_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
