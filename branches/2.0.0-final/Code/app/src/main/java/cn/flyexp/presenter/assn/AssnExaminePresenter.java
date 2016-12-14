package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.AssnExamineCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnExamineRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnExaminePresenter extends BasePresenter implements AssnExamineCallback.RequestCallback {

    private AssnExamineCallback.ResponseCallback callback;

    public AssnExaminePresenter(AssnExamineCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAssnExamine(AssnExamineRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnExamineRequest(data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnExamine(response);
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
