package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class ReplyListResponse implements Serializable {

   @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ArrayList<ReplyListResponseData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<ReplyListResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<ReplyListResponseData> data) {
        this.data = data;
    }

    public class ReplyListResponseData implements Serializable{

        @SerializedName("uid")
        private int uid;

        @SerializedName("tid")
        private int tid;

        @SerializedName("tcid")
        private int tcid;

        @SerializedName("id")
        private int id;

        @SerializedName("avatar_url")
        private String avatar_url;

        @SerializedName("nickname")
        private String nickname;

        @SerializedName("openid")
        private String openid;

        @SerializedName("content")
        private String content;

        @SerializedName("object_content")
        private String object_content;

        @SerializedName("created_at")
        private String created_at;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getTcid() {
            return tcid;
        }

        public void setTcid(int tcid) {
            this.tcid = tcid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getObject_content() {
            return object_content;
        }

        public void setObject_content(String object_content) {
            this.object_content = object_content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
