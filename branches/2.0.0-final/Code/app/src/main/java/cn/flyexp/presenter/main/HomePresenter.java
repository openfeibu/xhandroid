package cn.flyexp.presenter.main;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.main.HomeCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class HomePresenter extends BasePresenter implements HomeCallback.RequestCallback {

    private HomeCallback.ResponseCallback callback;

    public HomePresenter(HomeCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestAd() {
        String data = GsonUtil.getInstance().encodeJson("");
        execute(ApiManager.getOtherService().adRequest(data), AdResponse.class, new ObservableCallback<AdResponse>() {
            @Override
            public void onSuccess(AdResponse response) {
                if (response.getCode() == ResponseCode.RESPONSE_200) {
                    callback.responseAd(response);
                }
            }

        });
    }

    @Override
    public void requestRecommendTask() {
        String data = GsonUtil.getInstance().encodeJson("");
        execute(ApiManager.getTaskService().recommendTaskRequest(data), TaskResponse.class, new ObservableCallback<TaskResponse>() {
            @Override
            public void onSuccess(TaskResponse response) {
                if(response.getCode() == ResponseCode.RESPONSE_200){
                    callback.responseRecommendTask(response);
                }
            }
        });
    }

    @Override
    public void requestHotActivity() {
        String data = GsonUtil.getInstance().encodeJson("");
        execute(ApiManager.getAssnService().hotActiRequest(data), AssnActivityResponse.class, new ObservableCallback<AssnActivityResponse>() {
            @Override
            public void onSuccess(AssnActivityResponse response) {
                if(response.getCode() == ResponseCode.RESPONSE_200){
                    callback.responseHotActivity(response);
                }
            }

        });
    }
}
