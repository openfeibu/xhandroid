package cn.flyexp.mvc.topic;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.adapter.PicPublishAdapter;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.entity.UploadImageResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/12 0012.
 */
public class TopicPublishWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath();

    private PopupWindow popupWindow;
    private int popupHeight;
    private TextView tv_label;
    private int popupWidth;
    private PicPublishAdapter picAdapter;
    private ArrayList<PhotoInfo> data = new ArrayList<PhotoInfo>();
    private EditText et_content;
    private TopicViewCallBack callBack;
    private TopicPublishRequest topicPublishRequest;
    private PopupWindow picPopupWindow;
    private View windowLayout;
    private TextView tv_send;

    public TopicPublishWindow(TopicViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_topic_publish);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);
        tv_send = (TextView) findViewById(R.id.tv_send);
        tv_send.setOnClickListener(this);
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_label.setOnClickListener(this);
        et_content = (EditText) findViewById(R.id.et_content);
        et_content.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.TOPIC_CONTENT));
        et_content.addTextChangedListener(this);

        windowLayout = findViewById(R.id.ll_topic_publish);

        View popPicLayout = LayoutInflater.from(getContext()).inflate(R.layout
                .pop_pic_method, null);
        popPicLayout.findViewById(R.id.btn_album).setOnClickListener(this);
        popPicLayout.findViewById(R.id.btn_photograph).setOnClickListener(this);
        popPicLayout.findViewById(R.id.btn_cancel).setOnClickListener(this);
        picPopupWindow = new PopupWindow(popPicLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        picPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        picPopupWindow.setFocusable(true);
        picPopupWindow.setOutsideTouchable(true);
        picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);


        View popLayout = LayoutInflater.from(getContext()).inflate(R.layout
                .pop_topic_publish, null);
        popLayout.findViewById(R.id.tv_label_essay).setOnClickListener(this);
        popLayout.findViewById(R.id.tv_label_novelty).setOnClickListener(this);
        popLayout.findViewById(R.id.tv_label_tryst).setOnClickListener(this);
        popLayout.findViewById(R.id.tv_label_help).setOnClickListener(this);
        popLayout.findViewById(R.id.tv_label_complaints).setOnClickListener(this);
        popLayout.findViewById(R.id.tv_label_ask).setOnClickListener(this);
        popLayout.findViewById(R.id.tv_label_thelost).setOnClickListener(this);

        popLayout.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        popupHeight = popLayout.getMeasuredHeight();
        popupWidth = popLayout.getMeasuredWidth();
        popupWindow = new PopupWindow(popLayout, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        picAdapter = new PicPublishAdapter(getContext(), data);
        picAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ArrayList<String> imgUrl = new ArrayList<String>();
                for (PhotoInfo photoInfo : data) {
                    imgUrl.add(photoInfo.getPhotoPath());
                }
                PicBrowserBean picBrowserBean = new PicBrowserBean();
                picBrowserBean.setImgUrl(imgUrl);
                picBrowserBean.setCurSelectedIndex(position);
                picBrowserBean.setType(0);
                callBack.picBrowserEnter(picBrowserBean);
            }
        });
        final RecyclerView rv_pic = (RecyclerView) findViewById(R.id.rv_pic);
        rv_pic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_pic.setAdapter(picAdapter);
        rv_pic.setItemAnimator(new DefaultItemAnimator());
    }


    public void deletePhoto(int index) {
        LogUtil.error(getClass(), index + "index");
        data.remove(index);
        picAdapter.notifyDataSetChanged();
    }

    public void response() {
        dismissProgressDialog();
        tv_send.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                hideWindow(false);
                break;
            case R.id.tv_label:
                showPopupWindow();
                break;
            case R.id.tv_label_essay:
                popupWindow.dismiss();
                tv_label.setText(getResources().getString(R.string.label_essay));
                break;
            case R.id.tv_label_novelty:
                popupWindow.dismiss();
                tv_label.setText(getResources().getString(R.string.label_novelty));
                break;
            case R.id.tv_label_tryst:
                popupWindow.dismiss();
                tv_label.setText(getResources().getString(R.string.label_tryst));
                break;
            case R.id.tv_label_help:
                popupWindow.dismiss();
                tv_label.setText(getResources().getString(R.string.label_help));
                break;
            case R.id.tv_label_complaints:
                popupWindow.dismiss();
                tv_label.setText(getResources().getString(R.string.label_complaints));
                break;
            case R.id.tv_label_thelost:
                popupWindow.dismiss();
                tv_label.setText(getResources().getString(R.string.label_thelost));
                break;
            case R.id.tv_label_ask:
                popupWindow.dismiss();
                tv_label.setText(getResources().getString(R.string.label_ask));
                break;
            case R.id.tv_send:
                String content = et_content.getText().toString().trim();
                String label = tv_label.getText().toString();
                if (label.equals("选标签") && !popupWindow.isShowing()) {
                    showPopupWindow();
                    return;
                }
                final String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    return;
                }
                topicPublishRequest = new TopicPublishRequest();
                topicPublishRequest.setToken(token);
                topicPublishRequest.setTopic_content(content);
                topicPublishRequest.setTopic_type(label);
                showProgressDialog("发布中...");
                v.setEnabled(false);
                if (data.size() == 0) {
                    callBack.submitTopic(topicPublishRequest);
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            ArrayList<File> files = new ArrayList<>();
                            for (PhotoInfo photoInfo : data) {
                                files.add(BitmapUtil.compressBmpToFile(photoInfo.getPhotoPath()));
                            }
                            callBack.uploadImageTopic(token, files);
                        }
                    }.start();
                }
                break;
            case R.id.iv_add:
                if (data.size() >= 9) {
                    return;
                }
                picPopupWindow.showAtLocation(windowLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_album:
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                    public void onSuccess() {
                        GalleryFinal.openGalleryMuti(100, 9 - data.size(), new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                data.addAll(resultList);
                                picAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onHanlderFailure(int requestCode, String errorMsg) {

                            }
                        });
                    }

                    public void onFail(int[] ids) {
                    }

                    public void onCancel() {
                    }

                    public void goSetting() {
                    }
                }, new int[]{PermissionHandler.PERMISSION_FILE});
                picPopupWindow.dismiss();
                break;
            case R.id.btn_photograph:
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                    @Override
                    public void onSuccess() {
                        GalleryFinal.openCamera(100, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                data.addAll(resultList);
                                picAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onHanlderFailure(int requestCode, String errorMsg) {

                            }
                        });
                    }

                    @Override
                    public void goSetting() {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onFail(int[] ids) {

                    }
                }, new int[]{PermissionHandler.PERMISSION_CAMERA, PermissionHandler.PERMISSION_FILE});
                picPopupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                picPopupWindow.dismiss();
                break;
        }
    }

    private void showPopupWindow() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            int[] location = new int[2];
            tv_label.getLocationOnScreen(location);
            popupWindow.showAsDropDown(tv_label);
        }
    }

    public void uploadImageResponse(UploadImageResponse uploadImageResponse) {
        topicPublishRequest.setImg(uploadImageResponse.getUrl());
        topicPublishRequest.setThumb(uploadImageResponse.getThumb_url());
        callBack.submitTopic(topicPublishRequest);
    }

    public void uploadImageResponseFail() {
        dismissProgressDialog();
        WindowHelper.showToast(getContext().getResources().getString(R.string.topic_public_error));
        tv_send.setEnabled(true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String content = et_content.getText().toString().trim();
        callBack.setUIData(WindowCallBack.UIDataKeysDef.TOPIC_CONTENT, content);
        if (content.equals("")) {
            tv_send.setAlpha(0.5f);
            tv_send.setEnabled(false);
        } else {
            tv_send.setAlpha(1f);
            tv_send.setEnabled(true);
        }
    }
}
