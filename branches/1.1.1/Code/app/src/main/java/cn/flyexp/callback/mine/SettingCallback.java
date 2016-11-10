package cn.flyexp.callback.mine;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/28.
 */
public interface SettingCallback {

    interface RequestCallback extends BaseRequestCallback{
        void requestLogout(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback{
        void responseLogout(BaseResponse response);
    }
}
