package cn.flyexp.mvc.mine;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.mvc.user.WebWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/8 0008.
 */
public class IntegralWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    //    private SeekBar seekBar;
    private TextView tv_grade;
    private TextView tv_curIntegral;
    private TextView tv_dstIntegral;
    //    private TextView tv_today;
    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    private TextView tv_todayIntegral;
    private ProgressBar progressBarToday;

    public IntegralWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        WebUrlRequest webUrlRequest = new WebUrlRequest();
        webUrlRequest.setUrl_name("integral");
        callBack.getWebUrl(webUrlRequest);
    }

    private void initView() {
        setContentView(R.layout.window_myintegral);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_record).setOnClickListener(this);

        tv_grade = (TextView) findViewById(R.id.tv_grade);
        tv_curIntegral = (TextView) findViewById(R.id.tv_curIntegral);
        tv_dstIntegral = (TextView) findViewById(R.id.tv_dstIntegral);
        tv_todayIntegral = (TextView) findViewById(R.id.tv_todayIntegral);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBarToday = (ProgressBar) findViewById(R.id.progressBarToday);

        webView = (WebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(new JavaScriptInterface(), "feibu");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
    }

    public class JavaScriptInterface {

        @JavascriptInterface
        public String interactive(String json) {
            LogUtil.error(getClass(), json);
            JSONObject jsonObject = null;
            String action = "";
            try {
                jsonObject = new JSONObject(json);
                action = jsonObject.getString("action");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (action.equals("getToken")) {
                String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return "";
                }
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("value", token);
                    LogUtil.error(getClass(), jsonObject.toString());
                    return jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return "";
        }
    }

    public void loadUrl(String url) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                webView.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);

    }

    public void initIntegral(MyInfoResponse.MyInfoResponseData responseData) {
        tv_grade.setText(responseData.getLevel() + "");
        tv_todayIntegral.setText("今日完成" + responseData.getToday_integral() + "分");
        tv_curIntegral.setText("当前积分值：" + responseData.getIntegral());
        tv_dstIntegral.setText("还差" + responseData.getUpgrade() + "点升级");
        progressBar.setProgress((int) (((float) responseData.getIntegral() / (float) (responseData.getIntegral() + responseData.getUpgrade())) * 100));
        progressBarToday.setProgress(responseData.getToday_integral());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_record:
                callBack.integralRecordEnter();
                break;
        }

    }


}
