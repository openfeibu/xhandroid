package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class TopicListResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<TopicResponseData> data;

    public ArrayList<TopicResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<TopicResponseData> data) {
        this.data = data;
    }
}
