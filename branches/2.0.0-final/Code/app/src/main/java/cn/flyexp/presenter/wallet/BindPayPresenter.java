package cn.flyexp.presenter.wallet;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.BindPayCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.BindAlipayRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class BindPayPresenter extends BasePresenter implements BindPayCallback.RequestCallback {

    private BindPayCallback.ResponseCallback callback;

    public BindPayPresenter(BindPayCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestBindAlipay(BindAlipayRequest request) {
        execute(ApiManager.getWalletService().bindAlipayRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseBindAlipay(response);
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
