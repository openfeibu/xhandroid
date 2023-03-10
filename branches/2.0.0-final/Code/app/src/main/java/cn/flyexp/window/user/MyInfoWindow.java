package cn.flyexp.window.user;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.io.File;
import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.MyInfoCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.user.MyInfoPresenter;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.util.UploadFileHelper;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;

/**
 * Created by huangju on 2016/12/20.
 */

public class MyInfoWindow extends BaseWindow implements NotifyManager.Notify, MyInfoCallback.ResponseCallback {

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_nickname)
    TextView tvNickName;
    @InjectView(R.id.tv_gender)
    TextView tvGender;
    @InjectView(R.id.tv_intro)
    TextView tvIntro;
    @InjectView(R.id.tv_certification)
    TextView tvCertification;
    @InjectView(R.id.tv_address)
    TextView tvAddress;

    private MyInfoResponse.MyInfoResponseData datas;
    private PopupWindow popupWindow;
    private MyInfoPresenter myInfoEditPresenter;
    private SweetAlertDialog loadingDialog;
    private int gender;
    private String imgPath;
    private String address;
    private String nickname;
    private String intro;
    private boolean isChanged;

    @Override
    protected int getLayoutId() {
        return R.layout.window_myinfo;
    }

    public MyInfoWindow(Bundle bundle) {
        datas = (MyInfoResponse.MyInfoResponseData) bundle.getSerializable("myinfo");
        myInfoEditPresenter = new MyInfoPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));
        getNotifyManager().register(NotifyIDDefine.NOTIFY_EDIT_RESULT, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_GALLERY, this);
        initView();
    }

    private void initView() {
        Glide.with(getContext()).load(datas.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgAvatar);
        tvNickName.setText(datas.getNickname());
        if (datas.getGender() == 1) {
            tvGender.setText("???");
        } else if (datas.getGender() == 2) {
            tvGender.setText("???");
        }

        int auth = SharePresUtil.getInt(SharePresUtil.KEY_AUTH);
        if (auth == 0) {
            tvCertification.setText("?????????");
        } else if (auth == 1) {
            tvCertification.setText("?????????");
        } else if (auth == 2) {
            tvCertification.setText("?????????");
        }
        tvIntro.setText(datas.getIntroduction());
        tvAddress.setText(datas.getAddress());

        View popLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_gender, null);
        popLayout.findViewById(R.id.tv_man).setOnClickListener(popOnListener);
        popLayout.findViewById(R.id.tv_women).setOnClickListener(popOnListener);
        popupWindow = new PopupWindow(popLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                    gender = 1;
                    tvGender.setText("???");
                    isChanged = true;
                    break;
                case R.id.tv_women:
                    gender = 2;
                    tvGender.setText("???");
                    isChanged = true;
                    break;
            }
        }
    };


    @OnClick({R.id.img_back, R.id.rl_certification, R.id.rl_gender, R.id.rl_avatar,
            R.id.rl_address, R.id.rl_fillinfo, R.id.rl_nickname})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.rl_certification:
                if (SharePresUtil.getInt(SharePresUtil.KEY_AUTH) == 0) {
                    openWindow(WindowIDDefine.WINDOW_CERTIFITION);
                }
                break;
            case R.id.rl_gender:
                popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.rl_address:
                if (datas.getAddress().toString().trim() == tvAddress.getText().toString().trim()) {
                    openEditWindow("????????????", datas.getAddress(), 20);
                } else {
                    openEditWindow("????????????", tvAddress.getText().toString().trim(), 20);
                }
                break;
            case R.id.rl_fillinfo:
                if (datas.getIntroduction().toString().trim() == tvIntro.getText().toString().trim()) {
                    openEditWindow("??????", datas.getIntroduction(), 40);
                } else {
                    openEditWindow("??????", tvIntro.getText().toString().trim(), 40);
                }
                break;
            case R.id.rl_nickname:
                if (datas.getNickname().toString().trim() == tvNickName.getText().toString().trim()) {
                    openEditWindow("??????", datas.getNickname(), 6);
                } else {
                    openEditWindow("??????", tvNickName.getText().toString().trim(), 6);
                }

                break;
            case R.id.rl_avatar:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("max", 1);
                openWindow(WindowIDDefine.WINDOW_GALLERY, bundle1);
                break;
        }
    }

    private void openEditWindow(String name, String value, int length) {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("value", value);
        bundle.putInt("length", length);
        openWindow(WindowIDDefine.WINDOW_EDIT, bundle);
    }

    private void readyChangeMyInfo() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            ChangeMyInfoRequest changeMyInfoRequest = new ChangeMyInfoRequest();
            changeMyInfoRequest.setGender(gender);
            changeMyInfoRequest.setIntroduction(TextUtils.isEmpty(intro) ? datas.getIntroduction() : intro);
            changeMyInfoRequest.setNickname(TextUtils.isEmpty(nickname) ? datas.getNickname() : nickname);
            changeMyInfoRequest.setAddress(TextUtils.isEmpty(address) ? datas.getAddress() : address);
            changeMyInfoRequest.setToken(token);
            myInfoEditPresenter.requestChangeMyInfo(changeMyInfoRequest);
            loadingDialog.show();
        }
    }

    private void readyUploadAvatar() {
        final String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            new Thread() {
                @Override
                public void run() {
                    ArrayList<File> files = new ArrayList<>();
                    files.add(BitmapUtil.compressBmpToFile(imgPath));
                    MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(files);
                    myInfoEditPresenter.requestUploadAvatar(multipartBody, new TokenRequest(token));
                }
            }.start();
        }
    }

    private void changeWindowAlpha(float v) {
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (isChanged) {
            readyChangeMyInfo();
        }
    }

    @Override
    public void responseUploadAvatar() {
        Glide.with(getContext()).load(imgPath).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgAvatar);
    }

    @Override
    public void responseChangeMyInfo(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
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
        if (mes.what == NotifyIDDefine.NOTIFY_EDIT_RESULT) {
            Bundle bundle = mes.getData();
            String result = bundle.getString("result");
            String name = bundle.getString("name");
            if (TextUtils.equals(name, "????????????")) {
                address = result;
                tvAddress.setText(result);
            } else if (TextUtils.equals(name, "??????")) {
                intro = result;
                tvIntro.setText(result);
            } else if (TextUtils.equals(name, "??????")) {
                nickname = result;
                tvNickName.setText(result);
            }
            isChanged = true;
        } else if (mes.what == NotifyIDDefine.NOTIFY_GALLERY) {
            Bundle bundle = mes.getData();
            ArrayList<String> images = bundle.getStringArrayList("images");
            imgPath = images.get(0);
            readyUploadAvatar();
        }
    }
}
