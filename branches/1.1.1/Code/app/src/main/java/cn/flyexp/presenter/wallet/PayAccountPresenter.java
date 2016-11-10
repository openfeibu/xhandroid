package cn.flyexp.presenter.wallet;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.PayAccountCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class PayAccountPresenter extends BasePresenter implements PayAccountCallback.RequestCallback {

    private PayAccountCallback.ResponseCallback callback;

    public PayAccountPresenter(PayAccountCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAlipayInfo(TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getWalletService().alipayInfoRequest(data), AlipayInfoResponse.class, new ObservableCallback<AlipayInfoResponse>() {
            @Override
            public void onSuccess(AlipayInfoResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAlipayInfo(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });

    }
}
