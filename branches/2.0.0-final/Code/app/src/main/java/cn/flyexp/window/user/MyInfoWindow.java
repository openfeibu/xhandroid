package cn.flyexp.window.user;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.media.tv.TvContentRating;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.MyInfoEditCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.user.MyInfoEditPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by huangju on 2016/12/20.
 */

public class MyInfoWindow extends BaseWindow implements NotifyManager.Notify,MyInfoEditCallback.ResponseCallback{

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_nickname)
    TextView tvNickName;
    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_gender)
    TextView tvGender;
    @InjectView(R.id.tv_intro)
    TextView tvIntro;
    @InjectView(R.id.tv_certification)
    TextView tvCertification;
    @InjectView(R.id.tv_address)
    TextView tvAddress;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;


    private Bundle bundle;
    private MyInfoResponse.MyInfoResponseData datas;
    private PopupWindow popupWindow;
    private ChangeMyInfoRequest changeMyInfoRequest;
    private MyInfoEditPresenter myInfoEditPresenter;
    private SweetAlertDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_myinfo;
    }

    public MyInfoWindow(Bundle bundle) {
        this.bundle = bundle;
        datas = (MyInfoResponse.MyInfoResponseData) bundle.getSerializable("myinfo");
        myInfoEditPresenter = new MyInfoEditPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));
        getNotifyManager().register(NotifyIDDefine.NOTIFY_EDIT_RESULT,this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_GALLERY, this);
        initView();
    }

    private void initView() {
        Glide.with(getContext()).load(datas.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgAvatar);
        tvNickName.setText(datas.getNickname());
        tvName.setText(datas.getRealname());

        if(datas.getGender() == 1){
            tvGender.setText("男");
        }else if(datas.getGender() == 2){
            tvGender.setText("女");
        } else {
            tvGender.setText("报名");
        }

        if(SharePresUtil.getInt(SharePresUtil.KEY_AUTH) == 0) {
            tvCertification.setText("未实名");
        } else if(SharePresUtil.getInt(SharePresUtil.KEY_AUTH) == 1){
            tvCertification.setText("已实名");
        } else if(SharePresUtil.getInt(SharePresUtil.KEY_AUTH) == 2) {
            tvCertification.setText("实名中");
        }
        tvPhone.setText(datas.getMobile_no());
        tvIntro.setText(datas.getIntroduction());
        tvAddress.setText(datas.getAddress());


        View popLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_gender,null);
        popLayout.findViewById(R.id.tv_man).setOnClickListener(popOnListener);
        popLayout.findViewById(R.id.tv_women).setOnClickListener(popOnListener);
        popupWindow = new PopupWindow(popLayout, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1f);
            }
        });
    }

    private OnClickListener popOnListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_man:
                    readyChangeGender(1);
                    tvGender.setText("男");
                    break;
                case R.id.tv_women:
                    readyChangeGender(2);
                    tvGender.setText("女");
                    break;
            }
        }
    };


    @OnClick({R.id.img_back,R.id.rl_certification,R.id.rl_gender,R.id.img_avatar,R.id.rl_address,R.id.rl_fillinfo,R.id.rl_nickname})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.rl_certification:
                if (SharePresUtil.getInt(SharePresUtil.KEY_AUTH)==0)
                    openWindow(WindowIDDefine.WINDOW_CERTIFITION);
                break;
            case R.id.rl_gender:
                popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
                break;

            case R.id.rl_address:


                break;
            case R.id.rl_fillinfo:
                openWindow(WindowIDDefine.WINDOW_MYINFO_EDIT);
                break;

            case R.id.rl_nickname:
                Bundle bundle = new Bundle();
                bundle.putString("key","昵称");
                bundle.putString("value",datas.getNickname());
                bundle.putInt("length",6);
                openWindow(WindowIDDefine.WINDOW_MYINFO_EDIT,bundle);
                break;

            case R.id.img_avatar:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("max", 1);
                openWindow(WindowIDDefine.WINDOW_GALLERY, bundle1);
                break;

        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void changeWindowAlpha(float v) {
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    private void readyChangeGender(int gender) {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        changeMyInfoRequest = new ChangeMyInfoRequest();
        changeMyInfoRequest.setToken(token);
        changeMyInfoRequest.setGender(gender);
        myInfoEditPresenter.requestChangeMyInfo(changeMyInfoRequest);
        loadingDialog.show();
    }

    @Override
    public void responseChangeMyInfo(BaseResponse response) {

    }

    public void requestFailure() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void onNotify(Message mes) {
        if(mes.what==NotifyIDDefine.NOTIFY_GALLERY) {
            Bundle bundle = mes.getData();

        }
    }
}
