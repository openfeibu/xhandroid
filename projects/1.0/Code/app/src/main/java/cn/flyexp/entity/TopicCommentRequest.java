package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/7/30
 *
 */
public class TopicCommentRequest implements Serializable {

    @SerializedName("topic_id")
    private int topic_id;

    @SerializedName("page")
    private int page;

    public int getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(int topic_id) {
        this.topic_id = topic_id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
