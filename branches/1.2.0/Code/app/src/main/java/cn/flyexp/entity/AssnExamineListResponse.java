package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/14.
 */
public class AssnExamineListResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private ArrayList<AssnExamineListResponse.AssnExamineListResponseData> data;


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

    public ArrayList<AssnExamineListResponse.AssnExamineListResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<AssnExamineListResponse.AssnExamineListResponseData> data) {
        this.data = data;
    }

   public class AssnExamineListResponseData implements Serializable {

        @SerializedName("id")
        private int id;

        @SerializedName("uid")
        private int uid;

        @SerializedName("aid")
        private int aid;

        @SerializedName("causes")
        private String causes;

        @SerializedName("ar_username")
        private String ar_username;

        @SerializedName("profession")
        private String profession;

       @SerializedName("mobile_no")
       private String mobile_no;

        @SerializedName("status")
        private String status;

        @SerializedName("deleted_at")
        private String deleted_at;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("updated_at")
        private String updated_at;

       @SerializedName("avatar_url")
       private String avatar_url;

       public String getAvatar_url() {
           return avatar_url;
       }

       public void setAvatar_url(String avatar_url) {
           this.avatar_url = avatar_url;
       }

       public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getCauses() {
            return causes;
        }

        public void setCauses(String causes) {
            this.causes = causes;
        }

        public String getAr_username() {
            return ar_username;
        }

        public void setAr_username(String ar_username) {
            this.ar_username = ar_username;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

       public String getMobile_no() {
           return mobile_no;
       }

       public void setMobile_no(String mobile_no) {
           this.mobile_no = mobile_no;
       }

       public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
