package cn.flyexp.window.task;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.task.MyTaskDetailCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TaskCancelRequest;
import cn.flyexp.entity.TaskCompleteRequest;
import cn.flyexp.entity.TaskFinishRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.task.MyTaskDetailPresenter;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.ShareHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.PasswordView;
import cn.flyexp.view.TaskStepView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/6.
 */
public class MyTaskDetailWindow extends BaseWindow implements MyTaskDetailCallback.ResponseCallback {

    @InjectView(R.id.tv_nickname)
    TextView tvNickname;
    @InjectView(R.id.tv_state)
    TextView tvState;
    @InjectView(R.id.tv_fee)
    TextView tvFee;
    @InjectView(R.id.tv_destination)
    TextView tvDestination;
    @InjectView(R.id.tv_description)
    TextView tvDescription;
    @InjectView(R.id.tasklayout)
    View taskLayout;
    @InjectView(R.id.btn_task)
    Button btnTask;
    @InjectView(R.id.img_share)
    ImageView imgShare;
    @InjectView(R.id.sv_task)
    TaskStepView taskStepView;

    private MyTaskResponse.MyTaskResponseData data;
    private MyTaskDetailPresenter myTaskDetailPresenter;
    private PopupWindow picPopupWindow;
    private String currState;
    private boolean isTask;
    private SweetAlertDialog loadingDialog;
    private View inputPayPwdLayout;
    private String paypwd;
    private AlertDialog inputPayPwdDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_mytask_detail;
    }

    public MyTaskDetailWindow(Bundle bundle) {
        isTask = bundle.getBoolean("mytask");
        data = (MyTaskResponse.MyTaskResponseData) bundle.getSerializable("mytaskDetail");
        myTaskDetailPresenter = new MyTaskDetailPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
        inputPayPwdLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_paypwd, null);

        initView();
    }

    private void initView() {
        hideViewByStatus(data.getStatus());
        tvNickname.setText(String.format(getResources().getString(R.string.task_sender), data.getNickname()));
        tvFee.setText(String.format(getResources().getString(R.string.hint_task_money), data.getFee()));
        tvDestination.setText(data.getDestination());
        tvDescription.setText(data.getDescription());
        tvState.setText(currState);
        MyTaskResponse.MyTaskResponseData.TimeData timeData = data.getTime();
        if (TextUtils.isEmpty(timeData.getNew_time())) {
            taskStepView.showCancel(timeData.getCancelled_time());
        } else {
            ArrayList<String> taskDate = new ArrayList<>();
            taskDate.add(DateUtil.long2Date(DateUtil.date2Long(timeData.getNew_time()), "MM-dd HH:mm"));
            if (!TextUtils.isEmpty(timeData.getAccepted_time())) {
                taskDate.add(DateUtil.long2Date(DateUtil.date2Long(timeData.getAccepted_time()), "MM-dd HH:mm"));
                if (!TextUtils.isEmpty(timeData.getFinish_time())) {
                    taskDate.add(DateUtil.long2Date(DateUtil.date2Long(timeData.getFinish_time()), "MM-dd HH:mm"));
                    if (!TextUtils.isEmpty(timeData.getCompleted_time())) {
                        taskDate.add(DateUtil.long2Date(DateUtil.date2Long(timeData.getCompleted_time()), "MM-dd HH:mm"));
                    }
                }
            }
            taskStepView.showData(taskDate);
        }

        View popShareLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_share, null);
        popShareLayout.findViewById(R.id.tv_qq).setOnClickListener(shareOnClickListener);
        popShareLayout.findViewById(R.id.tv_wxf).setOnClickListener(shareOnClickListener);
        popShareLayout.findViewById(R.id.tv_wxq).setOnClickListener(shareOnClickListener);
        picPopupWindow = new PopupWindow(popShareLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        picPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        picPopupWindow.setFocusable(true);
        picPopupWindow.setOutsideTouchable(true);
        picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);

    }

    private OnClickListener shareOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            picPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.tv_qq:
                    ShareHelper.shareQQ(getContext(), getResources().getString(R.string.share_task_title),
                            data.getDescription(), data.getShare_url(), null);
                    break;
                case R.id.tv_wxf:
                    ShareHelper.shareWX(getContext(), ShareHelper.SHARE_WX_FIREND, getResources().getString(R.string.share_task_title),
                            data.getDescription(), data.getShare_url(), null);
                    break;
                case R.id.tv_wxq:
                    ShareHelper.shareWX(getContext(), ShareHelper.SHARE_WX_CIRCLE, getResources().getString(R.string.share_task_title),
                            data.getDescription(), data.getShare_url(), null);
                    break;
            }
        }
    };

    private void hideViewByStatus(String status) {
        if (status.equals("new")) {
            currState = getResources().getString(R.string.task_state_new);
            if (isTask) {
                btnTask.setVisibility(GONE);
                imgShare.setVisibility(VISIBLE);
            } else {
                btnTask.setText(R.string.cancel_task);
            }
        } else if (status.equals("finish")) {
            currState = getResources().getString(R.string.task_state_finish);
            if (isTask) {
                btnTask.setText(R.string.complete_task);
            } else {
                btnTask.setVisibility(GONE);
            }
        } else if (status.equals("accepted")) {
            currState = getResources().getString(R.string.task_state_accepted);
            if (isTask) {
                btnTask.setVisibility(GONE);
            } else {
                btnTask.setText(R.string.finish_task);
            }
        } else if (status.equals("completed")) {
            currState = getResources().getString(R.string.task_state_completed);
            btnTask.setVisibility(GONE);
        } else {
            btnTask.setVisibility(GONE);
        }
    }


    @OnClick({R.id.img_back, R.id.img_share, R.id.tv_report, R.id.btn_task, R.id.tv_contact})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.img_share:
                picPopupWindow.showAtLocation(taskLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.tv_report:
                Bundle bundle = new Bundle();
                bundle.putInt("oid", data.getOid());
                openWindow(WindowIDDefine.WINDOW_TASK_REPORT, bundle);
                break;
            case R.id.btn_task:
                String status = data.getStatus();
                if (status.equals("new")) {
                    readyTaskCancel();
                } else if (status.equals("accepted")) {
                    readyTaskFinish();
                } else if (status.equals("finish")) {
                    showInputPayPwd();
                }
                break;
            case R.id.tv_contact:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String mobile = isTask ? data.getAlt_phone() : data.getPhone();
                Uri uri = Uri.parse("tel:" + mobile);
                intent.setData(uri);
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    showToast(R.string.unable_call);
                }
                break;
        }
    }

    private void showInputPayPwd() {
        if (inputPayPwdDialog == null) {
            inputPayPwdDialog = new AlertDialog.Builder(getContext()).setView(inputPayPwdLayout).create();
        }
        inputPayPwdDialog.show();
        final PasswordView passwordView = (PasswordView) inputPayPwdDialog.findViewById(R.id.edt_paypwd);
        passwordView.requestFocus();
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                paypwd = passwordView.getText().toString();
                if (paypwd.length() == 6) {
                    inputPayPwdDialog.dismiss();
                    readyTaskComplelte();
                }
            }
        });
    }

    private void readyTaskCancel() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            TaskCancelRequest taskCancelRequest = new TaskCancelRequest();
            taskCancelRequest.setToken(token);
            taskCancelRequest.setOrder_id(data.getOid());
            myTaskDetailPresenter.requestTaskCancel(taskCancelRequest);
            loadingDialog.show();
        }
    }

    private void readyTaskFinish() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            TaskFinishRequest taskFinishRequest = new TaskFinishRequest();
            taskFinishRequest.setToken(token);
            taskFinishRequest.setOrder_id(data.getOid());
            myTaskDetailPresenter.requestTaskFinish(taskFinishRequest);
            loadingDialog.show();
        }
    }

    private void readyTaskComplelte() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            TaskCompleteRequest taskCompleteRequest = new TaskCompleteRequest();
            taskCompleteRequest.setToken(token);
            taskCompleteRequest.setOrder_id(data.getOid());
            taskCompleteRequest.setPay_password(paypwd);
            myTaskDetailPresenter.requestTaskComplete(taskCompleteRequest);
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
    public void responseTaskCancel(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MYTASK);
        tvState.setText(R.string.task_state_cancel);
        btnTask.setVisibility(GONE);
        showToast(R.string.task_cancel_success);
    }

    @Override
    public void responseTaskFinish(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MYTASK);
        tvState.setText(R.string.task_state_finish);
        btnTask.setVisibility(GONE);
        showToast(R.string.task_finish_success);
    }

    @Override
    public void responseTaskComplete(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MYTASK);
        tvState.setText(R.string.task_state_completed);
        btnTask.setVisibility(GONE);
        showToast(R.string.task_complete_success);
    }
}
