package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnListRequest;
import cn.flyexp.entity.AssnListResponse;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.PageRequest;

/**
 * Created by tanxinye on 2016/11/1.
 */
public interface AssnListCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAssnActivity(PageRequest request);

        void requestAssnList(AssnListRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAssnActivity(AssnActivityResponse response);

        void responseAssnList(AssnListResponse response);
    }
}
