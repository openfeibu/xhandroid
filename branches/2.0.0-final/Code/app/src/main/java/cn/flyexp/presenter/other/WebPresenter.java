package cn.flyexp.presenter.other;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.other.WebCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.WebUrlRequest;
import cn.flyexp.entity.WebUrlResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/25.
 */
public class WebPresenter extends BasePresenter implements WebCallback.RequestCallback {

    private WebCallback.ResponseCallback callback;

    public WebPresenter(WebCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestWebUrl(final WebUrlRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getOtherService().webUrlRequest(data), WebUrlResponse.class, new ObservableCallback<WebUrlResponse>() {
            @Override
            public void onSuccess(WebUrlResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseWebUrl(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }
}
