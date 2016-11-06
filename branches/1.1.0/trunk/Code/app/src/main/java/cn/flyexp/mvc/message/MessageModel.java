package cn.flyexp.mvc.message;

import cn.flyexp.entity.EncodeData;
import cn.flyexp.entity.MessageRequest;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.net.NetWork;
import cn.flyexp.net.NetWorkService;
import cn.flyexp.util.GsonUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by txy on 2016/6/6.
 */
public class MessageModel {

    private MessageModelCallBack callBack;
    private NetWorkService service;

    public MessageModel(MessageModelCallBack callBack) {
        this.callBack = callBack;
        service = NetWork.getInstance().getService();
    }

    public void getMessageList(MessageRequest messageRequest) {
        if (messageRequest == null) {
            return;
        }
        String data = GsonUtil.toJson(messageRequest, MessageRequest.class);
        Call<EncodeData> respond = service.getMessageList(data);
        respond.enqueue(new Callback<EncodeData>() {
            @Override
            public void onResponse(Call<EncodeData> call, Response<EncodeData> response) {
                if (response.body() != null && response.isSuccess()) {
                    MessageResponse messageResponse =  GsonUtil.fromJson(response.body().getData(), MessageResponse.class);
                    callBack.getMessageListResponse(messageResponse);
                } else {
                    callBack.getMessageListResponse(null);
                }
            }

            @Override
            public void onFailure(Call<EncodeData> call, Throwable t) {
                callBack.getMessageListResponse(null);
            }
        });

    }
}
