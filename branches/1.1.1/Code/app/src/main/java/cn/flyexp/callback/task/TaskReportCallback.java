package cn.flyexp.callback.task;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskReportRequest;

/**
 * Created by tanxinye on 2016/11/6.
 */
public interface TaskReportCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestTaskReport(TaskReportRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseTaskReport(BaseResponse response);
    }
}
