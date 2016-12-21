package cn.flyexp.presenter.topic;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.topic.MyTopicCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.TopicListRequest;
import cn.flyexp.entity.TopicListResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/21.
 */
public class MyTopicPresenter extends BasePresenter implements MyTopicCallback.RequestCallback {

    private MyTopicCallback.ResponseCallback callback;

    public MyTopicPresenter(MyTopicCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMyTopicList(TopicListRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getTopicService().myTopicListRequest(data), TopicListResponse.class, new ObservableCallback<TopicListResponse>() {
            @Override
            public void onSuccess(TopicListResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyTopicList(response);
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }
}
