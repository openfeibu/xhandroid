package cn.flyexp.window.topic;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
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
import butterknife.OnLongClick;
import cn.flyexp.R;
import cn.flyexp.adapter.TopicCommentAdapter;
import cn.flyexp.adapter.TopicPicAdapter;
import cn.flyexp.callback.topic.TopicDetailCallback;
import cn.flyexp.constants.Constants;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CommentListRequest;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommentResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.ControllerManager;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.topic.TopicDetailPresenter;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

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
    private EditText edtComment;
    private ArrayList<String> imgs = new ArrayList<>();
    private ArrayList<String> tImgs = new ArrayList<>();
    private ArrayList<TopicResponseData.CommentResponseData> comments = new ArrayList<>();
    private TopicCommentAdapter topicCommentAdapter;
    private boolean isRefresh;
    private int commentPage = 1;
    private SweetAlertDialog dialog;
    private int tcid;
    private Drawable[] likeDrawable = new Drawable[]{getResources().getDrawable(R.mipmap.icon_top_like_nor),
            getResources().getDrawable(R.mipmap.icon_top_like_sel)};
    private String comment;
    private int deletePosition;
    private AlertDialog commentDialog;
    private View popLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.window_topic_detail;
    }

    public TopicDetailWindow(Bundle bundle) {
        data = (TopicResponseData) bundle.getSerializable("topicDetail");
        if (data == null) {
            showToast(R.string.data_unable);
            return;
        }
        topicDetailPresenter = new TopicDetailPresenter(this);
        initView();
        readyCommentList();
    }

    private void initView() {
        tvNickName.setText(data.getNickname());
        tvContent.setText(getClickableHtml(data.getContent().trim()));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(data.getCreated_at())));
        updateDetail();
        Glide.with(getContext()).load(data.getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgAvatar);

        if (!TextUtils.isEmpty(data.getImg())) {
            imgs.addAll(Arrays.asList(splitImageUrl(data.getImg())));
        }
        if (TextUtils.isEmpty(data.getThumb())) {
            rvTopicPic.setVisibility(GONE);
        } else {
            tImgs.addAll(Arrays.asList(splitImageUrl(data.getThumb())));
        }
        TopicPicAdapter topicPicAdapter = new TopicPicAdapter(getContext(), tImgs);
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

        topicCommentAdapter = new TopicCommentAdapter(getContext(), comments);
        topicCommentAdapter.setOnItemClickLinstener(new TopicCommentAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                tcid = comments.get(position).getTcid();
                edtComment.setText("");
                edtComment.setHint("??????@" + comments.get(position).getNickname());
                showCommentPublish();
            }

            @Override
            public void onDelete(int position) {
                deletePosition = position;
                DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_delete_comment), getResources().getString(R.string.comfirm), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        tcid = comments.get(deletePosition).getTcid();
                        readyDeleteComment();
                        dismissProgressDialog(sweetAlertDialog);
                        dialog.show();
                    }
                });
            }
        });
        rvComment.setAdapter(topicCommentAdapter);
        rvComment.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComment.addItemDecoration(new DividerItemDecoration(getContext()));
        rvComment.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                commentPage++;
                readyCommentList();
            }
        });

        popLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_topic_comment, null);
        popLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        edtComment = (EditText) popLayout.findViewById(R.id.edt_comment);
        popLayout.findViewById(R.id.tv_publish).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                comment = edtComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    showToast(R.string.hint_comment_null);
                } else {
                    readyComment();
                }
                toggleKeyboard();
                commentDialog.dismiss();
            }
        });

        likeDrawable[0].setBounds(0, 0, likeDrawable[0].getMinimumWidth(), likeDrawable[0].getMinimumHeight());
        likeDrawable[1].setBounds(0, 0, likeDrawable[1].getMinimumWidth(), likeDrawable[1].getMinimumHeight());
        updateLike(data.getFavorited() == 1);

        dialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));

        String openId = SharePresUtil.getString(SharePresUtil.KEY_OPENID);
        if (TextUtils.equals(data.getOpenid(), openId)) {
            tvDelete.setVisibility(VISIBLE);
        } else {
            tvDelete.setVisibility(GONE);
        }
    }

    private String[] splitImageUrl(String imgUrl) {
        return imgUrl.split("\\,");
    }

    private void toggleKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void updateLike(boolean isLike) {
        if (isLike) {
            tvLike.setCompoundDrawables(likeDrawable[1], null, null, null);
        } else {
            tvLike.setCompoundDrawables(likeDrawable[0], null, null, null);
        }
    }

    @OnLongClick({R.id.tv_content})
    boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.tv_content:
                if (data == null) {
                    return true;
                }
                final SweetAlertDialog dialog = DialogHelper.showSingleDialog(getContext(), getResources().getString(R.string.topic_copy), null);
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, data.getContent()));
                        dialog.dismissWithAnimation();
                    }
                });
                break;
        }
        return true;
    }

    @OnClick({R.id.img_back, R.id.tv_delete, R.id.layout_comment, R.id.layout_like})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.layout_comment:
                tcid = 0;
                edtComment.setHint(getResources().getString(R.string.hint_comment_topic));
                showCommentPublish();
                break;
            case R.id.layout_like:
                readyTopicLike();
                break;
            case R.id.tv_delete:
                DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_delete_topic), getResources().getString(R.string.comfirm), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        readyDeleteTopic();
                        dismissProgressDialog(sweetAlertDialog);
                    }
                });
                break;
        }
    }

    private void showCommentPublish() {
        if (commentDialog == null) {
            commentDialog = new AlertDialog.Builder(getContext()).setView(popLayout).create();
        }
        commentDialog.show();
    }

    private void readyDeleteComment() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
            deleteCommentRequest.setToken(token);
            deleteCommentRequest.setTopic_id(data.getTid());
            deleteCommentRequest.setComment_id(tcid);
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

    private void readyCommentList() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            CommentListRequest commentListRequest = new CommentListRequest();
            commentListRequest.setToken(token);
            commentListRequest.setTopic_id(data.getTid());
            commentListRequest.setPage(commentPage);
            topicDetailPresenter.requestCommentList(commentListRequest);
        }
    }

    private void readyComment() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setToken(token);
            commentRequest.setTopic_comment(comment);
            commentRequest.setTopic_id(data.getTid());
            commentRequest.setComment_id(tcid);
            topicDetailPresenter.requestComment(commentRequest);
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(dialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(dialog);
    }

    @Override
    public void responseCommentList(CommentResponse response) {
        if (isRefresh) {
            comments.clear();
        }
        comments.addAll(response.getData());
        topicCommentAdapter.notifyDataSetChanged();
    }

    @Override
    public void responseComment(CommentResponse response) {
        isRefresh = true;
        edtComment.setText("");
        if (isRefresh) {
            comments.clear();
        }
        comments.addAll(response.getData());
        topicCommentAdapter.notifyDataSetChanged();
        data.setComment_num(comments.size());
        updateDetail();
    }

    @Override
    public void responseThumbUp(BaseResponse response) {
        data.setFavorited(data.getFavorited() == 1 ? 0 : 1);
        data.setFavourites_count(data.getFavorited() == 1 ? data.getFavourites_count() + 1 :
                data.getFavourites_count() - 1);
        updateDetail();
        updateLike(data.getFavorited() == 1);
    }

    @Override
    public void responseDeleteComment(BaseResponse response) {
        comments.remove(deletePosition);
        topicCommentAdapter.notifyDataSetChanged();
        data.setComment_num(data.getComment_num() - 1);
        updateDetail();
    }

    private void updateDetail() {
        tvDetail.setText(String.format(getResources().getString(R.string.format_topic_detail),
                String.valueOf(data.getView_num()), String.valueOf(data.getComment_num()),
                String.valueOf(data.getFavourites_count())));
    }

    @Override
    public void responseDeleteTopic(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_TOPIC_DELETE_ITEM);
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MYTOPIC_DELETE_ITEM);
        hideWindow(true);
    }


    private void setLinkClickable(final SpannableStringBuilder clickableHtmlBuilder,
                                  final URLSpan urlSpan) {

        int start = clickableHtmlBuilder.getSpanStart(urlSpan);
        int end = clickableHtmlBuilder.getSpanEnd(urlSpan);
        int flags = clickableHtmlBuilder.getSpanFlags(urlSpan);
        ClickableSpan clickableSpan = new ClickableSpan() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                int windowId = WindowIDDefine.WINDOW_WEBVIEW;
                WebBean webBean = new WebBean();
                webBean.setTitle("");
                webBean.setUrl(urlSpan.getURL());
                webBean.setRequest(false);
                bundle.putSerializable("webbean", webBean);
                ControllerManager.getInstance().sendMessage(windowId, bundle);
            }
        };
        clickableHtmlBuilder.removeSpan(urlSpan);
        clickableHtmlBuilder.setSpan(clickableSpan, start, end, flags);
    }


    private CharSequence getClickableHtml(String html) {
        Spanned spannedHtml = Html.fromHtml(PatternUtil.regexReplaceURL(html));
        SpannableStringBuilder clickableHtmlBuilder = new SpannableStringBuilder(spannedHtml);
        URLSpan[] urls = clickableHtmlBuilder.getSpans(0, spannedHtml.length(), URLSpan.class);
        for (final URLSpan span : urls) {
            setLinkClickable(clickableHtmlBuilder, span);
        }
        return clickableHtmlBuilder;
    }
}
