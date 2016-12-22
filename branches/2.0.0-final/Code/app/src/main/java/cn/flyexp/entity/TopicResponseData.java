package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class TopicResponseData implements Serializable {

    @SerializedName("nickname")
    private String nickname;
    @SerializedName("avatar_url")
    private String avatar_url;
    @SerializedName("content")
    private String content;
    @SerializedName("img")
    private String img;
    @SerializedName("view_num")
    private int view_num;
    @SerializedName("comment_num")
    private int comment_num;
    @SerializedName("favorited")
    private int favorited;
    @SerializedName("favourites_count")
    private int favourites_count;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("is_top")
    private int is_top;
    @SerializedName("openid")
    private String openid;
    @SerializedName("tid")
    private int tid;
    @SerializedName("thumb")
    private String thumb;

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getView_num() {
        return view_num;
    }

    public void setView_num(int view_num) {
        this.view_num = view_num;
    }

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }

    public int getComment_num() {
        return comment_num;
    }

    public void setComment_num(int comment_num) {
        this.comment_num = comment_num;
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

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public class CommentResponseData implements Serializable{
        @SerializedName("uid")
        private int uid;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public int getTcid() {
            return tcid;
        }

        public void setTcid(int tcid) {
            this.tcid = tcid;
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
