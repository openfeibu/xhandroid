package cn.flyexp.mvc.task;

import cn.flyexp.entity.AlipayRequest;
import cn.flyexp.entity.AlipayResponse;
import cn.flyexp.entity.CancelTaskRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.FinishWorkRequest;
import cn.flyexp.entity.MyTaskRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.OrderCreateRequest;
import cn.flyexp.entity.TaskPublishResponse;
import cn.flyexp.entity.TaskRequest;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/6/5.
 */
public class TaskModel {

    private TaskModelCallBack callBack;
    private NetWorkService service;

    public TaskModel(TaskModelCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void getOrderList(TaskRequest taskRequest) {
        if (taskRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(taskRequest, TaskRequest.class);
        Call<EncodeData> call = service.getOrderList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    OrderResponse orderResponse = GsonUtil.fromJson(response.body().getData(), OrderResponse.class);
                    callBack.orderListResponse(orderResponse);
                } else {
                    callBack.orderListResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.orderListResponse(null);
            }
        });
    }


    public void createOrder(OrderCreateRequest orderCreateRequest) {
        if (orderCreateRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createOrder(orderCreateRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TaskPublishResponse taskPublishResponse = GsonUtil.fromJson(response.body().getData(), TaskPublishResponse.class);
                    callBack.taskPublishResponse(taskPublishResponse);
                } else {
                    callBack.taskPublishResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.taskPublishResponse(null);
            }
        });
    }

    public void claimOrder(TaskClaimRequest taskClaimRequest) {
        if (taskClaimRequest == null) {
            return;
        }
        Call<EncodeData> call = service.claimOrder(taskClaimRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.orderClaimResponse(commonResponse);
                } else {
                    callBack.orderClaimResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.orderClaimResponse(null);
            }
        });
    }

    public void finishOrder(FinishOrderRequest finishOrderRequest) {
        if (finishOrderRequest == null) {
            return;
        }
        Call<EncodeData> call = service.confirmFinishWork(finishOrderRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.finishOrderResponse(commonResponse);
                } else {
                    callBack.finishOrderResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.finishOrderResponse(null);
            }
        });
    }

    public void myOrder(MyTaskRequest myTaskRequest) {
        if (myTaskRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(myTaskRequest, MyTaskRequest.class);
        Call<EncodeData> call = service.myOrder(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyTaskResponse myTaskResponse = GsonUtil.fromJson(response.body().getData(), MyTaskResponse.class);
                    callBack.myOrderResponse(myTaskResponse);
                } else {
                    callBack.myOrderResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.myOrderResponse(null);
            }
        });
    }

    public void myTask(MyTaskRequest myTaskRequest) {
        if (myTaskRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(myTaskRequest, MyTaskRequest.class);
        Call<EncodeData> call = service.myTask(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyTaskResponse myTaskResponse = GsonUtil.fromJson(response.body().getData(), MyTaskResponse.class);
                    callBack.myTaskResponse(myTaskResponse);
                } else {
                    callBack.myTaskResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.myTaskResponse(null);
            }
        });
    }

    public void finishWork(FinishWorkRequest finishWorkRequest) {
        if (finishWorkRequest == null) {
            return;
        }
        Call<EncodeData> call = service.finishWork(finishWorkRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.finishWorkResponse(commonResponse);
                } else {
                    callBack.finishWorkResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.finishWorkResponse(null);
            }
        });
    }

    public void cancelTask(CancelTaskRequest cancelTaskRequest) {
        if (cancelTaskRequest == null) {
            return;
        }
        Call<EncodeData> call = service.cancelTask(cancelTaskRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromJson(response.body().getData(), CommonResponse.class);
                    callBack.cancelTaskResponse(commonResponse);
                } else {
                    callBack.cancelTaskResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.cancelTaskResponse(null);
            }
        });
    }

    public void aliPay(AlipayRequest alipayRequest) {
        if (alipayRequest == null) {
            return;
        }
        Call<EncodeData> call = service.aliPay(alipayRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AlipayResponse alipayResponse = GsonUtil.fromJson(response.body().getData(), AlipayResponse.class);
                    callBack.alipayResponse(alipayResponse);
                } else {
                    callBack.alipayResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.alipayResponse(null);
            }
        });
    }
}
