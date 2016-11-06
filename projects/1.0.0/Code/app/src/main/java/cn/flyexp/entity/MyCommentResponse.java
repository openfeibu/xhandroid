package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/8/20 0020.
 */
public class MyCommentResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private ArrayList<MyCommentResponseData> data;

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

    public ArrayList<MyCommentResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<MyCommentResponseData> data) {
        this.data = data;
    }

    public class MyCommentResponseData implements Serializable{

        @SerializedName("tid")
        private int tid;

        @SerializedName("cid")
        private int cid;

        @SerializedName("content")
        private String content;

        @SerializedName("tcid")
        private int tcid;

        @SerializedName("cid_username")
        private String cid_username;

        @SerializedName("favourites_count")
        private int favourites_count;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("deleted_at")
        private String deleted_at;

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

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getFavourites_count() {
            return favourites_count;
        }

        public void setFavourites_count(int favourites_count) {
            this.favourites_count = favourites_count;
        }

        public int getTcid() {
            return tcid;
        }

        public void setTcid(int tcid) {
            this.tcid = tcid;
        }

        public String getCid_username() {
            return cid_username;
        }

        public void setCid_username(String cid_username) {
            this.cid_username = cid_username;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
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
