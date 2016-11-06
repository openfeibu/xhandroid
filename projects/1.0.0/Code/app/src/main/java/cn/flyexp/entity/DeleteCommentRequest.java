package cn.flyexp.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/7/22.
 */
public class DeleteCommentRequest implements Serializable {
    private String token;
    private int topic_id;
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
