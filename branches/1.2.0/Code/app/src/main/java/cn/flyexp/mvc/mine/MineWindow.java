package cn.flyexp.mvc.mine;

import android.graphics.Bitmap;
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
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.view.RoundImageView;
//import cn.flyexp.view.RoundImageView;


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
    //    private RoundImageView iv_avatar;
    private TextView tv_year;
    private MyInfoResponse.MyInfoResponseData responseData;
    private ArrayList<MineBean> mineBeanArrayList = new ArrayList<>();
    private ImageView iv_gender;
    private MineAdapter mineAdapter;
    private ImageView iv_mytask;
    private ImageView iv_message;
    private boolean isMesRemind;
    private boolean isTaskRemind;
    private int isAuth;
    private View certifiLayout;

    public MineWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    public void request() {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            return;
        }
        MyInfoRequest myInfoRequest = new MyInfoRequest();
        myInfoRequest.setToken(token);
        callBack.getMyInfo(myInfoRequest);
    }

    private RecyclerView rv_mine;
    private RoundImageView round;
    private TextView userName;
    private TextView address;

    private void initView() {
        setContentView(R.layout.layout_mine_mainpage);
        findViewById(R.id.mine_mymessage).setOnClickListener(this);
        findViewById(R.id.sel_school).setOnClickListener(this);
        findViewById(R.id.tape).setOnClickListener(this);
        findViewById(R.id.mime_topic_center).setOnClickListener(this);
        findViewById(R.id.mime_my_task).setOnClickListener(this);
        findViewById(R.id.mine_my_order).setOnClickListener(this);
        findViewById(R.id.my_wallet).setOnClickListener(this);
        findViewById(R.id.my_points).setOnClickListener(this);
        findViewById(R.id.my_corporation).setOnClickListener(this);
        findViewById(R.id.my_store).setOnClickListener(this);
        findViewById(R.id.my_enjoy).setOnClickListener(this);
        findViewById(R.id.my_setting).setOnClickListener(this);

        round = (RoundImageView) findViewById(R.id.round);
        userName = (TextView) findViewById(R.id.user_name);
        address = (TextView) findViewById(R.id.address);

        iv_mytask = (ImageView) findViewById(R.id.iv_mytask);
        iv_message = (ImageView) findViewById(R.id.iv_message);

        certifiLayout = findViewById(R.id.layout_certification);
        certifiLayout.setOnClickListener(this);

//        setContentView(R.layout.window_mine);
//        findViewById(R.id.tv_changecampus).setOnClickListener(this);
////        findViewById(R.id.tv_setting).setOnClickListener(this);
//        findViewById(R.id.tv_tape).setOnClickListener(this);
//        iv_bg = (ImageView) findViewById(R.id.iv_bg);
//        iv_avatar = (RoundImageView) findViewById(R.id.iv_avatar);
//        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
//        tv_college = (TextView) findViewById(R.id.tv_college);
//        tv_introduction = (TextView) findViewById(R.id.tv_introduction);
//        tv_year = (TextView) findViewById(R.id.tv_year);
//        iv_gender = (ImageView) findViewById(R.id.iv_gender);
//        rv_mine = (RecyclerView) findViewById(R.id.rv_mine);
//        rv_mine.setAdapter(mineAdapter = new MineAdapter(getContext(), mineBeanArrayList));
//        mineAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                String name = mineBeanArrayList.get(position).getName();
//                LogUtil.error(getClass(), "name" + name);
//                if (name.equals("我的资料")) {
//                    callBack.myInfoEnter(responseData);
//                } else if (name.equals("我的校汇圈")) {
//                    callBack.myTopicEnter();
//                } else if (name.equals("社团管理")) {
//                    callBack.assnManageEnter();
//                } else if (name.equals("我的钱包")) {
//                    callBack.walletEnter();
//                } else if (name.equals("我的任务")) {
//                    callBack.myTaskEnter();
//                } else if (name.equals("我的积分")) {
//                    callBack.integralEnter(responseData);
//                } else if (name.equals("店铺收藏")) {
//                    showToast("敬请期待~");
////                    callBack.webWindowEnter("storeCollection", 0);
//                } else if (name.equals("我要开店")) {
//                    showToast("敬请期待~");
////                    if (responseData.getIs_auth() == 0) {
////                        showToast("您还未实名");
////                        return;
////                    } else if (responseData.getIs_auth() == 2) {
////                        showToast("实名资料已在审核的路上，请等待...");
////                        return;
////                    }
////                    if (responseData.getIs_merchant() == 0) {
////                        callBack.webWindowEnter("storeApply", 0);
////                    } else if (responseData.getIs_cheif() == 2) {
////                        showToast("开店资料已在审核的路上，请等待...");
////                    }
//                } else if (name.equals("每日分享")) {
//                    callBack.shareEnter();
//                } else if (name.equals("社团入驻")) {
//                    if (responseData.getIs_auth() == 0) {
//                        showToast(getContext().getString(R.string.none_certifition));
//                        return;
//                    } else if (responseData.getIs_auth() == 2) {
//                        showToast(getContext().getString(R.string.certifing));
//                        return;
//                    }
//                    if (responseData.getIs_cheif() == 0) {
//                        callBack.assnJoinEnter();
//                    } else if (responseData.getIs_cheif() == 2) {
//                        showToast("入驻资料已在审核的路上，请等待...");
//                    }
//                } else if (name.equals("店铺管理")) {
//                    showToast("敬请期待~");
////                    callBack.webWindowEnter("storeManage", 0);
//                }
//            }
//
//        });
//        rv_mine.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        rv_mine.setAdapter(mineAdapter);
//        rv_mine.addItemDecoration(new GridSpacingItemDecoration(3, CommonUtil.dip2px(getContext(), 1), true));
//        rv_mine.setItemAnimator(new DefaultItemAnimator());
//        rv_mine.setHasFixedSize(false);
//        rv_mine.setNestedScrollingEnabled(false);
    }


    public void responseData(MyInfoResponse.MyInfoResponseData responseData) {
        if (responseData == null) {
            return;
        }
        this.responseData = responseData;
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(getContext()).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(getContext(), 50), CommonUtil.dip2px(getContext(), 50))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(getResources().getDrawable(R.mipmap.icon_defaultavatar_big)).centerCrop().into(round);
        } else {
            round.setImageResource(R.mipmap.icon_defaultavatar_big);
        }
        userName.setText(responseData.getNickname());
        address.setText(responseData.getCollege());
        isAuth = responseData.getIs_auth();

        if (isAuth == 1) {
            certifiLayout.setVisibility(View.GONE);
        } else {
            certifiLayout.setVisibility(View.VISIBLE);
        }

        WindowHelper.putIntByPreference("association_id", responseData.getAssociation_id());
        WindowHelper.putStringByPreference("mobile_no", responseData.getMobile_no());
        WindowHelper.putStringByPreference("address", responseData.getAddress());
        WindowHelper.putIntByPreference("is_auth", responseData.getIs_auth());
        WindowHelper.putIntByPreference("is_paypwd", responseData.getIs_paypassword());
        WindowHelper.putIntByPreference("is_alipay", responseData.getIs_alipay());
        WindowHelper.putFloatByPreference("balance", responseData.getWallet());
//
//        //森彬添加
        WindowHelper.putStringByPreference("nickName", responseData.getNickname());
    }

    public void remindMyTask() {
        isTaskRemind = true;
        iv_mytask.setImageDrawable(getResources().getDrawable(R.mipmap.icon_mine_campustask_remind));
    }

    public void remindMessage() {
        isMesRemind = true;
        iv_message.setImageDrawable(getResources().getDrawable(R.drawable.icon_message_sel));
        iv_message.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tape:
                if (isMesRemind) {
                    isMesRemind = false;
                    iv_message.setImageDrawable(getResources().getDrawable(R.drawable.icon_message_nor));
                }
                callBack.messageEnter();
                break;
            case R.id.mine_mymessage:
                callBack.myInfoEnter(responseData);
                break;
            case R.id.sel_school:
                break;
            case R.id.mime_topic_center:
                callBack.myTopicEnter();
                break;
            case R.id.mime_my_task:
                callBack.myTaskEnter();
                if (isTaskRemind) {
                    isTaskRemind = false;
                    iv_mytask.setImageDrawable(getResources().getDrawable(R.drawable.icon_mine_campustask));
                }
                break;
            case R.id.mine_my_order:
                WindowHelper.showToast("敬请期待~");
                break;
            case R.id.my_wallet:
                callBack.walletEnter();
                break;
            case R.id.my_points:
                callBack.integralEnter(responseData);
                break;
            case R.id.my_corporation:
                callBack.myAssnEnter();
                break;
            case R.id.my_store:
                WindowHelper.showToast("敬请期待~");
//                callBack.webWindowEnter("storeManage",0);
                break;
            case R.id.my_setting:
                callBack.settingEnter();
                break;
            case R.id.my_enjoy:
                callBack.shareEnter();
                break;
            case R.id.layout_certification:
                if (isAuth == 0) {
                    callBack.certificationEnter();
                } else if (isAuth == 2) {
                    WindowHelper.showToast("实名资料已在审核的路上，请等待...");
                }
                break;
        }
    }
}
