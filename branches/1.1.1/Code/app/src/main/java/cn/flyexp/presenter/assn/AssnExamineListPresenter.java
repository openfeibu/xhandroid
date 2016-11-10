package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.AssnExamineListCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnListResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnExamineListPresenter extends BasePresenter implements AssnExamineListCallback.RequestCallback {

    private AssnExamineListCallback.ResponseCallback callback;

    public AssnExamineListPresenter(AssnExamineListCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAssnExamineList(AssnExamineListRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnExamineListRequest(data), AssnExamineListResponse.class, new ObservableCallback<AssnExamineListResponse>() {
            @Override
            public void onSuccess(AssnExamineListResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnExamineList(response);
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
