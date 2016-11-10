package cn.flyexp.util;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import cn.flyexp.constants.Constants;
import cn.flyexp.entity.CrashRequest;

/**
 * Created by tanxinye on 2016/10/26.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Context context;

    public CrashHandler(Context context) {
        this.context = context;
    }

    public void start() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable == null) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, throwable);
        } else {
            saveCarshInfo(throwable);
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    private void saveCarshInfo(Throwable throwable) {
        final StringBuffer sb = new StringBuffer();
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        String deviceToken = SharePresUtil.getString(SharePresUtil.KEY_DEVICE_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            sb.append("token=" + token + "\n");
        }
        if (!TextUtils.isEmpty(deviceToken)) {
            sb.append("device_token=" + deviceToken + "\n");
        }
        if (PackageUtil.checkPhoneStatePermission(context)) {
            String model = Build.MODEL;
            String release = Build.VERSION.RELEASE;
            String product = Build.PRODUCT;
            sb.append("手机型号=" + model + "\n");
            sb.append("版本=" + release + "\n");
            sb.append("手机制造商=" + product + "\n");
        }
        sb.append("版本名=" + PackageUtil.getVersionName(context) + "\n");
        sb.append("时间=" + DateUtil.long2Date(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        Throwable cause = throwable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append("\n日志\n" + writer.toString());

        SharePresUtil.putBoolean(SharePresUtil.KEY_CRASH, true);
        CrashRequest crashRequest = new CrashRequest();
        crashRequest.setToken(token);
        crashRequest.setLog(sb.toString());
        String carshJson = GsonUtil.getInstance().toJson(crashRequest);
        FileUtil.saveFile(context, carshJson, Constants.FOLDER_PATH_CRASH, Constants.FILE_PATH_CRASH_LOG);
    }
}
