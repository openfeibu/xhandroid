package cn.flyexp.framework;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;

import cn.flyexp.net.NetWork;

/**
 * Created by zlk on 2016/3/27.
 * Modified by zlk on 2016/3/31 添加广播消息系统
 * Modified by txy on 2016/8/4
 */
public class Environment {

    public static void init(Activity activity) {//把一些变量初始化好，并把ControllerManager及WindowManager初始化
        ControllerManager cm = new ControllerManager();
        AbstractController.controllerManager = cm;
        AbstractController.activity = activity;
        WindowManager wm = new WindowManager(activity);
        AbstractWindow.windowManager = wm;
        AbstractWindow.activity = activity;
        cm.initControllers();//把各模块初始化好，并注册相关的消息id到ControllerManager中，则所有模块可以进行消息调用
        Message message = Message.obtain();
        message.what = MessageIDDefine.SPLASH_OPEN;
        cm.sendMessage(message);
    }

    public static void onResume() {
        Message message = Message.obtain();
        message.what = NotifyIDDefine.ON_ACTIVITY_RESUME;
        AbstractController.notifyManager.notify(message);
    }

    public static void onPause() {
        Message message = Message.obtain();
        message.what = NotifyIDDefine.ON_ACTIVITY_PAUSE;
        AbstractController.notifyManager.notify(message);
    }

    public static void onSaveInstanceState(Bundle outState) {
        Message message = Message.obtain();
        message.what = NotifyIDDefine.ON_ACTIVITY_SAVEINSTANCESTATE;
        message.obj = outState;
        AbstractController.notifyManager.notify(message);
    }

    public static void onDestroy() {
        Message message = Message.obtain();
        message.what = NotifyIDDefine.ON_ACTIVITY_DESTROY;
        AbstractController.notifyManager.notify(message);
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }
}
