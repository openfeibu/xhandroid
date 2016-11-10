package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeAlipayRequest;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface ChangePayAccountCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestChangeAlipay(ChangeAlipayRequest request);

        void requestVercode(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseChangeAlipay(BaseResponse response);
    }

}
