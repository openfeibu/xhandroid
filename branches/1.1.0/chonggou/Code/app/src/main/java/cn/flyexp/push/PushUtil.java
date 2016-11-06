package cn.flyexp.push;

import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by tanxinye on 2016/10/4.
 */
public class PushUtil {

    /**
     * 分发透传数据
     * <p/>
     * refresh: 1或0 是否刷新
     * target: 指定区域  ad、extra、assn_hotact、message、assn_member_review、assn_act、assn_notice、mytask、topic
     * data: 数据或null
     * <p/>
     * 通知刷新的window未初始化用sharedpreference保存
     *
     * @param throughData 透传数据
     */
    public static void dispatchData(String throughData) {
        LogUtil.error(PushUtil.class,throughData);
        Message mes = Message.obtain();
        try {
            JSONObject jsonObject = new JSONObject(throughData);
            int refresh = jsonObject.getInt("refresh");
            String target = jsonObject.getString("target");
            String data = jsonObject.getString("data");
    
            if (refresh == 1) {
                if (target.equals("ad")) {
                    mes.what = NotifyIDDefine.NOTIFY_AD_REFRESH;
                } else if (target.equals("extra")) {
                    mes.what = NotifyIDDefine.NOTIFY_EXTRA_REFRESH;
                } else if (target.equals("assn_hotact")) {
                    mes.what = NotifyIDDefine.NOTIFY_HOTACT_REFRESH;
                } else if (target.equals("message")) {
                    mes.what = NotifyIDDefine.NOTIFY_MESSAGE_REFRESH;
                } else if (target.equals("assn_member_review")) {
                    mes.what = NotifyIDDefine.NOTIFY_ASSN_MENBER_REFRESH;
                    mes.obj = data;
                } else if (target.equals("assn_act")) {
                    mes.what = NotifyIDDefine.NOTIFY_ASSN_ACT_REFRESH;
                    mes.obj = data;
                } else if (target.equals("assn_notice")) {
                    mes.what = NotifyIDDefine.NOTIFY_ASSN_NOTICE_REFRESH;
                    mes.obj = data;
                } else if (target.equals("mytask")) {
                    mes.what = NotifyIDDefine.NOTIFY_MYTASK_REFRESH;
                } else if (target.equals("topic")) {
                    mes.what = NotifyIDDefine.NOTIFY_TOPIC_REFRESH;
                }
                AbstractController.notifyManager.notify(mes);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
