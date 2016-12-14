package cn.flyexp.window.assn;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.assn.AssnExamineCallback;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnExamineRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.presenter.assn.AssnExaminePresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class AssnExamineDetailWindow extends BaseWindow implements AssnExamineCallback.ResponseCallback {

    @InjectView(R.id.tv_name)
    TextView tvName;
    @InjectView(R.id.tv_pro)
    TextView tvPro;
    @InjectView(R.id.tv_phone)
    TextView tvPhone;
    @InjectView(R.id.tv_cause)
    TextView tvCause;


    private AssnExamineListResponse.AssnExamineListResponseData data;
    private AssnExaminePresenter assnExaminePresenter;
    private SweetAlertDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_examine_detail;
    }

    public AssnExamineDetailWindow(Bundle bundle) {
        data = (AssnExamineListResponse.AssnExamineListResponseData) bundle.getSerializable("examine");
        assnExaminePresenter = new AssnExaminePresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));

        initView();
    }

    private void initView() {
        tvName.setText(data.getAr_username());
        tvPro.setText(data.getProfession());
        tvPhone.setText(data.getMobile_no());
        tvCause.setText(data.getCauses());
    }


    @OnClick({R.id.img_back, R.id.btn_nopass, R.id.btn_pass})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_nopass:
                readyAssnExamine(1);
                break;
            case R.id.btn_pass:
                readyAssnExamine(0);
                break;
        }
    }

    private void readyAssnExamine(int status) {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if(TextUtils.isEmpty(token)){
            renewLogin();
        }else{
            AssnExamineRequest assnExamineRequest = new AssnExamineRequest();
            assnExamineRequest.setAssociation_id(data.getAid());
            assnExamineRequest.setStatus(status);
            assnExamineRequest.setToken(token);
            assnExamineRequest.setUid(data.getUid());
            assnExaminePresenter.requestAssnExamine(assnExamineRequest);
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
    public void responseAssnExamine(BaseResponse response) {
        showToast(R.string.success);
        getNotifyManager().notify(NotifyIDDefine.NOTICE_ASSN_EXAMINE_LIST);
        hideWindow(true);
    }
}
