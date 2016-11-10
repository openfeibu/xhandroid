package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/28.
 */
public class FeedbackRequest extends BaseRequest{

    @SerializedName("token")
    private String token;

    @SerializedName("content")
    private String content;

    @SerializedName("contact_way")
    private String contact_way;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContact_way() {
        return contact_way;
    }

    public void setContact_way(String contact_way) {
        this.contact_way = contact_way;
    }
}
