package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.AssnNoticePublishCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnNoticePublishRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnNoticePublishPresenter extends BasePresenter implements AssnNoticePublishCallback.RequestCallback {

    private AssnNoticePublishCallback.ResponseCallback callback;

    public AssnNoticePublishPresenter(AssnNoticePublishCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAssnNoticePublish(AssnNoticePublishRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnNoticePublishRequest(data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnNoticePublish(response);
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
