package cn.flyexp.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by tanxinye on 2016/11/2.
 */
public class AssnListResponse extends BaseResponse {

    @SerializedName("data")
    private AssnListResponseData data;

    public AssnListResponseData getData() {
        return data;
    }

    public void setData(AssnListResponseData data) {
        this.data = data;
    }

    public class AssnListResponseData implements Serializable{

        @SerializedName("association_sum")
        private int association_sum;

        @SerializedName("activity_sum")
        private int activity_sum;

        @SerializedName("associations")
        private ArrayList<AssnResponseData> associations;

        public int getAssociation_sum() {
            return association_sum;
        }

        public void setAssociation_sum(int association_sum) {
            this.association_sum = association_sum;
        }

        public int getActivity_sum() {
            return activity_sum;
        }

        public void setActivity_sum(int activity_sum) {
            this.activity_sum = activity_sum;
        }

        public ArrayList<AssnResponseData> getAssociations() {
            return associations;
        }

        public void setAssociations(ArrayList<AssnResponseData> associations) {
            this.associations = associations;
        }

        public class AssnResponseData implements Serializable{

            @SerializedName("aid")
            private int aid;

            @SerializedName("aname")
            private String aname;

            @SerializedName("avatar_url")
            private String avatar_url;

            @SerializedName("member_number")
            private int member_number;

            @SerializedName("leader")
            private String leader;

            @SerializedName("label")
            private String label;

            @SerializedName("activity_count")
            private int activity_count;

            @SerializedName("introduction")
            private String introduction;

            @SerializedName("uid")
            private int uid;

            public int getAid() {
                return aid;
            }

            public void setAid(int aid) {
                this.aid = aid;
            }

            public String getAname() {
                return aname;
            }

            public void setAname(String aname) {
                this.aname = aname;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public int getMember_number() {
                return member_number;
            }

            public void setMember_number(int member_number) {
                this.member_number = member_number;
            }

            public String getLeader() {
                return leader;
            }

            public void setLeader(String leader) {
                this.leader = leader;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getActivity_count() {
                return activity_count;
            }

            public void setActivity_count(int activity_count) {
                this.activity_count = activity_count;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }
        }

    }

}
