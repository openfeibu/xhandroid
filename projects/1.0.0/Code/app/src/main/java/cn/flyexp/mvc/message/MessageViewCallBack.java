package cn.flyexp.mvc.message;

import cn.flyexp.entity.MessageRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/20 0020.
 */
public interface MessageViewCallBack extends AbstractWindow.WindowCallBack {

    public void getMessageList(MessageRequest messageRequest);
}
