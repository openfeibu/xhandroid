package cn.flyexp.callback.task;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TokenPageRequest;

/**
 * Created by tanxinye on 2016/11/4.
 */
public interface MyTaskCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestMyTask(TokenPageRequest request);

        void requestMyOrder(TokenPageRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseMyTask(MyTaskResponse response);

        void responseMyOrder(MyTaskResponse response);
    }
}
