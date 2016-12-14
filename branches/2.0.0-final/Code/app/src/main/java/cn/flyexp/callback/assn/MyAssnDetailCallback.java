package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.entity.AssnMemberListResponse;
import cn.flyexp.entity.AssnMemberListRequest;
import cn.flyexp.entity.AssnQuitRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DelelteMemberRequest;
import cn.flyexp.entity.MemberManageRequest;

/**
 * Created by tanxinye on 2016/11/3.
 */
public interface MyAssnDetailCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestMyAssnDetail(AssnDetailRequest request);

        void requestAssnMemberList(AssnMemberListRequest request);

        void requestDeleteMember(DelelteMemberRequest request);

        void requestMemberManage(MemberManageRequest request);

        void requestAssnQuit(AssnQuitRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseMyAssnDetail(AssnDetailResponse response);

        void responseAssnMemberList(AssnMemberListResponse response);

        void responseDeleteMember(BaseResponse response);

        void responseMemberManage(BaseResponse response);

        void responseAssnQuit(BaseResponse response);
    }

}
