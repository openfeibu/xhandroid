package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/7/25 0025.
 */
public class MyInfoResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private MyInfoResponseData data;

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

    public MyInfoResponseData getData() {
        return data;
    }

    public void setData(MyInfoResponseData data) {
        this.data = data;
    }

    public class MyInfoResponseData implements Serializable {

        @SerializedName("openid")
        private String openid;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("realname")
        private String realname;

        @SerializedName("mobile_no")
        private String mobile_no;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("gender")
        private int gender;

        @SerializedName("birth_year")
        private int birth_year;

        @SerializedName("favourites_count")
        private int favourites_count;

        @SerializedName("introduction")
        private String introduction;

        @SerializedName("association_id")
        private int association_id;

        @SerializedName("enrollment_year")
        private int enrollment_year;

        @SerializedName("integral")
        private int integral;

        @SerializedName("address")
        private String address;

        @SerializedName("college")
        private String college;

        @SerializedName("is_cheif")
        private int is_cheif;

        @SerializedName("is_merchant")
        private int is_merchant;

        @SerializedName("is_auth")
        private int is_auth;

        @SerializedName("today_integral")
        private int today_integral;

        @SerializedName("level")
        private int level;

        @SerializedName("upgrade")
        private int upgrade;

        @SerializedName("wallet")
        private float wallet;

        @SerializedName("alipay")
        private String alipay;

        @SerializedName("alipay_name")
        private String alipay_name;

        @SerializedName("is_alipay")
        private int is_alipay;

        @SerializedName("is_paypassword")
        private int is_paypassword;

        public int getIs_cheif() {
            return is_cheif;
        }

        public void setIs_cheif(int is_cheif) {
            this.is_cheif = is_cheif;
        }

        public int getIs_merchant() {
            return is_merchant;
        }

        public void setIs_merchant(int is_merchant) {
            this.is_merchant = is_merchant;
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

        public void setGender(int gender) {
            this.gender = gender;
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

        public String getRealname() {
            return realname;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public int getAssociation_id() {
            return association_id;
        }

        public void setAssociation_id(int association_id) {
            this.association_id = association_id;
        }

        public int getEnrollment_year() {
            return enrollment_year;
        }

        public void setEnrollment_year(int enrollment_year) {
            this.enrollment_year = enrollment_year;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIs_auth() {
            return is_auth;
        }

        public void setIs_auth(int is_auth) {
            this.is_auth = is_auth;
        }

        public int getToday_integral() {
            return today_integral;
        }

        public void setToday_integral(int today_integral) {
            this.today_integral = today_integral;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getUpgrade() {
            return upgrade;
        }

        public void setUpgrade(int upgrade) {
            this.upgrade = upgrade;
        }

        public float getWallet() {
            return wallet;
        }

        public void setWallet(float wallet) {
            this.wallet = wallet;
        }

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public String getAlipay_name() {
            return alipay_name;
        }

        public void setAlipay_name(String alipay_name) {
            this.alipay_name = alipay_name;
        }

        public int getIs_alipay() {
            return is_alipay;
        }

        public void setIs_alipay(int is_alipay) {
            this.is_alipay = is_alipay;
        }

        public int getIs_paypassword() {
            return is_paypassword;
        }

        public void setIs_paypassword(int is_paypassword) {
            this.is_paypassword = is_paypassword;
        }
    }
}
