package cn.flyexp.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

import cn.flyexp.entity.MyTopicResponse;

/**
 * Created by txy on 2016/6/24 0024.
 */
public class GsonUtil {

    private Gson gson;
    private static GsonUtil gsonUtil;

    private GsonUtil() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    private static GsonUtil getInstance() {
        if (gsonUtil == null) {
            synchronized (GsonUtil.class) {
                if (gsonUtil == null) {
                    gsonUtil = new GsonUtil();
                }
            }
        }
        return gsonUtil;
    }

    private <T> T _fromEncodeJson(String data, Class dataClass) {
        T object = null;
        try {
            String dataResult = CommonUtil.decode(data);
            LogUtil.error(getClass(), "decode classname:" + dataClass.getSimpleName() + " data:" + dataResult);
            object = (T) gson.fromJson(dataResult, dataClass);
        } catch (Exception e) {
            LogUtil.error(getClass(), "classname:" + dataClass.getSimpleName() + "parseError");
        }
        return (T) object;
    }

    private <T> T _fromJson(String data, Class dataClass) {
        T object = null;
        try {
            object = (T) gson.fromJson(data, dataClass);
        } catch (Exception e) {
        }
        return (T) object;
    }

    private <T> T _fromJsonList(String data, Type type) {
        T object = null;
        try {
            String dataResult = CommonUtil.decode(data);
            LogUtil.error(getClass(), "decode classname:" + data.getClass().getSimpleName() + " data:" + dataResult);
            object = gson.fromJson(dataResult, type);
        } catch (Exception e) {
            LogUtil.error(getClass(), "classname:" + type.getClass().getSimpleName() + "parseError");
        }
        return object;
    }

    private String _toEncodeJson(Object object, Class dataClass) {
        String data = null;
        try {
            data = gson.toJson(object, dataClass);
        } catch (Exception e) {
            LogUtil.error(getClass(), "classname:" + dataClass.getSimpleName() + "parseError");
        }
        LogUtil.error(getClass(), "encode classname:" + dataClass.getSimpleName() + " data:" + data);
        return CommonUtil.encode(data);
    }

    private String _toEncodeJson(Object object) {
        String data = null;
        try {
            data = gson.toJson(object);
        } catch (Exception e) {
            LogUtil.error(getClass(), "classname:" + object.getClass().getSimpleName() + "parseError");
        }
        LogUtil.error(getClass(), "encode classname:" + object.getClass().getSimpleName() + " data:" + data);
        return CommonUtil.encode(data);
    }

    private String _toJson(Object object) {
        String data = null;
        try {
            data = gson.toJson(object);
        } catch (Exception e) {
        }
        return data;
    }

    public static <T> T fromEncodeJson(String data, Class dataClass) {
        return getInstance()._fromEncodeJson(data, dataClass);
    }

    public static <T> T fromJson(String data, Class dataClass) {
        return getInstance()._fromJson(data, dataClass);
    }

    public static <T> T fromJsonList(String data, Type type) {
        return getInstance()._fromJsonList(data, type);
    }

    public static String toEncodeJson(Object object, Class dataClass) {
        return getInstance()._toEncodeJson(object, dataClass);
    }

    public static String toEncodeJson(Object object) {
        return getInstance()._toEncodeJson(object);
    }

    public static String toJson(Object object) {
        return getInstance()._toJson(object);
    }
}
