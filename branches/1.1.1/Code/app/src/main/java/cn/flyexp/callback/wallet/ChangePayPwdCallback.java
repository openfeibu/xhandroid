package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeAlipayRequest;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface ChangePayPwdCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestChangePayPwd(ChangePayPwdRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseChangePayPwd(BaseResponse response);
    }

}
