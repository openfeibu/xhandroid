package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/21.
 */
public class DeleteCommentRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("topic_id")
    private int topic_id;

    @SerializedName("comment_id")
    private int comment_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }
}
