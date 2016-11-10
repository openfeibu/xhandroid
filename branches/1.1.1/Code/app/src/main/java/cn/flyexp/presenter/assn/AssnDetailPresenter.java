package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.AssnDetailCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/2.
 */
public class AssnDetailPresenter extends BasePresenter implements AssnDetailCallback.RequestCallback {

    private AssnDetailCallback.ResponseCallback callback;

    public AssnDetailPresenter(AssnDetailCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAssnDetail(AssnDetailRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnDetailRequest(data), AssnDetailResponse.class, new ObservableCallback<AssnDetailResponse>() {
            @Override
            public void onSuccess(AssnDetailResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnDetail(response);
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
