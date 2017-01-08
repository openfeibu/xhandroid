package cn.flyexp.callback.wallet;

import cn.flyexp.R;
import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface ResetPayPwdCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestResetPayPwd(ResetPayPwdRequest request);

        void requestVerCode(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseResetPayPwd(BaseResponse response);
        void responseVercodeSuccess();
        void responseVercodeFailure();
    }

}
