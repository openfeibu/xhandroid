package cn.flyexp.presenter.wallet;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.SetPayPwdCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class SetPayPwdPresenter extends BasePresenter implements SetPayPwdCallback.RequestCallback {

    private SetPayPwdCallback.ResponseCallback callback;

    public SetPayPwdPresenter(SetPayPwdCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestSetPayPwd(SetPayPwdRequest request) {
        execute(ApiManager.getWalletService().setPayPwdRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseSetPayPwd(response);
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
