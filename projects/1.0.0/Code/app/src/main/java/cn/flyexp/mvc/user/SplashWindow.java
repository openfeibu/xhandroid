package cn.flyexp.mvc.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.Constants;
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
                    boolean isNoFirstRun = getBooleanByPreference("isNoFirstRun");
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

    public void logResponse() {
        File dir = new File(CommonUtil.getFilePath(getContext()) + "/carsh/carsh.log");
        if (dir.exists()) {
            dir.delete();
        }
    }

    private void checkUpdate() {
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.setPlatform("and");
        callBack.update(updateRequest);
    }

    public void responseData(final UpdateResponse.UpdateResponseData data) {
        if (data.getCompulsion() == 1) {
            showAlertDialog(data.getDetail(), "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((Activity) getContext()).finish();
                }
            }, "前往下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Uri uri = Uri.parse(data.getDownload());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    getContext().startActivity(intent);
                }
            });
        }
    }

    private void checkClient() {
        String mid = getStringByPreference("mid");
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
