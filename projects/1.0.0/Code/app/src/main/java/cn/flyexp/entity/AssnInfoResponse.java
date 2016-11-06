package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/7/26 0026.
 */
public class AssnInfoResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ArrayList<AssnInfoResponseData> data;

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

    public ArrayList<AssnInfoResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<AssnInfoResponseData> data) {
        this.data = data;
    }

    public class AssnInfoResponseData implements Serializable {

        @SerializedName("iid")
        private int iid;

        @SerializedName("aid")
        private int aid;

        @SerializedName("aname")
        private String aname;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("title")
        private String title;

        @SerializedName("content")
        private String content;

        @SerializedName("view_num")
        private int view_num;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("img_url")
        private String img_url;

        public int getIid() {
            return iid;
        }

        public void setIid(int iid) {
            this.iid = iid;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getAname() {
            return aname;
        }

        public void setAname(String aname) {
            this.aname = aname;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
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

        public int getView_num() {
            return view_num;
        }

        public void setView_num(int view_num) {
            this.view_num = view_num;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
