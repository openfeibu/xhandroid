package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.MyAssnActivityCallback;
import cn.flyexp.callback.assn.MyAssnCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class MyAssnActivityPresenter extends BasePresenter implements MyAssnActivityCallback.RequestCallback {

    private MyAssnActivityCallback.ResponseCallback callback;

    public MyAssnActivityPresenter(MyAssnActivityCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMyAssnActivity(MyAssnActivityRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().myAssnActivityRequest(data), AssnActivityResponse.class, new ObservableCallback<AssnActivityResponse>() {
            @Override
            public void onSuccess(AssnActivityResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyAssnActivity(response);
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
