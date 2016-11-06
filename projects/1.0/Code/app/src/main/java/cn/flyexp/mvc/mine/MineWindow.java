package cn.flyexp.mvc.mine;

import android.graphics.Bitmap;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.MineAdapter;
import cn.flyexp.entity.MineBean;
import cn.flyexp.entity.MyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.GridSpacingItemDecoration;
import cn.flyexp.view.RoundImageView;


/**
 * Created by zlk on 2016/3/26.
 * Moditied by txy on 2016/8/16
 */
public class MineWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private ImageView iv_bg;
    private TextView tv_nickname;
    private TextView tv_college;
    private TextView tv_introduction;
    private RoundImageView iv_avatar;
    private TextView tv_year;
    private MyInfoResponse.MyInfoResponseData responseData;
    private ArrayList<MineBean> mineBeanArrayList = new ArrayList<>();
    private ImageView iv_gender;
    private MineAdapter mineAdapter;

    public MineWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getMyInfo(getMyInfoRequest());
    }

    private RecyclerView rv_mine;

    private void initView() {
        setContentView(R.layout.window_mine);
        findViewById(R.id.tv_changecampus).setOnClickListener(this);
        findViewById(R.id.tv_setting).setOnClickListener(this);
        iv_bg = (ImageView) findViewById(R.id.iv_bg);
        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_college = (TextView) findViewById(R.id.tv_college);
        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
        tv_year = (TextView) findViewById(R.id.tv_year);
        iv_gender = (ImageView) findViewById(R.id.iv_gender);
        rv_mine = (RecyclerView) findViewById(R.id.rv_mine);
        rv_mine.setAdapter(mineAdapter = new MineAdapter(getContext(), mineBeanArrayList));
        mineAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = mineBeanArrayList.get(position).getName();
                LogUtil.error(getClass(), "name" + name);
                if (name.equals("我的资料")) {
                    callBack.myInfoEnter(responseData);
                } else if (name.equals("我的校汇圈")) {
                    callBack.myTopicEnter();
                } else if (name.equals("社团管理")) {
                    callBack.assnManageEnter();
                } else if (name.equals("我的钱包")) {
                    callBack.walletEnter();
                } else if (name.equals("我的任务")) {
                    callBack.myTaskEnter();
                } else if (name.equals("我的积分")) {
                    callBack.integralEnter(responseData);
                } else if (name.equals("店铺收藏")) {
                    showToast("敬请期待~");
//                    callBack.webWindowEnter("storeCollection", 0);
                } else if (name.equals("我要开店")) {
                    showToast("敬请期待~");
//                    if (responseData.getIs_auth() == 0) {
//                        showToast("您还未实名");
//                        return;
//                    } else if (responseData.getIs_auth() == 2) {
//                        showToast("实名资料已在审核的路上，请等待...");
//                        return;
//                    }
//                    if (responseData.getIs_merchant() == 0) {
//                        callBack.webWindowEnter("storeApply", 0);
//                    } else if (responseData.getIs_cheif() == 2) {
//                        showToast("开店资料已在审核的路上，请等待...");
//                    }
                } else if (name.equals("每日分享")) {
                    callBack.shareEnter();
                } else if (name.equals("社团入驻")) {
                    if (responseData.getIs_auth() == 0) {
                        showToast(getContext().getString(R.string.none_certifition));
                        return;
                    } else if (responseData.getIs_auth() == 2) {
                        showToast(getContext().getString(R.string.certifing));
                        return;
                    }
                    if (responseData.getIs_cheif() == 0) {
                        callBack.assnJoinEnter();
                    } else if (responseData.getIs_cheif() == 2) {
                        showToast("入驻资料已在审核的路上，请等待...");
                    }
                } else if (name.equals("店铺管理")) {
                    showToast("敬请期待~");
//                    callBack.webWindowEnter("storeManage", 0);
                }
            }

        });
        rv_mine.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_mine.setAdapter(mineAdapter);
        rv_mine.addItemDecoration(new GridSpacingItemDecoration(3, CommonUtil.dip2px(getContext(), 1), true));
        rv_mine.setItemAnimator(new DefaultItemAnimator());
        rv_mine.setHasFixedSize(false);
        rv_mine.setNestedScrollingEnabled(false);
    }

    public void refreshData() {
        callBack.getMyInfo(getMyInfoRequest());
    }

    public MyInfoRequest getMyInfoRequest() {
        String token = getStringByPreference("token");
        if (token.equals("")) {
            return null;
        }
        MyInfoRequest myInfoRequest = new MyInfoRequest();
        myInfoRequest.setToken(token);
        return myInfoRequest;
    }

    public void myProileResponse(MyInfoResponse.MyInfoResponseData responseData) {
        this.responseData = responseData;
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 100), CommonUtil.dip2px(getContext(), 100))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(getResources().getDrawable(R.mipmap.icon_defaultavatar_big)).centerCrop().into(iv_avatar);
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.getScreenWidth(getContext()), CommonUtil.dip2px(getContext(), 250))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(getResources().getDrawable(R.mipmap.icon_defaultavatar_big)).centerCrop().into(iv_bg);
        } else {
            iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_big);
        }
        tv_nickname.setText(responseData.getNickname());
        tv_college.setText(responseData.getCollege());
        tv_introduction.setText(responseData.getIntroduction());
        tv_year.setText(responseData.getEnrollment_year() + "级");

        if (responseData.getGender() == 1) {
            iv_gender.setImageResource(R.mipmap.icon_mysex_man);
        } else if (responseData.getGender() == 2) {
            iv_gender.setImageResource(R.mipmap.icon_mysex_woman);
        }
        mineBeanArrayList.clear();
        mineBeanArrayList.add(new MineBean("我的资料", R.mipmap.icon_mine_myinfo));
        mineBeanArrayList.add(new MineBean("我的校汇圈", R.mipmap.icon_mine_mytopic));
        if (responseData.getIs_cheif() == 0|| responseData.getIs_cheif() == 2) {
            mineBeanArrayList.add(new MineBean("社团入驻", R.mipmap.icon_mine_communitymanagement));
        } else if (responseData.getIs_cheif() == 1) {
            mineBeanArrayList.add(new MineBean("社团管理", R.mipmap.icon_mine_communitymanagement));
        }
        mineBeanArrayList.add(new MineBean("我的钱包", R.mipmap.icon_mine_wallet));
        mineBeanArrayList.add(new MineBean("我的任务", R.mipmap.icon_mine_mytask));
        mineBeanArrayList.add(new MineBean("我的积分", R.mipmap.icon_mine_level));

        mineBeanArrayList.add(new MineBean("店铺收藏", R.mipmap.icon_mine_storecollection));
        if (responseData.getIs_merchant() == 0 || responseData.getIs_merchant() == 2) {
            mineBeanArrayList.add(new MineBean("我要开店", R.mipmap.icon_mine_openstore));
        } else if (responseData.getIs_merchant() == 1) {
            mineBeanArrayList.add(new MineBean("店铺管理", R.mipmap.icon_mine_myinfo));
        }
        mineBeanArrayList.add(new MineBean("每日分享", R.mipmap.icon_mine_share));
        mineAdapter.notifyDataSetChanged();


        putIntByPreference("association_id", responseData.getAssociation_id());
        putStringByPreference("mobile_no", responseData.getMobile_no());
        putStringByPreference("address", responseData.getAddress());
        putIntByPreference("is_auth", responseData.getIs_auth());
        putIntByPreference("is_paypwd", responseData.getIs_paypassword());
        putIntByPreference("is_alipay", responseData.getIs_alipay());
        putFloatByPreference("balance", responseData.getWallet());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:
                callBack.settingEnter();
                break;
            case R.id.tv_changecampus:
                showToast(getResources().getString(R.string.changecampus_error_hint));
                break;
        }
    }

}
