package cn.flyexp.framework;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.flyexp.window.assn.AssnActiDetailWindow;
import cn.flyexp.window.assn.AssnActiPublishWindow;
import cn.flyexp.window.assn.AssnDetailWindow;
import cn.flyexp.window.assn.AssnExamineDetailWindow;
import cn.flyexp.window.assn.AssnExamineListWindow;
import cn.flyexp.window.assn.AssnJoinWindow;
import cn.flyexp.window.assn.AssnNoticePublishWindow;
import cn.flyexp.window.assn.AssnWindow;
import cn.flyexp.window.assn.MyAssnActivityWindow;
import cn.flyexp.window.assn.TMyAssnDetailWindow;
import cn.flyexp.window.assn.MyAssnWindow;
import cn.flyexp.window.main.MainWindow;
import cn.flyexp.window.mine.AboutWindow;
import cn.flyexp.window.mine.FeedbackWindow;
import cn.flyexp.window.mine.MessageWindow;
import cn.flyexp.window.mine.SettingWindow;
import cn.flyexp.window.mine.ShareWindow;
import cn.flyexp.window.other.GalleryWindow;
import cn.flyexp.window.other.GuideWindow;
import cn.flyexp.window.other.PicBrowserWindow;
import cn.flyexp.window.other.SplashWindow;
import cn.flyexp.window.other.WebWindow;
import cn.flyexp.window.task.MyTaskDetailWindow;
import cn.flyexp.window.task.MyTaskWindow;
import cn.flyexp.window.task.TaskDetailWindow;
import cn.flyexp.window.task.TaskPublishWindow;
import cn.flyexp.window.task.TaskReportWindow;
import cn.flyexp.window.topic.MyTopicWindow;
import cn.flyexp.window.topic.TopicPublishWindow;
import cn.flyexp.window.user.CertificationWindow;
import cn.flyexp.window.user.LoginWindow;
import cn.flyexp.window.user.RegisterVercodeWindow;
import cn.flyexp.window.user.RegisterWindow;
import cn.flyexp.window.user.ResetPwdVercodeWindow;
import cn.flyexp.window.user.MyInfoEditWindow;
import cn.flyexp.window.user.MyInfoWindow;
import cn.flyexp.window.user.ResetPwdWindow;
import cn.flyexp.window.wallet.BindAlipayWindow;
import cn.flyexp.window.wallet.ChangePayAccountWindow;
import cn.flyexp.window.wallet.ChangePayPwdWindow;
import cn.flyexp.window.wallet.ResetPayPwdWindow;
import cn.flyexp.window.wallet.SetPayPwdWindow;
import cn.flyexp.window.wallet.WalletDetailWindow;
import cn.flyexp.window.wallet.WalletWindow;
import cn.flyexp.window.wallet.WithdrawalWindow;

/**
 * Created by tanxinye on 2016/10/21.
 */
public class ControllerManager {

    private static ControllerManager controllerManager;
    private ControllerHandler controllerHandler;

    private ControllerManager() {
        controllerHandler = new ControllerHandler();
    }

    public static ControllerManager getInstance() {
        if (controllerManager == null) {
            synchronized (ControllerManager.class) {
                if (controllerManager == null) {
                    controllerManager = new ControllerManager();
                }
            }
        }
        return controllerManager;
    }

    public void sendMessage(int id) {
        controllerHandler.sendEmptyMessage(id);
    }

    public void sendMessage(int id, Bundle bundle) {
        Message message = Message.obtain();
        message.what = id;
        message.setData(bundle);
        controllerHandler.sendMessage(message);
    }

    public class ControllerHandler extends Handler {

        public ControllerHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WindowIDDefine.WINDOW_MAIN) {
                MainWindow mainWindow = new MainWindow();
                mainWindow.clearStack();
                mainWindow.showWindow(false);
            } else if (msg.what == WindowIDDefine.WINDOW_LOGIN) {
                new LoginWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_REGISTER) {
                new RegisterWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_REGISTER_VERCODE) {
                new RegisterVercodeWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_SPLASH) {
                new SplashWindow().showWindow(false);
            } else if (msg.what == WindowIDDefine.WINDOW_GUIDE) {
                new GuideWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WEBVIEW) {
                new WebWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_SETTING) {
                new SettingWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ABOUT) {
                new AboutWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_FEEDBACK) {
                new FeedbackWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSN) {
                new AssnWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_RESETPWD) {
                new ResetPwdWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_RESETPWD_VERCODE) {
                new ResetPwdVercodeWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET) {
                new WalletWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET_WITHDRAWAL) {
                new WithdrawalWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET_DETAIL) {
                new WalletDetailWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET_RESETPAYPWD) {
                new ResetPayPwdWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET_BINDPAYPWD) {
                new BindAlipayWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET_CHANGEPAYPWD) {
                new ChangePayPwdWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET_SETPAYPWD) {
                new SetPayPwdWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_WALLET_CHANGEPAYACCOUNT) {
                new ChangePayAccountWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSNACTI_DETAIL) {
                new AssnActiDetailWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSN_DETAIL) {
                new AssnDetailWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSN_JOIN) {
                new AssnJoinWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYASSN) {
                new MyAssnWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYASSN_DETAIL) {
                new TMyAssnDetailWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYASSN_ACTIVITY) {
                new MyAssnActivityWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSN_EXAMINE_LIST) {
                new AssnExamineListWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSN_EXAMINE_DETAIL) {
                new AssnExamineDetailWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSN_NOTICE_PULISH) {
                new AssnNoticePublishWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_SHARE) {
                new ShareWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MESSAGE) {
                new MessageWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYTASK) {
                new MyTaskWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_ASSNACTI_PUBLISH) {
                new AssnActiPublishWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_CERTIFITION) {
                new CertificationWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_TASK_DETAIL) {
                new TaskDetailWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYTASK_DETAIL) {
                new MyTaskDetailWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_TASK_REPORT) {
                new TaskReportWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_TASK_PUBLISH) {
                new TaskPublishWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_PICBROWSER) {
                new PicBrowserWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_TOPIC_PUBLISH) {
                new TopicPublishWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYTOPIC) {
                new MyTopicWindow().showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYINFO) {
                new MyInfoWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_MYINFO_EDIT) {
                new MyInfoEditWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_OTHERS) {
                new MyInfoEditWindow(msg.getData()).showWindow(true);
            } else if (msg.what == WindowIDDefine.WINDOW_GALLERY) {
                new GalleryWindow(msg.getData()).showWindow(true);
            }
        }
    }

}
