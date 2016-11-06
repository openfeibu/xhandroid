package cn.flyexp.mvc.campus;

import cn.flyexp.entity.AdRequest;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/7/25 0025.
 */
public class CampusModel {

    private CampusModelCallBack callBack;
    private NetWorkService service;

    public CampusModel(CampusModelCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void getAdList(AdRequest adRequest) {
        if (adRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(adRequest, AdRequest.class);
        Call<EncodeData> call = service.getAdList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AdResponse adResponse = GsonUtil.fromJson(response.body().getData(), AdResponse.class);
                    callBack.AdListResponse(adResponse);
                } else {
                    callBack.AdListResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.AdListResponse(null);
            }
        });

    }

    public void getHotAssnActivity() {
        String data = GsonUtil.toJson("");
        Call<EncodeData> call = service.getHotActivities(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnActivityResponse assnActivityResponse = GsonUtil.fromJson(response.body().getData(), AssnActivityResponse.class);
                    callBack.assnActivityResponse(assnActivityResponse);
                } else {
                    callBack.assnActivityResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnActivityResponse(null);
            }
        });

    }

    public void getHotAssnInfo() {
        String data = GsonUtil.toJson("");
        Call<EncodeData> call = service.getHotInformation(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    AssnInfoResponse assnInfoResponse = GsonUtil.fromJson(response.body().getData(), AssnInfoResponse.class);
                    callBack.assnInfoResponse(assnInfoResponse);
                } else {
                    callBack.assnInfoResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.assnInfoResponse(null);
            }
        });

    }
}
