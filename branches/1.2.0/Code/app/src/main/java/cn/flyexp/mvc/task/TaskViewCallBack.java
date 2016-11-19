package cn.flyexp.mvc.task;

import cn.flyexp.entity.AlipayRequest;
import cn.flyexp.entity.CancelTaskRequest;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.FinishWorkRequest;
import cn.flyexp.entity.MyTaskRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.OrderCreateRequest;
import cn.flyexp.entity.TaskRequest;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/6/13 0013.
 */
public interface TaskViewCallBack extends AbstractWindow.WindowCallBack {

    void publishEnter();

    void myTaskEnter();

    void reportEnter(int oid);

    void detailEnter(OrderResponse.OrderResponseData data);

    void myOrderDetailEnter(MyTaskResponse.MyTaskResponseData data);

    void myTaskDetailEnter(MyTaskResponse.MyTaskResponseData data);

    void getOrderList(TaskRequest orderRequest);

    void createOrder(OrderCreateRequest orderCreateRequest);

    void claimOrder(TaskClaimRequest orderClaimRequest);

    void finishOrder(FinishOrderRequest finishOrderRequest);

    void getMyOrderList(MyTaskRequest myTaskRequest);

    void getMyTaskList(MyTaskRequest myTaskRequest);

    void finishWork(FinishWorkRequest finishWorkRequest);

    void cancelTask(CancelTaskRequest cancelTaskRequest, boolean from);

    void setPayPwdEnter();

    void verifiPayPwdEnter(int payResultTask);
}
