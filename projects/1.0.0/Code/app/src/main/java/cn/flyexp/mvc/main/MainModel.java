package cn.flyexp.mvc.main;

import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.NoResponse;
import cn.flyexp.entity.UserCountRequset;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/8/20 0020.
 */
public class MainModel {

    private MainModelCallBack callBack;
    private NetWorkService service;

    public MainModel(MainModelCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void campusNo() {
        String data = GsonUtil.toJson("");
        Call<EncodeData> call = service.campusNo(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    NoResponse noResponse = GsonUtil.fromJson(response.body().getData(), NoResponse.class);
                    callBack.campusNoResponse(noResponse);
                    LogUtil.error(getClass(), noResponse.getCode() + " no");
                } else {
                    callBack.campusNoResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.campusNoResponse(null);
            }
        });
    }

    public void userCount(UserCountRequset userCountRequset) {
        if (userCountRequset == null) {
            return;
        }
//        Call<EncodeData> call = service.userCount(userCountRequset);
//        call.enqueue(new Callback<EncodeData>() {
//            @Override
//            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
//            }
//
//            @Override
//            public void onFailure(Call<EncodeData> call, Throwable t) {
//            }
//        });
    }
}
