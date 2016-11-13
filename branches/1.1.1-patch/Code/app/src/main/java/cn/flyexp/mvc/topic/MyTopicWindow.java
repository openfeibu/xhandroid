package cn.flyexp.mvc.topic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.IBinder;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.adapter.MyTopicAdapter;
import cn.flyexp.entity.CommentRequest;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.MyTopicRequest;
import cn.flyexp.entity.MyTopicResponseNew;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.view.CommonDialog;
import cn.flyexp.view.LoadMoreRecyclerView;


/**
 * Created by txy on 2016/6/6.
 */
public class MyTopicWindow extends AbstractWindow implements View.OnClickListener {

    private SwipeRefreshLayout refreshlayout;
    private TopicViewCallBack callBack;
    private int topicPage = 1;
    private boolean isResponse = true;
    private ContentLoadingProgressBar topicProgressBar;
    private TextView topicState;
    private LoadMoreRecyclerView topicRecyclerView;
    private MyTopicAdapter topicAdapter;
    private int deleteTopicPosition;

    public MyTopicWindow(TopicViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getMyTopic(getMyTopicRequest());
    }

    private LinearLayout inputContainer;
    private EditText editTextInput;
    private Button buttonSubmit;
    private ImageView writeTopic;
    private int currentPosition=0;
    private CommonDialog dialog;
    private void initView() {
        setContentView(R.layout.window_topic);
        findViewById(R.id.iv_back).setOnClickListener(this);

        topicProgressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
        topicProgressBar.show();
        topicProgressBar.hide();

        inputContainer = (LinearLayout) findViewById(R.id.input_container);
        editTextInput = (EditText) findViewById(R.id.circle_edt);
        buttonSubmit = (Button) findViewById(R.id.circle_btn);
        buttonSubmit.setOnClickListener(this);
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
                callBack.getMyTopic(getMyTopicRequest());
            }
        });
        findViewById(R.id.write_topic).setVisibility(View.GONE);
        topicRecyclerView = (LoadMoreRecyclerView)findViewById(R.id.rv_topic);
        TextView title = (TextView)findViewById(R.id.title_text);
        title.setText(getContext().getText(R.string.my_topic));
        final LinearLayoutManager lm=new LinearLayoutManager(getContext());
        topicRecyclerView.setLayoutManager(lm);

        topicRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView view, int scrollState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (lm.findFirstCompletelyVisibleItemPosition() == 0) {
                    refreshlayout.setEnabled(true);
                }
                else{
                    refreshlayout.setEnabled(false);
                }
            }
        });

        topicAdapter = new MyTopicAdapter(getContext(), list, callBack, new MyTopicAdapter.IDeletePosition() {
            @Override
            public void delete(final int position) {
                WindowHelper.showAlertDialog("是否删除", "取消", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest();
                        deleteTopicRequest.setTopic_id(list.get(position).getTid());
                        callBack.deleteTopic(deleteTopicRequest);
                        deleteTopicPosition = position;
                    }
                });
            }
        });
        topicRecyclerView.setAdapter(topicAdapter);
        topicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        topicRecyclerView.setVisibility(VISIBLE);
        topicAdapter.setOnCommentClickListener(new MyTopicAdapter.CommentOnClickListener() {
            @Override
            public void onReview(TextView v, int position, String nickName, final int commentId, final int TopicId) {
                final String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                String userName=WindowHelper.getStringByPreference("nickName");
                if(!userName.equals(nickName)){
                    if (currentPosition != position){//评论不同的东西，清掉评论数据
                        editTextInput.setText("");
                    }
                    editTextInput.setHint("回复 "+nickName);
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
                    LinearLayoutManager layoutManager = (LinearLayoutManager) topicRecyclerView.getLayoutManager();
                    if(position<0){
                        layoutManager.scrollToPositionWithOffset(position+1,0);
                    }

                    buttonSubmit.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String comment = editTextInput.getText().toString().trim();
                            if (comment == null || comment.equals("")) {
                                editTextInput.setHint("评论一下");
                                return;
                            }
                            CommentRequest commentRequest = new CommentRequest();
                            commentRequest.setToken(token);
//                            int topicId=data.get(currentPosition).getTid();
                            if (TopicId != 0) {
                                commentRequest.setTopic_id(TopicId);
                            }
                            commentRequest.setTopic_comment(comment);
                            commentRequest.setComment_id(commentId);
//                            callBack.commentTopic(commentRequest);
                            callBack.myCommentTopic(commentRequest);
                            v.setEnabled(false);
                        }
                    });
                }else{
                    editTextInput.setHint("评论一下下");
                    if (dialog == null) {
                        dialog = new CommonDialog.Builder(getContext()).create();
                        dialog.setContentListener(new CommonDialog.ContentClickListenter() {
                            @Override
                            public void onContentClick() {
                                DeleteCommentRequest request = new DeleteCommentRequest();
                                request.setToken(token);
                                request.setComment_id(commentId);
                                callBack.mydeleteComment(request);
                                dialog.dismiss();
                                dialog = null;
                            }
                        });
                        dialog.setCanceledOnTouchOutside(true);
                    }
                    dialog.show();
                }
            }
        });
    }

    public void deleteCommentResponse(){
        callBack.getMyTopic(getMyTopicRequest());
    }



    public void deleteTopic() {
        list.remove(deleteTopicPosition);
        topicAdapter.notifyDataSetChanged();
        WindowHelper.showToast("删除成功");
    }

    public void loadingFailure() {
        refreshlayout.setRefreshing(false);
        isResponse = true;
    }


    private ArrayList<MyTopicResponseNew.DataBean> list=new ArrayList<>();
    public void responseTopicData(List<MyTopicResponseNew.DataBean> list){
        if(list == null || list.size()==0){
            return;
        }
        this.list.clear();
        this.list.addAll(list);
        topicAdapter.notifyDataSetChanged();
        refreshlayout.setRefreshing(false);
    }

    private MyTopicRequest getMyTopicRequest() {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return null;
        }
        MyTopicRequest myTopicRequest = new MyTopicRequest();
        myTopicRequest.setToken(token);
        myTopicRequest.setPage(topicPage);
        return myTopicRequest;
    }

    public void onCommentSubmitResponse(){
        inputContainer.setVisibility(View.GONE);
        buttonSubmit.setEnabled(true);
        editTextInput.clearFocus();
        editTextInput.setText("");
        InputMethodManager im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        callBack.getMyTopic(getMyTopicRequest());
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.circle_btn:
//                String tokens = WindowHelper.getStringByPreference("token");
//                if (tokens.equals("")) {
//                    callBack.loginWindowEnter();
//                    return;
//                }
//                String comment = editTextInput.getText().toString().trim();
//                if (comment == null || comment.equals("")) {
//                    editTextInput.setHint("评论一下");
//                    return;
//                }
//                hideKeyboard(v.getWindowToken());
//                CommentRequest commentRequest = new CommentRequest();
//                commentRequest.setToken(tokens);
//                int topicId=myTopicData.get(currentPosition).getTid();
//                if (topicId != 0) {
//                    commentRequest.setTopic_id(topicId);
//                }
//                commentRequest.setTopic_comment(comment);
//                commentRequest.setComment_id(0);
//                callBack.myCommentTopic(commentRequest);
                break;
        }
    }

}
