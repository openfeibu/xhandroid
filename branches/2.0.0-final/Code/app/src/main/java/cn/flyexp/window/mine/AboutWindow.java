package cn.flyexp.window.mine;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.mine.AboutCallback;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.mine.AboutPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.PackageUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/28.
 */
public class AboutWindow extends BaseWindow implements AboutCallback.ResponseCallback {

    @InjectView(R.id.tv_versionname)
    TextView versionNameTextView;
    private int versionCode;
    private AboutPresenter aboutPresenter;
    private View updateLayout;
    private AlertDialog dialog;
    private UpdateResponse.UpdateResponseData data;
    private final TextView tvMsg;

    @Override
    protected int getLayoutId() {
        return R.layout.window_about;
    }

    public AboutWindow() {
        String versionName = PackageUtil.getVersionName(getContext());
        versionCode = PackageUtil.getVersionCode(getContext());
        versionNameTextView.setText(String.format(getResources().getString(R.string.verison_name), versionName));
        aboutPresenter = new AboutPresenter(this);
        updateLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update, null);
        updateLayout.findViewById(R.id.tv_yes).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(data.getDownload());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });
        updateLayout.findViewById(R.id.tv_no).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvMsg = (TextView) updateLayout.findViewById(R.id.tv_msg);
    }

    @OnClick({R.id.img_back, R.id.layout_checkupdate, R.id.layout_agreement, R.id.layout_taskstatement, R.id.layout_integral})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.layout_checkupdate:
                UpdateRequest updateRequest = new UpdateRequest();
                updateRequest.setPlatform("and");
                aboutPresenter.requestUpdate(updateRequest);
                break;
            case R.id.layout_agreement:
                WebBean agreeBean = new WebBean();
                agreeBean.setRequest(true);
                agreeBean.setTitle(getResources().getString(R.string.user_agreement));
                agreeBean.setName("userAgreement");
                openWebWindow(agreeBean);
                break;
            case R.id.layout_taskstatement:
                WebBean taskBean = new WebBean();
                taskBean.setRequest(true);
                taskBean.setTitle(getResources().getString(R.string.task_statement));
                taskBean.setName("taskStatement");
                openWebWindow(taskBean);
                break;
            case R.id.layout_integral:
                WebBean integralBean = new WebBean();
                integralBean.setRequest(true);
                integralBean.setTitle(getResources().getString(R.string.integral_statement));
                integralBean.setName("integral");
                openWebWindow(integralBean);
                break;
        }
    }

    private void openWebWindow(WebBean webBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("webbean", webBean);
        openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
    }

    @Override
    public void responseUpdate(UpdateResponse response) {
        final UpdateResponse.UpdateResponseData responseData = response.getData();
        int downloadVersionCode = responseData.getCode();
        if (downloadVersionCode > versionCode) {
            data = response.getData();
            showDialog();
        } else {
            DialogHelper.showSingleDialog(getContext(), getResources().getString(R.string.newest_version), null);
        }
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(getContext()).setView(updateLayout).create();
        }
        tvMsg.setText(data.getDetail());
        dialog.show();
    }
}
