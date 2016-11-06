package cn.flyexp.mvc.shop;

import android.os.Message;

import cn.flyexp.entity.ResponseCode;
import cn.flyexp.entity.ShopRequest;
import cn.flyexp.entity.ShopResponse;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class ShopController extends AbstractController implements ShopViewCallBack, ShopModeCallBack {

    private ShopWindow_temp shopWindow_temp;
    private ShopDetailWindow shopDetailWindow;
    private ShopWindow shopWindow;
    private ShopSearchWindow shopSearchWindow;
    private ShopModel shopModel;

    public ShopController() {
        super();
        shopModel = new ShopModel(this);
    }

    @Override
    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.SHOP_OPEN) {
            shopWindow = new ShopWindow(this);
            shopWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.SHOP_DETAIL_OPEN) {
            shopDetailWindow = new ShopDetailWindow(this);
            shopDetailWindow.showWindow(true, true);
        }else if (mes.what == MessageIDDefine.SHOP_SEARCH_OPEN) {
            shopSearchWindow = new ShopSearchWindow(this);
            shopSearchWindow.showWindow(true, true);
        }
    }

    @Override
    protected void registerMessages() {
        registerMessage(MessageIDDefine.SHOP_OPEN, this);
        registerMessage(MessageIDDefine.SHOP_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.SHOP_SEARCH_OPEN, this);
    }

    @Override
    public void detailEnter() {
        sendMessage(MessageIDDefine.SHOP_DETAIL_OPEN);
    }

    @Override
    public void searchEnter() {
        sendMessage(MessageIDDefine.SHOP_SEARCH_OPEN);
    }

    @Override
    public void getShopListRequest(ShopRequest shopRequest) {
        shopModel.getShopList(shopRequest);
    }

    @Override
    public void taskPublishEnter() {
        sendMessage(MessageIDDefine.TASK_PUBLISH_OPEN);
    }

    @Override
    public void shopListResponse(ShopResponse shopResponse) {
        if (shopResponse == null) {
            shopWindow_temp.shopLoadingFailure();
            return;
        }
        int code = shopResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                shopWindow_temp.shopListResponse(shopResponse.getData());
                break;
        }
    }
}
