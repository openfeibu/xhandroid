package cn.flyexp.presenter.mine;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.mine.ShareCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class SharePresenter extends BasePresenter implements ShareCallback.RequestCallback {

    private ShareCallback.ResponseCallback callback;

    public SharePresenter(ShareCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestShare(TokenRequest request) {
        execute(ApiManager.getOtherService().shareRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getCode() == ResponseCode.RESPONSE_200) {
                    callback.responseShare(response);
                }
            }
        });
    }
}
