package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tanxinye on 2016/10/18.
 */
public class AssnDetailResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private AssnDetailResponseData data;

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

    public AssnDetailResponseData getData() {
        return data;
    }

    public void setData(AssnDetailResponseData data) {
        this.data = data;
    }

    public class AssnDetailResponseData implements Serializable{

        @SerializedName("aid")
        private int aid;

        @SerializedName("aname")
        private String aname;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("background_url")
        private String background_url;

        @SerializedName("member_number")
        private int member_number;

        @SerializedName("leader")
        private String leader;

        @SerializedName("activity_count")
        private int activity_count;

        @SerializedName("introduction")
        private String introduction;

        @SerializedName("uid")
        private int uid;

        @SerializedName("level")
        private int level;

        @SerializedName("label")
        private String label;

        @SerializedName("association_level")
        private String association_level;

        @SerializedName("notice")
        private String notice;

        public String getNotice() {
            return notice;
        }

        public void setNotice(String notice) {
            this.notice = notice;
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

        public String getBackground_url() {
            return background_url;
        }

        public void setBackground_url(String background_url) {
            this.background_url = background_url;
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

        public int getActivity_count() {
            return activity_count;
        }

        public void setActivity_count(int activity_count) {
            this.activity_count = activity_count;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getAssociation_level() {
            return association_level;
        }

        public void setAssociation_level(String association_level) {
            this.association_level = association_level;
        }
    }
}
