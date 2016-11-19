package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by txy on 2016/6/8 0008.
 */
public class TaskRequest implements Serializable {

    @SerializedName("page")
    private int page;

    /**
     * type	否	string	all（全部）,personal（校友任务）,business（商家任务）
     */
    @SerializedName("type")
    private String type;

    public static final String ALL = "all";
    public static final String PERSONAL = "personal";
    public static final String BUSINESS = "business";
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
