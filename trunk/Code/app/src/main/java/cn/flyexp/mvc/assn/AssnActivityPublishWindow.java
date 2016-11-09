package cn.flyexp.mvc.assn;

import android.app.AlertDialog;
import android.graphics.Bitmap;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.adapter.PicPublishAdapter;
import cn.flyexp.entity.AssnActivityPublishRequest;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/27 0027.
 */
public class AssnActivityPublishWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private AssnViewCallBack callBack;
    private ArrayList<PhotoInfo> data = new ArrayList<PhotoInfo>();
    private EditText et_title;
    private EditText et_content;
    private PopupWindow picPopupWindow;
    private View activityLayout;
    private TextView tv_startTime;
    private TextView tv_endTime;
    private EditText et_place;
    private AssnActivityPublishRequest assnActivityPublishRequest;
    private Button btn_confirm;
    private ImageView iv_img;
    private int aid;

    public AssnActivityPublishWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_activity_publish);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_startTime = (TextView) findViewById(R.id.tv_startTime);
        tv_endTime = (TextView) findViewById(R.id.tv_endTime);
        iv_img = (ImageView) findViewById(R.id.iv_img);
        btn_confirm.setOnClickListener(this);
        tv_startTime.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);

        activityLayout = findViewById(R.id.activityLayout);

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
        et_place = (EditText) findViewById(R.id.et_place);
        et_title.addTextChangedListener(this);
        et_content.addTextChangedListener(this);
        et_place.addTextChangedListener(this);
    }

    public void response() {
        dismissProgressDialog();
        btn_confirm.setEnabled(true);
    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return !(et_content.isFocused() || et_place.isFocused() || et_title.isFocused());
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_startTime:
                showTimeDialog(true);
                break;
            case R.id.tv_endTime:
                showTimeDialog(false);
                break;
            case R.id.btn_confirm:
                String title = et_title.getText().toString().trim();
                String content = et_content.getText().toString().trim();
                String place = et_place.getText().toString().trim();
                String startTime = tv_startTime.getText().toString().trim();
                String endTime = tv_endTime.getText().toString().trim();
                if (startTime.equals("选择活动开始时间")) {
                    WindowHelper.showToast("选择活动开始时间");
                    return;
                }
                if (endTime.equals("选择活动结束时间")) {
                    WindowHelper.showToast("选择活动结束时间");
                    return;
                }
                final String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    return;
                }
                assnActivityPublishRequest = new AssnActivityPublishRequest();
                assnActivityPublishRequest.setToken(token);
                assnActivityPublishRequest.setTitle(title);
                assnActivityPublishRequest.setContent(content);
                assnActivityPublishRequest.setPlace(place);
                assnActivityPublishRequest.setStart_time(startTime);
                assnActivityPublishRequest.setEnd_time(endTime);
                assnActivityPublishRequest.setAid(aid);
                btn_confirm.setEnabled(false);
                showProgressDialog("发布中...");
                if (data.size() == 0) {
                    callBack.submitActivity(assnActivityPublishRequest);
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            ArrayList<File> files = new ArrayList<>();
                            for (PhotoInfo photoInfo : data) {
                                files.add(BitmapUtil.compressBmpToFile(photoInfo.getPhotoPath()));
                            }
                            callBack.uploadImageActi(token, files);
                        }
                    }.start();
                }
                break;
            case R.id.iv_add:
                picPopupWindow.showAtLocation(activityLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_album:
                picPopupWindow.dismiss();
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                    public void onSuccess() {
                        GalleryFinal.openGalleryMuti(100, 1, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                data.clear();
                                data.addAll(resultList);
                                Picasso.with(getContext()).load(new File(resultList.get(0).getPhotoPath())).config(Bitmap.Config.RGB_565).resize(CommonUtil.dip2px(getContext(), 50), CommonUtil.dip2px(getContext(), 50)).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_img);
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
                                Picasso.with(getContext()).load(new File(resultList.get(0).getPhotoPath())).config(Bitmap.Config.RGB_565).resize(CommonUtil.dip2px(getContext(), 50), CommonUtil.dip2px(getContext(), 50)).memoryPolicy(MemoryPolicy.NO_CACHE).into(iv_img);
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
            case R.id.iv_img:
                ArrayList<String> imgUrl = new ArrayList<String>();
                for (PhotoInfo photoInfo : data) {
                    imgUrl.add(photoInfo.getPhotoPath());
                }
                PicBrowserBean picBrowserBean = new PicBrowserBean();
                picBrowserBean.setImgUrl(imgUrl);
                picBrowserBean.setCurSelectedIndex(0);
                picBrowserBean.setType(0);
                callBack.picBrowserEnter(picBrowserBean);
                break;
        }
    }

    private void showTimeDialog(final boolean isStartTime) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_assnactivity_time, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final AlertDialog dialog = builder.setTitle(isStartTime ? "选择活动开始时间" : "选择活动结束时间")
                .setView(dialogView).create();
        dialog.show();
        final DatePicker dp_assnact = (DatePicker) dialogView.findViewById(R.id
                .dp_assnact);
        final TimePicker tp_assnact = (TimePicker) dialogView.findViewById(R.id
                .tp_assnact);
        tp_assnact.setIs24HourView(true);
        dialogView.findViewById(R.id.tv_comfirm).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = dp_assnact.getYear();
                int month = dp_assnact.getMonth() + 1;
                int day = dp_assnact.getDayOfMonth();
                int hour = tp_assnact.getCurrentHour();
                Calendar c = Calendar.getInstance();
                c.set(year, month, day, hour, 0);
                String dataStr = DateUtil.long2Date(c.getTimeInMillis(), "yyyy-MM-dd HH:mm");
                if (isStartTime) {
                    tv_startTime.setText(dataStr);
                } else {
                    tv_endTime.setText(dataStr);
                }
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public void uploadImageResponse(String image_url) {
        assnActivityPublishRequest.setImg_url(image_url);
        callBack.submitActivity(assnActivityPublishRequest);
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
        String place = et_place.getText().toString().trim();
        if (title.equals("") || content.equals("") || place.equals("")) {
            btn_confirm.setEnabled(false);
            btn_confirm.setAlpha(0.5f);
        } else {
            btn_confirm.setEnabled(true);
            btn_confirm.setAlpha(1f);
        }
    }
}
