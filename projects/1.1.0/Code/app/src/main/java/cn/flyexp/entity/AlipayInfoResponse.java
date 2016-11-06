package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/24 0024.
 */
public class AlipayInfoResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private AlipayInfoResponseData data;

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

    public AlipayInfoResponseData getData() {
        return data;
    }

    public void setData(AlipayInfoResponseData data) {
        this.data = data;
    }

    public class AlipayInfoResponseData implements Serializable {

        @SerializedName("alipay")
        private String alipay;

        @SerializedName("alipay_name")
        private String alipay_name;

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
    }
}
