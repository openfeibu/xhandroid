package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/7/25 0025.
 */
public class AdResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ArrayList<AdResponse.AdResponseData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<AdResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<AdResponseData> data) {
        this.data = data;
    }

    public class AdResponseData implements Serializable {

        @SerializedName("adid")
        private int adid;

        @SerializedName("ad_url")
        private String ad_url;

        @SerializedName("ad_image_url")
        private String ad_image_url;

        @SerializedName("description")
        private String description;

        @SerializedName("rank")
        private String rank;

        @SerializedName("title")
        private String title;

        @SerializedName("created_at")
        private String created_at;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getAdid() {
            return adid;
        }

        public void setAdid(int adid) {
            this.adid = adid;
        }

        public String getAd_url() {
            return ad_url;
        }

        public void setAd_url(String ad_url) {
            this.ad_url = ad_url;
        }

        public String getAd_image_url() {
            return ad_image_url;
        }

        public void setAd_image_url(String ad_image_url) {
            this.ad_image_url = ad_image_url;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

}
