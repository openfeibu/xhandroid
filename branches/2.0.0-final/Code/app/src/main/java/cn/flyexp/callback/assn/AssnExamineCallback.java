package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnExamineRequest;
import cn.flyexp.entity.BaseResponse;

/**
 * Created by tanxinye on 2016/11/3.
 */
public interface AssnExamineCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAssnExamine(AssnExamineRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAssnExamine(BaseResponse response);
    }

}
