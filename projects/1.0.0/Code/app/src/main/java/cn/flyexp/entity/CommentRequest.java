package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/22.
 *
 */
public class CommentRequest implements Serializable {

    @SerializedName("token")
    private String token;

    @SerializedName("topic_id")
    private int topic_id;

    @SerializedName("topic_comment")
    private String topic_comment;

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

    public String getTopic_comment() {
        return topic_comment;
    }

    public void setTopic_comment(String topic_comment) {
        this.topic_comment = topic_comment;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }
}
