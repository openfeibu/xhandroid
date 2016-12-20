package cn.flyexp.callback.main;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;

/**
 * Created by tanxinye on 2016/10/27.
 */
public interface HomeCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestCheckUpdate(UpdateRequest request);

        void requestAd();

        void requestRecommendTask();

        void requestHotActivity();
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseCheckUpdate(UpdateResponse response);

        void responseAd(AdResponse response);

        void responseRecommendTask(TaskResponse response);

        void responseHotActivity(AssnActivityResponse response);
    }

}
