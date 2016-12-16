package cn.flyexp.window.topic;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.TTopicAdapter;
import cn.flyexp.callback.topic.TopicCallback;
import cn.flyexp.constants.Constants;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.CommentResponse;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.topic.TopicPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.PopupUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class TTopicWindow extends BaseWindow implements NotifyManager.Notify, TopicCallback.ResponseCallback {

    @InjectView(R.id.vp_topic)
    ViewPager vpTopic;
    @InjectView(R.id.layout_topiclist)
    View layoutTopicList;
    @InjectView(R.id.img_newmsg)
    ImageView imgNewMsg;

    private int page = 1;
    private ArrayList<TopicResponseData> datas = new ArrayList<>();
    private TopicPresenter topicPresenter;
    private TTopicAdapter topicAdapter;
    private View topicInputLayout;
    private PopupUtil popupHelper;
    private EditText edtComment;
    private TextView tvComment;
    private View layoutTopic;
    private SwipeRefreshLayout refreshLayout;
    private boolean isRefresh;
    private String comment;
    private int currPosition = -1;
    private int tcid;
    private int commentpos;
    private LinearLayoutManager linearLayoutManager;
    private boolean isRequesting;
    private LoadMoreRecyclerView rvTopic;

    @Override
    protected int getLayoutId() {
        return R.layout.window_topict;
    }

    public TTopicWindow() {
        topicPresenter = new TopicPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TOPIC, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TOPIC_PUSH, this);
        topicInputLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_topic_input, null);
        popupHelper = new PopupUtil(new PopupUtil.Builder(topicInputLayout).create());
        initView();
    }

    @Override
    public void onStart() {
        readyTopicList();
    }

    @Override
    public void onRenew() {
        isRefresh = true;
        if (linearLayoutManager != null) {
            linearLayoutManager.scrollToPosition(0);
        }
        readyTopicList();
    }

    private void initView() {
        topicAdapter = new TTopicAdapter(getContext(), datas);
        topicAdapter.setOnTopicClickListener(new TTopicAdapter.OnTopicClickListener() {
            @Override
            public void onLikeClick(int position) {
                currPosition = position;
                if (!isRequesting) {
                    readyTopicLike();
                }
            }

            @Override
            public void onCommentClick(int position, int cpos, int commentid, String nickname, String openid) {
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                    return;
                }
                if (currPosition != position || commentpos != cpos) {
                    edtComment.setText("");
                }
                tcid = commentid;
                currPosition = position;
                commentpos = cpos;
                String myOpenId = SharePresUtil.getString(SharePresUtil.KEY_OPENID);
                if (TextUtils.equals(myOpenId, openid) && commentpos != -1 && !isRequesting) {
                    DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_delete_comment), getResources().getString(R.string.comfirm), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            readyDeleteComment();
                            dismissProgressDialog(sweetAlertDialog);
                        }
                    });
                    return;
                }
                if (TextUtils.isEmpty(nickname)) {
                    edtComment.setHint(R.string.hint_topic_comment);
                } else {
                    edtComment.setHint(String.format(getResources().getString(R.string.format_topic_comment), nickname));
                }
                popupHelper.showAtLocation(layoutTopicList, Gravity.BOTTOM, 0, 0);
            }

            @Override
            public void onPicClick(int position, int picPosition) {
                Bundle bundle = new Bundle();
                ArrayList<String> imageUrl = new ArrayList<>();
                imageUrl.addAll(Arrays.asList(splitImageUrl(datas.get(position).getImg())));
                bundle.putStringArrayList("uri", imageUrl);
                bundle.putInt("position", picPosition);
                bundle.putString("type", Constants.NET);
                openWindow(WindowIDDefine.WINDOW_PICBROWSER, bundle);
            }

            @Override
            public void onDeleteTopicClick(int position) {
                currPosition = position;
                if (!isRequesting) {
                    DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_delete_topic), getResources().getString(R.string.comfirm), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            readyDeleteTopic();
                            dismissProgressDialog(sweetAlertDialog);
                        }
                    });
                }
            }

            @Override
            public void onLongClick() {

            }
        });
        vpTopic.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(layoutTopic);
                return layoutTopic;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(layoutTopic);
            }
        });
        layoutTopic = LayoutInflater.from(getContext()).inflate(R.layout.layout_topic, null);
        refreshLayout = (SwipeRefreshLayout) layoutTopic.findViewById(R.id.layout_refresh);
        refreshLayout.setColorSchemeResources(R.color.light_blue);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                isRefresh = true;
                readyTopicList();
            }
        });
        rvTopic = (LoadMoreRecyclerView) layoutTopic.findViewById(R.id.rv_topic);
        ((SimpleItemAnimator) rvTopic.getItemAnimator()).setSupportsChangeAnimations(false);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rvTopic.setLayoutManager(linearLayoutManager);
        rvTopic.setAdapter(topicAdapter);
        rvTopic.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                readyTopicList();
            }
        });

        edtComment = (EditText) topicInputLayout.findViewById(R.id.edt_comment);
        edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                comment = edtComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    tvComment.setEnabled(false);
                    tvComment.setAlpha(0.5f);
                } else {
                    tvComment.setEnabled(true);
                    tvComment.setAlpha(1f);
                }
            }
        });
        tvComment = (TextView) topicInputLayout.findViewById(R.id.tv_comment);
        tvComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHelper.dismiss();
                readyComment();
            }
        });
    }

    private void readyDeleteComment() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
            deleteCommentRequest.setToken(token);
            deleteCommentRequest.setTopic_id(datas.get(currPosition).getTid());
            deleteCommentRequest.setComment_id(tcid);
            topicPresenter.requestDeleteComment(deleteCommentRequest);
            isRequesting = true;
        }
    }

    private void readyDeleteTopic() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest();
            deleteTopicRequest.setToken(token);
            deleteTopicRequest.setTopic_id(datas.get(currPosition).getTid());
            topicPresenter.requestDeleteTopic(deleteTopicRequest);
            isRequesting = true;
        }
    }

    private void readyTopicList() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        TopicListRequest topicListRequest = new TopicListRequest();
        if (!TextUtils.isEmpty(token)) {
            topicListRequest.setToken(token);
        }
        topicListRequest.setPage(page);
        topicPresenter.requestTopicList(topicListRequest);
    }

    private void readyTopicLike() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            ThumbUpRequest thumbUpRequest = new ThumbUpRequest();
            thumbUpRequest.setToken(token);
            thumbUpRequest.setTopic_id(datas.get(currPosition).getTid());
            topicPresenter.requestThumbUp(thumbUpRequest);
            isRequesting = true;
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
            commentRequest.setTopic_id(datas.get(currPosition).getTid());
            commentRequest.setComment_id(tcid);
            topicPresenter.requestComment(commentRequest);
            isRequesting = true;
        }
    }

    public String[] splitImageUrl(String imgUrl) {
        return imgUrl.split("\\,");
    }

    @OnClick({R.id.fab_publish,R.id.img_newmsg})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_publish:
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                } else {
                    openWindow(WindowIDDefine.WINDOW_TOPIC_PUBLISH);
                }
                break;
            case R.id.img_newmsg:
                imgNewMsg.setVisibility(GONE);
                openWindow(WindowIDDefine.WINDOW_TOPIC_PUBLISH);
                break;
        }
    }

    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

    @Override
    public void requestFailure() {
        isRequesting = false;
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void requestFinish() {
        isRequesting = false;
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void responseTopicList(TopicListResponse response) {
        if (isRefresh) {
            datas.clear();
            isRefresh = false;
        }
        datas.addAll(response.getData());
        topicAdapter.notifyDataSetChanged();
    }

    @Override
    public void responseComment(CommentResponse response) {
        datas.get(currPosition).getComment().clear();
        datas.get(currPosition).getComment().addAll(response.getData());
        topicAdapter.notifyItemChanged(currPosition);
    }

    @Override
    public void responseThumbUp(BaseResponse response) {
        TopicResponseData topicResponseData = datas.get(currPosition);
        if (topicResponseData.getFavorited() == 0) {
            topicResponseData.setFavorited(1);
            topicResponseData.setFavourites_count(topicResponseData.getFavourites_count() + 1);
        } else {
            topicResponseData.setFavorited(0);
            topicResponseData.setFavourites_count(topicResponseData.getFavourites_count() - 1);
        }
        topicAdapter.notifyItemChanged(currPosition);
    }

    @Override
    public void responseDeleteComment(BaseResponse response) {
        datas.get(currPosition).getComment().remove(commentpos);
        topicAdapter.notifyItemChanged(currPosition);
    }

    @Override
    public void responseDeleteTopic(BaseResponse response) {
        datas.remove(currPosition);
        topicAdapter.notifyItemRemoved(currPosition);
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_TOPIC) {
            isRefresh = true;
            page = 1;
            readyTopicList();
        } else if (mes.what == NotifyIDDefine.NOTIFY_TOPIC_PUSH) {
            ((Activity)getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imgNewMsg.setVisibility(VISIBLE);
                }
            });
        }
    }
}
