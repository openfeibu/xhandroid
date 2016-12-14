package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.MyAssnResponse;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/11/3.
 */
public interface MyAssnCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestMyAssn(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseMyAssn(MyAssnResponse response);
    }

}
