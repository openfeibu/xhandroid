package cn.flyexp.mvc.task;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.CancelTaskRequest;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.Constants;


/**
 * Created by txy on 2016/8/2.
 */
public class MyOrderDetailWindow extends AbstractWindow implements View.OnClickListener {

    private TaskViewCallBack callBack;
    private TextView tv_nickname;
    private TextView tv_money;
    private TextView tv_destination;
    private TextView tv_description;
    private int orderId;
    private String openId;
    private Button btn_finish;
    private String phone;
    private HorizontalStepView stepview;
    private String status;
    private FinishOrderRequest finishOrderRequest;

    public MyOrderDetailWindow(TaskViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_mytask_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_report).setOnClickListener(this);
        findViewById(R.id.tv_contact).setOnClickListener(this);

        btn_finish = (Button) findViewById(R.id.btn_finish);

        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_nickname.setOnClickListener(this);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_destination = (TextView) findViewById(R.id.tv_destination);
        tv_description = (TextView) findViewById(R.id.tv_description);

        stepview = (HorizontalStepView) findViewById(R.id.step_view);
        List<String> list = new ArrayList<>();
        list.add("??????");
        list.add("??????");
        list.add("??????");
        list.add("??????");
        stepview//?????????????????????
                .setStepViewTexts(list)//?????????
                .setTextSize(16)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getContext(), R.color.light_green))//??????StepsViewIndicator??????????????????
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getContext(), R.color.font_brown_light))//??????StepsViewIndicator?????????????????????
                .setStepViewComplectedTextColor(ContextCompat.getColor(getContext(), R.color.light_green))//??????StepsView text??????????????????
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getContext(), R.color.font_brown_light))//??????StepsView text?????????????????????
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getContext(), R.mipmap.icon_selected_green))//??????StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getContext(), R.mipmap.icon_prompt_gray))//??????StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getContext(), R.mipmap.icon_prompt_gray));//??????StepsViewIndicator DefaultIcon
    }

    public void initMyOrderDetail(MyTaskResponse.MyTaskResponseData responseData) {
        if (responseData == null) {
            return;
        }
        orderId = responseData.getOid();
        openId = responseData.getOpenid();
        phone = responseData.getPhone();
        tv_destination.setText("???????????????" + responseData.getDestination());
        tv_money.setText(responseData.getFee() + "");
        tv_description.setText(responseData.getDescription());
        if (responseData.getNickname().equals("")) {
            tv_nickname.setText("???????????????");
            tv_nickname.setTextColor(getResources().getColor(R.color.font_brown_light));
        } else {
            tv_nickname.setText("????????????" + responseData.getNickname());
            tv_nickname.setTextColor(getResources().getColor(R.color.font_brown_dark));
        }
        status = responseData.getStatus();
        if (status.equals("new")) {
            btn_finish.setVisibility(VISIBLE);
            btn_finish.setText("????????????");
            btn_finish.setOnClickListener(this);
            stepview.setStepsViewIndicatorComplectingPosition(1);
        } else if (status.equals("accepted")) {
            btn_finish.setVisibility(GONE);
            stepview.setStepsViewIndicatorComplectingPosition(2);
        } else if (status.equals("finish")) {
            btn_finish.setVisibility(VISIBLE);
            btn_finish.setText("????????????");
            btn_finish.setOnClickListener(this);
            stepview.setStepsViewIndicatorComplectingPosition(3);
        } else if (status.equals("completed")) {
            btn_finish.setVisibility(GONE);
            stepview.setStepsViewIndicatorComplectingPosition(4);
        } else {
            btn_finish.setVisibility(GONE);
        }

    }

    public void finishWork(String pwd) {
        finishOrderRequest.setPay_password(pwd);
        callBack.finishOrder(finishOrderRequest);
        showProgressDialog("????????????...");
        btn_finish.setEnabled(false);
    }


    public void response() {
        dismissProgressDialog();
        btn_finish.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_report:
                callBack.reportEnter(orderId);
                break;
            case R.id.tv_nickname:
                callBack.taWindowEnter(openId);
                break;
            case R.id.btn_finish:
                final String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                int is_paypwd = getIntByPreference("is_paypwd");
                if (is_paypwd == 0) {
                    callBack.setPayPwdEnter();
                    return;
                }
                if (status.equals("new")) {
                    showAlertDialog("????????????????????????~", "????????????", "????????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CancelTaskRequest cancelTaskRequest = new CancelTaskRequest();
                            cancelTaskRequest.setToken(token);
                            cancelTaskRequest.setOrder_id(orderId);
                            callBack.cancelTask(cancelTaskRequest);
                        }
                    });

                } else if (status.equals("finish")) {
                    showAlertDialog("????????????????????????", "??????", "?????????", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishOrderRequest = new FinishOrderRequest();
                            finishOrderRequest.setToken(token);
                            finishOrderRequest.setOrder_id(orderId);
                            callBack.verifiPayPwdEnter(Constants.PAY_RESULT_FINISHTAK);
                        }
                    });
                }
                break;
            case R.id.tv_contact:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    showToast("????????????????????????");
                }
                break;
        }
    }
}
