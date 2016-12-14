package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnMemberListResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<AssnMemberResponseData> data;

    public ArrayList<AssnMemberResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<AssnMemberResponseData> data) {
        this.data = data;
    }

    public class AssnMemberResponseData implements Serializable {

        @SerializedName("aid")
        private int aid;

        @SerializedName("uid")
        private int uid;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("realname")
        private String realname;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("level")
        private int level;

        @SerializedName("mobile_no")
        private String mobile_no;

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }
    }

}
