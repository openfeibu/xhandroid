package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/6/13.
 */
public class MyTaskResponse implements Serializable {

    @SerializedName("code")
    private int code;
    @SerializedName("detail")
    private String detail;
    @SerializedName("data")
    private ArrayList<MyTaskResponseData> data;
    public ArrayList<MyTaskResponseData> getData() {
        return data;
    }

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


    public void setData(ArrayList<MyTaskResponseData> data) {
        this.data = data;
    }

    public class MyTaskResponseData implements Serializable {
        @SerializedName("oid")
        private int oid;
        @SerializedName("status")
        private String status;
        @SerializedName("nickname")
        private String nickname;
        @SerializedName("avatar_url")
        private String avatar_url;
        @SerializedName("destination")
        private String destination;
        @SerializedName("description")
        private String description;
        @SerializedName("alt_phone")
        private String alt_phone;
        @SerializedName("fee")
        private float fee;
        @SerializedName("phone")
        private String phone;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("openid")
        private String openid;

        public int getOid() {
            return oid;
        }

        public void setOid(int oid) {
            this.oid = oid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAlt_phone() {
            return alt_phone;
        }

        public void setAlt_phone(String alt_phone) {
            this.alt_phone = alt_phone;
        }

        public float getFee() {
            return fee;
        }

        public void setFee(float fee) {
            this.fee = fee;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
}
