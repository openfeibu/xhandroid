package cn.flyexp.window.store;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

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
                view.loadUrl(url);
                return true;
            }
        });
    }

    private class JavaScriptInterface {

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
