package cn.flyexp.callback.task;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DataResponse;
import cn.flyexp.entity.TaskPublishRequest;

/**
 * Created by tanxinye on 2016/11/8.
 */
public interface TaskPublishCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestTaskPublish(TaskPublishRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseTaskPublish(DataResponse response);
    }

}
