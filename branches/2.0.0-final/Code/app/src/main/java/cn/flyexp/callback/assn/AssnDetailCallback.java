package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;

/**
 * Created by tanxinye on 2016/11/2.
 */
public interface AssnDetailCallback  {

    interface RequestCallback extends BaseRequestCallback {
        void requestAssnDetail(AssnDetailRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAssnDetail(AssnDetailResponse response);
    }
}
