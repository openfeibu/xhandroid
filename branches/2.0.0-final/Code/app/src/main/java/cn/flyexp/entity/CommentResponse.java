package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/11/21.
 */
public class CommentResponse extends BaseResponse {

    @SerializedName("data")
    private ArrayList<TopicResponseData.CommentResponseData> data;

    public ArrayList<TopicResponseData.CommentResponseData> getData() {
        return data;
    }

    public void setData(ArrayList<TopicResponseData.CommentResponseData> data) {
        this.data = data;
    }
}
