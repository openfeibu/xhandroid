package cn.flyexp.callback.assn;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.AssnMemberListRequest;
import cn.flyexp.entity.AssnMemberListResponse;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.DelelteMemberRequest;
import cn.flyexp.entity.MemberManageRequest;

/**
 * Created by tanxinye on 2016/12/19.
 */
public interface AssnMemberCallback {

    interface RequestCallback extends BaseRequestCallback {
        void requestAssnMemberList(AssnMemberListRequest request);

        void requestDeleteMember(DelelteMemberRequest request);

        void requestMemberManage(MemberManageRequest request);
    }

    interface ResponseCallback extends BaseResponseCallback {
        void responseAssnMemberList(AssnMemberListResponse response);

        void responseDeleteMember(BaseResponse response);

        void responseMemberManage(BaseResponse response);
    }
}
