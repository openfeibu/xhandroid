package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface SetPayPwdCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestSetPayPwd(SetPayPwdRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseSetPayPwd(BaseResponse response);
    }

}
