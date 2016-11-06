package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/20.
 */
public class DeleteTopicRequest implements Serializable {

    @SerializedName("token")
    private String token;

    @SerializedName("topic_id")
    private int topic_id;

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
}
