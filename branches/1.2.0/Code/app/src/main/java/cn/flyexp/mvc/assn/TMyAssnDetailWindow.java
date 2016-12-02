package cn.flyexp.mvc.assn;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnMemberAdapter;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.AssnMemberRequest;
import cn.flyexp.entity.AssnMemberResponse;
import cn.flyexp.entity.DeleteMemberRequest;
import cn.flyexp.entity.SetMemberLevelRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreListener;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.view.ProgressView;

/**
 * Created by tanxinye on 2016/9/30.
 */
public class TMyAssnDetailWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private LoadMoreRecyclerView rv_myassnmember;
    private TextView tv_detail;
    private TextView tv_assn;
    private ImageView iv_avatar;
    private ImageView iv_bg;
    private ArrayList<AssnMemberResponse.AssnMemberResponseData> data = new ArrayList<>();
    private AssnMemberAdapter assnMemberAdapter;
    private int aid;
    private PopupWindow menuPopupWindow;
    private View menuPopLayout;
    private ImageView iv_assnnotice;
    private ImageView iv_assnexamine;
    private ImageView iv_assnactivity;
    private boolean isNoticeRemind;
    private boolean isActiRemind;
    private boolean isExamineRemind;
    private boolean isAssnHeader;
    private int page = 1;
    private View myassnLayout;
    private PopupWindow memberPopupWindow;
    private View memberPopLayout;
    private boolean isAssnAdmin;
    private Button btn_set;
    private Button btn_getout;
    private int currItem = -1;
    private boolean isSetAdmin;
    private int level;
    private AssnDetailResponse.AssnDetailResponseData detailData;

    public TMyAssnDetailWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_myassn_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        iv_assnnotice.setOnClickListener(this);
        iv_assnexamine.setOnClickListener(this);
        iv_assnactivity.setOnClickListener(this);


        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        tv_assn = (TextView) findViewById(R.id.tv_assn);
        tv_detail = (TextView) findViewById(R.id.tv_detail);


        memberPopLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_assn_member, null);
        memberPopLayout.findViewById(R.id.btn_call).setOnClickListener(this);

        btn_set = (Button) memberPopLayout.findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);
        btn_getout = (Button) memberPopLayout.findViewById(R.id.btn_getout);
        btn_getout.setOnClickListener(this);
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
                    btn_set.setText("降级为成员");
                    isSetAdmin = false;
                } else {
                    btn_set.setText("设置管理员");
                    isSetAdmin = true;
                }
                if (isAssnAdmin) {
                    btn_getout.setVisibility(VISIBLE);
                    if (isAssnHeader) {
                        btn_set.setVisibility(VISIBLE);
                    }
                } else {
                    btn_set.setVisibility(GONE);
                    btn_getout.setVisibility(GONE);
                }
                currItem = position;
                memberPopupWindow.showAtLocation(myassnLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        rv_myassnmember.setAdapter(assnMemberAdapter);
        rv_myassnmember.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_myassnmember.setHasFixedSize(false);
        rv_myassnmember.setNestedScrollingEnabled(false);
        rv_myassnmember.setFootLoadingView(ProgressView.BallPulse);
        rv_myassnmember.setFootEndView("没有更多成员了~");
        rv_myassnmember.setLoadMoreListener(new LoadMoreListener() {

            @Override
            public void onLoadMore() {
                page++;
                getMemberList();
            }
        });
    }

    public void getAssnDetail() {
        String token = WindowHelper.getStringByPreference("token");
        if (!TextUtils.isEmpty(token)) {
            AssnDetailRequest assnDetailRequest = new AssnDetailRequest();
            assnDetailRequest.setToken(token);
            assnDetailRequest.setAssociation_id(aid);
            callBack.getMyAssnDetails(assnDetailRequest);
        } else {
            callBack.loginWindowEnter();
        }
    }

    public void getMemberList() {
        String token = WindowHelper.getStringByPreference("token");
        if (!TextUtils.isEmpty(token)) {
            AssnMemberRequest assnMemberRequest = new AssnMemberRequest();
            assnMemberRequest.setToken(token);
            assnMemberRequest.setAssociation_id(aid);
            assnMemberRequest.setPage(page);
            callBack.getMemberList(assnMemberRequest);
        } else {
            callBack.loginWindowEnter();
        }
    }

    public void responseData(AssnDetailResponse.AssnDetailResponseData responseData) {
        this.detailData = responseData;
        tv_assn.setText(responseData.getAname());
        tv_detail.setText(responseData.getMember_number() + "人活跃 · " + responseData.getActivity_count() + "活动 · " + responseData.getLabel());
        if (!TextUtils.isEmpty(responseData.getAvatar_url())) {
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 50), CommonUtil.dip2px(getContext(), 50))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().into(iv_avatar);
        }
        if (!TextUtils.isEmpty(responseData.getBackground_url())) {
            Picasso.with(getContext()).load(responseData.getBackground_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.getScreenWidth(getContext()), CommonUtil.dip2px(getContext(), 150))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().into(iv_bg);
        }
    }

    public void responseData(ArrayList<AssnMemberResponse.AssnMemberResponseData> responseData) {
        data.addAll(responseData);
        assnMemberAdapter.notifyDataSetChanged();
    }

    public void init(int aid, int level) {
        this.level = level;
        this.aid = aid;
        if (level == 0) {
            iv_assnexamine.setVisibility(GONE);
        } else {
            iv_assnexamine.setVisibility(VISIBLE);
            isAssnAdmin = true;
            if (level == 1) {
                isAssnHeader = true;
            }
        }
        getAssnDetail();
        getMemberList();
        if (WindowHelper.getBooleanByPreference(SharedPrefs.KET_REMIND_NOTICE)) {
            remindNotice(WindowHelper.getIntByPreference(SharedPrefs.KET_REMIND_NOTICE_AID));
            WindowHelper.putBooleanByPreference(SharedPrefs.KET_REMIND_NOTICE, false);
        }
        if (WindowHelper.getBooleanByPreference(SharedPrefs.KET_REMIND_ACTI)) {
            remindActi(WindowHelper.getIntByPreference(SharedPrefs.KET_REMIND_ACTI_AID));
            WindowHelper.putBooleanByPreference(SharedPrefs.KET_REMIND_ACTI, false);
        }
        if (WindowHelper.getBooleanByPreference(SharedPrefs.KET_REMIND_EXAMINE)) {
            remindExamine(WindowHelper.getIntByPreference(SharedPrefs.KET_REMIND_EXAMINE_AID));
            WindowHelper.putBooleanByPreference(SharedPrefs.KET_REMIND_EXAMINE, false);
        }
    }

    public void remindNotice(int aid) {
        if (this.aid != aid) {
            return;
        }
        isNoticeRemind = true;
        iv_assnnotice.setImageDrawable(getResources().getDrawable(R.mipmap.icon_announcement_ing));
    }

    public void remindActi(int aid) {
        if (this.aid != aid) {
            return;
        }
        isActiRemind = true;
        iv_assnactivity.setImageDrawable(getResources().getDrawable(R.mipmap.icon_activity_ing));
    }

    public void remindExamine(int aid) {
        if (this.aid != aid) {
            return;
        }
        isExamineRemind = true;
        iv_assnexamine.setImageDrawable(getResources().getDrawable(R.mipmap.icon_audit_ing));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_exit:
                menuPopupWindow.dismiss();
                if (isAssnHeader) {
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
            case R.id.tv_about:
                menuPopupWindow.dismiss();
                WindowHelper.showAlertDialog(detailData.getIntroduction());
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
