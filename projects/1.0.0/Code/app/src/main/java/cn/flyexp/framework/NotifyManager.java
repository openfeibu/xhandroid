package cn.flyexp.framework;

import android.os.Message;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zlk on 2016/3/29.
 */
public class NotifyManager {
    public interface Notify {
        void onNotify(Message message);
    }

    private SparseArray<List<Notify>> notifies = new SparseArray<>();

    public NotifyManager() {

    }

    public void register(int notifyId, Notify notify) {
        List<Notify> list = notifies.get(notifyId);
        if (list == null) {
            list = new ArrayList<>();
            notifies.put(notifyId, list);
        }
        list.add(notify);
    }

    public void unRegister(int notifyId, Notify notify) {
        List<Notify> list = notifies.get(notifyId);
        if (list != null) {
            list.remove(notify);
        }
    }

    public void notify(Message message) {
        List<Notify> list = notifies.get(message.what);
        if (list != null) {
            for (Notify n : list) {
                n.onNotify(message);
            }
        }
    }
}
