package cn.flyexp.mvc.assn;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnExamineRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;

/**
 * Created by tanxinye on 2016/10/3.
 */
public class AssnExamineDetailWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private TextView tv_nickname;
    private TextView tv_pro;
    private TextView tv_phone;
    private TextView tv_casues;
    private int aid;
    private int uid;

    public AssnExamineDetailWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_examine_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_nopass).setOnClickListener(this);
        findViewById(R.id.btn_pass).setOnClickListener(this);
        tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        tv_pro = (TextView) findViewById(R.id.tv_pro);
        tv_casues = (TextView) findViewById(R.id.tv_casues);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
    }

    public void initData(AssnExamineListResponse.AssnExamineListResponseData data) {
        if (data == null) {
            return;
        }
        aid = data.getAid();
        uid = data.getUid();
        tv_nickname.setText(data.getAr_username());
        tv_pro.setText(data.getProfession());
        tv_casues.setText(data.getCauses());
        tv_phone.setText(data.getMobile_no());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_nopass:
                passExamine(false);
                break;
            case R.id.btn_pass:
                passExamine(true);
                break;
        }
    }

    private void passExamine(boolean pass) {
        String token = WindowHelper.getStringByPreference(SharedPrefs.KET_TOKEN);
        if (TextUtils.isEmpty(token)) {
            callBack.loginWindowEnter();
            return;
        }
        AssnExamineRequest assnExamineRequest = new AssnExamineRequest();
        assnExamineRequest.setToken(token);
        assnExamineRequest.setAssociation_id(aid);
        assnExamineRequest.setUid(uid);
        assnExamineRequest.setStatus(pass ? 0 : 1);
        callBack.assnExamine(assnExamineRequest);
    }
}
