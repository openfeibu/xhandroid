package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/8.
 */
public class TaskPublishRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("destination")
    private String destination;

    @SerializedName("description")
    private String description;

    @SerializedName("fee")
    private float fee;

    @SerializedName("goods_fee")
    private float goods_fee;

    @SerializedName("phone")
    private String phone;

    @SerializedName("pay_id")
    private int pay_id;

    @SerializedName("pay_password")
    private String pay_password;

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public void setGoods_fee(int goods_fee) {
        this.goods_fee = goods_fee;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public float getFee() {
        return fee;
    }

    public void setFee(float fee) {
        this.fee = fee;
    }

    public float getGoods_fee() {
        return goods_fee;
    }

    public void setGoods_fee(float goods_fee) {
        this.goods_fee = goods_fee;
    }

    public int getPay_id() {
        return pay_id;
    }

    public void setPay_id(int pay_id) {
        this.pay_id = pay_id;
    }
}
