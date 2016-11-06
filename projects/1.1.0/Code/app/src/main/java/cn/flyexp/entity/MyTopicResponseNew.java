package cn.flyexp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */

public class MyTopicResponseNew implements Serializable {

    /**
     * code : 200
     * detail : 请求成功
     * data : [{"tid":599,"type":"随心写","content":"test","img":"http://xh.feibu.info//uploads/topic/eb3d6128279cc21a16ac7b6d5d4769c2.jpg","view_num":8,"comment_num":1,"favourites_count":1,"created_at":"2016-10-14 09:10:53","deleted_at":null,"is_deleted":0,"admin_deleted":0,"nickname":"小郭","avatar_url":"http://xhplus.feibu.info/uploads/avatar/7b93ee579952c432019a05a896b0b7fe.jpg","comment":[{"uid":91,"tcid":675,"openid":"75db6cf52237fb69a7b58347879ed7b0","nickname":"小郭","avatar_url":"http://xhplus.feibu.info/uploads/avatar/7b93ee579952c432019a05a896b0b7fe.jpg","content":"评论你","favorited":0,"favourites_count":0,"created_at":"2016-10-14 09:21:32","be_review_id":0,"be_review_username":""}]}]
     */

    private int code;
    private String detail;
    /**
     * tid : 599
     * type : 随心写
     * content : test
     * img : http://xh.feibu.info//uploads/topic/eb3d6128279cc21a16ac7b6d5d4769c2.jpg
     * view_num : 8
     * comment_num : 1
     * favourites_count : 1
     * created_at : 2016-10-14 09:10:53
     * deleted_at : null
     * is_deleted : 0
     * admin_deleted : 0
     * nickname : 小郭
     * avatar_url : http://xhplus.feibu.info/uploads/avatar/7b93ee579952c432019a05a896b0b7fe.jpg
     * comment : [{"uid":91,"tcid":675,"openid":"75db6cf52237fb69a7b58347879ed7b0","nickname":"小郭","avatar_url":"http://xhplus.feibu.info/uploads/avatar/7b93ee579952c432019a05a896b0b7fe.jpg","content":"评论你","favorited":0,"favourites_count":0,"created_at":"2016-10-14 09:21:32","be_review_id":0,"be_review_username":""}]
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int tid;
        private String type;
        private String content;
        private String img;
        private int view_num;
        private int comment_num;
        private int favourites_count;
        private String created_at;
        private Object deleted_at;
        private int is_deleted;
        private int admin_deleted;
        private String nickname;
        private String avatar_url;
        /**
         * uid : 91
         * tcid : 675
         * openid : 75db6cf52237fb69a7b58347879ed7b0
         * nickname : 小郭
         * avatar_url : http://xhplus.feibu.info/uploads/avatar/7b93ee579952c432019a05a896b0b7fe.jpg
         * content : 评论你
         * favorited : 0
         * favourites_count : 0
         * created_at : 2016-10-14 09:21:32
         * be_review_id : 0
         * be_review_username :
         */

        private List<CommentBean> comment;

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getView_num() {
            return view_num;
        }

        public void setView_num(int view_num) {
            this.view_num = view_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getFavourites_count() {
            return favourites_count;
        }

        public void setFavourites_count(int favourites_count) {
            this.favourites_count = favourites_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public int getIs_deleted() {
            return is_deleted;
        }

        public void setIs_deleted(int is_deleted) {
            this.is_deleted = is_deleted;
        }

        public int getAdmin_deleted() {
            return admin_deleted;
        }

        public void setAdmin_deleted(int admin_deleted) {
            this.admin_deleted = admin_deleted;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar_url() {
            return avatar_url;
        }

        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        public static class CommentBean {
            private int uid;
            private int tcid;
            private String openid;
            private String nickname;
            private String avatar_url;
            private String content;
            private int favorited;
            private int favourites_count;
            private String created_at;
            private int be_review_id;
            private String be_review_username;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getTcid() {
                return tcid;
            }

            public void setTcid(int tcid) {
                this.tcid = tcid;
            }

            public String getOpenid() {
                return openid;
            }

            public void setOpenid(String openid) {
                this.openid = openid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar_url() {
                return avatar_url;
            }

            public void setAvatar_url(String avatar_url) {
                this.avatar_url = avatar_url;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getFavorited() {
                return favorited;
            }

            public void setFavorited(int favorited) {
                this.favorited = favorited;
            }

            public int getFavourites_count() {
                return favourites_count;
            }

            public void setFavourites_count(int favourites_count) {
                this.favourites_count = favourites_count;
            }

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public int getBe_review_id() {
                return be_review_id;
            }

            public void setBe_review_id(int be_review_id) {
                this.be_review_id = be_review_id;
            }

            public String getBe_review_username() {
                return be_review_username;
            }

            public void setBe_review_username(String be_review_username) {
                this.be_review_username = be_review_username;
            }
        }
    }
}
