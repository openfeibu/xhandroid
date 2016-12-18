package cn.flyexp.callback.task;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.PageRequest;
import cn.flyexp.entity.TaskRequest;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/27.
 */
public interface TaskCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestTaskList(TaskRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseTaskList(TaskResponse response);
    }

}
