package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface PayAccountCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAlipayInfo(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAlipayInfo(AlipayInfoResponse response);
    }

}
