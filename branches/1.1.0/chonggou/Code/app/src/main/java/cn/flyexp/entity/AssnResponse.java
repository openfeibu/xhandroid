package cn.flyexp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guo on 2016/10/13.
 */

public class AssnResponse implements Serializable{

    /**
     * code : 200
     * detail : 请求成功
     * data : {"association_sum":1,"activity_sum":1,"associations":[{"aid":1,"aname":"系统","avatar_url":"http://xhplus.feibu.info/fb/images/logo.png","member_number":0,"leader":"","label":"社联","activity_count":1,"introduction":"这个家伙很懒，什么都没留下。"}]}
     */

    private int code;
    private String detail;
    /**
     * association_sum : 1
     * activity_sum : 1
     * associations : [{"aid":1,"aname":"系统","avatar_url":"http://xhplus.feibu.info/fb/images/logo.png","member_number":0,"leader":"","label":"社联","activity_count":1,"introduction":"这个家伙很懒，什么都没留下。"}]
     */

    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int association_sum;
        private int activity_sum;
        /**
         * aid : 1
         * aname : 系统
         * avatar_url : http://xhplus.feibu.info/fb/images/logo.png
         * member_number : 0
         * leader :
         * label : 社联
         * activity_count : 1
         * introduction : 这个家伙很懒，什么都没留下。
         */

        private List<AssociationsBean> associations;

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

        public List<AssociationsBean> getAssociations() {
            return associations;
        }

        public void setAssociations(List<AssociationsBean> associations) {
            this.associations = associations;
        }

        public static class AssociationsBean {
            private int aid;
            private String aname;
            private String avatar_url;
            private int member_number;
            private String leader;
            private String label;
            private int activity_count;
            private String introduction;
            private int uid;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

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
        }
    }
}
