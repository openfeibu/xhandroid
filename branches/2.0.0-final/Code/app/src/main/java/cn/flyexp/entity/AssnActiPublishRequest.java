package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class AssnActiPublishRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("start_time")
    private String start_time;

    @SerializedName("end_time")
    private String end_time;

    @SerializedName("place")
    private String place;

    @SerializedName("img_url")
    private String img_url;

    @SerializedName("aid")
    private int aid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }
}
