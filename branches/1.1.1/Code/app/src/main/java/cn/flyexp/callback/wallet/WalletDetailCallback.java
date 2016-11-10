package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface WalletDetailCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestWalletDetail(WalletDetailRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseWalletDetail(WalletDetailResponse response);
    }

}
