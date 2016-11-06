package cn.flyexp.mvc.message;

import cn.flyexp.entity.MessageResponse;

/**
 * Created by txy on 2016/7/25 0025.
 */
public interface MessageModelCallBack {

    public void getMessageListResponse(MessageResponse messageResponse);
}
