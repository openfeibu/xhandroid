package cn.flyexp.presenter.user;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.user.CertificationCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.CertificationRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class CertificationPresenter extends BasePresenter implements CertificationCallback.RequestCallback {

    private CertificationCallback.ResponseCallback callback;

    public CertificationPresenter(CertificationCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestCertification(MultipartBody multipartBody, CertificationRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getUserService().certificationRequest(multipartBody, data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseCertification(response);
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
