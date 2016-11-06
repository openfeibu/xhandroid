package cn.flyexp.mvc.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.BitmapUtil;

/**
 * Created by txy on 2016/8/8 0008.
 */
public class CertificationWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private ImageView iv_front;
    private ImageView iv_inver;
    private View certifiLayout;
    private PopupWindow picPopupWindow;
    private boolean isFront = true;
    private String frontPath;
    private String inverPath;
    private String token;
    private Button btn_confirm;

    public CertificationWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_certification);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);
        findViewById(R.id.iv_add2).setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        iv_front = (ImageView) findViewById(R.id.iv_front);
        iv_inver = (ImageView) findViewById(R.id.iv_inver);

        certifiLayout = findViewById(R.id.certifiLayout);

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
        dismissProgressDialog();
        btn_confirm.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.iv_add:
                isFront = true;
                picPopupWindow.showAtLocation(certifiLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_add2:
                isFront = false;
                picPopupWindow.showAtLocation(certifiLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_confirm:
                if (frontPath.equals("")) {
                    showToast("请上传身份证正面");
                    return;
                }
                if (inverPath.equals("")) {
                    showToast("请上传身份证反面");
                    return;
                }
                token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                showProgressDialog("提交中...");
                v.setEnabled(false);
                new Thread(){
                    @Override
                    public void run() {
                        final ArrayList<File> files = new ArrayList<>();
                        files.add(BitmapUtil.compressBmpToFile(frontPath));
                        files.add(BitmapUtil.compressBmpToFile(inverPath));
                        callBack.uploadImageCertifi(token, files);
                    }
                }.start();
                break;
            case R.id.btn_album:
                picPopupWindow.dismiss();
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                    public void onSuccess() {
                        GalleryFinal.openGalleryMuti(100, 1, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                if (isFront) {
                                    frontPath = resultList.get(0).getPhotoPath();
                                    iv_front.setImageBitmap(BitmapUtil.compressImage(BitmapFactory.decodeFile(frontPath)));
                                } else {
                                    inverPath = resultList.get(0).getPhotoPath();
                                    iv_inver.setImageBitmap(BitmapUtil.compressImage(BitmapFactory.decodeFile(inverPath)));
                                }
                                toBtn();
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
                                if (isFront) {
                                    frontPath = resultList.get(0).getPhotoPath();
                                    iv_front.setImageBitmap(BitmapUtil.compressImage(BitmapFactory.decodeFile(frontPath)));
                                } else {
                                    inverPath = resultList.get(0).getPhotoPath();
                                    iv_inver.setImageBitmap(BitmapUtil.compressImage(BitmapFactory.decodeFile(inverPath)));
                                }
                                toBtn();
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

    private void toBtn() {
        if (inverPath == null || frontPath == null) {
            return;
        }
        if (inverPath.equals("") && frontPath.equals("")) {
            btn_confirm.setAlpha(0.5f);
            btn_confirm.setEnabled(false);
        } else {
            btn_confirm.setAlpha(1f);
            btn_confirm.setEnabled(true);
        }
    }

}
