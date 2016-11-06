package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/7/25 0025.
 */
public class TaInfoResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private TaInfoResponse.TaProileResponseData data;

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

    public TaProileResponseData getData() {
        return data;
    }

    public void setData(TaProileResponseData data) {
        this.data = data;
    }

    public class TaProileResponseData implements Serializable {

        @SerializedName("openid")
        private String openid;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("gender")
        private int gender;

        @SerializedName("college")
        private String college;

        @SerializedName("birth_year")
        private int birth_year;

        @SerializedName("favourites_count")
        private int favourites_count;

        @SerializedName("introduction")
        private String introduction;

        @SerializedName("enrollment_year")
        private int enrollment_year;

        @SerializedName("integral")
        private int integral;

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getEnrollment_year() {
            return enrollment_year;
        }

        public void setEnrollment_year(int enrollment_year) {
            this.enrollment_year = enrollment_year;
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

        public int getGender() {
            return gender;
        }

        public String getCollege() {
            return college;
        }

        public void setCollege(String college) {
            this.college = college;
        }

        public int getBirth_year() {
            return birth_year;
        }

        public void setBirth_year(int birth_year) {
            this.birth_year = birth_year;
        }

        public int getFavourites_count() {
            return favourites_count;
        }

        public void setFavourites_count(int favourites_count) {
            this.favourites_count = favourites_count;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }
    }
}
