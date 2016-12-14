package cn.flyexp.window.assn;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.assn.AssnNoticePublishCallback;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.presenter.assn.AssnNoticePublishPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnNoticePublishWindow extends BaseWindow implements TextWatcher, AssnNoticePublishCallback.ResponseCallback {

    @InjectView(R.id.edt_content)
    EditText edtContent;
    @InjectView(R.id.btn_publish)
    Button btnPublish;

    private int aid;
    private AssnNoticePublishPresenter assnNoticePublishPresenter;
    private String content;
    private SweetAlertDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_notice_publish;
    }

    public AssnNoticePublishWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        assnNoticePublishPresenter = new AssnNoticePublishPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));

        edtContent.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.btn_publish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_publish:
                readyAssnNoticePublish();
                break;
        }
    }

    private void readyAssnNoticePublish() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            AssnNoticePublishRequest assnNoticePublishRequest = new AssnNoticePublishRequest();
            assnNoticePublishRequest.setAssociation_id(aid);
            assnNoticePublishRequest.setToken(token);
            assnNoticePublishRequest.setNotice(content);
            assnNoticePublishPresenter.requestAssnNoticePublish(assnNoticePublishRequest);
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
    public void responseAssnNoticePublish(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTICE_MYASSN_DETAIL);
        showToast(R.string.publish_success);
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
        content = edtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            btnPublish.setAlpha(0.5f);
            btnPublish.setEnabled(false);
        } else {
            btnPublish.setAlpha(1);
            btnPublish.setEnabled(true);
        }
    }
}
