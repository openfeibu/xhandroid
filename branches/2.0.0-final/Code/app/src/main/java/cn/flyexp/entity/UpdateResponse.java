package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class UpdateResponse extends BaseResponse {

    @SerializedName("data")
    private UpdateResponseData data;

    public UpdateResponseData getData() {
        return data;
    }

    public void setData(UpdateResponseData data) {
        this.data = data;
    }

    public class UpdateResponseData implements Serializable {

        @SerializedName("id")
        private int id;

        @SerializedName("code")
        private int code;

        @SerializedName("name")
        private String name;

        @SerializedName("detail")
        private String detail;

        @SerializedName("download")
        private String download;

        @SerializedName("compulsion")
        private int compulsion;

        public int getCompulsion() {
            return compulsion;
        }

        public void setCompulsion(int compulsion) {
            this.compulsion = compulsion;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }
    }
}
