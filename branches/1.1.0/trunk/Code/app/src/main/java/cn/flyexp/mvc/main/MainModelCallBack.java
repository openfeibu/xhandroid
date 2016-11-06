package cn.flyexp.mvc.main;

import cn.flyexp.entity.NoResponse;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/8/20 0020.
 */
public interface MainModelCallBack extends AbstractWindow.WindowCallBack {

    public void campusNoResponse(NoResponse noResponse);
}
