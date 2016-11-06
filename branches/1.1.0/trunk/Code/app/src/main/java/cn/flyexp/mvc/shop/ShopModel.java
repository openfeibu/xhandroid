package cn.flyexp.mvc.shop;

import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.ShopRequest;
import cn.flyexp.entity.ShopResponse;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/8/2 0002.
 */
public class ShopModel {

    private ShopModeCallBack callBack;
    private NetWorkService service;

    public ShopModel(ShopModeCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void getShopList(ShopRequest shopRequest) {
        if (shopRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(shopRequest, ShopRequest.class);
        Call<EncodeData> call = service.getShopList(data);
        call.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    ShopResponse shopResponse = (ShopResponse) GsonUtil.fromJson(response.body().getData(), ShopResponse.class);
                    callBack.shopListResponse(shopResponse);
                } else {
                    callBack.shopListResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.shopListResponse(null);
            }
        });
    }
}
