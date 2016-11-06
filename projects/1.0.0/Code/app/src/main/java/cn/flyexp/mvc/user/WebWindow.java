package cn.flyexp.mvc.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import cn.flyexp.R;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
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

    public WebWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    @SuppressLint("JavascriptInterface")
    private void initView() {
        setContentView(R.layout.window_web);
        findViewById(R.id.iv_back).setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_title);

        webView = (WebView) findViewById(R.id.webView);
        webView.addJavascriptInterface(anInterface = new JavaScriptInterface(), "feibu");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);
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

    public void getWebUrl(String[] pa) {
        String name = pa[0];
        if (name.equals("store")) {
            tv_title.setText("小店铺");
        } else if (name.equals("storeManage")) {
            tv_title.setText("店铺管理");
        } else if (name.equals("storeCollection")) {
            tv_title.setText("店铺收藏");
        } else if (name.equals("userAgreement")) {
            tv_title.setText("用户服务协议");
        } else if (name.equals("taskStatement")) {
            tv_title.setText("任务声明");
        } else if (name.equals("faq")) {
            tv_title.setText("常见问题");
        } else if (name.equals("storeApply")) {
            tv_title.setText("开店");
        } else if (name.equals("integral")) {
            tv_title.setText("积分说明");
        } else if (name.equals("walletStatement")) {
            tv_title.setText("钱包声明");
        }
        WebUrlRequest webUrlRequest = new WebUrlRequest();
        webUrlRequest.setUrl_name(name);
        callBack.getWebUrl(webUrlRequest);
    }

    public void loadUrl(String[] pa) {
        if (pa == null) {
            return;
        }
        String url = pa[0];
        if (pa.length > 1) {
            String title = pa[1];
            if (title != null && !title.equals("")) {
                tv_title.setText(title);
            }
        }
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
        });

        webView.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }
}
