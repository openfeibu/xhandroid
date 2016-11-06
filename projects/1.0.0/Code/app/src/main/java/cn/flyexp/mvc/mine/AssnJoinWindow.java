package cn.flyexp.mvc.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.entity.AssnJoinRequset;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/8/18 0018.
 */
public class AssnJoinWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private View assnEnterLayout;
    private EditText et_name;
    private EditText et_content;
    private RoundImageView iv_logo;
    private RoundImageView iv_paper;
    private PopupWindow picPopupWindow;
    private boolean isLogoPic;
    private AssnJoinRequset assnJoinRequset;
    private Button btn_apply;
    private boolean input;
    private String logoPicPath;
    private String cardPicPath;

    public AssnJoinWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_join);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_addlogo).setOnClickListener(this);
        findViewById(R.id.iv_addpaper).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_apply = (Button) findViewById(R.id.btn_apply);
        btn_apply.setOnClickListener(this);

        assnEnterLayout = findViewById(R.id.assnEnterLayout);
        et_name = (EditText) findViewById(R.id.et_name);
        et_content = (EditText) findViewById(R.id.et_content);

        iv_logo = (RoundImageView) findViewById(R.id.iv_logo);
        iv_paper = (RoundImageView) findViewById(R.id.iv_paper);

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
    }

    public void response() {
        btn_apply.setEnabled(true);
        dismissProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.iv_addlogo:
                isLogoPic = true;
                picPopupWindow.showAtLocation(assnEnterLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_addpaper:
                isLogoPic = false;
                picPopupWindow.showAtLocation(assnEnterLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_album:
                picPopupWindow.dismiss();
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                    public void onSuccess() {
                        GalleryFinal.openGalleryMuti(100, 1, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                if (isLogoPic) {
                                    logoPicPath = resultList.get(0).getPhotoPath();
                                    Picasso.with(getContext()).load(new File(resultList.get(0).getPhotoPath())).config(Bitmap.Config.RGB_565).resize(CommonUtil.dip2px(getContext(), 40), CommonUtil.dip2px(getContext(), 40)).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_logo);
                                } else {
                                    cardPicPath = resultList.get(0).getPhotoPath();
                                    Picasso.with(getContext()).load(new File(resultList.get(0).getPhotoPath())).config(Bitmap.Config.RGB_565).resize(CommonUtil.dip2px(getContext(), 150), CommonUtil.dip2px(getContext(), 150)).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_paper);
                                }
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
                    @Override
                    public void onSuccess() {
                        GalleryFinal.openCamera(100, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                if (isLogoPic) {
                                    logoPicPath = resultList.get(0).getPhotoPath();
                                    Picasso.with(getContext()).load(new File(resultList.get(0).getPhotoPath())).config(Bitmap.Config.RGB_565).resize(CommonUtil.dip2px(getContext(), 40), CommonUtil.dip2px(getContext(), 40)).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_logo);
                                } else {
                                    cardPicPath = resultList.get(0).getPhotoPath();
                                    Picasso.with(getContext()).load(new File(resultList.get(0).getPhotoPath())).config(Bitmap.Config.RGB_565).resize(CommonUtil.dip2px(getContext(), 150), CommonUtil.dip2px(getContext(), 150)).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_paper);
                                }
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
                break;
            case R.id.btn_cancel:
                picPopupWindow.dismiss();
                break;
            case R.id.btn_apply:
                String name = et_name.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                if (name.equals("")) {
                    showToast("社团名不能缺少");
                    return;
                }
                if (content.equals("")) {
                    showToast("申请意愿不能缺少");
                    return;
                }
                if (logoPicPath.equals("") || cardPicPath.equals("")) {
                    showToast("缺少上传图片");
                    return;
                }
                final String token = getStringByPreference("token");
                if (token.equals("")) {
                    return;
                }
                assnJoinRequset = new AssnJoinRequset();
                assnJoinRequset.setToken(token);
                assnJoinRequset.setCauses(content);
                assnJoinRequset.setName(name);
                btn_apply.setEnabled(false);
                showProgressDialog("正在提交...");
                new Thread(){
                    @Override
                    public void run() {
                        ArrayList<File> files = new ArrayList<>();
                        files.add(BitmapUtil.compressBmpToFile(logoPicPath));
                        files.add(BitmapUtil.compressBmpToFile(cardPicPath));
                        callBack.uploadImageAssn(token, files);
                    }
                }.start();
                break;
        }
    }

    public void assnJoinRequset(String url) {
        String[] imageUrl = CommonUtil.splitImageUrl(url);
        assnJoinRequset.setAvatar_url(imageUrl[0]);
        assnJoinRequset.setCertificate(imageUrl[1]);
        callBack.assnJoin(assnJoinRequset);
    }

}
