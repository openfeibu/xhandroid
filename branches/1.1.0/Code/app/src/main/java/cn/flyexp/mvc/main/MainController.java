package cn.flyexp.mvc.main;

import android.os.Message;

import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;

/**
 * Created by txy on 2016/7/16 0016.
 */
public class MainController extends AbstractController implements MainViewCallBack, NotifyManager.Notify{

    private MainWindow mainWindow;

    public MainController() {
        super();
        registerNotify(NotifyIDDefine.NOTIFY_MESSAGE_REFRESH, this);
        registerNotify(NotifyIDDefine.NOTIFY_MESSAGE_CONSUME, this);
        registerNotify(NotifyIDDefine.NOTIFY_TOPIC_REFRESH, this);
        registerNotify(NotifyIDDefine.NOTIFY_TOPIC_CONSUME, this);
    }

    @Override
    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.MAIN_OPEN_4_INIT) {
            if (mainWindow == null){
                mainWindow = new MainWindow(this);
            }
            mainWindow.clearWithMe();
        } else if (mes.what == MessageIDDefine.MAIN_OPEN) {
            if (mainWindow == null){
                mainWindow = new MainWindow(this);
            }
            mainWindow.showWindow(false);
        }
    }

    @Override
    protected void registerMessages() {
        registerMessage(MessageIDDefine.MAIN_OPEN_4_INIT, this);
        registerMessage(MessageIDDefine.MAIN_OPEN, this);
    }
    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_MESSAGE_REFRESH) {
            if (mainWindow != null) {
                mainWindow.showTip(MainWindow.MINE_TIP);
            }
        } else if (mes.what == NotifyIDDefine.NOTIFY_MESSAGE_CONSUME) {
            if (mainWindow != null) {
                mainWindow.hideTip(MainWindow.MINE_TIP);
            }
        } else if (mes.what == NotifyIDDefine.NOTIFY_TOPIC_REFRESH) {
            if (mainWindow != null) {
                mainWindow.showTip(MainWindow.TOPIC_TIP);
            }
        } else if (mes.what == NotifyIDDefine.NOTIFY_TOPIC_CONSUME) {
            if (mainWindow != null) {
                mainWindow.hideTip(MainWindow.TOPIC_TIP);
            }
        }
    }

}
