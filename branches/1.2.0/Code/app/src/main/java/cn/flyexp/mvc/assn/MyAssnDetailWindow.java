package cn.flyexp.mvc.assn;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.AvatarAdapter;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.AssnActivityRequest;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnMemberRequest;
import cn.flyexp.entity.AssnMemberResponse;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.RoundImageView;

/**
 * Created by tanxinye on 2016/11/29.
 */
public class MyAssnDetailWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private int aid;
    private int level;
    private RoundImageView imgAvatar;
    private TextView tvAssn;
    private TextView tvNum;
    private TextView tvIntroduction;
    private TextView tvRemine;
    private TextView tvEdit;
    private TextView tvNoticeContent;
    private TextView tvNoticeDate;
    private LoadMoreRecyclerView rvActi;
    private RecyclerView rvMember;
    private TextView tvActiPublish;
    private ArrayList<String> urls = new ArrayList<>();
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> assnActiData = new ArrayList<>();
    private AvatarAdapter avatarAdapter;
    private AssnActivityAdapter assnActivityAdapter;
    private int actiPage = 1;

    public MyAssnDetailWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_myassn_detail);
        findViewById(R.id.img_back).setOnClickListener(this);
        findViewById(R.id.img_exit).setOnClickListener(this);
        findViewById(R.id.layout_member).setOnClickListener(this);

        imgAvatar = (RoundImageView) findViewById(R.id.img_avatar);
        tvAssn = (TextView) findViewById(R.id.tv_assn);
        tvNum = (TextView) findViewById(R.id.tv_num);
        tvIntroduction = (TextView) findViewById(R.id.tv_introduction);
        tvRemine = (TextView) findViewById(R.id.tv_remine);
        tvEdit = (TextView) findViewById(R.id.tv_edit);
        tvNoticeContent = (TextView) findViewById(R.id.tv_notice_content);
        tvNoticeDate = (TextView) findViewById(R.id.tv_notice_date);
        tvActiPublish = (TextView) findViewById(R.id.tv_acti_publish);
        rvMember = (RecyclerView) findViewById(R.id.rv_member);
        rvActi = (LoadMoreRecyclerView) findViewById(R.id.rv_acti);

        tvEdit.setOnClickListener(this);
        tvActiPublish.setOnClickListener(this);

        avatarAdapter = new AvatarAdapter(getContext(), urls);
        rvMember.setAdapter(avatarAdapter);
        rvMember.setEnabled(false);
        rvMember.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMember.setNestedScrollingEnabled(false);
        rvMember.setHasFixedSize(true);

        assnActivityAdapter = new AssnActivityAdapter(getContext(), assnActiData);
        assnActivityAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.assnDetailEnter(assnActiData.get(position).getAid());
            }
        });
        rvActi.addItemDecoration(new DividerItemDecoration(getContext()));
        rvActi.setAdapter(assnActivityAdapter);
        rvActi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvActi.setNestedScrollingEnabled(false);
        rvActi.setHasFixedSize(true);
        rvActi.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                actiPage++;
                readyAssnActi();
            }
        });
    }

    public void init(int aid, int level) {
        this.aid = aid;
        this.level = level;
        if (level == 0) {
            tvEdit.setVisibility(GONE);
            tvActiPublish.setVisibility(GONE);
        }
        readyAssnDetail();
        readyAssnMember();
        readyAssnActi();
        readyAssnExamie();
    }

    private void readyAssnDetail() {
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (TextUtils.isEmpty(token)) {
            callBack.loginWindowEnter();
        } else {
            AssnDetailRequest assnDetailRequest = new AssnDetailRequest();
            assnDetailRequest.setAssociation_id(aid);
            assnDetailRequest.setToken(token);
            callBack.getMyAssnDetails(assnDetailRequest);
        }
    }

    private void readyAssnMember() {
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (TextUtils.isEmpty(token)) {
            callBack.loginWindowEnter();
        } else {
            AssnMemberRequest assnMemberRequest = new AssnMemberRequest();
            assnMemberRequest.setAssociation_id(aid);
            assnMemberRequest.setToken(token);
            assnMemberRequest.setPage(1);
            callBack.getMemberListByMyAssn(assnMemberRequest);
        }
    }

    private void readyAssnActi() {
        MyAssnActivityRequest myAssnActivityRequest = new MyAssnActivityRequest();
        myAssnActivityRequest.setPage(actiPage);
        myAssnActivityRequest.setAssociation_id(aid);
        callBack.getMyAssnActivity(myAssnActivityRequest);
    }

    private void readyAssnExamie() {
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (TextUtils.isEmpty(token)) {
            callBack.loginWindowEnter();
        } else {
            AssnExamineListRequest assnExamineListRequest = new AssnExamineListRequest();
            assnExamineListRequest.setAssociation_id(aid);
            assnExamineListRequest.setToken(token);
            callBack.examineMembarListByMyAssn(assnExamineListRequest);
        }
    }

    public void myAssnDetailResponse(AssnDetailResponse.AssnDetailResponseData data) {
        tvAssn.setText(data.getAname());
        tvNum.setText(String.valueOf(data.getMember_number()));
        tvIntroduction.setText(data.getIntroduction());
        tvNoticeContent.setText(data.getNotice());
        tvNoticeDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(data.getNotice_created_at())));

        if (!TextUtils.isEmpty(data.getAvatar_url())) {
            Picasso.with(getContext()).load(data.getAvatar_url())
                    .resize(CommonUtil.dip2px(getContext(), 50), CommonUtil.dip2px(getContext(), 50))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().into(imgAvatar);
        }
    }

    public void memberListResponse(ArrayList<AssnMemberResponse.AssnMemberResponseData> data) {
        for (int i = 0; i < 7; i++) {
            urls.add(data.get(i).getAvatar_url());
        }
        avatarAdapter.notifyDataSetChanged();
    }

    public void myAssnActiResponse(ArrayList<AssnActivityResponse.AssnActivityResponseData> data) {
        assnActiData.addAll(data);
        if (data.isEmpty()) {
            rvActi.loadMoreEnd();
        } else {
            rvActi.loadMoreComplete();
        }
        assnActivityAdapter.notifyDataSetChanged();
    }

    public void examineListResponse(ArrayList<AssnExamineListResponse.AssnExamineListResponseData> data) {
        if (data != null && level != 0) {
            tvRemine.setVisibility(VISIBLE);
        } else {
            tvRemine.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.img_exit:
                if (level == 1) {
                    WindowHelper.showToast("社长不能退出社团");
                    return;
                }
                WindowHelper.showAlertDialog("是否退出", "否", "是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
                        if (TextUtils.isEmpty(token)) {
                            return;
                        }
                        AssnQuitRequest assnQuitRequest = new AssnQuitRequest();
                        assnQuitRequest.setToken(token);
                        assnQuitRequest.setAssociation_id(aid);
                        callBack.assnQuit(assnQuitRequest);
                    }
                });
                break;
            case R.id.layout_member:
                Bundle bundle = new Bundle();
                bundle.putInt("aid", aid);
                bundle.putInt("level", level);
                bundle.putBoolean("isExamine", tvRemine.getVisibility() == VISIBLE);
                callBack.assnMemberEnter(bundle);
                break;
            case R.id.tv_edit:
                callBack.noticePublishEnter(aid);
                break;
            case R.id.tv_acti_publish:
                callBack.activityPublishEnter(aid);
                break;
        }
    }
}

