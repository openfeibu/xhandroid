package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guo on 2016/7/23.
 * Modity by txy on 2016/7/31.
 */
public class TopicCommentResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ArrayList<TopicCommentResponseData> data;

    @SerializedName("detail")
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<TopicCommentResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<TopicCommentResponseData> data) {
        this.data = data;
    }

    public class TopicCommentResponseData implements Serializable {

        @SerializedName("tcid")
        private int tcid;

        @SerializedName("openid")
        private String openid;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("content")
        private String content;

        @SerializedName("favorited")
        private int favorited;

        @SerializedName("favourites_count")
        private int favourites_count;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("be_review_id")
        private int be_review_id;

        @SerializedName("be_review_username")
        private String be_review_username;


        public int getTcid() {
            return tcid;
        }

        public void setTcid(int tcid) {
            this.tcid = tcid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getFavorited() {
            return favorited;
        }

        public void setFavorited(int favorited) {
            this.favorited = favorited;
        }

        public int getFavourites_count() {
            return favourites_count;
        }

        public void setFavourites_count(int favourites_count) {
            this.favourites_count = favourites_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getBe_review_id() {
            return be_review_id;
        }

        public void setBe_review_id(int be_review_id) {
            this.be_review_id = be_review_id;
        }

        public String getBe_review_username() {
            return be_review_username;
        }

        public void setBe_review_username(String be_review_username) {
            this.be_review_username = be_review_username;
        }
    }
}
