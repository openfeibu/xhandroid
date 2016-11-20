package cn.flyexp.mvc.topic;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.IBinder;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.TopicAdapter;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.view.CommonDialog;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;


/**
 * Created by txy on 2016/6/6.
 */
public class TopicWindow extends AbstractWindow implements View.OnClickListener {

    private SwipeRefreshLayout refreshlayout;
    private TopicViewCallBack callBack;
    private TopicAdapter topicAdapter;
    private LoadMoreRecyclerView rv_topic;
    private ArrayList<TopicResponseData> data = new ArrayList<>();
    private ContentLoadingProgressBar progressBar;
    private TextView tv_state;
    private int page = 1;
    private boolean isUpLoading = true;
    private boolean isResponse;
    private boolean isVis = true;
    private TextView tv_newmes;
    private boolean isTopicRemind;

    public TopicWindow(TopicController controller) {
        super(controller);
        this.callBack = controller;
        initView();
    }

    public void requestTopic() {
        callBack.getTopicList(getTopicListRequest());
    }

    private RelativeLayout titleBar;
    private LinearLayout inputContainer;
    private EditText editTextInput;
    private Button buttonSubmit;
    private ImageView writeTopic;
    private int currentPosition = 0;

    private void initView() {
        setContentView(R.layout.window_topic);
        titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        findViewById(R.id.iv_back).setVisibility(View.GONE);

        tv_newmes = (TextView) findViewById(R.id.tv_newmes);
        tv_newmes.setOnClickListener(this);

//        findViewById(R.id.iv_search).setOnClickListener(this);
        inputContainer = (LinearLayout) findViewById(R.id.input_container);
        editTextInput = (EditText) findViewById(R.id.circle_edt);
        buttonSubmit = (Button) findViewById(R.id.circle_btn);
        buttonSubmit.setOnClickListener(this);
        writeTopic = (ImageView) findViewById(R.id.write_topic);
        writeTopic.setOnClickListener(this);
        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        progressBar.show();
        tv_state = (TextView) findViewById(R.id.tv_state);
        refreshlayout = (SwipeRefreshLayout) findViewById(R.id.refreshlayout);
        refreshlayout.setColorSchemeResources(R.color.light_blue);
        refreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isResponse || !canRequest()) {
                    refreshlayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshlayout.setRefreshing(false);
                        }
                    }, 200);
                    return;
                }
                setRequestTimeNow();
                page = 1;
                isUpLoading = true;
                rv_topic.loadMoreComplete();
                callBack.getTopicList(getTopicListRequest());
            }
        });
        topicAdapter = new TopicAdapter(getContext(), data, callBack);
        topicAdapter.setOnCommentClickListener(new TopicAdapter.OnCommentClickListener() {
            @Override
            public void onCommentClick(View v, int position) {
                if (currentPosition != position) {//评论不同的东西，清掉评论数据
                    editTextInput.setText("");
                }
                buttonSubmit.setOnClickListener(TopicWindow.this);
                currentPosition = position;
                inputContainer.setVisibility(View.VISIBLE);
                editTextInput.setFocusable(true);
                editTextInput.setFocusableInTouchMode(true);
                editTextInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                int[] positions = new int[2];
                v.getLocationOnScreen(positions);
                LinearLayoutManager layoutManager = (LinearLayoutManager) rv_topic.getLayoutManager();
                if (position < 0) {
                    layoutManager.scrollToPositionWithOffset(position + 1, 0);
                }
            }

            @Override
            public void onLaudClick(View v, int position, int top_id) {
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                currentPosition = position;
                TopicResponseData topicResponseData = data.get(position);
                ThumbUpRequest thumbUpRequest = new ThumbUpRequest();
                thumbUpRequest.setTopic_id(topicResponseData.getTid());
                callBack.thumbUp(thumbUpRequest);
            }

            @Override
            public void onReviewClick(LinearLayout v, int position, String nickName, final int commentId, final int topicId) {
                final String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                String userName = WindowHelper.getStringByPreference("nickName");
                if (!userName.equals(nickName)) {
                    if (currentPosition != position) {//评论不同的东西，清掉评论数据
                        editTextInput.setText("");
                    }
                    editTextInput.setHint("回复 " + nickName);
                    currentPosition = position;
                    inputContainer.setVisibility(View.VISIBLE);
                    editTextInput.setFocusable(true);
                    editTextInput.setFocusableInTouchMode(true);
                    editTextInput.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    int[] positions = new int[2];
                    v.getLocationOnScreen(positions);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rv_topic.getLayoutManager();
                    if (position < 0) {
                        layoutManager.scrollToPositionWithOffset(position + 1, 0);
                    }

                    buttonSubmit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String comment = editTextInput.getText().toString().trim();
                            if (comment == null || comment.equals("")) {
                                editTextInput.setHint("评论一下");
                                return;
                            }
                            editTextInput.setText("");
                            CommentRequest commentRequest = new CommentRequest();
                            commentRequest.setToken(token);
                            hideKeyboard(editTextInput.getWindowToken());
//                            int topicId=data.get(currentPosition).getTid();
                            if (topicId != 0) {
                                commentRequest.setTopic_id(topicId);
                            }
                            commentRequest.setTopic_comment(comment);
                            commentRequest.setComment_id(commentId);
                            callBack.commentTopic(commentRequest);
                            WindowHelper.showToast(getContext().getString(R.string.please_wait));
                        }
                    });
                } else {
                    editTextInput.setHint("评论一下");
                    final CommonDialog commentCallbackDialog = new CommonDialog.Builder(getContext()).create();
                    commentCallbackDialog.setContentListener(new CommonDialog.ContentClickListenter() {
                        @Override
                        public void onContentClick() {
                            DeleteCommentRequest request = new DeleteCommentRequest();
                            request.setToken(token);
                            request.setComment_id(commentId);
                            callBack.deleteComment(request);
                            commentCallbackDialog.dismiss();
                        }
                    });
                    commentCallbackDialog.setCanceledOnTouchOutside(true);
                    commentCallbackDialog.show();
                }
            }
        });
        topicAdapter.setOnItemCallback(new TopicAdapter.OnItemCallback() {
            public void onContentLongClicked(final int position, final int topic_id) {
                final CommonDialog itemCallbackDialog = new CommonDialog.Builder(getContext()).create(getContext().getText(R.string.copy));
                itemCallbackDialog.setContentListener(new CommonDialog.ContentClickListenter() {
                    @Override
                    public void onContentClick() {
                        TopicResponseData rd = topicAdapter.getData().get(position);
                        String text = rd.getContent();
                        ClipboardManager myClipboard;
                        myClipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        myClipboard.setPrimaryClip(ClipData.newPlainText("text", text));
                        itemCallbackDialog.dismiss();
                    }
                });
                itemCallbackDialog.setCanceledOnTouchOutside(true);
                itemCallbackDialog.show();
            }
        });
        rv_topic = (LoadMoreRecyclerView) findViewById(R.id.rv_topic);
        final LinearLayoutManager lm = new LinearLayoutManager(getContext());
        rv_topic.setLayoutManager(lm);
        rv_topic.setAdapter(topicAdapter);
        rv_topic.setItemAnimator(new DefaultItemAnimator());
        rv_topic.setFootLoadingView(ProgressView.BallPulse);
        rv_topic.setFootEndView("没有话题了~");
        rv_topic.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                if (!isResponse) {
                    return;
                }
                page++;
                isUpLoading = false;
                callBack.getTopicList(getTopicListRequest());
            }
        });
        rv_topic.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (lm.findFirstCompletelyVisibleItemPosition() == 0) {
                    refreshlayout.setEnabled(true);
                } else {
                    refreshlayout.setEnabled(false);
                }
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = ((Activity) getContext()).getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = inputContainer.getWidth();
            if (event.getRawX() > left && event.getRawX() < right
                    && event.getRawY() > top && event.getRawY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            editTextInput.clearFocus();
            editTextInput.setHint("评论一下");
//            editTextInput.setText("");用户输入的数据不要清掉，除非点击去评论其他时
            inputContainer.setVisibility(View.GONE);
            InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void thumbUpResponse() {
//        topicAdapter.notifyDataSetChanged();
        TopicResponseData topicResponseData = data.get(currentPosition);
        if (topicResponseData.getFavorited() == 0) {
            topicResponseData.setFavorited(1);
            topicResponseData.setFavourites_count(topicResponseData.getFavourites_count() + 1);
        } else {
            topicResponseData.setFavorited(0);
            topicResponseData.setFavourites_count(topicResponseData.getFavourites_count() - 1);
        }
        topicAdapter.notifyItemChanged(currentPosition);

    }

    private float getImmInputHeight() {
        return 0;
    }

    private TopicListRequest getTopicListRequest() {
        isResponse = false;
        TopicListRequest topicListRequest = new TopicListRequest();
        String token = WindowHelper.getStringByPreference("token");
        topicListRequest.setToken(token);
        topicListRequest.setPage(page);
        return topicListRequest;
    }

    public void refreshTopic() {
        isUpLoading = true;
        callBack.getTopicList(getTopicListRequest());
    }

    public void topicResponse(ArrayList<TopicResponseData> topicResponseDatas) {
        //隐藏加载提示
        progressBar.hide();
        tv_state.setVisibility(View.GONE);
        //上拉刷新清空数据 下拉加载追加数据
        if (isUpLoading) {
            data.clear();
            data.addAll(topicResponseDatas);
        } else {
            //追加的数据为空 显示没有更多数据
            this.data.addAll(topicResponseDatas);
            if (topicResponseDatas.size() == 0) {
                rv_topic.loadMoreEnd();
            } else {
                rv_topic.loadMoreComplete();
            }
        }
        //追加的数据为空显示友好提示
        if (data.size() == 0) {
            tv_state.setText("暂无话题");
            tv_state.setVisibility(View.VISIBLE);
            rv_topic.setVisibility(View.GONE);
        } else {
            rv_topic.setVisibility(View.VISIBLE);
            topicAdapter.notifyDataSetChanged();
        }
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }

    public void loadingFailure() {
        progressBar.hide();
        WindowHelper.showToast("数据加载失败...");
//        tv_state.setText("数据加载失败...");
//        tv_state.setVisibility(View.VISIBLE);
//        rv_topic.setVisibility(View.GONE);
        rv_topic.loadMoreComplete();
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }

    public void setCommentComponentReset() {
        inputContainer.setVisibility(View.GONE);
        buttonSubmit.setEnabled(true);
        editTextInput.clearFocus();
        editTextInput.setText("");
//        InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        refreshTopic();
    }

    public void remindTopic() {
        isTopicRemind = true;
        tv_newmes.setVisibility(VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_topic:
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                } else {
                    callBack.publishEnter();
                }
                break;
            case R.id.tv_newmes:
                if (isTopicRemind) {
                    tv_newmes.setVisibility(GONE);
                    callBack.replyListEnter();
                }
                break;
            case R.id.circle_btn:
                String tokens = WindowHelper.getStringByPreference("token");
                if (tokens.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                String comment = editTextInput.getText().toString().trim();
                if (comment == null || comment.equals("")) {
                    editTextInput.setHint("评论一下");
                    return;
                }
                hideKeyboard(v.getWindowToken());
                CommentRequest commentRequest = new CommentRequest();
                commentRequest.setToken(tokens);
                int topicId = data.get(currentPosition).getTid();
                if (topicId != 0) {
                    commentRequest.setTopic_id(topicId);
                }
                commentRequest.setTopic_comment(comment);
                commentRequest.setComment_id(0);
                callBack.commentTopic(commentRequest);
                WindowHelper.showToast(getContext().getString(R.string.please_wait));
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (inputContainer.getVisibility() == VISIBLE) {
            if (event.getAction() == KeyEvent.ACTION_UP) {
                int keyCode = event.getKeyCode();
                if (keyCode == event.KEYCODE_BACK) {
                    View v = ((Activity) getContext()).getCurrentFocus();
                    if (v != null) {
                        editTextInput.setHint("评论一下");
                        hideKeyboard(v.getWindowToken());
                        return true;
                    }
                    ;
                }
            }
            return super.dispatchKeyEvent(event);
        }
        return false;
    }

}
