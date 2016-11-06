package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/8/20 0020.
 */
public class IntegralRecordResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private ArrayList<IntegralRecordResponseData> data;

    public ArrayList<IntegralRecordResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<IntegralRecordResponseData> data) {
        this.data = data;
    }

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

    public class IntegralRecordResponseData implements Serializable{

        @SerializedName("score")
        private String score;

        @SerializedName("obtain_type")
        private String obtain_type;

        @SerializedName("updated_at")
        private String updated_at;


        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getObtain_type() {
            return obtain_type;
        }

        public void setObtain_type(String obtain_type) {
            this.obtain_type = obtain_type;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
