package cn.flyexp.framework;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class WindowIDDefine {

    private static int ID = 1;

    private static int generateMessageId() {
        return ID++;
    }

    public static int WINDOW_MAIN = generateMessageId();

    public static int WINDOW_SPLASH = generateMessageId();

    public static int WINDOW_GUIDE = generateMessageId();

    public static int WINDOW_WEBVIEW = generateMessageId();

    public static int WINDOW_LOGIN = generateMessageId();

    public static int WINDOW_SETTING = generateMessageId();

    public static int WINDOW_MESSAGE = generateMessageId();

    public static int WINDOW_SHARE = generateMessageId();

    public static int WINDOW_ABOUT = generateMessageId();

    public static int WINDOW_FEEDBACK = generateMessageId();

    public static int WINDOW_ASSN = generateMessageId();

    public static int WINDOW_MYASSN = generateMessageId();

    public static int WINDOW_MYINFO = generateMessageId();

    public static int WINDOW_MYORDER = generateMessageId();

    public static int WINDOW_MYTASK = generateMessageId();

    public static int WINDOW_MYTOPIC = generateMessageId();

    public static int WINDOW_TOPIC_PUBLISH = generateMessageId();

    public static int WINDOW_PICBROWSER = generateMessageId();

    public static int WINDOW_MYASSN_DETAIL = generateMessageId();

    public static int WINDOW_MYASSN_ACTIVITY = generateMessageId();

    public static int WINDOW_ASSNACTI_PUBLISH = generateMessageId();

    public static int WINDOW_ASSN_EXAMINE_LIST = generateMessageId();

    public static int WINDOW_ASSN_EXAMINE_DETAIL = generateMessageId();

    public static int WINDOW_ASSNACTI_DETAIL = generateMessageId();

    public static int WINDOW_ASSN_DETAIL = generateMessageId();

    public static int WINDOW_ASSN_JOIN = generateMessageId();

    public static int WINDOW_ASSN_NOTICE_PULISH = generateMessageId();

    public static int WINDOW_RESETPWD = generateMessageId();

    public static int WINDOW_RESETPWD_VERCODE = generateMessageId();

    public static int WINDOW_REGISTER = generateMessageId();

    public static int WINDOW_REGISTER_VERCODE = generateMessageId();

    public static int WINDOW_TASK_PUBLISH = generateMessageId();

    public static int WINDOW_TASK_DETAIL = generateMessageId();

    public static int WINDOW_MYTASK_DETAIL = generateMessageId();

    public static int WINDOW_TASK_REPORT = generateMessageId();

    public static int WINDOW_ORDER_DETAIL = generateMessageId();

    public static int WINDOW_TOPIC_DETAIL = generateMessageId();

    public static int WINDOW_CERTIFITION = generateMessageId();

    public static int WINDOW_WALLET = generateMessageId();

    public static int WINDOW_WALLET_SETPAYPWD = generateMessageId();

    public static int WINDOW_WALLET_RESETPAYPWD = generateMessageId();

    public static int WINDOW_WALLET_CHANGEPAYPWD = generateMessageId();

    public static int WINDOW_WALLET_BINDPAYPWD = generateMessageId();

    public static int WINDOW_WALLET_PAYACCOUNT = generateMessageId();

    public static int WINDOW_WALLET_WITHDRAWAL = generateMessageId();

    public static int WINDOW_WALLET_DETAIL = generateMessageId();

    public static int WINDOW_WALLET_CHANGEPAYACCOUNT = generateMessageId();

    public static int WINDOW_CHANGEPWD = generateMessageId();

    public static int WINDOW_MYINFO_EDIT = generateMessageId();

    public static int WINDOW_OTHERS = generateMessageId();
}
