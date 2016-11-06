package cn.flyexp.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by txy on 2016/8/5 0005.
 */
public class PicBrowserBean implements Serializable {

    private ArrayList<String> imgUrl;

    private int curSelectedIndex;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String> getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(ArrayList<String> imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getCurSelectedIndex() {
        return curSelectedIndex;
    }

    public void setCurSelectedIndex(int curSelectedIndex) {
        this.curSelectedIndex = curSelectedIndex;
    }
}
