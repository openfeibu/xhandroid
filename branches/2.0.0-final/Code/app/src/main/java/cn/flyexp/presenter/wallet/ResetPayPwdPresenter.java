package cn.flyexp.presenter.wallet;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.ResetPayPwdCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class ResetPayPwdPresenter extends BasePresenter implements ResetPayPwdCallback.RequestCallback {

    private ResetPayPwdCallback.ResponseCallback callback;

    public ResetPayPwdPresenter(ResetPayPwdCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestResetPayPwd(ResetPayPwdRequest request) {
        execute(ApiManager.getWalletService().resetPayPwdRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseResetPayPwd(response);
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

    @Override
    public void requestVerCode(TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getWalletService().resetPayPwdVerCodeRequest(data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

            }

        });
    }
}
