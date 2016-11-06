package cn.flyexp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import cn.flyexp.entity.LogRequest;

/**
 * Created by txy on 2016/9/13 0013.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler carshHandler;
    private Context context;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (carshHandler == null) {
            synchronized (CrashHandler.class) {
                if (carshHandler == null) {
                    carshHandler = new CrashHandler();
                }
            }
        }
        return carshHandler;
    }

    public void init(Context context) {
        this.context = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (ex == null) {
            Thread.getDefaultUncaughtExceptionHandler().uncaughtException(thread, ex);
        } else {
            sendCarshInfo(ex);
            Process.killProcess(Process.myPid());
            System.exit(0);
        }
    }

    private void sendCarshInfo(Throwable ex) {
        final StringBuffer sb = new StringBuffer();
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = preferences.getString("token", "");
        String device_token = preferences.getString("device_token", "");
        if (!token.equals("")) {
            sb.append("token=" + token + "\n");
        }
        if (!device_token.equals("")) {
            sb.append("device_token=" + device_token + "\n");
        }

        PackageManager pm = context.getPackageManager();
        boolean phoneStatePermission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()));
        if (phoneStatePermission) {
            String model = Build.MODEL;
            String release = Build.VERSION.RELEASE;
            String product = Build.PRODUCT;
            sb.append("手机型号=" + model + "\n");
            sb.append("版本=" + release + "\n");
            sb.append("手机制造商=" + product + "\n");
        }
        try {
            String versionName = pm.getPackageInfo(context.getPackageName(), 0).versionName;
            sb.append("版本名=" + versionName + "\n");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sb.append("时间=" + DateUtil.long2Date(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        sb.append("\n日志\n" + writer.toString());

        LogRequest logRequest = new LogRequest();
        logRequest.setToken(token);
        logRequest.setLog(sb.toString());
        String carshJson = GsonUtil.toJson(logRequest);
        File dir = new File(CommonUtil.getFilePath(context) + "/carsh");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(CommonUtil.getFilePath(context) + "/carsh/carsh.log");
            fos.write(carshJson.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
