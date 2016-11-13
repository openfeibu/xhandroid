package cn.flyexp.framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import android.view.KeyEvent;

import java.util.HashMap;

import cn.flyexp.R;
import cn.flyexp.entity.WebBean;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.LogUtil;

/**
 * Created by zlk on 2016/3/27.
 * Modified by zlk on 2016/3/31 添加广播消息系统
 */
public abstract class AbstractController implements AbstractWindow.WindowCallBack {
    public static ControllerManager controllerManager = null;//不同模块如user与home解耦,用消息来彼此调用
    public static NotifyManager notifyManager = new NotifyManager();
    static Activity activity = null;
    private static boolean isOnce = true;
    protected NetWorkService service;
    private static SparseArray<String> UI_DATAS = new SparseArray<>(10);

    public void setUIData(int key, String data) {
        UI_DATAS.put(key, data);
    }

    public String getUIData(int key) {
        String data = UI_DATAS.get(key);
        if (data == null) {
           data = "";
        }
        return data;
    }

    private static HashMap<String, Integer> data;

    public static HashMap<String, Integer> getData() {
        return data;
    }

    public static void setData(HashMap<String, Integer> data) {
        AbstractController.data = data;
    }

    Handler handler;

    public AbstractController() {
        registerMessages();
        handler = new Handler(Looper.getMainLooper());
        service = NetWork.getInstance().getService();
    }

    protected Handler getUIHandler() {
        return handler;
    }

    public Context getContext() {
        return activity;
    }

    protected abstract void registerMessages();

    public void registerMessage(int id, AbstractController controller) {
        controllerManager.registerMessage(id, controller);
    }

    public void registerNotify(int id, NotifyManager.Notify notify) {
        notifyManager.register(id, notify);
    }

    protected void sendMessage(Message message) {
        controllerManager.sendMessage(message);
    }

    protected void sendMessage(int mesId) {
        controllerManager.sendMessage(mesId);
    }

    protected void sendMessage(int mesId, Object obj) {
        Message message = Message.obtain();
        message.what = mesId;
        message.obj = obj;
        controllerManager.sendMessage(message);
    }

    protected void sendMessage(int mesId, int arg1) {
        Message message = Message.obtain();
        message.what = mesId;
        message.arg1 = arg1;
        controllerManager.sendMessage(message);
    }

    public void sendNotify(Message message) {
        notifyManager.notify(message);
    }

    public void sendNotify(int notifyId) {
        Message message = Message.obtain();
        message.what = notifyId;
        notifyManager.notify(message);
    }

    public void sendNotify(int notifyId,int arg1) {
        Message message = Message.obtain();
        message.what = notifyId;
        message.arg1 = arg1;
        notifyManager.notify(message);
    }

    public void sendNotify(int notifyId,int arg1,int arg2) {
        Message message = Message.obtain();
        message.what = notifyId;
        message.arg1 = arg1;
        message.arg2 = arg2;
        notifyManager.notify(message);
    }

    protected abstract void handleMessage(Message message);

    public void onWindowShow(AbstractWindow window) {

    }

    public void onWindowHide(AbstractWindow window) {

    }

    @Override
    public void taWindowEnter(String taId) {
        sendMessage(MessageIDDefine.TA_OPEN,taId);
    }


    @Override
    public void webWindowEnter(WebBean bean) {
        Message message = Message.obtain();
        message.what = MessageIDDefine.WEB_OPEN;
        message.obj = bean;
        sendMessage(message);
    }

    @Override
    public void userCount(String key) {
        if (data == null) {
            data = new HashMap<String, Integer>();
        }
        if (data.containsKey(key)) {
            data.put(key, data.get(key) + 1);
        } else {
            data.put(key, 1);
        }
    }

    /**
     * 重新登录
     *
     * @param window
     */
    public void againLogin(final AbstractWindow window) {
        if (!isOnce) {
            return;
        }
        isOnce = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(getContext().getString(R.string.please_again_login)).setPositiveButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout(window);
                isOnce = true;
            }
        }).create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void loginWindowEnter() {
        gotoLoginWindow();
    }

    private void gotoLoginWindow() {
        Message message = Message.obtain();
        message.what = MessageIDDefine.LOGIN_OPEN;
        sendMessage(message);
    }

    public void logout(AbstractWindow window) {
        WindowHelper.putStringByPreference("token", "");
        gotoLoginWindow();
    }

}
