package cn.flyexp.util;

import android.util.Log;

/**
 * Created by txy on 2016/8/4 0004.
 * Log输出工具类
 */
public class LogUtil {

    private static LogUtil logUtil;
    private static boolean isOpen = true;

    private static LogUtil getInstance() {
        if (logUtil == null) {
            synchronized (LogUtil.class) {
                if (logUtil == null) {
                    logUtil = new LogUtil();
                }
            }
        }
        return logUtil;
    }

    private void _error(Class cls, String mes) {
        if (!isOpen) {
            return;
        }
        if (cls == null || mes == null) {
            return;
        }
        Log.e(cls.getSimpleName(), mes);
    }

    private void _debug(Class cls, String mes) {
        if (!isOpen) {
            return;
        }
        if (cls == null || mes == null) {
            return;
        }
        Log.d(cls.getSimpleName(), mes);
    }

    private void _open() {
        isOpen = true;
    }

    private void _close() {
        isOpen = false;
    }

    public static void error(Class cls, String mes) {
        getInstance()._error(cls, mes);
    }

    public static void debug(Class cls, String mes) {
        getInstance()._debug(cls, mes);
    }

    public static void open() {
        getInstance().open();
    }

    public static void close() {
        getInstance()._close();
    }
}
