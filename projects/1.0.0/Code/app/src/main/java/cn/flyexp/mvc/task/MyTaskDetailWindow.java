package cn.flyexp.mvc.task;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.FinishWorkRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.Constants;


/**
 * Created by txy on 2016/8/2.
 */
public class MyTaskDetailWindow extends AbstractWindow implements View.OnClickListener {

    private TaskViewCallBack callBack;
    private TextView tv_nickname;
    private TextView tv_money;
    private TextView tv_destination;
    private TextView tv_description;
    private String openId;
    private Button btn_finish;
    private int orderId;
    private String phone;
    private HorizontalStepView stepview;

    public MyTaskDetailWindow(TaskViewCallBack callBack) {
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
        list.add("发单");
        list.add("接单");
        list.add("完成");
        list.add("结算");
        stepview//设置完成的步数
                .setStepViewTexts(list)//总步骤
                .setTextSize(16)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getContext(), R.color.light_green))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getContext(), R.color.font_brown_light))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getContext(), R.color.light_green))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getContext(), R.color.font_brown_light))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getContext(), R.mipmap.icon_selected_green))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getContext(), R.mipmap.icon_prompt_gray))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getContext(), R.mipmap.icon_prompt_gray));//设置StepsViewIndicator DefaultIcon
    }

    public void initMyTaskDetail(MyTaskResponse.MyTaskResponseData responseData) {
        if (responseData == null) {
            return;
        }
        orderId = responseData.getOid();
        openId = responseData.getOpenid();
        phone = responseData.getAlt_phone();
        tv_destination.setText("到达地点：" + responseData.getDestination());
        tv_money.setText(responseData.getFee() + "");
        tv_description.setText(responseData.getDescription());
        tv_nickname.setText("发单人：" + responseData.getNickname());
        if (responseData.getStatus().equals("accepted")) {
            btn_finish.setVisibility(VISIBLE);
            btn_finish.setText("完成任务");
            btn_finish.setOnClickListener(this);
            stepview.setStepsViewIndicatorComplectingPosition(2);
        } else if (responseData.getStatus().equals("finish")) {
            btn_finish.setVisibility(GONE);
            stepview.setStepsViewIndicatorComplectingPosition(3);
        } else if (responseData.getStatus().equals("completed")) {
            btn_finish.setVisibility(GONE);
            stepview.setStepsViewIndicatorComplectingPosition(4);
        } else {
            btn_finish.setVisibility(GONE);
        }
    }

    public void response() {
        btn_finish.setEnabled(true);
        dismissProgressDialog();
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
            case R.id.btn_finish:
                String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                FinishWorkRequest finishWorkRequest = new FinishWorkRequest();
                finishWorkRequest.setToken(token);
                finishWorkRequest.setOrder_id(orderId);
                callBack.finishWork(finishWorkRequest);
                v.setEnabled(false);
                showProgressDialog("正在提交...");
                break;
            case R.id.tv_nickname:
                callBack.taWindowEnter(openId);
                break;
            case R.id.tv_contact:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
                    showToast("无法使用电话");
                }
                break;
        }
    }

}
