package cn.flyexp.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tanxinye on 2016/10/26.
 */
public class FileUtil {

    public static String getFilePath(Context context) {
        LogUtil.e("filepath", context.getFilesDir().getPath());
        return context.getFilesDir().getPath();
    }

    public static boolean saveFile(Context context, String data, String folderName, String fileName) {
        boolean isSuccess = false;
        File dir = new File(getFilePath(context) + folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(getFilePath(context) + fileName);
            fos.write(data.getBytes());
            fos.close();
            isSuccess = true;
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
        return isSuccess;
    }

    public static String loadFile(Context context, String fileName) {
        String data = "";
        File dir = new File(getFilePath(context) + fileName);
        if (dir.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dir);
                byte[] buf = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (fis.read(buf) != -1) {
                    baos.write(buf, 0, 1024);
                }
                baos.close();
                fis.close();
                return baos.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static void deleleFile(Context context, String filePath) {
        File dir = new File(getFilePath(context) + filePath);
        if (dir.exists()) {
            dir.delete();
        }
    }
}
