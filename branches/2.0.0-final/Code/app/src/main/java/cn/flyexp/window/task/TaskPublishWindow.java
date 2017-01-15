package cn.flyexp.window.task;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.task.TaskPublishCallback;
import cn.flyexp.entity.DataResponse;
import cn.flyexp.entity.PayResult;
import cn.flyexp.entity.TaskPublishRequest;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.presenter.task.TaskPublishPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.PasswordView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/8.
 */
public class TaskPublishWindow extends BaseWindow implements TextWatcher, TaskPublishCallback.ResponseCallback {

    @InjectView(R.id.edt_description)
    EditText edtDescription;
    @InjectView(R.id.edt_fee)
    EditText edtFee;
    @InjectView(R.id.edt_destination)
    EditText edtDestination;
    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.tv_limit)
    TextView tv_limit;
    @InjectView(R.id.btn_publish)
    Button btnPublish;

    private TaskPublishPresenter taskPublishPresenter;
    private SweetAlertDialog loadingDialog;
    private float fee;
    private String description;
    private String phone;
    private String destination;
    private TextView tvBalance;
    private float balance;
    private TaskPublishRequest taskPublishRequest;
    private View inputPayPwdLayout;
    private String paypwd;
    private AlertDialog inputPayPwdDialog;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.window_task_publish;
    }

    public TaskPublishWindow() {
        taskPublishPresenter = new TaskPublishPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
        inputPayPwdLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_paypwd, null);

        initView();
        readDefault();
    }

    private void initView() {
        String phone = SharePresUtil.getString(SharePresUtil.KEY_PHONE);
        if (!TextUtils.isEmpty(phone)) {
            edtPhone.setText(phone);
        }
        String address = SharePresUtil.getString(SharePresUtil.KEY_ADDRESS);
        if (!TextUtils.isEmpty(address)) {
            edtDestination.setText(address);
        }
        edtPhone.addTextChangedListener(this);
        edtDestination.addTextChangedListener(this);
        edtDescription.addTextChangedListener(this);
        edtFee.addTextChangedListener(this);

        View popPicLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_payway, null);
        balance = SharePresUtil.getFloat(SharePresUtil.KEY_BALANCE);
        tvBalance = (TextView) popPicLayout.findViewById(R.id.tv_balance);
        tvBalance.setText(String.format(getResources().getString(R.string.hint_paybalance), balance));
        tvBalance.setOnClickListener(paywayOnClick);
        popPicLayout.findViewById(R.id.tv_alipay).setOnClickListener(paywayOnClick);
        popPicLayout.findViewById(R.id.btn_cancel).setOnClickListener(paywayOnClick);

        popupWindow = new PopupWindow(popPicLayout,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1f);
            }
        });
    }

    private void changeWindowAlpha(float v) {
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    private OnClickListener paywayOnClick = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_balance:
                    taskPublishRequest.setPay_id(3);
                    int setPayPwd = SharePresUtil.getInt(SharePresUtil.KEY_SETPAYPWD);
                    if (setPayPwd == 0) {
                        openWindow(WindowIDDefine.WINDOW_WALLET_SETPAYPWD);
                    } else if (setPayPwd == 1) {
                        showInputPayPwd();
                    }
                    break;
                case R.id.tv_alipay:
                    taskPublishRequest.setPay_id(1);
                    taskPublishPresenter.requestTaskPublish(taskPublishRequest);
                    break;
                case R.id.btn_cancel:
                    break;
            }
            popupWindow.dismiss();
        }
    };

    @OnClick({R.id.img_back, R.id.tv_taskstatement, R.id.btn_publish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if (!isBackInEdit()) {
                    hideWindow(true);
                }
                break;
            case R.id.tv_taskstatement:
                WebBean webBean = new WebBean();
                webBean.setRequest(true);
                webBean.setTitle(getResources().getString(R.string.task_statement));
                webBean.setName("taskStatement");
                Bundle bundle = new Bundle();
                bundle.putSerializable("webbean", webBean);
                openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
                break;
            case R.id.btn_publish:
                readyTaskPublish();
                break;
        }
    }

    private void readyTaskPublish() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        if (!PatternUtil.validatePhone(phone)) {
            showToast(R.string.phone_format_illegal);
            return;
        }
        if (balance < fee) {
            tvBalance.setText(String.format(getResources().getString(R.string.hint_paybalance_unable), balance));
            tvBalance.setTextColor(getResources().getColor(R.color.font_light));
            tvBalance.setEnabled(false);
        }
        taskPublishRequest = new TaskPublishRequest();
        taskPublishRequest.setToken(token);
        taskPublishRequest.setDescription(description);
        taskPublishRequest.setDestination(destination);
        taskPublishRequest.setFee(fee);
        taskPublishRequest.setGoods_fee(0);
        taskPublishRequest.setPhone(phone);
        popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
        changeWindowAlpha(0.7f);
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (inputPayPwdDialog == null) {
                return;
            }
            PasswordView passwordView = (PasswordView) inputPayPwdDialog.findViewById(R.id.edt_paypwd);
            paypwd = passwordView.getText().toString();
            if (paypwd.length() == 6) {
                taskPublishRequest.setPay_password(EncodeUtil.md5Encode(paypwd));
                taskPublishPresenter.requestTaskPublish(taskPublishRequest);
                inputPayPwdDialog.dismiss();
                loadingDialog.show();
            }
        }
    };

    private void showInputPayPwd() {
        if (inputPayPwdDialog == null) {
            inputPayPwdDialog = new AlertDialog.Builder(getContext()).setView(inputPayPwdLayout).create();
        }
        inputPayPwdDialog.show();
        PasswordView passwordView = (PasswordView) inputPayPwdDialog.findViewById(R.id.edt_paypwd);
        passwordView.setText("");
        passwordView.removeTextChangedListener(watcher);
        passwordView.addTextChangedListener(watcher);
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
    public void responseTaskPublish(DataResponse response) {
        final String payInfo = response.getData();
        if (TextUtils.isEmpty(payInfo)) {
            hideWindow(true);
            showToast(R.string.hint_pay_success);
            getNotifyManager().notify(NotifyIDDefine.NOTIFY_TASK_REFRESH);
        } else {
            PermissionHandler.PermissionCallback permissionCallback = new PermissionHandler.PermissionCallback() {
                public void onSuccess() {
                    alipay(payInfo);
                }

                public void onFail(int[] ids) {
                }

                public void onCancel() {
                }

                public void goSetting() {
                }
            };
            PermissionTools.requestPermission(getContext(), permissionCallback, new int[]{PermissionHandler.PERMISSION_FILE, PermissionHandler.PERMISSION_PHONE});
        }
    }

    private Handler payHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                PayResult payResult = new PayResult((String) msg.obj);
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    showToast(R.string.alipay_success);
                    hideWindow(true);
                    getNotifyManager().notify(NotifyIDDefine.NOTIFY_TASK_REFRESH);
                } else {
                    // 判断resultStatus 为非"9000"则代表可能支付失败
                    // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {
                        showToast(R.string.alipay_result_confirming);
                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        showToast(R.string.alipay_failure);
                    }
                }
            }
        }
    };

    private void alipay(final String payInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) getContext());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        readDefault();
        if (fee < 2) {
            tv_limit.setVisibility(VISIBLE);
        } else {
            tv_limit.setVisibility(GONE);
        }
        if (TextUtils.isEmpty(description) || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(destination) || fee < 2) {
            btnPublish.setEnabled(false);
            btnPublish.setAlpha(0.5f);
        } else {
            btnPublish.setEnabled(true);
            btnPublish.setAlpha(1);
        }
    }

    private void readDefault() {
        description = edtDescription.getText().toString().trim();
        phone = edtPhone.getText().toString().trim();
        destination = edtDestination.getText().toString().trim();
        String feeStr = edtFee.getText().toString().trim();
        if (TextUtils.isEmpty(feeStr)) {
            fee = 2;
        } else {
            try {
                fee = Float.valueOf(feeStr);
            } catch (NumberFormatException e) {
                fee = 2;
            }
        }
    }


    private boolean isBackInEdit() {
        if (TextUtils.isEmpty(description)) {
            return false;
        } else {
            DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_giveup_edit), getResources().getString(R.string.dialog_giveup), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dismissProgressDialog(sweetAlertDialog);
                    hideWindow(true);
                }
            });
            return true;
        }
    }

    @Override
    public boolean onBackPressed() {
        return isBackInEdit();
    }
}
