package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.callback.assn.AssnMemberCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnMemberListRequest;
import cn.flyexp.entity.AssnMemberListResponse;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DelelteMemberRequest;
import cn.flyexp.entity.MemberManageRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/12/19.
 */
public class AssnMemberPresenter extends BasePresenter implements AssnMemberCallback.RequestCallback {

    private AssnMemberCallback.ResponseCallback callback;

    public AssnMemberPresenter(AssnMemberCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }


    @Override
    public void requestAssnMemberList(AssnMemberListRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnMemberListRequest(data), AssnMemberListResponse.class, new ObservableCallback<AssnMemberListResponse>() {
            @Override
            public void onSuccess(AssnMemberListResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnMemberList(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }

    @Override
    public void requestDeleteMember(DelelteMemberRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().deleteMemberRequest(data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseDeleteMember(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }

    @Override
    public void requestMemberManage(MemberManageRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().memberManageRequest(data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMemberManage(response);
                        break;
                    case ResponseCode.RESPONSE_2001:
                        callback.renewLogin();
                        break;
                    case ResponseCode.RESPONSE_110:
                        callback.showDetail(response.getDetail());
                        break;
                }
            }
        });
    }
}
