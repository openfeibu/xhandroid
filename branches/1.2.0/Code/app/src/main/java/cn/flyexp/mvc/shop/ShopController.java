package cn.flyexp.mvc.shop;

import android.os.Message;

import java.util.Date;

import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.ImageVerifyRequest;
import cn.flyexp.entity.ImageVerifyResponse;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class ShopController extends AbstractController implements ShopViewCallBack {

    public ShopWindow shopWindow;

    public ShopController() {
        super();
        shopWindow = new ShopWindow(this);
    }

    @Override
    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.SHOP_OPEN) {
            shopWindow = new ShopWindow(this);
            shopWindow.showWindow();
        }
    }

    @Override
    protected void registerMessages() {
        registerMessage(MessageIDDefine.SHOP_OPEN, this);
    }

    @Override
    public void picBrowserEnter(PicBrowserBean picBrowserBean) {
        Message message = Message.obtain();
        message.obj = picBrowserBean;
        message.what = MessageIDDefine.PIC_BROWSER_OPEN;
        sendMessage(message);
    }

}
