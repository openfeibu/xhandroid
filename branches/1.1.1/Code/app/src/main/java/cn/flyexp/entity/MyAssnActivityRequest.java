package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class MyAssnActivityRequest extends BaseRequest {

    @SerializedName("page")
    private int page;

    @SerializedName("association_id")
    private int association_id;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getAssociation_id() {
        return association_id;
    }

    public void setAssociation_id(int association_id) {
        this.association_id = association_id;
    }
}
