package cn.flyexp.presenter.mine;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.mine.FeedbackCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.presenter.BasePresenter;

/**
 * Created by tanxinye on 2016/10/28.
 */
public class FeedbackPresenter extends BasePresenter implements FeedbackCallback.RequestCallback {

    private FeedbackCallback.ResponseCallback callback;

    public FeedbackPresenter(FeedbackCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestFeedback(FeedbackRequest request) {
        execute(ApiManager.getUserService().feedbackRequest(request), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response.getCode() == ResponseCode.RESPONSE_200) {
                    callback.responseFeedback(response);
                }
            }
        });
    }
}
