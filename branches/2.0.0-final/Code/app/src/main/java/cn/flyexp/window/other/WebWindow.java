package cn.flyexp.window.other;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.other.WebCallback;
import cn.flyexp.entity.WebBean;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.presenter.other.WebPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class WebWindow extends BaseWindow implements WebCallback.ResponseCallback {

    @InjectView(R.id.tv_close)
    TextView tvClose;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.webview)
    WebView webView;
    @InjectView(R.id.progressbar)
    ProgressBar progressBar;

    private WebSettings webSettings;
    private boolean goBack;
    private JavaScriptInterface anInterface;

    @Override
    protected int getLayoutId() {
        return R.layout.window_web;
    }

    public WebWindow(Bundle bundle) {
        initWebView();
        WebPresenter webPresenter = new WebPresenter(this);
        WebBean webBean = (WebBean) bundle.getSerializable("webbean");
        String pushdata = bundle.getString("pushdata");
//        if (pushdata != null && !TextUtils.isEmpty(pushdata)) {
//            loadUrl(pushdata);
//        } else
        if (webBean != null) {
            boolean isRequest = webBean.isRequest();
            tvTitle.setText(webBean.getTitle());
            if (isRequest) {
                WebUrlRequest webUrlRequest = new WebUrlRequest();
                webUrlRequest.setUrl_name(webBean.getName());
                webPresenter.requestWebUrl(webUrlRequest);
            } else {
                loadUrl(webBean.getUrl());
            }
        } else {
            DialogHelper.showErrorDialog(getContext(), getResources().getString(R.string.dialog_backfire));
        }
    }

    private void initWebView() {
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

    @Override
    public void responseWebUrl(WebUrlResponse response) {
        loadUrl(response.getUrl());
    }

    public class JavaScriptInterface {

        @JavascriptInterface
        public String interactive(String json) {
            JSONObject jsonObject = null;
            String action = "";
            try {
                jsonObject = new JSONObject(json);
                action = jsonObject.getString("action");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (action.equals("getToken")) {
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (token.equals("")) {
                    if (webView != null) {
                        openWindow(WindowIDDefine.WINDOW_LOGIN);
                    }
                    return "";
                }
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("value", token);
                    return jsonObject.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return "";
        }
    }

    public void loadUrl(String url) {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (View.GONE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }
            }
        });
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
                                            showToast(id);
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
                }
                return super.shouldOverrideUrlLoading(view, url);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webView == null) {
                    return;
                }
                if (webView.canGoBack()) {
                    goBack = true;
                    tvClose.setVisibility(VISIBLE);
                } else {
                    goBack = false;
                    tvClose.setVisibility(GONE);
                }
            }

        });
        webView.loadUrl(url);
    }

    @OnClick({R.id.tv_close, R.id.img_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
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
    public boolean onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return super.onBackPressed();
        }
    }
}
