package cn.flyexp.window.assn;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.AssnMemberAdapter;
import cn.flyexp.callback.assn.AssnMemberCallback;
import cn.flyexp.entity.AssnMemberListRequest;
import cn.flyexp.entity.AssnMemberListResponse;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DelelteMemberRequest;
import cn.flyexp.entity.MemberManageRequest;
import cn.flyexp.presenter.assn.AssnMemberPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/12/19.
 */
public class AssnMemberWindow extends BaseWindow implements AssnMemberCallback.ResponseCallback {

    @InjectView(R.id.rv_member)
    LoadMoreRecyclerView rvMember;
    private AssnMemberAdapter assnMemberAdapter;
    private AlertDialog assnMemberDialog;
    private View layoutCall;
    private View layoutSetMember;
    private View layoutSetManager;
    private View layoutExpel;
    private int mineUid;
    private View assnMemberLayout;
    private int currPosition;
    private int aid;
    private int page = 1;
    private int level;
    private ArrayList<AssnMemberListResponse.AssnMemberResponseData> memberDatas = new ArrayList<>();
    private AssnMemberPresenter assnMemberPresenter;
    private boolean isRefreshMember;
    private SweetAlertDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_member;
    }

    public AssnMemberWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        level = bundle.getInt("level");
        mineUid = bundle.getInt("uid");
        assnMemberPresenter = new AssnMemberPresenter(this);
        assnMemberLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_assn_member, null);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
        initView();
        readyAssnMemberList();
    }

    private void initView() {
        assnMemberAdapter = new AssnMemberAdapter(getContext(), memberDatas);
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
        AssnMemberListResponse.AssnMemberResponseData responseData = memberDatas.get(position);
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

    private OnClickListener memberDialogOnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.layout_call:
                    assnMemberDialog.dismiss();
                    String mobile = memberDatas.get(currPosition).getMobile_no();
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

    private void readySetLevel(int level) {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            MemberManageRequest memberManageRequest = new MemberManageRequest();
            memberManageRequest.setAssociation_id(aid);
            memberManageRequest.setToken(token);
            memberManageRequest.setUid(memberDatas.get(currPosition).getUid());
            memberManageRequest.setLevel(level);
            assnMemberPresenter.requestMemberManage(memberManageRequest);
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
            delelteMemberRequest.setUid(memberDatas.get(currPosition).getUid());
            assnMemberPresenter.requestDeleteMember(delelteMemberRequest);
            loadingDialog.show();
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
            assnMemberPresenter.requestAssnMemberList(assnMemberListRequest);
        }
    }

    @OnClick({R.id.img_back})
    void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                hideWindow(true);
                break;
        }
    }

    @Override
    public void responseAssnMemberList(AssnMemberListResponse response) {
        if (isRefreshMember) {
            memberDatas.clear();
        }
        memberDatas.addAll(response.getData());
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
}
