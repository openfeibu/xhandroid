package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.MyAssnCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.MyAssnResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class MyAssnPresenter extends BasePresenter implements MyAssnCallback.RequestCallback {

    private MyAssnCallback.ResponseCallback callback;

    public MyAssnPresenter(MyAssnCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMyAssn(TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().myAssnRequest(data), MyAssnResponse.class, new ObservableCallback<MyAssnResponse>() {
            @Override
            public void onSuccess(MyAssnResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyAssn(response);
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
