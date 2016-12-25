package cn.flyexp.window.mine;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.mine.MineCallback;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.presenter.mine.MinePresenter;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class MineWindow extends BaseWindow implements MineCallback.ResponseCallback, NotifyManager.Notify {

    @InjectView(R.id.img_avatar)
    ImageView imgAvatar;
    @InjectView(R.id.tv_nickname)
    TextView tvNickName;
    @InjectView(R.id.tv_campus)
    TextView tvCampus;
    @InjectView(R.id.tv_mytopic)
    TextView tvMyTopic;
    @InjectView(R.id.tv_mytask)
    TextView tvMyTask;
    @InjectView(R.id.tv_message)
    TextView tvMessage;

    private MinePresenter minePresenter;
    private MyInfoResponse.MyInfoResponseData responseData = new MyInfoResponse().new MyInfoResponseData();
    private Drawable remineTopic;
    private Drawable remineTask;
    private Drawable remineMessage;
    private boolean isTopicRemine;
    private boolean isTaskRemine;
    private boolean isMessageRemine;
    private Drawable topicDrawable;
    private Drawable taskDrawable;
    private Drawable messageDrawable;

    @Override
    protected int getLayoutId() {
        return R.layout.window_mine;
    }

    public MineWindow() {
        minePresenter = new MinePresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_MINE_REFRESH, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_MESSAGE_PUSH, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_MYTASK_PUSH, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_TOPIC_PUSH, this);
        initView();
        readyMineRequest();
    }

    private void initView() {
        remineTopic = getResources().getDrawable(R.mipmap.icon_topic_news);
        remineTask = getResources().getDrawable(R.mipmap.icon_mine_campustask_remind);
        remineMessage = getResources().getDrawable(R.mipmap.icon_message_sel);
        remineTopic.setBounds(0, 0, remineTopic.getMinimumWidth(), remineTopic.getMinimumHeight());
        remineTask.setBounds(0, 0, remineTopic.getMinimumWidth(), remineTopic.getMinimumHeight());
        remineMessage.setBounds(0, 0, remineTopic.getMinimumWidth(), remineTopic.getMinimumHeight());

        topicDrawable = getResources().getDrawable(R.mipmap.icon_mine_topic);
        taskDrawable = getResources().getDrawable(R.mipmap.icon_mine_campustask);
        messageDrawable = getResources().getDrawable(R.mipmap.icon_message_nor);
        topicDrawable.setBounds(0, 0, topicDrawable.getMinimumWidth(), topicDrawable.getMinimumHeight());
        taskDrawable.setBounds(0, 0, taskDrawable.getMinimumWidth(), taskDrawable.getMinimumHeight());
        messageDrawable.setBounds(0, 0, messageDrawable.getMinimumWidth(), messageDrawable.getMinimumHeight());

        String nickname = SharePresUtil.getString(SharePresUtil.KEY_NICK_NAME);
        String college = SharePresUtil.getString(SharePresUtil.KEY_COLLEGE);
        tvNickName.setText(nickname);
        tvCampus.setText(college);
    }

    private void readyMineRequest() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            minePresenter.requestMyInfo(new TokenRequest(token));
        }
    }

    @OnClick({R.id.tv_changecampus, R.id.tv_message, R.id.layout_setting, R.id.layout_mywallet,
            R.id.layout_myassn, R.id.layout_share, R.id.layout_myintergal, R.id.layout_myinfo, R.id.tv_mytopic,
            R.id.tv_mytask, R.id.tv_myorder, R.id.layout_storecollection,R.id.layout_mystore})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_changecampus:
                showToast(R.string.comming_soon);
                break;
            case R.id.tv_message:
                if (isMessageRemine) {
                    tvMyTopic.setCompoundDrawables(null, messageDrawable, null, null);
                    isMessageRemine = false;
                }
                openWindow(WindowIDDefine.WINDOW_MESSAGE);
                break;
            case R.id.layout_myinfo:
                if (responseData != null) {
                    Bundle myinfoBundle = new Bundle();
                    myinfoBundle.putSerializable("myinfo", responseData);
                    openWindow(WindowIDDefine.WINDOW_MYINFO, myinfoBundle);
                }
                break;
            case R.id.tv_mytopic:
                if (isTopicRemine) {
                    tvMyTopic.setCompoundDrawables(null, topicDrawable, null, null);
                    isTopicRemine = false;
                }
                openWindow(WindowIDDefine.WINDOW_MYTOPIC);
                break;
            case R.id.tv_mytask:
                if (isTaskRemine) {
                    tvMyTask.setCompoundDrawables(null, taskDrawable, null, null);
                    isTaskRemine = false;
                }
                openWindow(WindowIDDefine.WINDOW_MYTASK);
                break;
            case R.id.tv_myorder:
                WebBean webBean = new WebBean();
                webBean.setRequest(true);
                webBean.setTitle(getResources().getString(R.string.myorder));
                webBean.setName("myStoreOrder");
                openWebWindow(webBean);
                break;
            case R.id.layout_storecollection:
                WebBean webBean2 = new WebBean();
                webBean2.setRequest(true);
                webBean2.setTitle(getResources().getString(R.string.store_collection));
                webBean2.setName("storeCollection");
                openWebWindow(webBean2);
                break;

            case R.id.layout_mystore:
                WebBean webBean4 = new WebBean();
                webBean4.setRequest(true);
                webBean4.setTitle(getResources().getString(R.string.my_store));
                webBean4.setName("myShop");
                openWebWindow(webBean4);
                break;

            case R.id.layout_setting:
                openWindow(WindowIDDefine.WINDOW_SETTING);
                break;
            case R.id.layout_mywallet:
                openWindow(WindowIDDefine.WINDOW_WALLET);
                break;
            case R.id.layout_myassn:
                openWindow(WindowIDDefine.WINDOW_MYASSN);
                break;
            case R.id.layout_share:
                openWindow(WindowIDDefine.WINDOW_SHARE);
                break;
            case R.id.layout_myintergal:
                WebBean webBean3 = new WebBean();
                webBean3.setRequest(true);
                webBean3.setTitle(getResources().getString(R.string.myintergal));
                webBean3.setName("integral");
                openWebWindow(webBean3);
                break;
        }
    }

    private void openWebWindow(WebBean webBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("webbean", webBean);
        openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
    }

    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_MINE_REFRESH) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    readyMineRequest();
                }
            },200);
        } else if (mes.what == NotifyIDDefine.NOTIFY_MESSAGE_PUSH) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isMessageRemine = true;
                    tvMessage.setCompoundDrawables(remineMessage, null, null, null);
                }
            });
        } else if (mes.what == NotifyIDDefine.NOTIFY_MYTASK_PUSH) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isTaskRemine = true;
                    tvMyTask.setCompoundDrawables(null, remineTask, null, null);
                }
            });
        } else if (mes.what == NotifyIDDefine.NOTIFY_TOPIC_PUSH) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isTopicRemine = true;
                    tvMyTopic.setCompoundDrawables(null, remineTopic, null, null);
                }
            });
        }
    }

    @Override
    public void responseMyInfo(MyInfoResponse response) {
        responseData = null;
        responseData = response.getData();
        tvNickName.setText(responseData.getNickname());
        tvCampus.setText(responseData.getCollege());
        Glide.with(getContext()).load(responseData.getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(imgAvatar);

        SharePresUtil.putFloat(SharePresUtil.KEY_BALANCE, responseData.getWallet());
        SharePresUtil.putInt(SharePresUtil.KEY_SETPAYACCOUNT, responseData.getIs_alipay());
        SharePresUtil.putInt(SharePresUtil.KEY_SETPAYPWD, responseData.getIs_paypassword());
        SharePresUtil.putInt(SharePresUtil.KEY_AUTH, responseData.getIs_auth());
        SharePresUtil.putString(SharePresUtil.KEY_NICK_NAME, responseData.getNickname());
        SharePresUtil.putString(SharePresUtil.KEY_COLLEGE, responseData.getCollege());
        SharePresUtil.putString(SharePresUtil.KEY_ALIPAYNAME, responseData.getAlipay());
        SharePresUtil.putString(SharePresUtil.KEY_ADDRESS, responseData.getAddress());
        SharePresUtil.putString(SharePresUtil.KEY_PHONE, responseData.getMobile_no());
        SharePresUtil.putString(SharePresUtil.KEY_LAST_PHONE, responseData.getMobile_no());
        SharePresUtil.putString(SharePresUtil.KEY_LAST_AVATAR, responseData.getAvatar_url());
        SharePresUtil.putString(SharePresUtil.KEY_OPENID, responseData.getOpenid());
    }
}
