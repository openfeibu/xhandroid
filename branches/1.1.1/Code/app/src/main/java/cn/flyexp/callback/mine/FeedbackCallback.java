package cn.flyexp.callback.mine;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.entity.TokenRequest;

/**
 * Created by tanxinye on 2016/10/28.
 */
public interface FeedbackCallback {

    interface RequestCallback extends BaseRequestCallback{
        void requestFeedback(FeedbackRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback{
        void responseFeedback(BaseResponse response);
    }
}
