package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class AlipayInfoResponse extends BaseResponse {

    @SerializedName("data")
    private AlipayInfoResponseData data;

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
