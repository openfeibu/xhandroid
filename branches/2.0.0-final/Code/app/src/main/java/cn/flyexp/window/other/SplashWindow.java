package cn.flyexp.window.other;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.tencent.android.tpush.XGPushConfig;

import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.callback.other.SplashCallback;
import cn.flyexp.constants.Constants;
import cn.flyexp.entity.ClienVerifyResponse;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.CrashRequest;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.other.SplashPresenter;
import cn.flyexp.util.FileUtil;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.PackageUtil;
import cn.flyexp.util.ScreenHelper;
import cn.flyexp.window.BaseWindow;
import cn.flyexp.util.SharePresUtil;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class SplashWindow extends BaseWindow implements SplashCallback.ResponseCallback {

    @InjectView(R.id.img_logo)
    ImageView imgLogo;

    private SplashPresenter splashPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_splash;
    }

    public SplashWindow() {
        splashPresenter = new SplashPresenter(this);
        logoAnim();
        checkCrashFile();
        checkClient();
        enterDelayed();
    }

    private void logoAnim() {
        ObjectAnimator downAnim = ObjectAnimator.ofFloat(imgLogo, "y", 0, ScreenHelper.dip2px(getContext(), 120));
        downAnim.setDuration(500);
        downAnim.setInterpolator(new AccelerateInterpolator());
        downAnim.start();
    }

    private void checkCrashFile() {
        String crashJson = FileUtil.loadFile(getContext(), Constants.FILE_PATH_CRASH_LOG);
        if (!TextUtils.isEmpty(crashJson)) {
            CrashRequest crashRequest = GsonUtil.getInstance().decodeJson(crashJson, CrashRequest.class);
            splashPresenter.reqeustCrash(crashRequest);
        }
    }

    private void checkClient() {
        String mid = SharePresUtil.getString(SharePresUtil.KEY_MID);
        String versionCode = String.valueOf(PackageUtil.getVersionCode(getContext()));
        String platform = "and";
        String os = Build.VERSION.RELEASE;
        String brand = Build.MODEL;

        ClientVerifyRequest clientVerifyRequest = new ClientVerifyRequest();
        clientVerifyRequest.setOs(os);
        clientVerifyRequest.setBrand(brand);
        clientVerifyRequest.setPlateform(platform);
        clientVerifyRequest.setVersion(versionCode);
        if (!TextUtils.isEmpty(mid)) {
            clientVerifyRequest.setMid(mid);
        }
        splashPresenter.requestClientVerify(clientVerifyRequest);
    }

    private void enterDelayed() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean firstRun = SharePresUtil.getBoolean(SharePresUtil.KEY_FIRST_RUN);
                if (!firstRun) {
                    openWindow(WindowIDDefine.WINDOW_GUIDE);
                } else {
                    boolean crash = SharePresUtil.getBoolean(SharePresUtil.KEY_CRASH);
                    if (crash) {
                        SharePresUtil.putBoolean(SharePresUtil.KEY_CRASH, false);
                        Bundle feedbackBundler = new Bundle();
                        feedbackBundler.putBoolean("isCrash", true);
                        openWindow(WindowIDDefine.WINDOW_FEEDBACK, feedbackBundler);
                    } else {
                        openWindow(WindowIDDefine.WINDOW_MAIN);
                    }
                }

                String push = SharePresUtil.getString(SharePresUtil.KEY_PUSH_TYPE);
                if ("xinge".equals(push)) {
                    SharePresUtil.putString(SharePresUtil.KEY_DEVICE_TOKEN, XGPushConfig.getToken(getContext()));
                }
            }
        }, 2500);
    }

    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

    @Override
    public void responseClientVerify(ClienVerifyResponse response) {
        SharePresUtil.putString(SharePresUtil.KEY_MID, response.getMid());
    }

    @Override
    public void responseCrash() {
        FileUtil.deleleFile(getContext(), Constants.FILE_PATH_CRASH_LOG);
    }

    @Override
    public boolean onBackPressed() {
        getWindowManager().exitApp();
        return super.onBackPressed();
    }
}
