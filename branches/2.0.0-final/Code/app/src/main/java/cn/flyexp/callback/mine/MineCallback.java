package cn.flyexp.callback.mine;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/27.
 */
public interface MineCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestMyInfo(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseMyInfo(MyInfoResponse response);
    }

}
