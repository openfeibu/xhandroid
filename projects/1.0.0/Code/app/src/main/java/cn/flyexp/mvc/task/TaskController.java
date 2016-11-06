package cn.flyexp.mvc.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import cn.flyexp.R;
import cn.flyexp.entity.AlipayResponse;
import cn.flyexp.entity.CancelTaskRequest;
import cn.flyexp.entity.CommonResponse;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.Constants;
import cn.flyexp.entity.FinishOrderRequest;
import cn.flyexp.entity.FinishWorkRequest;
import cn.flyexp.entity.MyTaskRequest;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.AlipayRequest;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.OrderCreateRequest;
import cn.flyexp.entity.TaskPublishResponse;
import cn.flyexp.entity.TaskRequest;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.entity.ResponseCode;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.PayResult;


/**
 * Created by txy on 2016/6/5.
 * Modifity by txy on 2016/6/8.
 */
public class TaskController extends AbstractController implements TaskViewCallBack, TaskModelCallBack, NotifyManager.Notify {

    private TaskWindow taskWindow;
    private TaskPublishWindow taskPublishWindow;
    private TaskDetailWindow taskDetailWindow;
    private MyTaskWindow myTaskWindow;
    private TaskModel taskModel;
    private MyTaskDetailWindow myTaskDetailWindow;
    private MyOrderDetailWindow myOrderDetailWindow;

    public TaskController() {
        super();
        taskModel = new TaskModel(this);
    }

    protected void handleMessage(Message mes) {
        if (mes.what == MessageIDDefine.TASK_OPEN) {
            taskWindow = new TaskWindow(this);
            taskWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.TASK_DETAIL_OPEN) {
            taskDetailWindow = new TaskDetailWindow(this);
            if (mes.arg1 == -1) {
                taskDetailWindow.initOrderDetail((OrderResponse.OrderResponseData) mes.obj);
            } else {
                taskDetailWindow.detailRequest(mes.arg1);
            }
            taskDetailWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.TASK_PUBLISH_OPEN) {
            taskPublishWindow = new TaskPublishWindow(this);
            taskPublishWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.MYTASK_OPEN) {
            myTaskWindow = new MyTaskWindow(this);
            myTaskWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.MYTASK_DETAIL_OPEN) {
            myTaskDetailWindow = new MyTaskDetailWindow(this);
            myTaskDetailWindow.initMyTaskDetail((MyTaskResponse.MyTaskResponseData) mes.obj);
            myTaskDetailWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.MYORDER_DETAIL_OPEN) {
            myOrderDetailWindow = new MyOrderDetailWindow(this);
            myOrderDetailWindow.initMyOrderDetail((MyTaskResponse.MyTaskResponseData) mes.obj);
            myOrderDetailWindow.showWindow(true, true);
        } else if (mes.what == MessageIDDefine.SDK_PAY_FLAG) {
            PayResult payResult = new PayResult((String) mes.obj);
            String resultInfo = payResult.getResult();// 同步返回需要验证的信息
            String resultStatus = payResult.getResultStatus();
            taskPublishWindow.showToast("支付");
            // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
            if (TextUtils.equals(resultStatus, "9000")) {
                taskPublishWindow.showToast("支付成功");
                taskPublishWindow.hideWindow(true);
            } else {
                // 判断resultStatus 为非"9000"则代表可能支付失败
                // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                if (TextUtils.equals(resultStatus, "8000")) {
                    taskPublishWindow.showToast("支付结果确认中");
                } else {
                    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                    taskPublishWindow.showToast("支付失败");
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
            if (message.arg1 == Constants.PAY_RESULT_TASK) {
                if (taskPublishWindow != null) {
                    taskPublishWindow.balancePay((String) message.obj);
                }
            } else if (message.arg1 == Constants.PAY_RESULT_FINISHTAK) {
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
    public void getOrderList(TaskRequest orderRequest) {
        taskModel.getOrderList(orderRequest);
    }

    @Override
    public void createOrder(OrderCreateRequest orderCreateRequest) {
        taskModel.createOrder(orderCreateRequest);
    }

    @Override
    public void claimOrder(TaskClaimRequest orderClaimRequest) {
        taskModel.claimOrder(orderClaimRequest);
    }

    @Override
    public void finishOrder(FinishOrderRequest finishOrderRequest) {
        taskModel.finishOrder(finishOrderRequest);
    }

    @Override
    public void getMyOrderList(MyTaskRequest myTaskRequest) {
        taskModel.myOrder(myTaskRequest);
    }

    @Override
    public void getMyTaskList(MyTaskRequest myTaskRequest) {
        taskModel.myTask(myTaskRequest);
    }

    @Override
    public void finishWork(FinishWorkRequest finishWorkRequest) {
        taskModel.finishWork(finishWorkRequest);
    }

    @Override
    public void cancelTask(CancelTaskRequest cancelTaskRequest) {
        taskModel.cancelTask(cancelTaskRequest);
    }

    @Override
    public void aliPay(AlipayRequest alipayRequest) {
        taskModel.aliPay(alipayRequest);
    }

    @Override
    public void setPayPwdEnter() {
        sendMessage(MessageIDDefine.SETPAYPWD_OPEN);
    }

    @Override
    public void verifiPayPwdEnter(int requestCode) {
        sendMessage(MessageIDDefine.VERIFIPAYPWD_OPEN, requestCode);
    }

    @Override
    public void orderListResponse(OrderResponse orderResponse) {
        if (orderResponse == null) {
            taskWindow.loadingFailure();
            return;
        }
        int code = orderResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                taskWindow.orderListResponse(orderResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(taskWindow);
                break;
            case ResponseCode.RESPONSE_110:
                taskWindow.showToast(orderResponse.getDetail());
                break;
        }
    }

    @Override
    public void myTaskResponse(MyTaskResponse myTaskResponse) {
        if (myTaskResponse == null) {
            myTaskWindow.taskLoadingFailure();
            return;
        }
        int code = myTaskResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myTaskWindow.getMyTaskListResponse(myTaskResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myTaskWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myTaskWindow.showToast(myTaskResponse.getDetail());
                break;
        }
    }

    @Override
    public void myOrderResponse(MyTaskResponse myTaskResponse) {
        if (myTaskResponse == null) {
            myTaskWindow.orderLoadingFailure();
            return;
        }
        int code = myTaskResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myTaskWindow.getMyOrderListResponse(myTaskResponse.getData());
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myTaskWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myTaskWindow.showToast(myTaskResponse.getDetail());
                break;
        }
    }

    @Override
    public void orderClaimResponse(CommonResponse commonResponse) {
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                taskDetailWindow.showToast(getContext().getString(R.string.claim_success));
                taskDetailWindow.hideWindow(false);
                taskWindow.refreshData();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(taskDetailWindow);
                break;
            case ResponseCode.RESPONSE_110:
                taskDetailWindow.showToast(commonResponse.getDetail());
                break;
            case ResponseCode.RESPONSE_2011:
                taskDetailWindow.showToast("不能接自己发的任务");
                break;
        }
    }

    @Override
    public void finishOrderResponse(CommonResponse commonResponse) {
        myOrderDetailWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myOrderDetailWindow.hideWindow(false);
                myTaskWindow.refreshMyOrder();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myOrderDetailWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myOrderDetailWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void finishWorkResponse(CommonResponse commonResponse) {
        myTaskDetailWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myTaskDetailWindow.showToast("完成任务，请等待结算");
                myTaskDetailWindow.hideWindow(true);
                myTaskWindow.refreshMyTask();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myTaskDetailWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myTaskDetailWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void cancelTaskResponse(CommonResponse commonResponse) {
        myOrderDetailWindow.response();
        if (commonResponse == null) {
            return;
        }
        int code = commonResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                myOrderDetailWindow.showToast("取消成功");
                myOrderDetailWindow.hideWindow(true);
                myTaskWindow.refreshMyOrder();
                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(myOrderDetailWindow);
                break;
            case ResponseCode.RESPONSE_110:
                myOrderDetailWindow.showToast(commonResponse.getDetail());
                break;
        }
    }

    @Override
    public void alipayResponse(AlipayResponse alipayResponse) {
        if (alipayResponse == null) {
            return;
        }
        int code = alipayResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:

                break;
            case ResponseCode.RESPONSE_2001:
                againLogin(taskPublishWindow);
                break;
            case ResponseCode.RESPONSE_110:
                taskPublishWindow.showToast(alipayResponse.getDetail());
                break;
        }
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

    @Override
    public void taskPublishResponse(TaskPublishResponse taskPublishResponse) {
        taskPublishWindow.dismissProgressDialog();
        if (taskPublishResponse == null) {
            return;
        }
        int code = taskPublishResponse.getCode();
        switch (code) {
            case ResponseCode.RESPONSE_200:
                final String payInfo = taskPublishResponse.getData();
                if (payInfo.equals("")) {
                    taskPublishWindow.showToast("发单成功");
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
                taskPublishWindow.showToast(taskPublishResponse.getDetail());
                break;
        }
    }
}
