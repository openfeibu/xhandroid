package cn.flyexp.callback.task;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskCancelRequest;
import cn.flyexp.entity.TaskCompleteRequest;
import cn.flyexp.entity.TaskFinishRequest;

/**
 * Created by tanxinye on 2016/11/6.
 */
public interface MyTaskDetailCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestTaskCancel(TaskCancelRequest request);

        void requestTaskFinish(TaskFinishRequest request);

        void requestTaskComplete(TaskCompleteRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseTaskCancel(BaseResponse response);

        void responseTaskFinish(BaseResponse response);

        void responseTaskComplete(BaseResponse response);
    }

}
