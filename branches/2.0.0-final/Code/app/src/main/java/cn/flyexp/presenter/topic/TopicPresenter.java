package cn.flyexp.presenter.topic;

import com.google.gson.Gson;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.topic.TopicCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by huangju on 2016/12/17.
 */

public class TopicPresenter extends BasePresenter implements TopicCallback.RequestCallback{

    private TopicCallback.ResponseCallback callback;

    public TopicPresenter(TopicCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestTopicList(TopicListRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTopicService().topicListRequest(data), TopicListResponse.class, new ObservableCallback<TopicListResponse>() {
            @Override
            public void onSuccess(TopicListResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseTopicList(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }
}
