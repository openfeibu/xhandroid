package cn.flyexp.framework;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class NotifyIDDefine {

    private static int ID = 1;

    private static int generateMessageId() {
        return ID++;
    }

    public static int NOTIFY_AD_REFRESH = generateMessageId();

    public static int NOTIFY_HOTACT_REFRESH = generateMessageId();

    public static int NOTIFY_EXTRA_REFRESH = generateMessageId();

    public static int NOTIFY_ASSN_MENBER_REFRESH = generateMessageId();

    public static int NOTIFY_ASSN_ACT_REFRESH = generateMessageId();

    public static int NOTIFY_ASSN_NOTICE_REFRESH = generateMessageId();

    public static int NOTIFY_MINE_MYTASK_REFRESH = generateMessageId();

    public static int NOTIFY_MYTASK = generateMessageId();

    public static int NOTIFY_MYINFO = generateMessageId();

    public static int NOTIFY_ASSN_ACTIVITY = generateMessageId();

    public static int NOTIFY_TOPIC_PUSH = generateMessageId();

    public static int NOTIFY_TOPIC = generateMessageId();

    public static int NOTIFY_MINE_REFRESH = generateMessageId();

    public static int NOTIFY_TASK_REFRESH = generateMessageId();

    public static int NOTIFY_MAIN_TASK = generateMessageId();

    public static int NOTIFY_MESSAGE_REFRESH = generateMessageId();

    public static int NOTICE_MAIN_HOME = generateMessageId();

    public static int NOTICE_LOGIN = generateMessageId();

    public static int NOTICE_WALLET = generateMessageId();

    public static int NOTICE_MYASSN = generateMessageId();

    public static int NOTICE_MYASSN_DETAIL = generateMessageId();

    public static int NOTICE_ASSN_EXAMINE_LIST = generateMessageId();

    public static int NOTICE_ASSN_EXAMINE_NEW = generateMessageId();

    public static int NOTICE_SHARE = generateMessageId();

    public static int NOTICE_DELETE_PHOTO = generateMessageId();

    public static int NOTIFY_GALLERY = generateMessageId();

    public static int NOTIFY_CAMERA_RESULT = generateMessageId();

    public static int NOTIFY_EDIT_RESULT = generateMessageId();

}
