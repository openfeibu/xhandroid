package cn.flyexp.mvc.mine;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.BitmapUtil;

/**
 * Created by txy on 2016/7/20 0020.
 */
public class MyInfoWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private View myInfoLayout;
    private PopupWindow picPopupWindow;
    private boolean isChange = false;
    private int isAuth;

    public MyInfoWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    EditText nicknameEdited;
    RelativeLayout nicknameLayout;
    TextView nicknameTextView;

    EditText descriptionEdited;
    RelativeLayout descriptionLayout;
    TextView descriptionTextView;

    RadioGroup sexRadioGroup;
    RelativeLayout sexLayout;
    TextView sexTextView;

    EditText addressEdited;
    RelativeLayout addressLayout;
    TextView addressTextView;

    RelativeLayout layout_avatar;

    private void initView() {
        setContentView(R.layout.window_myinfo);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.layout_changePhone).setOnClickListener(this);
        findViewById(R.id.layout_changePwd).setOnClickListener(this);

        myInfoLayout = findViewById(R.id.myInfoLayout);

        nicknameEdited = (EditText) findViewById(R.id.nickname_edit);
        nicknameLayout = (RelativeLayout) findViewById(R.id.layout_nickname);
        nicknameTextView = (TextView) findViewById(R.id.tv_nickname);
        nicknameLayout.setOnClickListener(this);
        nicknameLayout.setTag(false);

        descriptionEdited = (EditText) findViewById(R.id.desricption_edit);
        descriptionLayout = (RelativeLayout) findViewById(R.id.layout_desricption);
        descriptionTextView = (TextView) findViewById(R.id.tv_desricption);
        descriptionLayout.setOnClickListener(this);
        descriptionLayout.setTag(false);

        sexRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_sex);
        sexLayout = (RelativeLayout) findViewById(R.id.layout_sex);
        sexTextView = (TextView) findViewById(R.id.tv_sex);
        sexLayout.setOnClickListener(this);
        sexLayout.setTag(false);

        addressEdited = (EditText) findViewById(R.id.address_edit);
        addressLayout = (RelativeLayout) findViewById(R.id.layout_address);
        addressTextView = (TextView) findViewById(R.id.tv_address);
        addressLayout.setOnClickListener(this);
        addressLayout.setTag(false);

        layout_avatar = (RelativeLayout) findViewById(R.id.layout_avatar);
        layout_avatar.setOnClickListener(this);

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

    public void initData(MyInfoResponse.MyInfoResponseData responseData) {
        if (responseData == null) {
            return;
        }
        nicknameTextView.setText(responseData.getNickname());
        descriptionTextView.setText(responseData.getIntroduction());
        sexTextView.setText(responseData.getGender() == 1 ? "男" : "女");
        addressTextView.setText(responseData.getAddress());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isChange) {
            return;
        }

        String nickName = nicknameEdited.getText().toString().trim();
        String introduction = descriptionEdited.getText().toString().trim();
        String address = addressEdited.getText().toString().trim();
        int gender = -1;
        if (sexRadioGroup.getCheckedRadioButtonId() == R.id.man) {
            gender = 1;
        } else if (sexRadioGroup.getCheckedRadioButtonId() == R.id.girl) {
            gender = 2;
        }
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return;
        }
        ChangeMyInfoRequest myInfoRequest = new ChangeMyInfoRequest();
        myInfoRequest.setToken(token);
        myInfoRequest.setNickname(nickName);
        myInfoRequest.setGender(gender);
        myInfoRequest.setIntroduction(introduction);
        myInfoRequest.setAddress(address);
        callBack.changeMyInfo(myInfoRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.layout_nickname:
                isChange = true;
                if (!(Boolean) nicknameLayout.getTag()) {
                    nicknameEdited.setVisibility(View.VISIBLE);
                    nicknameLayout.setTag(true);
                    nicknameEdited.setText("" + nicknameTextView.getText().toString());
                    nicknameTextView.setVisibility(View.GONE);
                    nicknameTextView.setTag(true);
                } else {
                    nicknameLayout.setTag(false);
                    nicknameEdited.setVisibility(View.GONE);
                    nicknameTextView.setVisibility(View.VISIBLE);
                    if (!nicknameEdited.getText().toString().trim().equals("")) {
                        nicknameTextView.setText("" + nicknameEdited.getText().toString());
                    }
                }
                break;
            case R.id.layout_desricption:
                isChange = true;
                if (!(Boolean) descriptionLayout.getTag()) {
                    descriptionEdited.setVisibility(View.VISIBLE);
                    descriptionLayout.setTag(true);
                    descriptionEdited.setText("" + descriptionTextView.getText().toString());
                    descriptionTextView.setVisibility(View.GONE);
                    descriptionTextView.setTag(true);
                } else {
                    descriptionLayout.setTag(false);
                    descriptionEdited.setVisibility(View.GONE);
                    descriptionTextView.setVisibility(View.VISIBLE);
                    if (!descriptionEdited.getText().toString().trim().equals("")) {
                        descriptionTextView.setText("" + descriptionEdited.getText().toString());
                    }
                }
                break;
            case R.id.layout_sex:
                isChange = true;
                if (!(Boolean) sexLayout.getTag()) {
                    sexTextView.setVisibility(View.VISIBLE);
                    sexLayout.setTag(true);
                    sexRadioGroup.setVisibility(View.VISIBLE);
                    if (sexTextView.getText().toString().trim().equals("男")) {
                        sexRadioGroup.check(R.id.man);
                    } else {
                        sexRadioGroup.check(R.id.girl);
                    }
                    sexTextView.setVisibility(View.GONE);
                    sexTextView.setTag(true);
                } else {
                    sexLayout.setTag(false);
                    sexRadioGroup.setVisibility(View.GONE);
                    sexTextView.setVisibility(View.VISIBLE);
                    if (sexRadioGroup.getCheckedRadioButtonId() == R.id.man) {
                        sexTextView.setText("男");
                    } else {
                        sexTextView.setText("女");
                    }
                }
                break;
            case R.id.layout_address:
                isChange = true;
                if (!(Boolean) addressLayout.getTag()) {
                    addressEdited.setVisibility(View.VISIBLE);
                    addressLayout.setTag(true);
                    addressEdited.setText("" + addressTextView.getText().toString());
                    addressTextView.setVisibility(View.GONE);
                    addressTextView.setTag(true);
                } else {
                    addressLayout.setTag(false);
                    addressEdited.setVisibility(View.GONE);
                    addressTextView.setVisibility(View.VISIBLE);
                    if (!addressEdited.getText().toString().trim().equals("")) {
                        addressTextView.setText("" + addressEdited.getText().toString());
                    }
                }
                break;
            case R.id.layout_changePhone:
                callBack.changePhoneEnter();
                break;
            case R.id.layout_changePwd:
                callBack.changePwdEnter();
                break;
            case R.id.layout_certification:
                if (isAuth == 0) {
                    callBack.certificationEnter();
                } else if (isAuth == 2) {
                    WindowHelper.showToast("实名资料已在审核的路上，请等待...");
                }
                break;
            case R.id.layout_avatar:
                picPopupWindow.showAtLocation(myInfoLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.btn_album:
                picPopupWindow.dismiss();
                PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                    public void onSuccess() {
                        GalleryFinal.openGalleryMuti(100, 1, new GalleryFinal.OnHanlderResultCallback() {
                            @Override
                            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                                if (resultList == null || resultList.size() <= 0) {
                                    return;
                                }
                                callBack.cutAvatarPicEnter(resultList.get(0).getPhotoPath());
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
                                if (resultList == null || resultList.size() <= 0) {
                                    return;
                                }
                                callBack.cutAvatarPicEnter(resultList.get(0).getPhotoPath());
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

}
