package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class AssnActivityResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<AssnActivityResponseData> data;

    public ArrayList<AssnActivityResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<AssnActivityResponseData> data) {
        this.data = data;
    }

    public class AssnActivityResponseData implements Serializable {

        @SerializedName("actid")
        private int actid;

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

        @SerializedName("start_time")
        private String start_time;

        @SerializedName("end_time")
        private String end_time;

        @SerializedName("place")
        private String place;

        @SerializedName("view_num")
        private int view_num;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("img_url")
        private String img_url;

        public int getActid() {
            return actid;
        }

        public void setActid(int actid) {
            this.actid = actid;
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
