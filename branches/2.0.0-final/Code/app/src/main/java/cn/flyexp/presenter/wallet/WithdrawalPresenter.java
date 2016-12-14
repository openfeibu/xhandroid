package cn.flyexp.presenter.wallet;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.WithdrawalCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.WithdrawalRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class WithdrawalPresenter extends BasePresenter implements WithdrawalCallback.RequestCallback {

    private WithdrawalCallback.ResponseCallback callback;

    public WithdrawalPresenter(WithdrawalCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestWithdrawal(WithdrawalRequest request) {
        execute(ApiManager.getWalletService().withdrawlRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseWithdrawal(response);
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
