package cn.flyexp.framework;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

import cn.flyexp.mvc.shop.BusinessController;
import cn.flyexp.mvc.main.MainController;
import cn.flyexp.mvc.shop.ShopController;
import cn.flyexp.mvc.assn.AssnController;
import cn.flyexp.mvc.task.TaskController;
import cn.flyexp.mvc.user.UserController;
import cn.flyexp.util.LogUtil;

/**
 * Created by zlk on 2016/3/27.
 */
public class ControllerManager {

    private Handler messageHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            AbstractController controller = controllerList.get(msg.what);
            if (controller != null) {
                controller.handleMessage(msg);
            }
        }
    };

    private SparseArray<AbstractController> controllerList = new SparseArray<AbstractController>();

    public ControllerManager() {
    }


    public void initControllers() {
        new MainController();
        new UserController();
        new TaskController();
        new BusinessController();
        new AssnController();
        new ShopController();
    }

    public void registerMessage(int id, AbstractController controller) {
        controllerList.put(id, controller);
    }

    public void sendMessage(Message msg) {
        messageHandler.sendMessage(msg);
    }

    public void sendMessage(int mesId) {
        messageHandler.sendEmptyMessage(mesId);
    }


}
