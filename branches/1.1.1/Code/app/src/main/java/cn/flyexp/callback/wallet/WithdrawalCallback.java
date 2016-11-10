package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.entity.WithdrawalRequest;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface WithdrawalCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestWithdrawal(WithdrawalRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseWithdrawal(BaseResponse response);
    }

}
