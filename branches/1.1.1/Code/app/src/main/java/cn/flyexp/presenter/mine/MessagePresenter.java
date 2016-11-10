package cn.flyexp.presenter.mine;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.user.MessageCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MessagePresenter extends BasePresenter implements MessageCallback.RequestCallback {

    private MessageCallback.ResponseCallback callback;

    public MessagePresenter(MessageCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMessage(MessageRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getUserService().messageRequest(data), MessageResponse.class, new ObservableCallback<MessageResponse>() {
            @Override
            public void onSuccess(MessageResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMessage(response);
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
