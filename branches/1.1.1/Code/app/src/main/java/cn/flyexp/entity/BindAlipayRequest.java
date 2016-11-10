package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class BindAlipayRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("alipay")
    private String alipay;

    @SerializedName("alipay_name")
    private String alipay_name;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
