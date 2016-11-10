package cn.flyexp.window.user;

import cn.flyexp.R;
import cn.flyexp.callback.user.ChangePwdCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/29.
 */
public class ChangePwdWindow extends BaseWindow implements ChangePwdCallback.ResponseCallback {

    @Override
    protected int getLayoutId() {
        return R.layout.window_changepwd;
    }

    @Override
    public void responseChangePwd(BaseResponse response) {

    }
}
