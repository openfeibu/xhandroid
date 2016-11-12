package cn.flyexp.entity;

import java.io.Serializable;

/**
 * Created by admin on 2016/7/22.
 */
public class UnDoDeleteTopic implements Serializable {
    private String token;
    private int topic;

    public UnDoDeleteTopic(String token, int topic) {
        this.token = token;
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getTopic() {
        return topic;
    }

    public void setTopic(int topic) {
        this.topic = topic;
    }
}
