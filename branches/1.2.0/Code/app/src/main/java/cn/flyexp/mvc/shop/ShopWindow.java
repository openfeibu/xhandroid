package cn.flyexp.mvc.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/9 0009.
 */
public class ShopWindow extends AbstractWindow implements View.OnClickListener {
    private WebView webView;
    private WebSettings webSettings;
    private JavaScriptInterface anInterface;
    private int times;
    private boolean goBack;
    private ProgressBar pb_web;
    private String originalUrlUrl;

    private ShopViewCallBack callBack;

    public ShopWindow(ShopViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        loadUrl("http://192.168.0.129/fb/index.html?device=android#/shop");
    }


    @SuppressLint("JavascriptInterface")
    private void initView() {
        setContentView(R.layout.window_shop);
        pb_web = (ProgressBar) findViewById(R.id.pb_web);
        findViewById(R.id.tv_refresh).setOnClickListener(this);
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

        @JavascriptInterface
        public void openWindow(final String type, final String url) {
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WindowHelper.showToast("type" + type + "url" + url);
                }
            });
            if (TextUtils.equals(type, "shop")) {
                WebBean webBean = new WebBean();
                webBean.setUrl(url);
                webBean.setRequest(false);
                webBean.setHideTitle(true);
                callBack.webWindowEnter(webBean);
            }
        }
    }

    private void loadUrl(String url) {
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
//                                8000?????????????????????
                                        else if (TextUtils.equals(resultStatus, "8000")) {
                                            resId = R.string.h5pay_ing;
                                        }
//                                4000????????????????????????
                                        else if (TextUtils.equals(resultStatus, "4000")) {
                                            resId = R.string.h5pay_fail;
                                        }
//                                5000??????????????????
                                        else if (TextUtils.equals(resultStatus, "5000")) {
                                            resId = R.string.h5pay_dup;
                                        }
//                                6001????????????????????????
                                        else if (TextUtils.equals(resultStatus, "6001")) {
                                            resId = R.string.h5pay_cancel;
                                        }
//                                6002????????????????????????
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
                } else {
                    goBack = false;
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
