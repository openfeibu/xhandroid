package cn.flyexp.callback.user;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.ResetPwdResponse;
import cn.flyexp.entity.SmsCodeRequest;

/**
 * Created by tanxinye on 2016/10/24.
 */
public interface ResetPwdCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestResetPwd(ResetPwdRequest request);

        void requestVercode(SmsCodeRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseResetPwd(ResetPwdResponse response);
    }

}
