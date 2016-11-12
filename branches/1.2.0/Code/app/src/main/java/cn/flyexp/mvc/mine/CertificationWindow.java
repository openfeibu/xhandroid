package cn.flyexp.mvc.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.entity.CertificationRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
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
    private EditText tv_name;
    private EditText tv_numberid;

    public CertificationWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_certification);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_neg).setOnClickListener(this);
        findViewById(R.id.iv_pos).setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);

        iv_front = (ImageView) findViewById(R.id.iv_neg);
        iv_inver = (ImageView) findViewById(R.id.iv_pos);

        certifiLayout = findViewById(R.id.certifiLayout);
        tv_name = (EditText) findViewById(R.id.tv_name);
        tv_numberid = (EditText) findViewById(R.id.tv_numberid);

        tv_numberid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    confirm();
                }
                return false;
            }
        });

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
            case R.id.iv_neg:
                isFront = true;
                picPopupWindow.showAtLocation(certifiLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.iv_pos:
                isFront = false;
                picPopupWindow.showAtLocation(certifiLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_confirm:
                confirm();
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

    private void confirm(){
        final String name = tv_name.getText().toString().trim();
        final String numberid = tv_numberid.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(numberid) ){
            WindowHelper.showToast("请输入完整信息");
            return;
        }

        int len = name.length();
        if (len < 2) {
            WindowHelper.showToast("请输入正确的姓名");
            return;
        }

        len = numberid.length();
        if (len != 15 && len != 18) {
            WindowHelper.showToast("身份证长度不对");
            return;
        }
        for(int i = 0; i < len - 1; i ++){
            if(!Character.isDigit(numberid.charAt(i))){
                WindowHelper.showToast("请输入正确的身份证号码");
                return;
            }
        }

        if (frontPath.equals("")) {
            WindowHelper.showToast("请上传自拍手持身份证有头像一面");
            return;
        }
        if (inverPath.equals("")) {
            WindowHelper.showToast("身份证有国微一面");
            return;
        }
        token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return;
        }
        showProgressDialog("提交中...");
        btn_confirm.setEnabled(false);
        new Thread(){
            @Override
            public void run() {
                CertificationRequest certificationRequest = new CertificationRequest();
                certificationRequest.setToken(token);
                certificationRequest.setId_number(numberid);
                certificationRequest.setName(name);
                final ArrayList<File> files = new ArrayList<>();
                files.add(BitmapUtil.compressBmpToFile(frontPath));
                files.add(BitmapUtil.compressBmpToFile(inverPath));
                callBack.uploadImageCertifi(certificationRequest, files);
            }
        }.start();
    }

}
