package cn.flyexp.presenter.user;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.user.OthersInfoCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.OthersInfoRequest;
import cn.flyexp.entity.OthersInfoResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class OthersInfoPresenter extends BasePresenter implements OthersInfoCallback.RequestCallback {

    private OthersInfoCallback.ResponseCallback callback;

    public OthersInfoPresenter(OthersInfoCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestOthersInfo(OthersInfoRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getUserService().othersRequest(data), OthersInfoResponse.class, new ObservableCallback<OthersInfoResponse>() {
            @Override
            public void onSuccess(OthersInfoResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseOthersInfo(response);
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
