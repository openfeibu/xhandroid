package cn.flyexp.window.assn;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.AvatarAdapter;
import cn.flyexp.callback.assn.MyAssnDetailCallback;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.entity.AssnMemberListRequest;
import cn.flyexp.entity.AssnMemberListResponse;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.assn.MyAssnDetailPresenter;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/12/4.
 */
public class MyAssnDetailWindow extends BaseWindow implements NotifyManager.Notify, MyAssnDetailCallback.ResponseCallback {

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_assnname)
    TextView tvAssnName;
    @InjectView(R.id.tv_intro)
    TextView tvIntro;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.tv_edit)
    TextView tvEdit;
    @InjectView(R.id.tv_notice)
    TextView tvNotice;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.tv_publish)
    TextView tvPublish;
    @InjectView(R.id.tv_exmine)
    TextView tvExmine;
    @InjectView(R.id.rv_member)
    RecyclerView rvMember;
    @InjectView(R.id.rv_assn_acti)
    LoadMoreRecyclerView rvAssnActi;

    private int aid;
    private int level;
    private MyAssnDetailPresenter myAssnDetailPresenter;
    private boolean isRefresh;
    private int mineUid;
    private int actiPage = 1;
    private AssnActivityAdapter assnActivityAdapter;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> actiDatas = new ArrayList<>();
    private AvatarAdapter avatarAdapter;
    private ArrayList<String> avatars = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.window_myassn_detail;
    }

    public MyAssnDetailWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        level = bundle.getInt("level");
        myAssnDetailPresenter = new MyAssnDetailPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTICE_MYASSN_DETAIL, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_ASSN_ACTIVITY, this);

        initView();
        readyMyAssnDetail();
        readyAssnMemberList();
        readyMyAssnActivity();
    }

    private void initView() {
        if (level == 0) {
            tvEdit.setVisibility(GONE);
            tvPublish.setVisibility(GONE);
        } else {
            tvEdit.setVisibility(VISIBLE);
            tvPublish.setVisibility(VISIBLE);
        }
        avatarAdapter = new AvatarAdapter(getContext(), avatars);
        rvMember.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvMember.setAdapter(avatarAdapter);

        assnActivityAdapter = new AssnActivityAdapter(getContext(), actiDatas);
        assnActivityAdapter.setOnItemClickLinstener(new AssnActivityAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("assnacti", actiDatas.get(position));
                openWindow(WindowIDDefine.WINDOW_ASSNACTI_DETAIL, bundle);
            }
        });
        rvAssnActi.setAdapter(assnActivityAdapter);
        rvAssnActi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAssnActi.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                actiPage++;
                readyMyAssnActivity();
            }
        });
    }

    private void readyMyAssnDetail() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            AssnDetailRequest assnDetailRequest = new AssnDetailRequest();
            assnDetailRequest.setToken(token);
            assnDetailRequest.setAssociation_id(aid);
            myAssnDetailPresenter.requestMyAssnDetail(assnDetailRequest);
        }
    }

    private void readyAssnMemberList() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            AssnMemberListRequest assnMemberListRequest = new AssnMemberListRequest();
            assnMemberListRequest.setToken(token);
            assnMemberListRequest.setAssociation_id(aid);
            assnMemberListRequest.setPage(1);
            myAssnDetailPresenter.requestAssnMemberList(assnMemberListRequest);
        }
    }

    private void readyAssnQuit() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            AssnQuitRequest assnQuitRequest = new AssnQuitRequest();
            assnQuitRequest.setAssociation_id(aid);
            assnQuitRequest.setToken(token);
            myAssnDetailPresenter.requestAssnQuit(assnQuitRequest);
        }
    }

    private void readyMyAssnActivity() {
        MyAssnActivityRequest myAssnActivityRequest = new MyAssnActivityRequest();
        myAssnActivityRequest.setPage(actiPage);
        myAssnActivityRequest.setAssociation_id(aid);
        myAssnDetailPresenter.requestMyAssnActivity(myAssnActivityRequest);
    }


    @OnClick({R.id.img_back, R.id.img_exit, R.id.tv_exmine, R.id.layout_member, R.id.tv_edit, R.id.tv_publish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.layout_member:
                Bundle bundle = new Bundle();
                bundle.putInt("aid", aid);
                bundle.putInt("level", level);
                bundle.putInt("uid", mineUid);
                openWindow(WindowIDDefine.WINDOW_ASSN_MEMBER, bundle);
                break;
            case R.id.tv_edit:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("aid", aid);
                openWindow(WindowIDDefine.WINDOW_ASSN_NOTICE_PULISH, bundle2);
                break;
            case R.id.tv_publish:
                Bundle bundle3 = new Bundle();
                bundle3.putInt("aid", aid);
                openWindow(WindowIDDefine.WINDOW_ASSNACTI_PUBLISH, bundle3);
                break;
            case R.id.tv_exmine:
                Bundle bundle4 = new Bundle();
                bundle4.putInt("aid", aid);
                openWindow(WindowIDDefine.WINDOW_ASSN_EXAMINE_LIST, bundle4);
                break;
            case R.id.img_exit:
                if (level == 1) {
                    showToast(R.string.president_unable_exit);
                } else {
                    DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.ask_assn_exit), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            readyAssnQuit();
                            dismissProgressDialog(sweetAlertDialog);
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void responseMyAssnDetail(AssnDetailResponse response) {
        AssnDetailResponse.AssnDetailResponseData responseData = response.getData();
        mineUid = responseData.getUid();
        tvIntro.setText(responseData.getIntroduction());
        tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getNotice_created_at())));
        tvNotice.setText(responseData.getNotice().trim());
        tvAssnName.setText(responseData.getAname());
        tvNum.setText(responseData.getMember_number() + "人");
        if (response.getData().getNewMember() == 1) {
            tvExmine.setVisibility(VISIBLE);
        } else {
            tvExmine.setVisibility(GONE);
        }
        Glide.with(getContext()).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imgAvatar);
    }

    @Override
    public void responseAssnMemberList(AssnMemberListResponse response) {
        ArrayList<AssnMemberListResponse.AssnMemberResponseData> data = response.getData();
        for (int i = 0; i < data.size() && i < 7; i++) {
            avatars.add(data.get(i).getAvatar_url());
        }
        avatarAdapter.notifyDataSetChanged();
    }

    @Override
    public void responseAssnQuit(BaseResponse response) {
        hideWindow(true);
        showToast(R.string.exit_success);
        getNotifyManager().notify(NotifyIDDefine.NOTICE_MYASSN);
    }

    @Override
    public void responseMyAssnActivity(AssnActivityResponse response) {
        if (isRefresh) {
            actiDatas.clear();
        }
        actiDatas.addAll(response.getData());
        assnActivityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTICE_MYASSN_DETAIL) {
            readyMyAssnDetail();
        } else if (mes.what == NotifyIDDefine.NOTICE_MYASSN_DETAIL) {
            isRefresh = true;
            readyMyAssnActivity();
        }
    }
}
