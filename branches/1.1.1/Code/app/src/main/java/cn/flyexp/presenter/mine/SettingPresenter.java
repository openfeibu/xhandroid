package cn.flyexp.presenter.mine;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.mine.SettingCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/28.
 */
public class SettingPresenter extends BasePresenter implements SettingCallback.RequestCallback {

    private SettingCallback.ResponseCallback callback;

    public SettingPresenter(SettingCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestLogout(TokenRequest request) {
        execute(ApiManager.getUserService().logoutRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getCode() == ResponseCode.RESPONSE_200) {
                    callback.responseLogout(response);
                }
            }
        });
    }
}
