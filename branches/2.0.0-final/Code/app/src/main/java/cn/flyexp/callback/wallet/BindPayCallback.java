package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface BindPayCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestBindAlipay(BindAlipayRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseBindAlipay(BaseResponse response);
    }

}
