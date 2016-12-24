package cn.flyexp.window.store;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;
import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.callback.other.WebCallback;
import cn.flyexp.entity.WebBean;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.presenter.other.WebPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class StoreWindow extends BaseWindow implements WebCallback.ResponseCallback {

    @InjectView(R.id.webview)
    WebView webView;

    private WebSettings webSettings;
    private WebPresenter webPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_store;
    }

    public StoreWindow() {
        webPresenter = new WebPresenter(this);
        initView();
        readyWebUrl();
    }

    @Override
    public void onRenew() {
        readyWebUrl();
    }

    private void initView() {
        webView.addJavascriptInterface(new JavaScriptInterface(), "feibu");
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

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                final PayTask task = new PayTask((Activity) MainActivity.getContext());
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
        });
    }

    private class JavaScriptInterface {
        @JavascriptInterface
        public String alipay(String json) {
            JSONObject jsonObject = null;
            String payUrl = "";
            try {
                jsonObject = new JSONObject(json);
                payUrl = jsonObject.getString("payUrl");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonObject = new JSONObject();

            final PayTask task = new PayTask((Activity) MainActivity.getContext());
            final String ex = task.fetchOrderInfoFromH5PayUrl(payUrl);
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

            return jsonObject.toString();
        }

        @JavascriptInterface
        public String getToken() {
            JSONObject jsonObject = new JSONObject();
            String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
            if ("".equals(token)) {
                token = "undefined";
            }
            try {
                jsonObject.put("value", token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject.toString();
        }

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
                        StoreWindow.this.openWindow(WindowIDDefine.WINDOW_LOGIN);
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

        @JavascriptInterface
        public void openWindow(final String type, final String url) {
            if (TextUtils.equals(type, "shop")) {
                WebBean webBean = new WebBean();
                webBean.setUrl(url);
                webBean.setRequest(false);
                Bundle bundle = new Bundle();
                bundle.putSerializable("webbean", webBean);
                StoreWindow.this.openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
            }
        }
    }

    private void readyWebUrl() {
        WebUrlRequest webUrlRequest = new WebUrlRequest();
        webUrlRequest.setUrl_name("shop");
        webPresenter.requestWebUrl(webUrlRequest);
    }

    @Override
    public void responseWebUrl(WebUrlResponse response) {
        webView.loadUrl(response.getUrl());
    }

    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

}
