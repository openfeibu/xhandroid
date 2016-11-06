package cn.flyexp.mvc.main;

import android.os.Message;

import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;

/**
 * Created by txy on 2016/7/16 0016.
 */
public class MainController extends AbstractController  implements MainViewCallBack{

    private MainWindow mainWindow;

    public MainController() {
        super();
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

}
