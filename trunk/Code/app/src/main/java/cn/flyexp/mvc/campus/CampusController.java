package cn.flyexp.mvc.campus;

import android.os.Message;

import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.LastTimeRequest;
import cn.flyexp.entity.NoResponse;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.entity.RecommendOrderRequest;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.MessageIDDefine;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by zlk on 2016/3/27.
 */
public class CampusController extends AbstractController implements CampusViewCallBack, NotifyManager.Notify {

    public CampusWindow campusWindow;
    private CampusModel campusModel;

    public CampusController() {
        campusModel = new CampusModel();
        campusWindow = new CampusWindow(this);
        getAd();
        getCampusNo();
        getHotAssnActivity();
        RecommendOrderRequest recommendOrderRequest = new RecommendOrderRequest();
        recommendOrderRequest.setNumber(2);
        recommendOrder(recommendOrderRequest);
    }

    @Override
    protected void handleMessage(Message mes) {
    }

    @Override
    protected void registerMessages() {
        registerNotify(NotifyIDDefine.ON_ACTIVITY_DESTROY, this);
        registerNotify(NotifyIDDefine.NOTIFY_AD_REFRESH, this);
        registerNotify(NotifyIDDefine.NOTIFY_EXTRA_REFRESH, this);
        registerNotify(NotifyIDDefine.NOTIFY_HOTACT_REFRESH, this);
    }


    @Override
    public void assnEnter() {
        sendMessage(MessageIDDefine.ASSN_OPEN);
    }

    @Override
    public void recomTaskDetailEnter(OrderResponse.OrderResponseData orderResponseData) {
        Message message = Message.obtain();
        message.what = MessageIDDefine.TASK_DETAIL_OPEN;
        message.obj = orderResponseData;
        message.arg1 = -1;
        sendMessage(message);
    }

    @Override
    public void activityDetailEnter(AssnActivityResponse.AssnActivityResponseData
                                            assnActivityResponseData) {
        sendMessage(MessageIDDefine.ASSN_ACTIVITY_DETAIL_OPEN, assnActivityResponseData);
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.ON_ACTIVITY_DESTROY) {
            campusWindow.removeRunn();
        } else if (mes.what == NotifyIDDefine.NOTIFY_AD_REFRESH) {
            getAd();
        } else if (mes.what == NotifyIDDefine.NOTIFY_EXTRA_REFRESH) {
            getCampusNo();
        } else if (mes.what == NotifyIDDefine.NOTIFY_HOTACT_REFRESH) {
            getHotAssnActivity();
        }
    }

    @Override
    public void getCampusNo() {
        Call<EncodeData> call = service.campusNo(GsonUtil.toEncodeJson(""));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    NoResponse noResponse = GsonUtil.fromEncodeJson(response.body().getData(), NoResponse.class);
                    int code = noResponse.getCode();
                    if (code == ResponseCode.RESPONSE_200) {
                        campusWindow.responseNo(noResponse.getData());
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {

            }
        });
    }

    @Override
    public void recommendOrder(RecommendOrderRequest recommendOrderRequest) {
        Call<EncodeData> call = service.getRecommendOrders(GsonUtil.toEncodeJson(recommendOrderRequest));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    OrderResponse orderResponse = GsonUtil.fromEncodeJson(response.body().getData(), OrderResponse.class);
                    int code = orderResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            campusWindow.responseOrder(orderResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(orderResponse.getDetail());
                            break;

                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }


    @Override
    public void getAd() {
        Call<EncodeData> call = service.getAdList(GsonUtil.toEncodeJson(""));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AdResponse adResponse = GsonUtil.fromEncodeJson(response.body().getData(), AdResponse.class);
                    int code = adResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            campusWindow.adResponse(adResponse.getData());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
            }
        });
    }

    @Override
    public void getHotAssnActivity() {
        Call<EncodeData> call = service.getHotActivities(GsonUtil.toEncodeJson(""));
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnActivityResponse assnActivityResponse = GsonUtil.fromEncodeJson(response.body().getData(), AssnActivityResponse.class);
                    int code = assnActivityResponse.getCode();
                    switch (code) {
                        case ResponseCode.RESPONSE_200:
                            campusWindow.assnHotActiResponse(assnActivityResponse.getData());
                            break;
                        case ResponseCode.RESPONSE_110:
                            WindowHelper.showToast(assnActivityResponse.getDetail());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {

            }
        });
    }


}
