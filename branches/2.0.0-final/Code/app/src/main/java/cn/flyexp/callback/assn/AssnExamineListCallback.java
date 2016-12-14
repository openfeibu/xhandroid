package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;

/**
 * Created by tanxinye on 2016/11/3.
 */
public interface AssnExamineListCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAssnExamineList(AssnExamineListRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAssnExamineList(AssnExamineListResponse response);
    }

}
