package cn.flyexp.mvc.shop;

import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/17 0017.
 */
public interface BusinessViewCallBack extends AbstractWindow.WindowCallBack{

    public void detailEnter();

    public void publishEnter();
}
