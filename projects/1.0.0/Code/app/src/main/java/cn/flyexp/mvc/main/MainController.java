package cn.flyexp.mvc.main;

import android.content.pm.ActivityInfo;
import android.os.Message;

import cn.flyexp.MainActivity;
import cn.flyexp.entity.NoResponse;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.entity.UserCountRequset;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/7/16 0016.
 */
public class MainController extends AbstractController implements MainViewCallBack, MainModelCallBack, NotifyManager.Notify {

    private MainWindow mainWindow;
    private MainModel mainModel;

    public MainController() {
        super();
        mainModel = new MainModel(this);
    }

    @Override
    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.MAIN_OPEN) {
            mainWindow = new MainWindow(this);
            mainWindow.showWindow(false, false);
        }
    }

    @Override
    protected void registerMessages() {
        registerMessage(MessageIDDefine.MAIN_OPEN, this);
        registerNotify(NotifyIDDefine.ON_ACTIVITY_RESUME, this);
        registerNotify(NotifyIDDefine.ON_ACTIVITY_PAUSE, this);
        registerNotify(NotifyIDDefine.ON_ACTIVITY_DESTROY, this);
    }

    @Override
    public void taskDetail(int taskId) {
        sendMessage(MessageIDDefine.TASK_DETAIL_OPEN, taskId);
    }

    @Override
    public void getNo() {
        mainModel.campusNo();
    }

    @Override
    public void onNotify(Message mes) {
     if (mes.what == NotifyIDDefine.ON_ACTIVITY_DESTROY) {
            UserCountRequset userCountRequset = new UserCountRequset(AbstractController.getData());
            String json = GsonUtil.toJson(userCountRequset);
            mainModel.userCount(userCountRequset);
        }
    }

    @Override
    public void campusNoResponse(NoResponse noResponse) {
        if (noResponse == null) {
            return;
        }
        int code = noResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                mainWindow.responseData(noResponse.getData());
                break;
            case ResponseCode.RESPONSE_110:
                mainWindow.showToast(noResponse.getDetail());
                break;
        }
    }
}
