package cn.flyexp.mvc.shop;

import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.ShopRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/21 0021.
 */
public interface ShopViewCallBack extends AbstractWindow.WindowCallBack{
    void picBrowserEnter(PicBrowserBean picBrowserBean);
}
