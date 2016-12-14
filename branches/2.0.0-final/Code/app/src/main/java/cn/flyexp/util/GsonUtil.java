package cn.flyexp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.PrintWriter;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class GsonUtil {

    private Gson gson;
    private static GsonUtil gsonUtil;

    private GsonUtil() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public static GsonUtil getInstance() {
        if (gsonUtil == null) {
            synchronized (GsonUtil.class) {
                if (gsonUtil == null) {
                    gsonUtil = new GsonUtil();
                }
            }
        }
        return gsonUtil;
    }

    public <T> T decodeJson(String data,Class cls) {
        T object = null;
        try {
            object = (T) gson.fromJson(EncodeUtil.decode(data),cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) object;
    }

    public String encodeJson(Object object) {
        String data = null;
        try {
            data = toJson(object);
            data = EncodeUtil.encode(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public String toJson(Object object) {
        String data = null;
        try {
            data = gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public <T> T fromJson(String data, Class cls ) {
        T object = null;
        try {
            object = (T) gson.fromJson(data,cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) object;
    }
}
