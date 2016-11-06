package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by guo on 2016/6/10.
 */
public class ClientVerifyRequest {
    @SerializedName("version")
    private String version;
    @SerializedName("platform")
    private String plateform;
    @SerializedName("os")
    private String os;
    @SerializedName("brand")
    private String brand;
    @SerializedName("mid")
    private String mid;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPlateform() {
        return plateform;
    }

    public void setPlateform(String plateform) {
        this.plateform = plateform;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }
}
