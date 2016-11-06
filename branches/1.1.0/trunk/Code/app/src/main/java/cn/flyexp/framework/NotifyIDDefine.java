package cn.flyexp.framework;

/**
 * Created by zlk on 2016/3/27.
 */
public class NotifyIDDefine {
    private static int ID = 1;

    private static int generateMessageId() {
        return ID++;
    }

    public static int ON_ACTIVITY_RESUME = generateMessageId();

    public static int ON_ACTIVITY_PAUSE = generateMessageId();

    public static int ON_ACTIVITY_DESTROY = generateMessageId();

    public static int ON_ACTIVITY_SAVEINSTANCESTATE = generateMessageId();

    public static int NOTIFY_MINE_REFRESH = generateMessageId();

    public static int NOTIFY_MESSAGE_REFRESH = generateMessageId();

    public static int SHARE_SUCCESS = generateMessageId();

    public static int PAY_PWD_RESULT = generateMessageId();


}
