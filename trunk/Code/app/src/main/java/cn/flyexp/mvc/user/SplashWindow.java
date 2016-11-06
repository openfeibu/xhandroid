package cn.flyexp.mvc.user;

import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.flyexp.R;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/7/31 0031.
 */
public class SplashWindow extends AbstractWindow {

    private UserViewCallBack callBack;

    public SplashWindow(final UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        checkClient();
        checkLog();
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    boolean isNoFirstRun = WindowHelper.getBooleanByPreference("isNoFirstRun");
                    if (isNoFirstRun) {
                        callBack.mainEnter();
                    } else {
                        callBack.guideEnter();
                    }
                }
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    private void checkLog() {
        LogUtil.error(getClass(), "filepath" + CommonUtil.getFilePath(getContext()));
        File dir = new File(CommonUtil.getFilePath(getContext()) + "/carsh/carsh.log");
        if (dir.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dir);
                byte[] buf = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (fis.read(buf) != -1) {
                    baos.write(buf, 0, 1024);
                }
                callBack.logRequest(baos.toString());
                baos.close();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void checkUpdate() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setPlatform("and");
        callBack.update(updateRequest);
    }


    private void checkClient() {
        String mid = WindowHelper.getStringByPreference("mid");
        String version = CommonUtil.getVersionCode(getContext()) + "";
        String platform = "and";
        String os = CommonUtil.getCurrentOS() + "";
        String brand = CommonUtil.getCurrentBrand();

        ClientVerifyRequest clientVerifyRequest = new ClientVerifyRequest();
        clientVerifyRequest.setVersion(version);
        clientVerifyRequest.setPlateform(platform);
        clientVerifyRequest.setOs(os);
        clientVerifyRequest.setBrand(brand);
        if (!mid.equals("")) {
            clientVerifyRequest.setMid(mid);
        }
        callBack.clientVerifyRequest(clientVerifyRequest);
    }

    private void initView() {
        setContentView(R.layout.window_splash);
    }

}
