package cn.flyexp.mvc.assn;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnMemberAdapter;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.AssnMemberRequest;
import cn.flyexp.entity.AssnMemberResponse;
import cn.flyexp.entity.DeleteMemberRequest;
import cn.flyexp.entity.SetMemberLevelRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;

/**
 * Created by tanxinye on 2016/12/1.
 */
public class AssnMemberWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private int aid;
    private int level;
    private int page = 1;
    private ImageView imgExmine;
    private LoadMoreRecyclerView rvMember;
    private ArrayList<AssnMemberResponse.AssnMemberResponseData> data = new ArrayList<>();
    private AssnMemberAdapter assnMemberAdapter;
    private View memberLayout;
    private Button btnSet;
    private Button btnGetout;
    private int currItem;
    private View memberPopLayout;
    private PopupWindow memberPopupWindow;
    private boolean isSetAdmin;

    public AssnMemberWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_member);
        findViewById(R.id.img_back).setOnClickListener(this);

        imgExmine = (ImageView) findViewById(R.id.img_exmine);
        imgExmine.setOnClickListener(this);

        memberLayout = findViewById(R.id.layout_member);

        memberPopLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_assn_member, null);
        memberPopLayout.findViewById(R.id.btn_call).setOnClickListener(this);

        btnSet = (Button) memberPopLayout.findViewById(R.id.btn_set);
        btnSet.setOnClickListener(this);
        btnGetout = (Button) memberPopLayout.findViewById(R.id.btn_getout);
        btnGetout.setOnClickListener(this);
        memberPopLayout.findViewById(R.id.btn_getout).setOnClickListener(this);
        memberPopupWindow = new PopupWindow(memberPopLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        memberPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        memberPopupWindow.setFocusable(true);
        memberPopupWindow.setOutsideTouchable(true);
        memberPopupWindow.setAnimationStyle(R.style.popwin_anim_style);

        assnMemberAdapter = new AssnMemberAdapter(getContext(), data);
        assnMemberAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (data.get(position).getLevel() != 0) {
                    btnSet.setText("降级为成员");
                    isSetAdmin = false;
                } else {
                    btnSet.setText("设置管理员");
                    isSetAdmin = true;
                }
                if (level != 0) {
                    btnGetout.setVisibility(VISIBLE);
                    if (level == 1) {
                        btnSet.setVisibility(VISIBLE);
                    }
                } else {
                    btnSet.setVisibility(GONE);
                    btnGetout.setVisibility(GONE);
                }
                currItem = position;
                memberPopupWindow.showAtLocation(memberLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        rvMember = (LoadMoreRecyclerView) findViewById(R.id.rv_member);
        rvMember.setAdapter(assnMemberAdapter);
        rvMember.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMember.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                readyAssnMember();
            }
        });
    }

    public void init(Bundle bundle) {
        aid = bundle.getInt("aid");
        level = bundle.getInt("level");
        boolean isExamine = bundle.getBoolean("isExamine");
        if (level != 0 && isExamine) {
            imgExmine.setVisibility(VISIBLE);
        } else {
            imgExmine.setVisibility(GONE);
        }
        readyAssnMember();
    }

    private void readyAssnMember() {
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (TextUtils.isEmpty(token)) {
            callBack.loginWindowEnter();
        } else {
            AssnMemberRequest assnMemberRequest = new AssnMemberRequest();
            assnMemberRequest.setAssociation_id(aid);
            assnMemberRequest.setToken(token);
            assnMemberRequest.setPage(page);
            callBack.getMemberList(assnMemberRequest);
        }
    }

    public void memberResponse(ArrayList<AssnMemberResponse.AssnMemberResponseData> data) {
        this.data.addAll(data);
        if (data.isEmpty()) {
            rvMember.loadMoreEnd();
        } else {
            rvMember.loadMoreComplete();
        }
        assnMemberAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.img_exmine:
                callBack.assnExamineEnter(aid);
                break;
            case R.id.btn_set:
                memberPopupWindow.dismiss();
                String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    callBack.loginWindowEnter();
                    return;
                }
                SetMemberLevelRequest setMemberLevelRequest = new SetMemberLevelRequest();
                setMemberLevelRequest.setToken(token);
                setMemberLevelRequest.setAssociation_id(aid);
                setMemberLevelRequest.setUid(data.get(currItem).getUid());
                if (isSetAdmin) {
                    setMemberLevelRequest.setLevel(2);
                } else {
                    setMemberLevelRequest.setLevel(0);
                }
                callBack.setMemberLevel(setMemberLevelRequest);
                break;
            case R.id.btn_call:
                memberPopupWindow.dismiss();
                String mobile = data.get(currItem).getMobile_no();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri uri = Uri.parse("tel:" + mobile);
                intent.setData(uri);
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    WindowHelper.showToast("无法调用电话界面");
                }
                break;
            case R.id.btn_getout:
                memberPopupWindow.dismiss();
                String token2 = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
                if (TextUtils.isEmpty(token2)) {
                    callBack.loginWindowEnter();
                    return;
                }
                DeleteMemberRequest deleteMemberRequest = new DeleteMemberRequest();
                deleteMemberRequest.setUid(data.get(currItem).getUid());
                deleteMemberRequest.setAssociation_id(aid);
                deleteMemberRequest.setToken(token2);
                callBack.deleteMember(deleteMemberRequest);
                break;
        }
    }
}
