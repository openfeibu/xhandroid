package cn.flyexp.mvc.mine;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class AboutWindow extends AbstractWindow implements View.OnClickListener {

    private TextView tv_versionName;
    private int curVersionCode;
    private String curVersionName;
    private MineViewCallBack callBack;

    public AboutWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        loadVersion();
    }

    private void initView() {
        setContentView(R.layout.window_about);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.layout_checkupdate).setOnClickListener(this);
        findViewById(R.id.layout_agreement).setOnClickListener(this);
        findViewById(R.id.layout_taskStatement).setOnClickListener(this);
        findViewById(R.id.layout_integral).setOnClickListener(this);

        tv_versionName = (TextView) findViewById(R.id.tv_versionName);
    }

    private void loadVersion() {
        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            curVersionCode = packageInfo.versionCode;
            curVersionName = packageInfo.versionName;
            tv_versionName.setText("当前版本 v" + curVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void response() {
        findViewById(R.id.layout_checkupdate).setEnabled(true);
    }

    public void responseData(final UpdateResponse.UpdateResponseData data) {
        int updateCode = data.getCode();
        if (updateCode > curVersionCode) {
            WindowHelper. showAlertDialog(data.getDetail(), "取消", "前往下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse(data.getDownload());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }
            });
        } else {
            WindowHelper.showToast("已经最新版本，敬请期待~");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.layout_checkupdate:
                UpdateRequest updateRequest = new UpdateRequest();
                updateRequest.setPlatform("and");
                callBack.update(updateRequest);
                v.setEnabled(false);
                break;
            case R.id.layout_agreement:
                WebBean agreeWebBean = new WebBean();
                agreeWebBean.setRequest(true);
                agreeWebBean.setTitle("用户服务协议");
                agreeWebBean.setName("userAgreement");
                callBack.webWindowEnter(agreeWebBean);
                break;
            case R.id.layout_taskStatement:
                WebBean taskWebBean = new WebBean();
                taskWebBean.setRequest(true);
                taskWebBean.setTitle("任务声明");
                taskWebBean.setName("taskStatement");
                callBack.webWindowEnter(taskWebBean);
                break;
            case R.id.layout_integral:
                WebBean integralWebBean = new WebBean();
                integralWebBean.setRequest(true);
                integralWebBean.setTitle("积分说明");
                integralWebBean.setName("integral");
                callBack.webWindowEnter(integralWebBean);
                break;
        }
    }
}
