package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by txy on 2016/6/15 0015.
 */
public class FinishOrderRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("order_id")
    private int order_id;

    @SerializedName("pay_password")
    private String pay_password;

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
