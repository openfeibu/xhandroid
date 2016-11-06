package cn.flyexp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by guo on 2016/5/28.
 */
public class CommonUtil {

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * get md5 code
     *
     * @param s
     * @return
     */
    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int integral2Grade(int integral) {
        int grade = 0;
        if (integral < 10) {
            grade = 0;
        } else if (integral >= 10 && integral < 20) {
            grade = 1;
        } else if (integral >= 20 && integral < 35) {
            grade = 2;
        } else if (integral >= 35 && integral < 55) {
            grade = 3;
        } else if (integral >= 55 && integral < 80) {
            grade = 4;
        } else if (integral >= 80 && integral < 110) {
            grade = 5;
        } else if (integral >= 110 && integral < 145) {
            grade = 6;
        } else if (integral >= 145 && integral < 185) {
            grade = 7;
        } else if (integral >= 185 && integral < 235) {
            grade = 8;
        } else if (integral >= 235 && integral < 300) {
            grade = 9;
        } else if (integral >= 300) {
            grade = 10;
        }
        return grade;
    }

    public static int getGradeMaxIntegral(int grade) {
        int intgral = -1;
        switch (grade) {
            case 0:
                intgral = 10;
                break;
            case 1:
                intgral = 20;
                break;
            case 2:
                intgral = 35;
                break;
            case 3:
                intgral = 55;
                break;
            case 4:
                intgral = 80;
                break;
            case 5:
                intgral = 110;
                break;
            case 6:
                intgral = 145;
                break;
            case 7:
                intgral = 185;
                break;
            case 8:
                intgral = 235;
                break;
            case 9:
                intgral = 300;
                break;
            case 10:
                intgral = 10000;
                break;
        }
        return intgral;
    }

    public static String[] splitImageUrl(String imgUrl) {
        return imgUrl.split("\\,");
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 检测字段是否为空
     */
    public static boolean isNumberNull(String text) {
        if (TextUtils.isEmpty(text) || text.equals("")) {
            return true;
        }
        return false;
    }

    public static boolean isFitLength(String text, int min, int max) {
        int number = text.trim().length();
        if (number >= min && number <= max) {
            return true;
        }
        return false;
    }

    public static void Toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }


    private static SharedPreferences preferences;


    // Save data String type use SharedPreferences
    public static String getStringData(Context context, String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String r = preferences.getString(name, "");
        return r;
    }

    public static void insertStringData(Context v, String name, String value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(v);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    public static int getIntData(Context v, String name) {
        preferences = PreferenceManager.getDefaultSharedPreferences(v);
        int r = preferences.getInt(name, 0);
        return r;
    }

    public static void insertIntData(Context v, String name, int value) {
        preferences = PreferenceManager.getDefaultSharedPreferences(v);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    /**
     * check net state
     *
     * @param v
     * @return
     */
    public static Boolean connectionStatus(Context v) {
        ConnectivityManager conMgr = (ConnectivityManager) v.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            return true;
        return false;
    }

    /**
     * check is wifi environment
     *
     * @param context
     * @return
     */
    public static boolean isWifiNetWork(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * getversion Code
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        int version_code = -1;
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            version_code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version_code;
    }

    private static final String VERSION_KEY = "fbks";

    public static boolean isFirstLaunch(Context context) {
        int currentVersion = getVersionCode(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        int lastVersion = preferences.getInt(VERSION_KEY, 0);
        if (currentVersion > lastVersion) {
            preferences.edit().putInt(VERSION_KEY, currentVersion).commit();
            return true;
        }
        return false;
    }

    public static String getCurrentOS() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getCurrentBrand() {
        return android.os.Build.MODEL;
    }

    public static void writeToFile(String str) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        try {
            String pathName = Environment.getExternalStorageDirectory() + "/feibu/";
            String fileName = "key.txt";
            File path = new File(pathName);
            File file = new File(pathName + fileName);
            if (!path.exists()) {
                path.mkdir();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream stream = new FileOutputStream(file);
            byte[] buf = str.getBytes();
            stream.write(buf);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getDefaultPath() {
        String str = Environment.getExternalStorageDirectory() + "/feibu/key.txt";
        return str;
    }

    public static String getFilePath(Context context){
        return context.getFilesDir().getPath();
    }


    public static String readFile(String filePath) {
        File readFile = new File(filePath);
        String str = "";
        try {
            if (!readFile.exists()) {
                return null;
            }
            FileInputStream inputStream = new FileInputStream(readFile);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int value = -1;
            while ((value = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, value);
            }
            inputStream.close();
            str = outputStream.toString();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String decode(String str) {
        if (str != null && !str.equals("")) {
            try {
                return Des3.decode(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String encode(String str) {
        if (str != null && !str.equals("")) {
            try {
                return Des3.encode(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String getMidToken() {
        String str = readFile(getDefaultPath());
        if (str != null && !str.equals("")) {
            return str;
        }
        return "";
    }

    private static Gson gson = new Gson();

    public static <T> T stringParse(String str, Class<T> obj) {
        return gson.fromJson(str, obj);
    }

    public static String objParse(Object object) {
        return gson.toJson(object);
    }

}
