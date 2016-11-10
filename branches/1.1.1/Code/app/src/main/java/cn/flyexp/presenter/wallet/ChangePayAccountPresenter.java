package cn.flyexp.presenter.wallet;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.wallet.ChangePayAccountCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeAlipayRequest;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class ChangePayAccountPresenter extends BasePresenter implements ChangePayAccountCallback.RequestCallback {

    private ChangePayAccountCallback.ResponseCallback callback;

    public ChangePayAccountPresenter(ChangePayAccountCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestChangeAlipay(ChangeAlipayRequest request) {
        execute(ApiManager.getWalletService().changeAlipayRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.showDetail(R.string.change_success);
                        callback.responseChangeAlipay(response);
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
    public void requestVercode(TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getUserService().changeAlipayRequest(data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

            }
        });
    }
}
