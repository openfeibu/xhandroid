package cn.flyexp.callback.main;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.ExtraResponse;
import cn.flyexp.entity.TaskResponse;

/**
 * Created by tanxinye on 2016/10/27.
 */
public interface HomeCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAd();

        void requestExtra();

        void requestRecommendOrder();

        void requestHotActivity();
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAd(AdResponse response);

        void responseExtra(ExtraResponse response);

        void responseRecommendOrder(TaskResponse response);

        void responseHotActivity(AssnActivityResponse response);
    }

}
