package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class WithdrawalRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("money")
    private float money;

    @SerializedName("pay_password")
    private String pay_password;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }
}
