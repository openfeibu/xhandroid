package cn.flyexp.presenter.user;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.user.ResetPwdVercodeCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.ResetPwdResponse;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class ResetPwdVercodePresenter extends BasePresenter implements ResetPwdVercodeCallback.RequestCallback {

    private ResetPwdVercodeCallback.ResponseCallback callback;

    public ResetPwdVercodePresenter(ResetPwdVercodeCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestResetPwd(ResetPwdRequest request) {
        execute(ApiManager.getUserService().resetPwdRequest(request), ResetPwdResponse.class, new ObservableCallback<ResetPwdResponse>() {
            @Override
            public void onSuccess(ResetPwdResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseResetPwd(response);
                        break;
                    case ResponseCode.RESPONSE_2003:
                        callback.showDetail(R.string.phone_no_register);
                        break;
                    case ResponseCode.RESPONSE_2004:
                        callback.showDetail(R.string.veritycode_failure);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });
    }

    @Override
    public void requestVercode(SmsCodeRequest request) {
        execute(ApiManager.getUserService().resetPwdVercodeRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(response.getCode() == ResponseCode.RESPONSE_200){
                    callback.responseVercodeSuccess();
                }else{
                    callback.responseVercodeFailure();
                }
            }
        });
    }
}
