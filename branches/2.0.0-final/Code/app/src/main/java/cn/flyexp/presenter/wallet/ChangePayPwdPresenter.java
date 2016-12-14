package cn.flyexp.presenter.wallet;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.ChangePayPwdCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangePayPwdRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class ChangePayPwdPresenter extends BasePresenter implements ChangePayPwdCallback.RequestCallback {

    private ChangePayPwdCallback.ResponseCallback callback;

    public ChangePayPwdPresenter(ChangePayPwdCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestChangePayPwd(ChangePayPwdRequest request) {
        execute(ApiManager.getWalletService().changePayPwdRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseChangePayPwd(response);
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
