package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnNoticePublishRequest extends BaseRequest {

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
