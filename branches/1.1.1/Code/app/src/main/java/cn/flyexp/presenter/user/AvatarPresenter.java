package cn.flyexp.presenter.user;

import cn.flyexp.R;
import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.user.AvatarCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class AvatarPresenter extends BasePresenter implements AvatarCallback.RequestCallback {

    private AvatarCallback.ResponseCallback callback;

    public AvatarPresenter(AvatarCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestUploadAvatar(MultipartBody multipartBody, TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getUserService().uploadAvatarRequest(multipartBody, data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.showDetail(R.string.upload_success);
                        callback.responseUploadAvatar(response);
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
