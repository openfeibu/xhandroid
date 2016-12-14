package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class WebBean implements Serializable{

    @SerializedName("name")
    private String name;

    @SerializedName("url")
    private String url;

    @SerializedName("title")
    private String title;

    /**
     * 是否先从服务器请求url
     */
    @SerializedName("isRequest")
    private boolean isRequest;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isRequest() {
        return isRequest;
    }

    public void setRequest(boolean request) {
        isRequest = request;
    }
}
