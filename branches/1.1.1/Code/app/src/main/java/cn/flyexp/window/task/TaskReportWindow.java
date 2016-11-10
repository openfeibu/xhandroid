package cn.flyexp.window.task;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.task.TaskReportCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskReportRequest;
import cn.flyexp.presenter.task.TaskReportPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class TaskReportWindow extends BaseWindow implements TextWatcher, TaskReportCallback.ResponseCallback {

    @InjectView(R.id.edt_content)
    EditText edtContent;
    @InjectView(R.id.btn_report)
    Button btnReport;

    private int oid;
    private ArrayList<String> reportList = new ArrayList<>();
    private TaskReportPresenter taskReportPresenter;
    private String content;
    private  SweetAlertDialog loadingDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_task_report;
    }

    public TaskReportWindow(Bundle bundle) {
        oid = bundle.getInt("oid");
        taskReportPresenter = new TaskReportPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(),getResources().getString(R.string.commiting));

        edtContent.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.btn_report})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_report:
                readyTaskReport();
                break;
        }
    }

    private void readyTaskReport() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        StringBuilder report = new StringBuilder();
        for (String r : reportList) {
            report.append(r);
            report.append("--");
        }
        TaskReportRequest taskReportRequest = new TaskReportRequest();
        taskReportRequest.setToken(token);
        taskReportRequest.setContent(content);
        taskReportRequest.setOid(oid);
        taskReportRequest.setType(report.toString());
        taskReportPresenter.requestTaskReport(taskReportRequest);
        loadingDialog.show();
    }

    @OnCheckedChanged({R.id.cb_report1, R.id.cb_report2, R.id.cb_report3, R.id.cb_report4})
    void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            reportList.add(compoundButton.getText().toString());
        } else {
            reportList.remove(compoundButton.getText().toString());
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
    public void responseTaskReport(BaseResponse response) {
        showToast(R.string.report_success);
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
            btnReport.setAlpha(0.5f);
            btnReport.setEnabled(false);
        } else {
            btnReport.setAlpha(1f);
            btnReport.setEnabled(true);
        }
    }
}
