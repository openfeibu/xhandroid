package cn.flyexp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by txy on 2016/8/20 0020.
 */
public class TopicResponseData implements Serializable {
    /**
     * tid : 526
     * type : 随心写
     * content : 不苟且的当前，就是诗和远方
     * img : http://xhplus.feibu.info/uploads/topic/6c5c031b77b34144e63ac3c5f8e76bea.jpg,http://xhplus.feibu.info/uploads/topic/c36ef5bfd068f7f817028703c33ea9e7.jpg,http://xhplus.feibu.info/uploads/topic/834025f5c3692115d45acc64fde43cd5.jpg,http://xhplus.feibu.info/uploads/topic/daf35b3febc3e6c7b719e90b9b1f634f.jpg,http://xhplus.feibu.info/uploads/topic/6b466a29fdbc0c44e01ac7feb3721fb3.jpg,http://xhplus.feibu.info/uploads/topic/4c062b26289a2953bf9fd9bc2fdb8bee.jpg,http://xhplus.feibu.info/uploads/topic/67a56d13a86e2fad98a00354487cc484.jpg,http://xhplus.feibu.info/uploads/topic/106a694f88f96641cbeeb7164f0d0f23.jpg,http://xhplus.feibu.info/uploads/topic/cf43f5d279741e4d22a02eb98b9f1dd9.jpg
     * thumb : http://xhplus.feibu.info/uploads/topic/thumb/6c5c031b77b34144e63ac3c5f8e76bea.jpg,http://xhplus.feibu.info/uploads/topic/thumb/c36ef5bfd068f7f817028703c33ea9e7.jpg,http://xhplus.feibu.info/uploads/topic/thumb/834025f5c3692115d45acc64fde43cd5.jpg,http://xhplus.feibu.info/uploads/topic/thumb/daf35b3febc3e6c7b719e90b9b1f634f.jpg,http://xhplus.feibu.info/uploads/topic/thumb/6b466a29fdbc0c44e01ac7feb3721fb3.jpg,http://xhplus.feibu.info/uploads/topic/thumb/4c062b26289a2953bf9fd9bc2fdb8bee.jpg,http://xhplus.feibu.info/uploads/topic/thumb/67a56d13a86e2fad98a00354487cc484.jpg,http://xhplus.feibu.info/uploads/topic/thumb/106a694f88f96641cbeeb7164f0d0f23.jpg,http://xhplus.feibu.info/uploads/topic/thumb/cf43f5d279741e4d22a02eb98b9f1dd9.jpg
     * view_num : 231
     * comment_num : 2
     * favourites_count : 4
     * created_at : 2016-09-25 22:01:10
     * openid : 1ae7cfe9378624fc02683a397069e010
     * nickname : Eunice
     * avatar_url : http://xhplus.feibu.info/uploads/avatar/9e4571edbf44b253ec94092402a9a04d.jpg
     * favorited : 0
     * comment : [{"uid":79,"tcid":630,"openid":"814427bd6b9f2c27ff2c41ff5171b416","nickname":"张柳科","avatar_url":"http://xhplus.feibu.info/uploads/avatar/01bff239e02de7d1485cdfbf295c4227.jpg","content":"你也好美","favorited":0,"favourites_count":0,"created_at":"2016-09-25 23:24:04","be_review_id":0,"be_review_username":""}]
     */

    private int tid;
    private String type;
    private String content;
    private String img;
    private String thumb;
    private int view_num;
    private int comment_num;
    private int favourites_count;
    private String created_at;
    private String openid;
    private String nickname;
    private String avatar_url;
    private int favorited;
    /**
     * uid : 79
     * tcid : 630
     * openid : 814427bd6b9f2c27ff2c41ff5171b416
     * nickname : 张柳科
     * avatar_url : http://xhplus.feibu.info/uploads/avatar/01bff239e02de7d1485cdfbf295c4227.jpg
     * content : 你也好美
     * favorited : 0
     * favourites_count : 0
     * created_at : 2016-09-25 23:24:04
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

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
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

//    @SerializedName("tid")
//    private int tid;
//
//    @SerializedName("type")
//    private String type;
//
//    @SerializedName("content")
//    private String content;
//
//    @SerializedName("img")
//    private String img;
//
//    @SerializedName("thumb")
//    private String thumb;
//
//    @SerializedName("view_num")
//    private int view_num;
//
//    @SerializedName("comment_num")
//    private int comment_num;
//
//    @SerializedName("favourites_count")
//    private int favourites_count;
//
//    @SerializedName("created_at")
//    private String created_at;
//
//    @SerializedName("openid")
//    private String openid;
//
//    @SerializedName("nickname")
//    private String nickname;
//
//    @SerializedName("avatar_url")
//    private String avatar_url;
//
//    @SerializedName("favorited")
//    private int favorited;
//
//    public String getThumb() {
//        return thumb;
//    }
//
//    public void setThumb(String thumb) {
//        this.thumb = thumb;
//    }
//
//    public int getTid() {
//        return tid;
//    }
//
//    public void setTid(int tid) {
//        this.tid = tid;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public int getView_num() {
//        return view_num;
//    }
//
//    public void setView_num(int view_num) {
//        this.view_num = view_num;
//    }
//
//    public int getComment_num() {
//        return comment_num;
//    }
//
//    public void setComment_num(int comment_num) {
//        this.comment_num = comment_num;
//    }
//
//    public int getFavourites_count() {
//        return favourites_count;
//    }
//
//    public void setFavourites_count(int favourites_count) {
//        this.favourites_count = favourites_count;
//    }
//
//    public String getCreated_at() {
//        return created_at;
//    }
//
//    public void setCreated_at(String created_at) {
//        this.created_at = created_at;
//    }
//
//    public String getOpenid() {
//        return openid;
//    }
//
//    public void setOpenid(String openid) {
//        this.openid = openid;
//    }
//
//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
//
//    public String getAvatar_url() {
//        return avatar_url;
//    }
//
//    public void setAvatar_url(String avatar_url) {
//        this.avatar_url = avatar_url;
//    }
//
//    public int getFavorited() {
//        return favorited;
//    }
//
//    public void setFavorited(int favorited) {
//        this.favorited = favorited;
//    }



}
