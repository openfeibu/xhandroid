package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/6/5.
 */
public class OrderResponse implements Serializable {

    @SerializedName("code")
    private int code;
    @SerializedName("detail")
    private String detail;
    @SerializedName("data")
    private ArrayList<OrderResponseData> data;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ArrayList<OrderResponseData> getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(ArrayList<OrderResponseData> data) {
        this.data = data;
    }

    public class OrderResponseData implements Serializable {
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
        @SerializedName("fee")
        private String fee;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("openid")
        private String openid;
        @SerializedName("share_url")
        private String share_url;
        @SerializedName("time")
        private TimeData time;

        public TimeData getTime() {
            return time;
        }

        public void setTime(TimeData time) {
            this.time = time;
        }

        public class TimeData implements Serializable{

            @SerializedName("new_time")
            private String new_time;

            @SerializedName("accepted_time")
            private String accepted_time;

            @SerializedName("finish_time")
            private String finish_time;

            @SerializedName("completed_time")
            private String completed_time;

            @SerializedName("cancelled_time")
            private String cancelled_time;

            public String getNew_time() {
                return new_time;
            }

            public void setNew_time(String new_time) {
                this.new_time = new_time;
            }

            public String getAccepted_time() {
                return accepted_time;
            }

            public void setAccepted_time(String accepted_time) {
                this.accepted_time = accepted_time;
            }

            public String getFinish_time() {
                return finish_time;
            }

            public void setFinish_time(String finish_time) {
                this.finish_time = finish_time;
            }

            public String getCompleted_time() {
                return completed_time;
            }

            public void setCompleted_time(String completed_time) {
                this.completed_time = completed_time;
            }

            public String getCancelled_time() {
                return cancelled_time;
            }

            public void setCancelled_time(String cancelled_time) {
                this.cancelled_time = cancelled_time;
            }
        }

        public String getShare_url() {
            return share_url;
        }

        public void setShare_url(String share_url) {
            this.share_url = share_url;
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

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public int getOid() {
            return oid;
        }

        public void setOid(int oid) {
            this.oid = oid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
}
