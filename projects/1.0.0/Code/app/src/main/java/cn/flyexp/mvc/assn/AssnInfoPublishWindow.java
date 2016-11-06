package cn.flyexp.mvc.assn;

import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.adapter.PicPublishAdapter;
import cn.flyexp.entity.AssnInfoPublishRequest;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/27 0027.
 */
public class AssnInfoPublishWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private AssnViewCallBack callBack;
    private RecyclerView rv_pic;
    private PicPublishAdapter picAdapter;
    private ArrayList<PhotoInfo> data = new ArrayList<PhotoInfo>();
    private EditText et_title;
    private EditText et_content;
    private AssnInfoPublishRequest assnInfoPublishRequest;
    private PopupWindow picPopupWindow;
    private View infoPublishLayout;
    private Button btn_confirm;

    public AssnInfoPublishWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_info_publish);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        infoPublishLayout = findViewById(R.id.infoPublishLayout);

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

        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        et_title.addTextChangedListener(this);
        et_content.addTextChangedListener(this);

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
        rv_pic = (RecyclerView) findViewById(R.id.rv_pic);
        rv_pic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_pic.setAdapter(picAdapter);
        rv_pic.setItemAnimator(new DefaultItemAnimator());
    }

    public void response() {
        btn_confirm.setEnabled(true);
        dismissProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                LogUtil.error(getClass(), "back");
                break;
            case R.id.btn_confirm:
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                final String token = getStringByPreference("token");
                if (token.equals("")) {
                    return;
                }
                assnInfoPublishRequest = new
                        AssnInfoPublishRequest();
                assnInfoPublishRequest.setToken(token);
                assnInfoPublishRequest.setTitle(title);
                assnInfoPublishRequest.setContent(content);
                if (data.size() == 0) {
                    callBack.submitInfo(assnInfoPublishRequest);
                } else {
                    new Thread(){
                        @Override
                        public void run() {
                            ArrayList<File> files = new ArrayList<>();
                            for (PhotoInfo photoInfo : data) {
                                files.add(BitmapUtil.compressBmpToFile(photoInfo.getPhotoPath()));
                            }
                            callBack.uploadImageInfo(token, files);
                        }
                    }.start();
                }
                btn_confirm.setEnabled(false);
                showProgressDialog("发布中...");
                break;
            case R.id.iv_add:
                if (data.size() >= 6) {
                    showToast("最多选择六张图片");
                    return;
                }
                picPopupWindow.showAtLocation(infoPublishLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_album:
                picPopupWindow.dismiss();
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                    public void onSuccess() {
                        GalleryFinal.openGalleryMuti(100, 6 - data.size(), new GalleryFinal.OnHanlderResultCallback() {
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
                break;
            case R.id.btn_photograph:
                picPopupWindow.dismiss();
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
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

                    public void onFail(int[] ids) {
                    }

                    public void onCancel() {
                    }

                    public void goSetting() {
                    }
                }, new int[]{PermissionHandler.PERMISSION_CAMERA, PermissionHandler.PERMISSION_FILE});
                break;
            case R.id.btn_cancel:
                picPopupWindow.dismiss();
                break;
        }
    }

    public void uploadImageResponse(String imageUrl) {
        assnInfoPublishRequest.setImg_url(imageUrl);
        callBack.submitInfo(assnInfoPublishRequest);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String title = et_title.getText().toString().trim();
        String content = et_content.getText().toString().trim();
        if (title.equals("") || content.equals("")) {
            btn_confirm.setAlpha(0.5f);
            btn_confirm.setEnabled(false);
        } else {
            btn_confirm.setAlpha(1f);
            btn_confirm.setEnabled(true);
        }
    }
}
