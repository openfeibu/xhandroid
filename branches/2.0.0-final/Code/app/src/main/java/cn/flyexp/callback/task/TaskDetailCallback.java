package cn.flyexp.callback.task;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskClaimRequest;

/**
 * Created by tanxinye on 2016/11/5.
 */
public interface TaskDetailCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestTaskClaim(TaskClaimRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseTaskClaim(BaseResponse response);
    }

}
