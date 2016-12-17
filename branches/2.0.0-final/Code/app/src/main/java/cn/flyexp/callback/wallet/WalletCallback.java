package cn.flyexp.callback.wallet;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.entity.WalletInfoResponse;

/**
 * Created by tanxinye on 2016/10/25.
 */
public interface WalletCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestWalletInfo(TokenRequest request);

        void requestAlipayInfo(TokenRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseWalletInfo(WalletInfoResponse response);

        void responseAlipayInfo(AlipayInfoResponse response);
    }

}
