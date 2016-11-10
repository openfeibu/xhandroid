package cn.flyexp.presenter.wallet;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.WalletDetailCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.WalletDetailRequest;
import cn.flyexp.entity.WalletDetailResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class WalletDetailPresenter extends BasePresenter implements WalletDetailCallback.RequestCallback {

    private WalletDetailCallback.ResponseCallback callback;

    public WalletDetailPresenter(WalletDetailCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestWalletDetail(WalletDetailRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getWalletService().walletDetailRequest(data), WalletDetailResponse.class, new ObservableCallback<WalletDetailResponse>() {
            @Override
            public void onSuccess(WalletDetailResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseWalletDetail(response);
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
