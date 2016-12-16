package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.assn.AssnListCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnListRequest;
import cn.flyexp.entity.AssnListResponse;
import cn.flyexp.entity.PageRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/1.
 */
public class AssnListPresenter extends BasePresenter implements AssnListCallback.RequestCallback {

    private AssnListCallback.ResponseCallback callback;

    public AssnListPresenter(AssnListCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAssnActivity(PageRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnActiRequest(data), AssnActivityResponse.class, new ObservableCallback<AssnActivityResponse>() {
            @Override
            public void onSuccess(AssnActivityResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnActivity(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }

    @Override
    public void requestAssnList(AssnListRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnListRequest(data), AssnListResponse.class, new ObservableCallback<AssnListResponse>() {
            @Override
            public void onSuccess(AssnListResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnList(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }
}
