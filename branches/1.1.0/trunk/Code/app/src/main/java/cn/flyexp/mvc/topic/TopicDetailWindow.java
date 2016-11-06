package cn.flyexp.mvc.topic;

import android.graphics.Bitmap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import cn.flyexp.R;
import cn.flyexp.adapter.CommentAdapter;
import cn.flyexp.adapter.PicDisPlayAdapter;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicCommentRequest;
import cn.flyexp.entity.TopicCommentResponse;
import cn.flyexp.entity.TopicRequest;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;


/**
 * Created by txy on 2016/6/6.
 */
public class TopicDetailWindow extends AbstractWindow implements View.OnClickListener {

    private TopicViewCallBack callBack;
    private PicDisPlayAdapter picAdapter;
    private RoundImageView iv_avatar;
    private RecyclerView rv_pic;
    private TextView tv_name;
    private TextView tv_time;
    private TextView tv_content;
    private ImageView iv_labeltype;
    private TextView tv_look;
    private TextView tv_comment;
    private TextView tv_good;
    private ArrayList<String> imgUrlList = new ArrayList<String>();
    private ArrayList<String> thumbUrlList = new ArrayList<String>();
    private int topicId;
    private CommentAdapter commentAdapter;
    private ArrayList<TopicCommentResponse.TopicCommentResponseData> data = new ArrayList<>();
    private EditText et_comment;
    private int commentId = 0;
    private String topicPpenId;
    private ImageView iv_like;

    public TopicDetailWindow(TopicViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_topic_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.contentLayout).setOnClickListener(this);
        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_content = (TextView) findViewById(R.id.tv_content);
        iv_labeltype = (ImageView) findViewById(R.id.iv_labeltype);
        tv_look = (TextView) findViewById(R.id.tv_look);
        tv_comment = (TextView) findViewById(R.id.tv_comment);
        tv_good = (TextView) findViewById(R.id.tv_good);
        et_comment = (EditText) findViewById(R.id.et_comment);
        iv_like = (ImageView) findViewById(R.id.iv_like);

        picAdapter = new PicDisPlayAdapter(getContext(), thumbUrlList);
        picAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PicBrowserBean picBrowserBean = new PicBrowserBean();
                picBrowserBean.setImgUrl(imgUrlList);
                picBrowserBean.setCurSelectedIndex(position);
                picBrowserBean.setType(1);
                callBack.picBrowserEnter(picBrowserBean);
            }
        });
        rv_pic = (RecyclerView) findViewById(R.id.rv_pic);
        rv_pic.setLayoutManager(new GridLayoutManager(getContext(), 3));

        rv_pic.setAdapter(picAdapter);
        rv_pic.setItemAnimator(new DefaultItemAnimator());
        commentAdapter = new CommentAdapter(getContext(), data, callBack);
        commentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                commentId = data.get(position).getTcid();
                et_comment.setHint("评论" + data.get(position).getNickname());
            }
        });
        final RecyclerView rv_comment = (RecyclerView) findViewById(R.id.rv_comment);
        rv_comment.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_comment.setAdapter(commentAdapter);
        rv_comment.setItemAnimator(new DefaultItemAnimator());
        rv_comment.setNestedScrollingEnabled(false);
        rv_comment.setHasFixedSize(false);

    }

    public void detailRequest(int topicId) {
        TopicRequest topicRequest = new TopicRequest();
        topicRequest.setTopic_id(topicId);
        callBack.getTopic(topicRequest);
    }

    public void responseData(final TopicResponseData responseData) {
        if (responseData == null) {
            return;
        }
        topicId = responseData.getTid();
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 32), CommonUtil.dip2px(getContext(), 32))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(getContext().getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(iv_avatar);
        } else {
            iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
        topicPpenId = responseData.getOpenid();
        iv_avatar.setOnClickListener(this);
        tv_name.setText(responseData.getNickname());
        tv_name.setOnClickListener(this);
        tv_time.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData
                .getCreated_at())));
        tv_content.setText(responseData.getContent());
        if (responseData.getImg() == null || responseData.getImg().equals("") || responseData.getThumb() == null || responseData.getThumb().equals("")) {
            rv_pic.setVisibility(View.GONE);
        } else {
            String[] thumbUrls = CommonUtil.splitImageUrl(responseData.getThumb());
            String[] urls = CommonUtil.splitImageUrl(responseData.getImg());
            rv_pic.setVisibility(View.VISIBLE);
            imgUrlList.addAll(Arrays.asList(urls));
            thumbUrlList.addAll(Arrays.asList(thumbUrls));
            picAdapter.notifyDataSetChanged();
        }
        tv_content.setText(responseData.getContent());
        tv_look.setText("浏览" + responseData.getView_num());
        tv_comment.setText("评论" + responseData.getComment_num());
        tv_good.setText("点赞" + responseData.getFavourites_count());
        iv_labeltype.setImageResource(typeImage(responseData.getType()));
        final int curLike = responseData.getFavorited();
        iv_like.setImageResource(curLike == 1 ? R.mipmap.icon_like_sel : R.mipmap.icon_like_nor);
        if (curLike == 0) {
            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ThumbUpRequest thumbUpRequest = new ThumbUpRequest();
                    thumbUpRequest.setTopic_id(topicId);
                    callBack.thumbUp(thumbUpRequest);
                    iv_like.setImageResource(R.mipmap.icon_like_sel);
                    v.setEnabled(false);
                    tv_good.setText("点赞" + (responseData.getFavourites_count() + 1));
                }
            });
        }
        commentRefresh();
    }

    public TopicCommentRequest getCommentRequest() {
        TopicCommentRequest topicCommentRequest = new TopicCommentRequest();
        topicCommentRequest.setPage(1);
        topicCommentRequest.setTopic_id(topicId);
        return topicCommentRequest;
    }

    public void commentListResponse(ArrayList<TopicCommentResponse.TopicCommentResponseData> responseDatas) {
        data.clear();
        data.addAll(responseDatas);
        commentAdapter.notifyDataSetChanged();
        et_comment.setHint("评论一下下");
        et_comment.setText("");
        commentId = 0;
    }

    public void commentRefresh() {
        callBack.getComment(getCommentRequest());
        et_comment.setHint("评论一下下");
        et_comment.setText("");
        commentId = 0;
    }

    private int typeImage(String type) {
        int imgId = -1;
        if (type.equals("随心写")) {
            imgId = R.mipmap.icon_random;
        } else if (type.equals("新鲜事")) {
            imgId = R.mipmap.icon_new;
        } else if (type.equals("一起约")) {
            imgId = R.mipmap.icon_appionment;
        } else if (type.equals("帮帮忙")) {
            imgId = R.mipmap.icon_help;
        } else if (type.equals("吐吐槽")) {
            imgId = R.mipmap.icon_teasing;
        } else if (type.equals("问一下")) {
            imgId = R.mipmap.icon_question;
        }
        return imgId;
    }

    public void response() {
        findViewById(R.id.btn_send).setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.contentLayout:
                et_comment.setHint("评论一下下");
                et_comment.setText("");
                commentId = 0;
                break;
            case R.id.iv_avatar:
            case R.id.tv_name:
                callBack.taWindowEnter(topicPpenId);
                break;
            case R.id.btn_send:
                String comment = et_comment.getText().toString().trim();
                if (comment == null || comment.equals("")) {
                    return;
                }
                String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                CommentRequest commentRequest = new CommentRequest();
                commentRequest.setToken(token);
                if (topicId != 0) {
                    commentRequest.setTopic_id(topicId);
                }
                commentRequest.setTopic_comment(comment);
                commentRequest.setComment_id(commentId);
                callBack.commentTopic(commentRequest);
                v.setEnabled(false);
                break;
        }
    }

}
