package cn.flyexp.window.topic;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.TopicCommentAdapter;
import cn.flyexp.adapter.TopicPicAdapter;
import cn.flyexp.callback.topic.TopicDetailCallback;
import cn.flyexp.constants.Constants;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommentResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.topic.TopicDetailPresenter;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/12/18.
 */
public class TopicDetailWindow extends BaseWindow implements TopicDetailCallback.ResponseCallback {

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_nickname)
    TextView tvNickName;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_detail)
    TextView tvDetail;
    @InjectView(R.id.tv_content)
    TextView tvContent;
    @InjectView(R.id.rv_topicpic)
    RecyclerView rvTopicPic;
    @InjectView(R.id.rv_comment)
    LoadMoreRecyclerView rvComment;
    @InjectView(R.id.tv_like)
    TextView tvLike;
    @InjectView(R.id.tv_delete)
    TextView tvDelete;

    private TopicResponseData data;
    private TopicDetailPresenter topicDetailPresenter;
    private PopupWindow popupWindow;
    private EditText edtComment;
    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<TopicResponseData.CommentResponseData> comments = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.window_topic_detail;
    }

    public TopicDetailWindow(Bundle bundle) {
        topicDetailPresenter = new TopicDetailPresenter(this);
        data = (TopicResponseData) bundle.getSerializable("topicDetail");
        initView();
    }

    private void initView() {
        tvNickName.setText(data.getNickname());
        tvContent.setText(data.getContent().trim());
        tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(data.getCreated_at())));
        tvDetail.setText(String.format(getResources().getString(R.string.format_topic_detail),
                String.valueOf(data.getView_num()), String.valueOf(data.getComment_num()),
                String.valueOf(data.getFavourites_count())));
        Glide.with(getContext()).load(data.getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgAvatar);

        imgs.addAll(Arrays.asList(splitImageUrl(data.getImg())));
        TopicPicAdapter topicPicAdapter = new TopicPicAdapter(getContext(), imgs);
        topicPicAdapter.setOnItemClickLinstener(new TopicPicAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(int position) {
                Bundle bundle = new Bundle();
                ArrayList<String> imageUrl = new ArrayList<>();
                imageUrl.addAll(imgs);
                bundle.putStringArrayList("uri", imageUrl);
                bundle.putInt("position", position);
                bundle.putString("type", Constants.NET);
                openWindow(WindowIDDefine.WINDOW_PICBROWSER, bundle);
            }
        });
        rvTopicPic.setAdapter(topicPicAdapter);
        rvTopicPic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        TopicCommentAdapter topicCommentAdapter = new TopicCommentAdapter(getContext(), comments);
        topicCommentAdapter.setOnItemClickLinstener(new TopicCommentAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {

            }
        });
        rvComment.setAdapter(topicCommentAdapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComment.addItemDecoration(new DividerItemDecoration(getContext()));
        rvComment.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {

            }
        });

        View popLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_topic_comment, null);
        edtComment = (EditText) popLayout.findViewById(R.id.edt_comment);

        popupWindow = new PopupWindow(popLayout,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1f);
                toggleKeyboard();
            }
        });

        String openId = SharePresUtil.getString(SharePresUtil.KEY_OPENID);
        if (TextUtils.equals(data.getOpenid(), openId)) {
            tvDelete.setVisibility(VISIBLE);
        }else{
            tvDelete.setVisibility(GONE);
        }
    }

    public String[] splitImageUrl(String imgUrl) {
        return imgUrl.split("\\,");
    }

    private void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick({R.id.img_back, R.id.layout_comment, R.id.layout_like})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.layout_comment:
                popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
                changeWindowAlpha(0.7f);
                toggleKeyboard();
                break;
            case R.id.layout_like:
                readyTopicLike();
                break;
        }
    }

    private void changeWindowAlpha(float v) {
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    private void readyDeleteComment() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
            deleteCommentRequest.setToken(token);
//            deleteCommentRequest.setTopic_id(datas.get(currPosition).getTid());
//            deleteCommentRequest.setComment_id(tcid);
            topicDetailPresenter.requestDeleteComment(deleteCommentRequest);
        }
    }

    private void readyDeleteTopic() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest();
            deleteTopicRequest.setToken(token);
            deleteTopicRequest.setTopic_id(data.getTid());
            topicDetailPresenter.requestDeleteTopic(deleteTopicRequest);
        }
    }

    private void readyTopicLike() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            ThumbUpRequest thumbUpRequest = new ThumbUpRequest();
            thumbUpRequest.setToken(token);
            thumbUpRequest.setTopic_id(data.getTid());
            topicDetailPresenter.requestThumbUp(thumbUpRequest);
        }
    }

    private void readyComment() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setToken(token);
//            commentRequest.setTopic_comment(comment);
//            commentRequest.setTopic_id(datas.get(currPosition).getTid());
//            commentRequest.setComment_id(data);
//            topicPresenter.requestComment(commentRequest);
        }
    }

    @Override
    public void responseComment(CommentResponse response) {

    }

    @Override
    public void responseThumbUp(BaseResponse response) {

    }

    @Override
    public void responseDeleteComment(BaseResponse response) {

    }

    @Override
    public void responseDeleteTopic(BaseResponse response) {

    }
}
