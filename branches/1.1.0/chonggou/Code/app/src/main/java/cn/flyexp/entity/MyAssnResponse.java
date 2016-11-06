package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/2.
 */
public class MyAssnResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private ArrayList<MyAssnResponseData> data;

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

    public ArrayList<MyAssnResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<MyAssnResponseData> data) {
        this.data = data;
    }

    public class MyAssnResponseData implements Serializable {

        @SerializedName("aid")
        private int aid;

        @SerializedName("aname")
        private String aname;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("member_number")
        private int member_number;

        @SerializedName("leader")
        private String leader;

        @SerializedName("level")
        private int level;

        @SerializedName("introduction")
        private String introduction ;

        @SerializedName("activity_count")
        private int activity_count ;

        @SerializedName("label")
        private String label;

        @SerializedName("association_level")
        private String association_level;

        public String getAssociation_level() {
            return association_level;
        }

        public void setAssociation_level(String association_level) {
            this.association_level = association_level;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getActivity_count() {
            return activity_count;
        }

        public void setActivity_count(int activity_count) {
            this.activity_count = activity_count;
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

        public int getMember_number() {
            return member_number;
        }

        public void setMember_number(int member_number) {
            this.member_number = member_number;
        }

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }
}
