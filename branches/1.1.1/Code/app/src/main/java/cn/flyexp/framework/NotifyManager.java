package cn.flyexp.framework;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlk on 2016/3/29.
 */
public class NotifyManager {
    public interface Notify {
        void onNotify(Message mes);
    }

    private static NotifyManager notifyManager;
    private SparseArray<Notify> notifies = new SparseArray<>();

    private NotifyManager() {
    }

    public static NotifyManager getInstance() {
        if (notifyManager == null) {
            synchronized (WindowManager.class) {
                if (notifyManager == null) {
                    notifyManager = new NotifyManager();
                }
            }
        }
        return notifyManager;
    }

    public void register(int notifyId, Notify notify) {
        notifies.put(notifyId, notify);
    }

    public void unRegister(int notifyId) {
        notifies.remove(notifyId);
    }

    public void notify(Message mes) {
        Notify notify = notifies.get(mes.what);
        if (notify != null) {
            notify.onNotify(mes);
        }
    }

    public void notify(int id) {
        Notify notify = notifies.get(id);
        if (notify != null) {
            Message mes = Message.obtain();
            mes.what = id;
            notify.onNotify(mes);
        }
    }
}
