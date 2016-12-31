package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class PushThroughBean {

    @SerializedName("refresh")
    private int refresh;
    @SerializedName("target")
    private String target;

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
