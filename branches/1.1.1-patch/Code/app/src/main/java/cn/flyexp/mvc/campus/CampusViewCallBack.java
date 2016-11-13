package cn.flyexp.mvc.campus;

import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.entity.RecommendOrderRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/17 0017.
 */
public interface CampusViewCallBack extends AbstractWindow.WindowCallBack {

    void getAd();

    void getHotAssnActivity();

    void assnEnter();

    void activityDetailEnter(AssnActivityResponse.AssnActivityResponseData assnActivityResponseData);

    void recomTaskDetailEnter(OrderResponse.OrderResponseData orderResponseData);

    void getCampusNo();

    void recommendOrder(RecommendOrderRequest recommendOrderRequest);
}
