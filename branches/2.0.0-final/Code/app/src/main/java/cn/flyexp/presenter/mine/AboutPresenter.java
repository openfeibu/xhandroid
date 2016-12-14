package cn.flyexp.presenter.mine;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.mine.AboutCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class AboutPresenter extends BasePresenter implements AboutCallback.RequestCallback {

    private AboutCallback.ResponseCallback callback;

    public AboutPresenter(AboutCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestUpdate(UpdateRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getOtherService().updateRequest(data), UpdateResponse.class, new ObservableCallback<UpdateResponse>() {
            @Override
            public void onSuccess(UpdateResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseUpdate(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }

        });

    }
}
