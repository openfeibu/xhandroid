package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/1.
 */
public class ImgUrlResponse extends BaseResponse {

    @SerializedName("url")
    private String url;

    @SerializedName("thumb_url")
    private String thumb_url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }
}
