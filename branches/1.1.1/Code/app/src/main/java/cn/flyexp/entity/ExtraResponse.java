package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class ExtraResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<ExtraResponseData> data;

    public ArrayList<ExtraResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<ExtraResponseData> data) {
        this.data = data;
    }

    public class ExtraResponseData implements Serializable {

        @SerializedName("extra")
        private String extra;

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        @SerializedName("created_at")
        private String created_at;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

}
