package cn.flyexp.mvc.shop;

import android.os.Message;

import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;

/**
 * Created by txy on 2016/7/17 0017.
 */
public class BusinessController extends AbstractController implements BusinessViewCallBack {

    private BusinessWindow businessWindow;
    private BusinessDetailWindow businessDetailWindow;
    private BusinessPublishWindow businessPublishWindow;

    public BusinessController() {
        super();
    }

    @Override
    protected void handleMessage(Message message) {
        if (message.what == MessageIDDefine.BUSINESS_OPEN) {
            businessWindow = new BusinessWindow(this);
            businessWindow.showWindow(true, true);
        } else if (message.what == MessageIDDefine.BUSINESS_DETAIL_OPEN) {
            businessDetailWindow = new BusinessDetailWindow(this);
            businessDetailWindow.showWindow(true, true);
        } else if (message.what == MessageIDDefine.BUSINESS_PUBLISH_OPEN) {
            businessPublishWindow = new BusinessPublishWindow(this);
            businessPublishWindow.showWindow(true, true);
        }
    }


    @Override
    protected void registerMessages() {
        registerMessage(MessageIDDefine.BUSINESS_OPEN, this);
        registerMessage(MessageIDDefine.BUSINESS_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.BUSINESS_PUBLISH_OPEN, this);
    }


    @Override
    public void detailEnter() {
        sendMessage(MessageIDDefine.BUSINESS_DETAIL_OPEN);
    }

    @Override
    public void publishEnter() {
        sendMessage(MessageIDDefine.BUSINESS_PUBLISH_OPEN);
    }
}
