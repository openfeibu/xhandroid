package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/6/13.
 */
public class MyOrderResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private ArrayList<MyOrderResponseData> data;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<MyOrderResponseData> getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(ArrayList<MyOrderResponseData> data) {
        this.data = data;
    }

    public class MyOrderResponseData implements Serializable {
        @SerializedName("oid")
        private int oid;
        @SerializedName("status")
        private String status;
        @SerializedName("courier_name")
        private String courier_name;
        @SerializedName("courier_avatar")
        private String courier_avatar;
        @SerializedName("destination")
        private String destination;
        @SerializedName("description")
        private String description;
        @SerializedName("arrival_time")
        private String arrival_time;
        @SerializedName("fee")
        private int fee;
        @SerializedName("courier_phone")
        private String courier_phone;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("courier_openid")
        private String courier_openid;

        public String getCourier_openid() {
            return courier_openid;
        }

        public void setCourier_openid(String courier_openid) {
            this.courier_openid = courier_openid;
        }

        public int getOid() {
            return oid;
        }

        public void setOid(int oid) {
            this.oid = oid;
        }

        public String getCourier_name() {
            return courier_name;
        }

        public void setCourier_name(String courier_name) {
            this.courier_name = courier_name;
        }

        public String getCourier_phone() {
            return courier_phone;
        }

        public void setCourier_phone(String courier_phone) {
            this.courier_phone = courier_phone;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
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

        public String getArrival_time() {
            return arrival_time;
        }

        public void setArrival_time(String arrival_time) {
            this.arrival_time = arrival_time;
        }

        public int getFee() {
            return fee;
        }

        public void setFee(int fee) {
            this.fee = fee;
        }

        public String getCourier_avatar() {
            return courier_avatar;
        }

        public void setCourier_avatar(String courier_avatar) {
            this.courier_avatar = courier_avatar;
        }
    }
}
