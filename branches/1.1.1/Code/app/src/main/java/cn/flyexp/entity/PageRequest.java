package cn.flyexp.entity;

import android.graphics.pdf.PdfDocument;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class PageRequest extends BaseRequest{

    @SerializedName("page")
    private int page;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public PageRequest(int page) {
        this.page = page;
    }
    public PageRequest() {
        this.page = page;
    }
}
