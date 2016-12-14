package cn.flyexp.presenter.mine;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.mine.MineCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.MyInfoResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class MinePresenter extends BasePresenter implements MineCallback.RequestCallback {

    private MineCallback.ResponseCallback callback;

    public MinePresenter(MineCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMyInfo(TokenRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getUserService().myInfoRequest(data), MyInfoResponse.class, new ObservableCallback<MyInfoResponse>() {
            @Override
            public void onSuccess(MyInfoResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyInfo(response);
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
