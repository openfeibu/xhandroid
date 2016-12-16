package cn.flyexp.entity;

import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class PushThroughBean {

    private int refresh;
    private String target;
    private ArrayList<String> data;

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

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
