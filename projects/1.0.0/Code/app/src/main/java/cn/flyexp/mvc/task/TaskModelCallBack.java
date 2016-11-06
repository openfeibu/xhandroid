package cn.flyexp.mvc.task;

import cn.flyexp.entity.AlipayResponse;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.entity.TaskPublishResponse;

/**
 * Created by txy on 2016/6/13 0013.
 */
public interface TaskModelCallBack {
    void orderListResponse(OrderResponse orderResponse);

    void myOrderResponse(MyTaskResponse myTaskResponse);

    void myTaskResponse(MyTaskResponse myTaskResponse);

    void orderClaimResponse(CommonResponse commonResponse);

    void finishOrderResponse(CommonResponse commonResponse);

    void finishWorkResponse(CommonResponse commonResponse);

    void cancelTaskResponse(CommonResponse commonResponse);

    void alipayResponse(AlipayResponse alipayResponse);

    void taskPublishResponse(TaskPublishResponse taskPublishResponse);
}
