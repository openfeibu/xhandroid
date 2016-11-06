package cn.flyexp.mvc.shop;

import android.os.Message;

import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;

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

}
