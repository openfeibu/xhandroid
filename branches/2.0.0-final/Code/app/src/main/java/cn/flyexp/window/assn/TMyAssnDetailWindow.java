package cn.flyexp.window.assn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.AssnMemberAdapter;
import cn.flyexp.callback.assn.MyAssnDetailCallback;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.entity.AssnMemberListRequest;
import cn.flyexp.entity.AssnMemberListResponse;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DelelteMemberRequest;
import cn.flyexp.entity.MemberManageRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.assn.MyAssnDetailPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.PopupUtil;
import cn.flyexp.util.ScreenHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class TMyAssnDetailWindow extends BaseWindow implements MyAssnDetailCallback.ResponseCallback, NotifyManager.Notify {

    @InjectView(R.id.img_bg)
    ImageView imgBg;
    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_assnname)
    TextView tvAssnName;
    @InjectView(R.id.tv_detail)
    TextView tvDetail;
    @InjectView(R.id.img_assnnotice)
    ImageView imgAssnNotice;
    @InjectView(R.id.img_assnactivity)
    ImageView imgAssnActivity;
    @InjectView(R.id.img_assnexamine)
    ImageView imgAssnExamine;
    @InjectView(R.id.rv_member)
    LoadMoreRecyclerView rvMember;

    private MyAssnDetailPresenter myAssnDetailPresenter;
    private ArrayList<AssnMemberListResponse.AssnMemberResponseData> datas = new ArrayList<>();
    private AssnMemberAdapter assnMemberAdapter;
    private int page = 1;
    private String intro;
    private int aid;
    private int level;
    private View assnMemberLayout;
    private AlertDialog assnMemberDialog;
    private int mineUid;
    private int currPosition;
    private View layoutCall;
    private View layoutSetMember;
    private View layoutSetManager;
    private View layoutExpel;
    private View menuPopLayout;
    private String notice;
    private SweetAlertDialog loadingDialog;
    private boolean isRefreshMember;
    private boolean isRefresh;
    private PopupUtil popupHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.window_myassn_detailt;
    }

    public TMyAssnDetailWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        level = bundle.getInt("level");
        myAssnDetailPresenter = new MyAssnDetailPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTICE_MYASSN_DETAIL, this);

        initView();
        readyMyAssnDetail();
        readyAssnMemberList();
    }

    private void initView() {
        assnMemberLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_assn_member, null);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));

        assnMemberAdapter = new AssnMemberAdapter(getContext(), datas);
        assnMemberAdapter.setOnItemClickLinstener(new AssnMemberAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                showMemberDialog(position);
            }
        });
        rvMember.setAdapter(assnMemberAdapter);
        rvMember.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMember.addItemDecoration(new DividerItemDecoration(getContext()));
        rvMember.setHasFixedSize(false);
        rvMember.setNestedScrollingEnabled(false);
        rvMember.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                readyAssnMemberList();
            }
        });

        if (level == 0) {
            imgAssnExamine.setVisibility(GONE);
        } else {
            imgAssnExamine.setVisibility(VISIBLE);
        }

        menuPopLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_assn_detail_menu, null);
        menuPopLayout.findViewById(R.id.tv_exit).setOnClickListener(menuOnClick);
        menuPopLayout.findViewById(R.id.tv_about).setOnClickListener(menuOnClick);
        popupHelper = new PopupUtil(new PopupUtil.Builder(menuPopLayout, ScreenHelper.dip2px(getContext(), 120), ViewGroup.LayoutParams.WRAP_CONTENT).create());
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
            assnMemberListRequest.setPage(page);
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
            loadingDialog.show();
        }
    }

    private void readySetLevel(int level) {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            MemberManageRequest memberManageRequest = new MemberManageRequest();
            memberManageRequest.setAssociation_id(aid);
            memberManageRequest.setToken(token);
            memberManageRequest.setUid(datas.get(currPosition).getUid());
            memberManageRequest.setLevel(level);
            myAssnDetailPresenter.requestMemberManage(memberManageRequest);
            loadingDialog.show();
        }
    }

    private void readyExpel() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            DelelteMemberRequest delelteMemberRequest = new DelelteMemberRequest();
            delelteMemberRequest.setAssociation_id(aid);
            delelteMemberRequest.setToken(token);
            delelteMemberRequest.setUid(datas.get(currPosition).getUid());
            myAssnDetailPresenter.requestDeleteMember(delelteMemberRequest);
            loadingDialog.show();
        }
    }

    private void showMemberDialog(int position) {
        if (assnMemberDialog == null) {
            assnMemberDialog = new AlertDialog.Builder(getContext()).setView(assnMemberLayout).create();
            layoutCall = assnMemberLayout.findViewById(R.id.layout_call);
            layoutSetMember = assnMemberLayout.findViewById(R.id.layout_setmember);
            layoutSetManager = assnMemberLayout.findViewById(R.id.layout_setmanager);
            layoutExpel = assnMemberLayout.findViewById(R.id.layout_expel);

            layoutCall.setOnClickListener(memberDialogOnClick);
            layoutSetMember.setOnClickListener(memberDialogOnClick);
            layoutSetManager.setOnClickListener(memberDialogOnClick);
            layoutExpel.setOnClickListener(memberDialogOnClick);
        }
        AssnMemberListResponse.AssnMemberResponseData responseData = datas.get(position);
        //当前项是自己则不响应
        if (responseData.getUid() == mineUid) {
            return;
        }
        //如果是成员只显示呼叫
        if (level == 0) {
            layoutCall.setVisibility(VISIBLE);
            layoutSetMember.setVisibility(GONE);
            layoutSetManager.setVisibility(GONE);
            layoutExpel.setVisibility(GONE);
        } else if (level == 1) {
            //当前项是成员升级 否则降级
            if (responseData.getLevel() == 0) {
                layoutCall.setVisibility(VISIBLE);
                layoutSetMember.setVisibility(GONE);
                layoutSetManager.setVisibility(VISIBLE);
                layoutExpel.setVisibility(VISIBLE);
            } else {
                layoutCall.setVisibility(VISIBLE);
                layoutSetMember.setVisibility(VISIBLE);
                layoutSetManager.setVisibility(GONE);
                layoutExpel.setVisibility(VISIBLE);
            }
        } else if (level == 2 || level == 3) {
            if (responseData.getLevel() == 0) {
                layoutCall.setVisibility(VISIBLE);
                layoutSetMember.setVisibility(GONE);
                layoutSetManager.setVisibility(GONE);
                layoutExpel.setVisibility(VISIBLE);
            } else {
                layoutCall.setVisibility(VISIBLE);
                layoutSetMember.setVisibility(GONE);
                layoutSetManager.setVisibility(GONE);
                layoutExpel.setVisibility(GONE);
            }
        }
        currPosition = position;
        assnMemberDialog.show();
    }

    private void showAssnNoticeDialog() {
        if (TextUtils.isEmpty(notice)) {
            notice = getResources().getString(R.string.no_notice);
        }
        if (level == 0) {
            DialogHelper.showSingleDialog(getContext(), notice, true, getResources().getString(R.string.isee), null);
        } else {
            DialogHelper.showSelectDialog(getContext(), notice, getResources().getString(R.string.edit), getResources().getString(R.string.isee), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("aid", aid);
                    openWindow(WindowIDDefine.WINDOW_ASSN_NOTICE_PULISH, bundle);
                    dismissProgressDialog(sweetAlertDialog);
                }
            });
        }
    }

    private OnClickListener memberDialogOnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_call:
                    assnMemberDialog.dismiss();
                    String mobile = datas.get(currPosition).getMobile_no();
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri uri = Uri.parse("tel:" + mobile);
                    intent.setData(uri);
                    try {
                        getContext().startActivity(intent);
                    } catch (Exception e) {
                        showToast(R.string.unable_call);
                    }
                    break;
                case R.id.layout_setmember:
                    assnMemberDialog.dismiss();
                    readySetLevel(0);
                    break;
                case R.id.layout_setmanager:
                    assnMemberDialog.dismiss();
                    readySetLevel(2);
                    break;
                case R.id.layout_expel:
                    assnMemberDialog.dismiss();
                    DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.ask_expel), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            readyExpel();
                            dismissProgressDialog(sweetAlertDialog);
                        }
                    });
                    break;
            }
        }

    };

    private OnClickListener menuOnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            popupHelper.dismiss();
            switch (view.getId()) {
                case R.id.tv_exit:
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
                case R.id.tv_about:
                    if (TextUtils.isEmpty(intro)) {
                        intro = getResources().getString(R.string.no_intro);
                    }
                    DialogHelper.showSingleDialog(getContext(), intro, true, getResources().getString(R.string.close), null);
                    break;
            }
        }

    };

    @OnClick({R.id.img_back,  R.id.img_assnnotice, R.id.img_assnactivity, R.id.img_assnexamine})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
//            case R.id.img_menu:
//                popupHelper.showAsDropDown(menuLayout, ScreenHelper.getScreenWidth(getContext()) - ScreenHelper.dip2px(getContext(), 120), 0);
//                break;
            case R.id.img_assnnotice:
                showAssnNoticeDialog();
                break;
            case R.id.img_assnactivity:
                Bundle bundle = new Bundle();
                bundle.putInt("aid", aid);
                bundle.putInt("level", level);
                openWindow(WindowIDDefine.WINDOW_MYASSN_ACTIVITY, bundle);
                break;
            case R.id.img_assnexamine:
                Bundle bundle2 = new Bundle();
                bundle2.putInt("aid", aid);
                openWindow(WindowIDDefine.WINDOW_ASSN_EXAMINE_LIST, bundle2);
                break;
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void responseMyAssnDetail(AssnDetailResponse response) {
        AssnDetailResponse.AssnDetailResponseData responseData = response.getData();
        mineUid = responseData.getUid();
        intro = responseData.getIntroduction();
        notice = responseData.getNotice();
        tvAssnName.setText(responseData.getAname());
        tvDetail.setText(String.format(getResources().getString(R.string.assnlist_detail), responseData.getMember_number(), responseData.getActivity_count(), responseData.getLabel()));
        Glide.with(getContext()).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imgAvatar);
        Glide.with(getContext()).load(responseData.getBackground_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imgBg);
        if (checkSampleNotice() && !isRefresh) {
            showAssnNoticeDialog();
        }
    }

    private boolean checkSampleNotice() {
        String noticeJson = SharePresUtil.getString(SharePresUtil.KEY_ACTI_NOTICE);
        HashMap<Integer, String> hashMap = new HashMap<>();
        if (TextUtils.isEmpty(noticeJson)) {
            String json = GsonUtil.getInstance().toJson(hashMap);
            noticeJson = json;
        }
        hashMap = GsonUtil.getInstance().fromJson(noticeJson, HashMap.class);
        if (hashMap == null) {
            hashMap = new HashMap<>();
        }
        if (hashMap.containsKey(String.valueOf(aid))) {
            String noti = hashMap.get(String.valueOf(aid));
            if (TextUtils.equals(notice, noti)) {
                return false;
            }
        }
        hashMap.put(aid, notice);
        String json = GsonUtil.getInstance().toJson(hashMap);
        SharePresUtil.putString(SharePresUtil.KEY_ACTI_NOTICE, json);
        return true;
    }

    @Override
    public void responseAssnMemberList(AssnMemberListResponse response) {
        if (isRefreshMember) {
            datas.clear();
        }
        datas.addAll(response.getData());
        assnMemberAdapter.notifyDataSetChanged();
        isRefreshMember = false;
    }

    @Override
    public void responseDeleteMember(BaseResponse response) {
        showToast(R.string.expel_success);
        readyAssnMemberList();
    }

    @Override
    public void responseMemberManage(BaseResponse response) {
        showToast(R.string.set_success);
        readyAssnMemberList();
    }

    @Override
    public void responseAssnQuit(BaseResponse response) {
        hideWindow(true);
        showToast(R.string.exit_success);
        isRefreshMember = true;
        getNotifyManager().notify(NotifyIDDefine.NOTICE_MYASSN);
    }

    @Override
    public void onNotify(Message mes) {
        readyMyAssnDetail();
        isRefresh = true;
    }
}

