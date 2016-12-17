package cn.flyexp.window.user;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.user.CertificationCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CertificationRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.user.CertificationPresenter;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.util.UploadFileHelper;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class CertificationWindow extends BaseWindow implements NotifyManager.Notify, TextWatcher, CertificationCallback.ResponseCallback {

    @InjectView(R.id.img_negphoto)
    ImageView imgNegphoto;
    @InjectView(R.id.img_posphoto)
    ImageView imgPosphoto;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_numberid)
    EditText edtNumberid;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;

    private CertificationPresenter certificationPresenter;
    private SweetAlertDialog loadingDialog;
    private String name;
    private String numberId;
    private String negphotoPath;
    private String posphotoPath;
    private boolean ispos;

    @Override
    protected int getLayoutId() {
        return R.layout.window_certification;
    }

    public CertificationWindow() {
        getNotifyManager().register(NotifyIDDefine.NOTIFY_GALLERY, this);
        certificationPresenter = new CertificationPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));
        edtName.addTextChangedListener(this);
        edtNumberid.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.layout_negphoto, R.id.layout_posphoto, R.id.btn_confirm})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.layout_negphoto:
                ispos = false;
                Bundle negbundle = new Bundle();
                negbundle.putInt("max", 1);
                openWindow(WindowIDDefine.WINDOW_GALLERY, negbundle);
                break;
            case R.id.layout_posphoto:
                ispos = true;
                Bundle posbundle = new Bundle();
                posbundle.putInt("max", 1);
                openWindow(WindowIDDefine.WINDOW_GALLERY, posbundle);
                break;
            case R.id.btn_confirm:
                if (numberId.length() != 18 && numberId.length() != 15) {
                    showToast(R.string.numberId_format_illegal);
                    return;
                }
                if (TextUtils.isEmpty(negphotoPath) || TextUtils.isEmpty(posphotoPath)) {
                    showToast(R.string.unable_lack_photo);
                    return;
                }
                final String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            CertificationRequest certificationRequest = new CertificationRequest();
                            certificationRequest.setName(name);
                            certificationRequest.setId_number(numberId);
                            certificationRequest.setToken(token);
                            ArrayList<File> files = new ArrayList<>();
                            files.add(BitmapUtil.compressBmpToFile(negphotoPath));
                            files.add(BitmapUtil.compressBmpToFile(posphotoPath));
                            MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(files);
                            certificationPresenter.requestCertification(multipartBody, certificationRequest);
                        }
                    }.start();
                    loadingDialog.show();
                }
                break;
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
    public void responseCertification(BaseResponse response) {
        showToast(R.string.upload_certifition_success);
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
        name = edtName.getText().toString().trim();
        numberId = edtNumberid.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(numberId)) {
            btnConfirm.setAlpha(0.5f);
            btnConfirm.setEnabled(false);
        } else {
            btnConfirm.setAlpha(1f);
            btnConfirm.setEnabled(true);
        }
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_GALLERY) {
            Bundle bundle = mes.getData();
            ArrayList<String> images = bundle.getStringArrayList("images");
            if (ispos) {
                posphotoPath = images.get(0);
                Glide.with(getContext()).load(posphotoPath).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imgPosphoto);
            } else {
                negphotoPath = images.get(0);
                Glide.with(getContext()).load(negphotoPath).diskCacheStrategy(DiskCacheStrategy.NONE).centerCrop().into(imgNegphoto);
            }
        }
    }
}
