package cn.flyexp.window.user;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.MyInfoEditCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeMyInfoRequest;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.presenter.user.MyInfoEditPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/23.
 */
public class MyInfoEditWindow extends BaseWindow implements NotifyManager.Notify,TextWatcher, MyInfoEditCallback.ResponseCallback {

    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.edt_nickname)
    EditText edtNickname;

    private MyInfoResponse.MyInfoResponseData data;
    private String nickName;
    private String profile;
    private String address;
    private int gender = 1;
    private MyInfoEditPresenter myInfoEditPresenter;
    private SweetAlertDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_myinfo_edit;
    }

    public MyInfoEditWindow(Bundle bundle) {
        String value = bundle.getString("value");
        String key =   bundle.getString("key");
        int length = bundle.getInt("length");
        Log.e("TAG","KEY" + key + "" + value);
        myInfoEditPresenter = new MyInfoEditPresenter(this);

        initView();
    }

    private void initView() {
        edtNickname.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back,R.id.tv_save})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_save:
                Bundle bundle = new Bundle();
                bundle.putString("value",nickName);
                Message msg = Message.obtain();
                msg.what = NotifyIDDefine.NOTIFY_EDIT_RESULT;
                msg.setData(bundle);
                getNotifyManager().notify(msg);
                break;
        }
    }

    private void readyChangeMyInfo() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            ChangeMyInfoRequest changeMyInfoRequest = new ChangeMyInfoRequest();
            changeMyInfoRequest.setToken(token);
            changeMyInfoRequest.setAddress(address);
            changeMyInfoRequest.setNickname(nickName);
            changeMyInfoRequest.setIntroduction(profile);
            changeMyInfoRequest.setGender(gender);
            myInfoEditPresenter.requestChangeMyInfo(changeMyInfoRequest);
            loadingDialog.show();
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void responseChangeMyInfo(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
        hideWindow(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        nickName = edtNickname.getText().toString().trim();
//        profile = edtProfile.getText().toString().trim();
//        address = edtAddress.getText().toString().trim();
        checkEnabled();
    }

    private void checkEnabled() {
        if (TextUtils.isEmpty(nickName)) {
            tvSave.setEnabled(false);
            tvSave.setAlpha(0.5f);
        } else {
            if (TextUtils.equals(nickName, data.getNickname())
                    && TextUtils.equals(profile, data.getIntroduction())
                    && TextUtils.equals(address, data.getAddress())
                    && gender == data.getGender()) {
                tvSave.setEnabled(false);
                tvSave.setAlpha(0.5f);
            } else {
                tvSave.setEnabled(true);
                tvSave.setAlpha(1);
            }
        }
    }

    @Override
    public void onNotify(Message mes) {
        Bundle data = mes.getData();
        String value =   data.getString("value");
        Log.e("TAG", value);
    }
}
