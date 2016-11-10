package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class TaskReportRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("oid")
    private int oid;

    @SerializedName("content")
    private String content;

    @SerializedName("type")
    private String type;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
