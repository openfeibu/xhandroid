package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.MyAssnActivityRequest;

/**
 * Created by tanxinye on 2016/11/3.
 */
public interface MyAssnActivityCallback  {

    interface RequestCallback extends BaseRequestCallback {
        void requestMyAssnActivity(MyAssnActivityRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseMyAssnActivity(AssnActivityResponse response);
    }
}
