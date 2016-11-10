package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.AssnActiPublishCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnActiPublishRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class AssnActiPublishPresenter extends BasePresenter implements AssnActiPublishCallback.RequestCallback {

    private AssnActiPublishCallback.ResponseCallback callback;

    public AssnActiPublishPresenter(AssnActiPublishCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestUploadImageActi(MultipartBody multipartBody, TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().uploadImageActiRequest(multipartBody, data), ImgUrlResponse.class, new ObservableCallback<ImgUrlResponse>() {
            @Override
            public void onSuccess(ImgUrlResponse response) {
                if (response.getCode() == ResponseCode.RESPONSE_200) {
                    callback.responseUploadImageActi(response);
                }
            }
        });
    }

    @Override
    public void requestAssnActiPublish(AssnActiPublishRequest request) {
        execute(ApiManager.getAssnService().assnActiPublishRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnActiPublish(response);
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
