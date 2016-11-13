package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/7/28 0028.
 */
public class AssnProfileResponse implements Serializable{

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private AssnProfileResponseData data;

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

    public AssnProfileResponseData getData() {
        return data;
    }

    public void setData(AssnProfileResponseData data) {
        this.data = data;
    }

    public class AssnProfileResponseData implements Serializable{

        @SerializedName("aname")
        private String aname;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("member_number")
        private int member_number;

        @SerializedName("introduction")
        private String introduction;

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

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
    }
}
