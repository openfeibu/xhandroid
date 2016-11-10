package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/2.
 */
public class AssnJoinRequest extends BaseRequest {

    @SerializedName("token")
    private String token;

    @SerializedName("ar_username")
    private String ar_username;

    @SerializedName("causes")
    private String causes;

    @SerializedName("profession")
    private String profession;

    @SerializedName("mobile_no")
    private String mobile_no;

    @SerializedName("association_id")
    private int association_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAr_username() {
        return ar_username;
    }

    public void setAr_username(String ar_username) {
        this.ar_username = ar_username;
    }

    public String getCauses() {
        return causes;
    }

    public void setCauses(String causes) {
        this.causes = causes;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public int getAssociation_id() {
        return association_id;
    }

    public void setAssociation_id(int association_id) {
        this.association_id = association_id;
    }

}
