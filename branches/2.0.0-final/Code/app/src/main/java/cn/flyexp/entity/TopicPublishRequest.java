package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/18.
 */
public class TopicPublishRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("topic_type")
    private String topic_type;

    @SerializedName("topic_content")
    private String topic_content;

    @SerializedName("img")
    private String img;

    @SerializedName("thumb")
    private String thumb;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(String topic_type) {
        this.topic_type = topic_type;
    }

    public String getTopic_content() {
        return topic_content;
    }

    public void setTopic_content(String topic_content) {
        this.topic_content = topic_content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
