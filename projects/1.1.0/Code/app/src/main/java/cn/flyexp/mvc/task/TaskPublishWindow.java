package cn.flyexp.mvc.task;

import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.regex.Pattern;

import cn.flyexp.R;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.constants.Config;
import cn.flyexp.entity.OrderCreateRequest;
import cn.flyexp.framework.AbstractWindow;


/**
 * Created by txy on 2016/6/5.
 */
public class TaskPublishWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private TaskViewCallBack callBack;
    private EditText et_description;
    private EditText et_fee;
    private EditText et_destination;
    private EditText et_phone;
    private View taskLayout;
    private PopupWindow picPopupWindow;
    private TextView tv_balance;
    private float balance;
    private OrderCreateRequest orderCreateRequest;
    private int textState;
    private Button btn_send;

    public TaskPublishWindow(TaskViewCallBack viewActionCallBack) {
        super(viewActionCallBack);
        this.callBack = viewActionCallBack;
        initView();
        setDefault();
    }

    private void initView() {
        setContentView(R.layout.window_task_publish);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_taskStatement).setOnClickListener(this);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        taskLayout = findViewById(R.id.taskLayout);

        et_description = (EditText) findViewById(R.id.et_description);
        et_description.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.TASK_CONTENT));
        et_fee = (EditText) findViewById(R.id.et_fee);
        et_destination = (EditText) findViewById(R.id.et_destination);
        et_phone = (EditText) findViewById(R.id.et_phone);

        View popPicLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_payway, null);
        balance = WindowHelper.getFloatByPreference("balance");
        tv_balance = (TextView) popPicLayout.findViewById(R.id.tv_balance);
        tv_balance.setText("账户余额：￥" + balance);
        tv_balance.setOnClickListener(this);
        popPicLayout.findViewById(R.id.tv_alipay).setOnClickListener(this);
        popPicLayout.findViewById(R.id.btn_cancel).setOnClickListener(this);
        picPopupWindow = new PopupWindow(popPicLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        picPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        picPopupWindow.setFocusable(true);
        picPopupWindow.setOutsideTouchable(true);
        picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);

        et_description.addTextChangedListener(this);
        et_fee.addTextChangedListener(this);
        et_destination.addTextChangedListener(this);
        et_phone.addTextChangedListener(this);
    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return false;
    }

    private void setDefault() {
        String phone = WindowHelper.getStringByPreference("mobile_no");
        if (!phone.equals("")) {
            et_phone.setText(phone);
        }
        String address = WindowHelper.getStringByPreference("address");
        if (!address.equals("")) {
            et_destination.setText(address);
        }
    }

    public void balancePay(String pwd) {
        showProgressDialog("正在支付...");
        orderCreateRequest.setPay_password(pwd);
        callBack.createOrder(orderCreateRequest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                String destination = et_destination.getText().toString().trim();
                String feeStr = et_fee.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                String description = et_description.getText().toString().trim();
                String token = WindowHelper.getStringByPreference("token");
                float fee = Float.parseFloat(feeStr);
                if (fee < 2) {
                    WindowHelper.showToast(getContext().getString(R.string.please_input_fee_error));
                    return;
                }
                if (!Pattern.compile("1\\d{10}").matcher(phone).matches()) {
                    WindowHelper.showToast(getContext().getString(R.string.phone_format_error));
                    return;
                }
                if (balance < fee) {
                    tv_balance.setTextColor(getResources().getColor(R.color.font_brown_light));
                    tv_balance.setEnabled(false);
                    tv_balance.setText("账户余额：￥" + balance + "（余额不足）");
                }
                picPopupWindow.showAtLocation(taskLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                orderCreateRequest = new OrderCreateRequest();
                orderCreateRequest.setToken(token);
                orderCreateRequest.setDestination(destination);
                orderCreateRequest.setDescription(description);
                orderCreateRequest.setFee(fee);
                orderCreateRequest.setGoods_fee(0);
                orderCreateRequest.setPhone(phone);
                break;
            case R.id.iv_back:
                hideWindow(false);
                break;
            case R.id.tv_taskStatement:
                WebBean taskWebBean = new WebBean();
                taskWebBean.setRequest(true);
                taskWebBean.setTitle("任务声明");
                taskWebBean.setName("taskStatement");
                callBack.webWindowEnter(taskWebBean);
                break;
            case R.id.tv_balance:
                orderCreateRequest.setPay_id(3);
                int is_paypwd = WindowHelper.getIntByPreference("is_paypwd");
                if (is_paypwd == 0) {
                    callBack.setPayPwdEnter();
                } else if (is_paypwd == 1) {
                    callBack.verifiPayPwdEnter(NotifyIDDefine.PRESULT_PAY_TASK);
                }
                picPopupWindow.dismiss();
                break;
            case R.id.tv_alipay:
                orderCreateRequest.setPay_id(1);
                callBack.createOrder(orderCreateRequest);
                picPopupWindow.dismiss();
                break;
            case R.id.btn_cancel:
                picPopupWindow.dismiss();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String desription = et_description.getText().toString().trim();
        callBack.setUIData(WindowCallBack.UIDataKeysDef.TASK_CONTENT, desription);
        String fee = et_fee.getText().toString().trim();
        String destination = et_destination.getText().toString().trim();
        String phone = et_phone.getText().toString().trim();
        if (desription.equals("") || fee.equals("") || destination.equals("") || phone.equals("")) {
            btn_send.setAlpha(0.5f);
            btn_send.setEnabled(false);
        } else {
            btn_send.setAlpha(1f);
            btn_send.setEnabled(true);
        }
    }
}
