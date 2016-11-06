package cn.flyexp.mvc.topic;

import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.MyCommentAdapter;
import cn.flyexp.adapter.MyTopicAdapter;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.DeleteTopicRequest;
import cn.flyexp.entity.MyCommentRequest;
import cn.flyexp.entity.MyCommentResponse;
import cn.flyexp.entity.MyTopicRequest;
import cn.flyexp.entity.MyTopicResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreRecyclerView;


/**
 * Created by txy on 2016/6/6.
 */
public class MyTopicWindow extends AbstractWindow implements View.OnClickListener {

    private TopicViewCallBack callBack;
    private ViewPager vp_mytopic;
    private View[] views;
    private RadioButton btn_topic;
    private RadioButton btn_comment;
    private int topicPage = 1;
    private int commentPage = 1;
    private boolean isResponse = true;
    private ContentLoadingProgressBar topicProgressBar;
    private ContentLoadingProgressBar commentProgressBar;
    private TextView topicState;
    private TextView commentState;
    private LoadMoreRecyclerView topicRecyclerView;
    private LoadMoreRecyclerView commentRecyclerView;
    private MyTopicAdapter topicAdapter;
    private MyCommentAdapter myCommentAdapter;
    private ArrayList<MyTopicResponse.MyTopicRequestData> myTopicData = new ArrayList<>();
    private ArrayList<MyCommentResponse.MyCommentResponseData> myCommentData = new ArrayList<>();
    private int deleteTopicPosition;
    private int deleteCommentPosition;

    public MyTopicWindow(TopicViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getMyTopic(getMyTopicRequest());
        callBack.getMyComment(getMyCommentRequest());
    }

    private void initView() {
        setContentView(R.layout.window_mytopic);
        findViewById(R.id.iv_back).setOnClickListener(this);
        btn_topic = (RadioButton) findViewById(R.id.btn_topic);
        btn_comment = (RadioButton) findViewById(R.id.btn_comment);
        btn_topic.setOnClickListener(this);
        btn_comment.setOnClickListener(this);


        views = new View[2];
        views[0] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview, null);
        views[1] = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_recyclerview, null);

        commentProgressBar = (ContentLoadingProgressBar) views[0].findViewById(R.id.progressBar);
        topicProgressBar = (ContentLoadingProgressBar) views[1].findViewById(R.id.progressBar);
        commentProgressBar.show();
        topicProgressBar.show();
        commentProgressBar.hide();
        topicProgressBar.hide();

        topicRecyclerView = (LoadMoreRecyclerView) views[0].findViewById(R.id.recyclerView);
        commentRecyclerView = (LoadMoreRecyclerView) views[1].findViewById(R.id.recyclerView);
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        topicAdapter = new MyTopicAdapter(getContext(), myTopicData, callBack, new MyTopicAdapter.IDeletePosition() {
            @Override
            public void delete(final int position) {
                showAlertDialog("是否删除", "取消", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteTopicRequest deleteTopicRequest = new DeleteTopicRequest();
                        deleteTopicRequest.setTopic_id(myTopicData.get(position).getTid());
                        callBack.deleteTopic(deleteTopicRequest);
                        deleteTopicPosition = position;
                    }
                });
            }
        });
        myCommentAdapter = new MyCommentAdapter(getContext(), myCommentData, callBack, new MyCommentAdapter.IDeletePosition() {
            @Override
            public void delete(final int position) {
                showAlertDialog("是否删除", "取消", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest();
                        deleteCommentRequest.setTopic_id(myCommentData.get(position).getTid());
                        deleteCommentRequest.setComment_id(myCommentData.get(position).getTcid());
                        callBack.deleteComment(deleteCommentRequest);
                        deleteCommentPosition = position;
                    }
                });
            }
        });
        topicAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.detailEnter(myTopicData.get(position).getTid());
            }
        });
        myCommentAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.detailEnter(myCommentData.get(position).getTid());
            }
        });
        topicRecyclerView.setAdapter(topicAdapter);
        commentRecyclerView.setAdapter(myCommentAdapter);
        topicRecyclerView.setItemAnimator(new DefaultItemAnimator());
        commentRecyclerView.setItemAnimator(new DefaultItemAnimator());

        topicRecyclerView.setVisibility(VISIBLE);
        commentRecyclerView.setVisibility(VISIBLE);

        vp_mytopic = (ViewPager) findViewById(R.id.vp_mytopic);
        vp_mytopic.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }

        });
        vp_mytopic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    btn_topic.setChecked(true);
                    btn_comment.setChecked(false);
                } else {
                    btn_topic.setChecked(false);
                    btn_comment.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void deleteTopic() {
        myTopicData.remove(deleteTopicPosition);
        topicAdapter.notifyDataSetChanged();
        showToast("删除成功");
    }

    public void deleteComment() {
        myCommentData.remove(deleteCommentPosition);
        myCommentAdapter.notifyDataSetChanged();
        showToast("删除成功");
    }

    public void responseTopicData(ArrayList<MyTopicResponse.MyTopicRequestData> myTopicRequestDatas) {
        myTopicData.addAll(myTopicRequestDatas);
        topicAdapter.notifyDataSetChanged();
    }

    public void responseCommentData(ArrayList<MyCommentResponse.MyCommentResponseData> myCommentResponseDatas) {
        myCommentData.addAll(myCommentResponseDatas);
        myCommentAdapter.notifyDataSetChanged();
    }

    private MyTopicRequest getMyTopicRequest() {
        String token = getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return null;
        }
        MyTopicRequest myTopicRequest = new MyTopicRequest();
        myTopicRequest.setToken(token);
        myTopicRequest.setPage(topicPage);
        return myTopicRequest;
    }

    private MyCommentRequest getMyCommentRequest() {
        String token = getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return null;
        }
        MyCommentRequest myCommentRequest = new MyCommentRequest();
        myCommentRequest.setToken(token);
        myCommentRequest.setPage(commentPage);
        return myCommentRequest;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_topic:
                vp_mytopic.setCurrentItem(0);
                break;
            case R.id.btn_comment:
                vp_mytopic.setCurrentItem(1);
                break;
        }
    }

}
