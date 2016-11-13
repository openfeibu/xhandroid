package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/8/21 0021.
 */
public class WalletResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("detail")
    private String detail;

    @SerializedName("data")
    private WalletResponseData data;

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

    public WalletResponseData getData() {
        return data;
    }

    public void setData(WalletResponseData data) {
        this.data = data;
    }

    public class WalletResponseData implements Serializable{

        @SerializedName("wallet")
        private float wallet;

        @SerializedName("is_alipay")
        private int is_alipay;

        @SerializedName("is_paypassword")
        private int is_paypassword;

        @SerializedName("alipay")
        private String alipay;

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public float getWallet() {
            return wallet;
        }

        public void setWallet(float wallet) {
            this.wallet = wallet;
        }

        public int getIs_alipay() {
            return is_alipay;
        }

        public void setIs_alipay(int is_alipay) {
            this.is_alipay = is_alipay;
        }

        public int getIs_paypassword() {
            return is_paypassword;
        }

        public void setIs_paypassword(int is_paypassword) {
            this.is_paypassword = is_paypassword;
        }
    }
}
