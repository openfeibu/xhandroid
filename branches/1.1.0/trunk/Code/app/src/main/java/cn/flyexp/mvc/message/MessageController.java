package cn.flyexp.mvc.message;

import android.os.Message;

import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;


/**
 * Created by txy on 2016/6/6.
 */
public class MessageController extends AbstractController implements MessageViewCallBack, MessageModelCallBack, NotifyManager.Notify {

    public MessageWindow messageWindow = null;
    private MessageModel messageModel = null;

    public MessageController() {
        super();
        messageModel = new MessageModel(this);
        messageWindow = new MessageWindow(this);
    }

    protected void handleMessage(Message mes) {
    }

    protected void registerMessages() {
        registerNotify(NotifyIDDefine.NOTIFY_MESSAGE_REFRESH, this);
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_MESSAGE_REFRESH) {
            messageWindow.refreshData();
        }
    }

    @Override
    public void getMessageList(MessageRequest messageRequest) {
        messageModel.getMessageList(messageRequest);
    }

    @Override
    public void getMessageListResponse(MessageResponse messageResponse) {
        if (messageResponse == null) {
            messageWindow.loadingFailure();
            return;
        }
        int code = messageResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                messageWindow.messageResponse(messageResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(messageWindow);
                break;
            case ResponseCode.RESPONSE_110:
                messageWindow.showToast(messageResponse.getDetail());
                break;
        }
    }

}
