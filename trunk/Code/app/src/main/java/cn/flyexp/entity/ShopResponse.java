package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/8/2 0002.
 */
public class ShopResponse implements Serializable {

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private ArrayList<ShopResponseData> data;

    @SerializedName("detail")
    private String detail;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<ShopResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<ShopResponseData> data) {
        this.data = data;
    }

    public class ShopResponseData implements Serializable {

        @SerializedName("sid")
        private int sid;

        @SerializedName("shop_name")
        private String shop_name;

        @SerializedName("shop_image")
        private String shop_image;

        @SerializedName("shop_intruduction")
        private String shop_intruduction;

        @SerializedName("shop_favorite")
        private int shop_favorite;

        @SerializedName("shop_view_num")
        private int shop_view_num;

        @SerializedName("shop_tag")
        private String shop_tag;


        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_image() {
            return shop_image;
        }

        public void setShop_image(String shop_image) {
            this.shop_image = shop_image;
        }

        public String getShop_intruduction() {
            return shop_intruduction;
        }

        public void setShop_intruduction(String shop_intruduction) {
            this.shop_intruduction = shop_intruduction;
        }

        public int getShop_favorite() {
            return shop_favorite;
        }

        public void setShop_favorite(int shop_favorite) {
            this.shop_favorite = shop_favorite;
        }

        public int getShop_view_num() {
            return shop_view_num;
        }

        public void setShop_view_num(int shop_view_num) {
            this.shop_view_num = shop_view_num;
        }

        public String getShop_tag() {
            return shop_tag;
        }

        public void setShop_tag(String shop_tag) {
            this.shop_tag = shop_tag;
        }
    }

}
