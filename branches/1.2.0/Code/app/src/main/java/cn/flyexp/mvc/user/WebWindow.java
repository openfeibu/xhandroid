package cn.flyexp.mvc.user;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.WebBean;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/21 0021.
 */
public class WebWindow extends AbstractWindow implements View.OnClickListener {

    private UserViewCallBack callBack;
    private WebView webView;
    private WebSettings webSettings;
    private TextView tv_title;
    private JavaScriptInterface anInterface;
    private int times;
    private boolean goBack;
    private TextView tv_close;
    private ProgressBar pb_web;
    private View toolbar;
    private String originalUrlUrl;

    public WebWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        setContentView(R.layout.window_web);
        findViewById(R.id.iv_back).setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        pb_web = (ProgressBar) findViewById(R.id.pb_web);
        tv_close = (TextView) findViewById(R.id.tv_close);
        tv_close.setOnClickListener(this);

        findViewById(R.id.tv_refresh).setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_title);

        webView = (WebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(anInterface = new JavaScriptInterface(), "feibu");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = getContext().getCacheDir().getAbsolutePath();
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
    }

    public void initData(WebBean bean) {
        boolean isRequest = bean.isRequest();
        tv_title.setText(bean.getTitle());
        if (bean.isHideTitle()) {
            toolbar.setVisibility(GONE);
        }
        if (isRequest) {
            WebUrlRequest webUrlRequest = new WebUrlRequest();
            webUrlRequest.setUrl_name(bean.getName());
            callBack.getWebUrl(webUrlRequest);
        } else {
            loadUrl(bean.getUrl());
        }
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
                String token = WindowHelper.getStringByPreference("token");
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
        originalUrlUrl = url;
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final PayTask task = new PayTask((Activity) getContext());
                final String ex = task.fetchOrderInfoFromH5PayUrl(url);
                if (!TextUtils.isEmpty(ex)) {
                    PermissionHandler.PermissionCallback permissionCallback = new PermissionHandler.PermissionCallback() {
                        public void onSuccess() {
                            new Thread() {
                                public void run() {
                                    final H5PayResultModel model = task.h5Pay(ex, true);
                                    int resId = R.string.h5pay_unknow;
                                    if (model != null) {
                                        String resultStatus = model.getResultCode();
                                        if (TextUtils.equals(resultStatus, "9000")) {
                                            post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    webView.loadUrl(model.getReturnUrl());
                                                }
                                            });
                                            return;
                                        }
//                                8000——正在处理中
                                        else if (TextUtils.equals(resultStatus, "8000")) {
                                            resId = R.string.h5pay_ing;
                                        }
//                                4000——订单支付失败
                                        else if (TextUtils.equals(resultStatus, "4000")) {
                                            resId = R.string.h5pay_fail;
                                        }
//                                5000——重复请求
                                        else if (TextUtils.equals(resultStatus, "5000")) {
                                            resId = R.string.h5pay_dup;
                                        }
//                                6001——用户中途取消
                                        else if (TextUtils.equals(resultStatus, "6001")) {
                                            resId = R.string.h5pay_cancel;
                                        }
//                                6002——网络连接出错
                                        else if (TextUtils.equals(resultStatus, "6002")) {
                                            resId = R.string.h5pay_network_error;
                                        }
                                    }
                                    final int id = resId;
                                    post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), getContext().getResources().getText(id), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                            }.start();
                        }

                        public void onFail(int[] ids) {
                        }

                        public void onCancel() {
                        }

                        public void goSetting() {
                        }
                    };
                    PermissionTools.requestPermission(getContext(), permissionCallback, new int[]{PermissionHandler.PERMISSION_FILE, PermissionHandler.PERMISSION_PHONE});
                } else {
                    return super.shouldOverrideUrlLoading(view, url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webView.canGoBack()) {
                    goBack = true;
                    tv_close.setVisibility(VISIBLE);
                } else {
                    goBack = false;
                    tv_close.setVisibility(GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }


        });

        webView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_refresh:
                if (webView.getUrl() != null) {
                    webView.reload();
                } else {
                    if (originalUrlUrl == null) {
                        if (webView.canGoBack()) {
                            webView.goBack();
                        } else {
                            WindowHelper.showToast(getContext().getString(R.string.neterror));
                        }
                    } else {
                        webView.loadUrl(originalUrlUrl);
                    }
                }
                break;
            case R.id.iv_back:
                if (goBack) {
                    webView.goBack();
                } else {
                    hideWindow(true);
                }
                break;
            case R.id.tv_close:
                hideWindow(true);
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.KEYCODE_BACK && MainActivity.HadKeyDown) {
                if (webView.canGoBack()) {
                    webView.goBack();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
