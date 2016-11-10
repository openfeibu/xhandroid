package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class WalletDetailResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<WalletDetailResponseData> data;

    public ArrayList<WalletDetailResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<WalletDetailResponseData> data) {
        this.data = data;
    }

    public class WalletDetailResponseData implements Serializable {

        @SerializedName("trade_type")
        private String trade_type;

        @SerializedName("time")
        private String time;

        @SerializedName("fee")
        private String fee;

        @SerializedName("service_fee")
        private String service_fee;

        @SerializedName("out_trade_no")
        private String out_trade_no;

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getService_fee() {
            return service_fee;
        }

        public void setService_fee(String service_fee) {
            this.service_fee = service_fee;
        }

        public String getOut_trade_no() {
            return out_trade_no;
        }

        public void setOut_trade_no(String out_trade_no) {
            this.out_trade_no = out_trade_no;
        }
    }
}
