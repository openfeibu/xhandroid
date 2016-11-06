package cn.flyexp.mvc.user;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.ReportRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/9 0009.
 */
public class ReportWindow extends AbstractWindow implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private UserViewCallBack callBack;
    private EditText et_content;
    private ArrayList<String> reportList = new ArrayList<String>();
    private int orderId = -1;

    public ReportWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_report);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_report).setOnClickListener(this);

        et_content = (EditText) findViewById(R.id.et_content);
        CheckBox checkBox1 = (CheckBox) findViewById(R.id.cb_report1);
        CheckBox checkBox2 = (CheckBox) findViewById(R.id.cb_report2);
        CheckBox checkBox3 = (CheckBox) findViewById(R.id.cb_report3);
        CheckBox checkBox4 = (CheckBox) findViewById(R.id.cb_report4);
        CheckBox checkBox5 = (CheckBox) findViewById(R.id.cb_report5);
        CheckBox checkBox6 = (CheckBox) findViewById(R.id.cb_report6);
        CheckBox checkBox7 = (CheckBox) findViewById(R.id.cb_report7);
        CheckBox checkBox8 = (CheckBox) findViewById(R.id.cb_report8);
        checkBox1.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        checkBox4.setOnCheckedChangeListener(this);
        checkBox5.setOnCheckedChangeListener(this);
        checkBox6.setOnCheckedChangeListener(this);
        checkBox7.setOnCheckedChangeListener(this);
        checkBox8.setOnCheckedChangeListener(this);
    }

    public void response() {
        findViewById(R.id.btn_report).setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_report:
                String content = et_content.getText().toString().trim();
                if (content == null || content.equals("")) {
                    showToast("内容不能为空");
                    return;
                }
                String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                if (orderId == -1) {
                    return;
                }
                String report = "";
                int i = 0;
                for (; i < reportList.size() - 1; i++) {
                    report += reportList.get(i) + ",";
                }
                report += reportList.get(i);
                ReportRequest reportRequest = new ReportRequest();
                reportRequest.setToken(token);
                reportRequest.setContent(content);
                reportRequest.setOid(orderId);
                reportRequest.setType(report);
                callBack.report(reportRequest);
                v.setEnabled(false);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            reportList.add(buttonView.getText().toString());
        } else {
            reportList.remove(buttonView.getText().toString());
        }
    }


    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
