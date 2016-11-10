package cn.flyexp.presenter.assn;

import cn.flyexp.api.ApiManager;
import cn.flyexp.callback.assn.MyAssnDetailCallback;
import cn.flyexp.constants.ResponseCode;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.entity.AssnMemberListRequest;
import cn.flyexp.entity.AssnMemberListResponse;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DelelteMemberRequest;
import cn.flyexp.entity.MemberManageRequest;
import cn.flyexp.presenter.BasePresenter;
import cn.flyexp.util.GsonUtil;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class MyAssnDetailPresenter extends BasePresenter implements MyAssnDetailCallback.RequestCallback {

    private MyAssnDetailCallback.ResponseCallback callback;

    public MyAssnDetailPresenter(MyAssnDetailCallback.ResponseCallback callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    public void requestMyAssnDetail(AssnDetailRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnDetailRequest(data), AssnDetailResponse.class, new ObservableCallback<AssnDetailResponse>() {
            @Override
            public void onSuccess(AssnDetailResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseMyAssnDetail(response);
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

    @Override
    public void requestAssnQuit(AssnQuitRequest request) {
        String data = GsonUtil.getInstance().encodeJson(request);
        execute(ApiManager.getAssnService().assnQuitRequest(data), BaseResponse.class, new ObservableCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                switch (response.getCode()) {
                    case ResponseCode.RESPONSE_200:
                        callback.responseAssnQuit(response);
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
