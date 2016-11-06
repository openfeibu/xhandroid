package cn.flyexp.mvc.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import cn.flyexp.R;
import cn.flyexp.entity.CancelTaskRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.constants.Config;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.FinishWorkRequest;
import cn.flyexp.entity.MyTaskRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.OrderCreateRequest;
import cn.flyexp.entity.TaskPublishResponse;
import cn.flyexp.entity.TaskRequest;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.PayResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by txy on 2016/6/5.
 * Modifity by txy on 2016/6/8.
 */
public class TaskController extends AbstractController implements TaskViewCallBack, NotifyManager.Notify {

    public TaskWindow taskWindow;
    private TaskPublishWindow taskPublishWindow;
    private TaskDetailWindow taskDetailWindow;
    private MyTaskWindow myTaskWindow;
    private MyTaskDetailWindow myTaskDetailWindow;
    private MyOrderDetailWindow myOrderDetailWindow;

    public TaskController() {
        super();
        taskWindow = new TaskWindow(this);
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.TASK_OPEN) {
            taskWindow = new TaskWindow(this);
            taskWindow.showWindow();
        } else if (mes.what == MessageIDDefine.TASK_DETAIL_OPEN) {
            taskDetailWindow = new TaskDetailWindow(this);
            if (mes.arg1 == -1) {
                taskDetailWindow.initOrderDetail((OrderResponse.OrderResponseData) mes.obj);
            } else {
                taskDetailWindow.detailRequest(mes.arg1);
            }
            taskDetailWindow.showWindow();
        } else if (mes.what == MessageIDDefine.TASK_PUBLISH_OPEN) {
            taskPublishWindow = new TaskPublishWindow(this);
            taskPublishWindow.showWindow();
        } else if (mes.what == MessageIDDefine.MYTASK_OPEN) {
            myTaskWindow = new MyTaskWindow(this);
            myTaskWindow.showWindow();
        } else if (mes.what == MessageIDDefine.MYTASK_DETAIL_OPEN) {
            myTaskDetailWindow = new MyTaskDetailWindow(this);
            myTaskDetailWindow.initMyTaskDetail((MyTaskResponse.MyTaskResponseData) mes.obj);
            myTaskDetailWindow.showWindow();
        } else if (mes.what == MessageIDDefine.MYORDER_DETAIL_OPEN) {
            myOrderDetailWindow = new MyOrderDetailWindow(this);
            myOrderDetailWindow.initMyOrderDetail((MyTaskResponse.MyTaskResponseData) mes.obj);
            myOrderDetailWindow.showWindow();
        } else if (mes.what == MessageIDDefine.SDK_PAY_FLAG) {
            PayResult payResult = new PayResult((String) mes.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            WindowHelper.showToast("支付");
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                WindowHelper.showToast("支付成功");
                taskPublishWindow.hideWindow(true);
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    WindowHelper.showToast("支付结果确认中");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    WindowHelper.showToast("支付失败");
                }
            }

        }
    }

    @Override
    protected void registerMessages() {
        registerMessage(MessageIDDefine.TASK_OPEN, this);
        registerMessage(MessageIDDefine.TASK_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.TASK_PUBLISH_OPEN, this);
        registerMessage(MessageIDDefine.MYTASK_OPEN, this);
        registerMessage(MessageIDDefine.MYORDER_DETAIL_OPEN, this);
        registerMessage(MessageIDDefine.MYTASK_DETAIL_OPEN, this);
        registerNotify(NotifyIDDefine.PAY_PWD_RESULT, this);
    }

    @Override
    public void onNotify(Message message) {
        if (message.what == NotifyIDDefine.PAY_PWD_RESULT) {
            if (message.arg1 == NotifyIDDefine.PRESULT_PAY_TASK) {
                if (taskPublishWindow != null) {
                    taskPublishWindow.balancePay((String) message.obj);
                }
            } else if (message.arg1 == NotifyIDDefine.RESULT_PAY_FINISHTAK) {
                if (myOrderDetailWindow != null) {
                    myOrderDetailWindow.finishWork((String) message.obj);
                }
            }
        }
    }

    @Override
    public void publishEnter() {
        sendMessage(MessageIDDefine.TASK_PUBLISH_OPEN);
    }

    @Override
    public void reportEnter(int oid) {
        sendMessage(MessageIDDefine.REPORT_OPEN, oid);
    }

    @Override
    public void detailEnter(OrderResponse.OrderResponseData data) {
        Message message = Message.obtain();
        message.what = MessageIDDefine.TASK_DETAIL_OPEN;
        message.obj = data;
        message.arg1 = -1;
        sendMessage(message);
    }

    @Override
    public void myOrderDetailEnter(MyTaskResponse.MyTaskResponseData data) {
        sendMessage(MessageIDDefine.MYORDER_DETAIL_OPEN, data);
    }

    @Override
    public void myTaskDetailEnter(MyTaskResponse.MyTaskResponseData data) {
        sendMessage(MessageIDDefine.MYTASK_DETAIL_OPEN, data);
    }

    @Override
    public void getOrderList(TaskRequest taskRequest) {
        if (taskRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(taskRequest, TaskRequest.class);
        Call<EncodeData> call = service.getOrderList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    OrderResponse orderResponse = GsonUtil.fromEncodeJson(response.body().getData(), OrderResponse.class);
                    int code = orderResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            taskWindow.orderListResponse(orderResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(taskWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(orderResponse.getDetail());
                            break;
                    }
                } else {
                    taskWindow.loadingFailure();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                taskWindow.loadingFailure();
            }
        });
    }

    @Override
    public void createOrder(OrderCreateRequest orderCreateRequest) {
        if (orderCreateRequest == null) {
            return;
        }
        Call<EncodeData> call = service.createOrder(orderCreateRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    TaskPublishResponse taskPublishResponse = GsonUtil.fromEncodeJson(response.body().getData(), TaskPublishResponse.class);
                    int code = taskPublishResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            final String payInfo = taskPublishResponse.getData();
                            if (payInfo.equals("")) {
                                taskPublishWindow.dismissProgressDialog();
                                setUIData(UIDataKeysDef.TASK_CONTENT, "");
                                WindowHelper.showToast("发单成功");
                                taskPublishWindow.hideWindow(true);
                                sendNotify(NotifyIDDefine.NOTIFY_MINE_REFRESH);
                                return;
                            }
                            PermissionHandler.PermissionCallback permissionCallback = new PermissionHandler.PermissionCallback() {
                                public void onSuccess() {
                                    Runnable payRunnable = new Runnable() {

                                        @Override
                                        public void run() {
                                            // 构造PayTask 对象
                                            PayTask alipay = new PayTask((Activity) getContext());
                                            // 调用支付接口，获取支付结果
                                            String result = alipay.pay(payInfo, true);
                                            Message msg = new Message();
                                            msg.what = SDK_PAY_FLAG;
                                            msg.obj = result;
                                            mHandler.sendMessage(msg);
                                        }
                                    };
                                    // 必须异步调用
                                    Thread payThread = new Thread(payRunnable);
                                    payThread.start();
                                }

                                public void onFail(int[] ids) {
                                }

                                public void onCancel() {
                                }

                                public void goSetting() {
                                }
                            };
                            PermissionTools.requestPermission(getContext(), permissionCallback, new int[]{PermissionHandler.PERMISSION_FILE, PermissionHandler.PERMISSION_PHONE});
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(taskPublishWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(taskPublishResponse.getDetail());
                            break;
                    }
                } else {
                }
                taskPublishWindow.dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                taskPublishWindow.dismissProgressDialog();
            }
        });
    }

    @Override
    public void claimOrder(TaskClaimRequest taskClaimRequest) {
        if (taskClaimRequest == null) {
            return;
        }
        Call<EncodeData> call = service.claimOrder(taskClaimRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast(getContext().getString(R.string.claim_success));
                            taskDetailWindow.hideWindow(false);
                            taskWindow.refreshData();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(taskDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                        case ResponseCode.RESPONSE_2011:
                            WindowHelper.showToast("不能接自己发的任务");
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void finishOrder(FinishOrderRequest finishOrderRequest) {
        if (finishOrderRequest == null) {
            return;
        }
        Call<EncodeData> call = service.confirmFinishWork(finishOrderRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    myOrderDetailWindow.response();
                    if (commonResponse == null) {
                        return;
                    }
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myOrderDetailWindow.hideWindow(true);
                            myTaskWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myOrderDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void getMyOrderList(MyTaskRequest myTaskRequest) {
        if (myTaskRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(myTaskRequest, MyTaskRequest.class);
        Call<EncodeData> call = service.myOrder(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyTaskResponse myTaskResponse = GsonUtil.fromEncodeJson(response.body().getData(), MyTaskResponse.class);
                    int code = myTaskResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myTaskWindow.getMyOrderListResponse(myTaskResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myTaskWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(myTaskResponse.getDetail());
                            break;
                    }
                } else {
                    myTaskWindow.orderLoadingFailure();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                myTaskWindow.orderLoadingFailure();
            }
        });
    }

    @Override
    public void getMyTaskList(MyTaskRequest myTaskRequest) {
        if (myTaskRequest == null) {
            return;
        }
        String data = GsonUtil.toEncodeJson(myTaskRequest, MyTaskRequest.class);
        Call<EncodeData> call = service.myTask(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MyTaskResponse myTaskResponse = GsonUtil.fromEncodeJson(response.body().getData(), MyTaskResponse.class);
                    int code = myTaskResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            myTaskWindow.getMyTaskListResponse(myTaskResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myTaskWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(myTaskResponse.getDetail());
                            break;
                    }
                } else {
                    myTaskWindow.taskLoadingFailure();
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                myTaskWindow.taskLoadingFailure();
            }
        });
    }

    @Override
    public void finishWork(FinishWorkRequest finishWorkRequest) {
        if (finishWorkRequest == null) {
            return;
        }
        Call<EncodeData> call = service.finishWork(finishWorkRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("完成任务，请等待结算");
                            myTaskDetailWindow.hideWindow(true);
                            myTaskWindow.hideWindow(true);
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myTaskDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                myTaskDetailWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                myTaskDetailWindow.response();
            }
        });
    }

    @Override
    public void cancelTask(CancelTaskRequest cancelTaskRequest) {
        if (cancelTaskRequest == null) {
            return;
        }
        Call<EncodeData> call = service.cancelTask(cancelTaskRequest);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    CommonResponse commonResponse = GsonUtil.fromEncodeJson(response.body().getData(), CommonResponse.class);
                    int code = commonResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            WindowHelper.showToast("取消成功");
                            myOrderDetailWindow.hideWindow(true);
                            myTaskWindow.refreshMyOrder();
                            break;
                        case ResponseCode.RESPONSE_2001:
                            againLogin(myOrderDetailWindow);
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(commonResponse.getDetail());
                            break;
                    }
                } else {
                }
                myOrderDetailWindow.response();
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                myOrderDetailWindow.response();
            }
        });
    }

    @Override
    public void setPayPwdEnter() {
        sendMessage(MessageIDDefine.SETPAYPWD_OPEN);
    }

    @Override
    public void verifiPayPwdEnter(int requestCode) {
        sendMessage(MessageIDDefine.VERIFIPAYPWD_OPEN, requestCode);
    }

    private static final int SDK_PAY_FLAG = 0x1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getContext(), "支付成功", Toast.LENGTH_SHORT).show();
                        taskPublishWindow.hideWindow(true);
                        taskWindow.refreshData();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getContext(), "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getContext(), "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
}
