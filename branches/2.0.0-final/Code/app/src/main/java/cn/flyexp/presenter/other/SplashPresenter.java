package cn.flyexp.presenter.other;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.other.SplashCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ClienVerifyResponse;
import cn.flyexp.entity.ClientVerifyRequest;
import cn.flyexp.entity.CrashRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class SplashPresenter extends BasePresenter implements SplashCallback.RequestCallback {

    private SplashCallback.ResponseCallback callback;

    public SplashPresenter(SplashCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestClientVerify(ClientVerifyRequest request) {
        execute(ApiManager.getUserService().clientVerifyRequest(request), ClienVerifyResponse.class, new ObservableCallback<ClienVerifyResponse>() {

            @Override
            public void onSuccess(ClienVerifyResponse response) {
                callback.responseClientVerify(response);
            }

        });
    }

    @Override
    public void reqeustCrash(CrashRequest request) {
        execute(ApiManager.getOtherService().crashRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {

            @Override
            public void onSuccess(BaseResponse response) {
                callback.responseCrash();
            }

        });
    }
}
