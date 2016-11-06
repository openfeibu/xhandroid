package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/8/20 0020.
 */
public class MyTopicResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private ArrayList<MyTopicRequestData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<MyTopicRequestData> getData() {
        return data;
    }

    public void setData(ArrayList<MyTopicRequestData> data) {
        this.data = data;
    }

    public class MyTopicRequestData implements Serializable {

        @SerializedName("tid")
        private int tid;

        @SerializedName("type")
        private String type;

        @SerializedName("content")
        private String content;

        @SerializedName("img")
        private String img;

        @SerializedName("view_num")
        private int view_num;

        @SerializedName("comment_num")
        private int comment_num;

        @SerializedName("favourites_count")
        private int favourites_count;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("is_deleted")
        private int is_deleted;

        @SerializedName("admin_deleted")
        private int admin_deleted;

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public int getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(int is_deleted) {
            this.is_deleted = is_deleted;
        }

        public int getAdmin_deleted() {
            return admin_deleted;
        }

        public void setAdmin_deleted(int admin_deleted) {
            this.admin_deleted = admin_deleted;
        }
    }
}
