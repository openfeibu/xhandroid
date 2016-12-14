package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MessageResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<MessageResponseData> data;

    public ArrayList<MessageResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<MessageResponseData> data) {
        this.data = data;
    }

    public class MessageResponseData implements Serializable {

        @SerializedName("mid")
        private int mid;

        @SerializedName("name")
        private String name;

        @SerializedName("content")
        private String content;

        @SerializedName("created_at")
        private String created_at;

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
