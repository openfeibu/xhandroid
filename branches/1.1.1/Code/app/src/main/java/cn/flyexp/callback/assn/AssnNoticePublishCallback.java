package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.BaseResponse;

/**
 * Created by tanxinye on 2016/11/3.
 */
public interface AssnNoticePublishCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAssnNoticePublish(AssnNoticePublishRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAssnNoticePublish(BaseResponse response);
    }

}
