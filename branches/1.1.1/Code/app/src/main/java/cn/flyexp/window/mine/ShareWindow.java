package cn.flyexp.window.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import butterknife.OnClick;
import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.callback.mine.ShareCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.presenter.mine.SharePresenter;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.ShareHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.flyexp.wxapi.Util;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class ShareWindow extends BaseWindow implements ShareCallback.ResponseCallback {

    private SharePresenter sharePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_share;
    }

    public ShareWindow() {
        sharePresenter = new SharePresenter(this);
    }

    @OnClick({R.id.img_back, R.id.tv_qq, R.id.tv_wxf, R.id.tv_wxq})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_qq:
                shareQQ();
                break;
            case R.id.tv_wxf:
                shareWX(ShareHelper.SHARE_WX_FIREND);
                break;
            case R.id.tv_wxq:
                shareWX(ShareHelper.SHARE_WX_CIRCLE);
                break;
        }
    }

    private void readyShare() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            sharePresenter.requestShare(new TokenRequest(token));
        }
    }

    private void shareQQ() {
        ShareHelper.shareQQ(getContext(), getResources().getString(R.string.share_qq_title),
                getResources().getString(R.string.share_qq_summary),
                getResources().getString(R.string.share_qq_target_url),
                new ShareHelper.ShareLinstener() {
                    @Override
                    public void onShareSuccess() {
                        LogUtil.e("onShareSuccess");
                        readyShare();
                    }
                });
    }


    private void shareWX(int scene) {
        ShareHelper.shareWX(getContext(), scene, getResources().getString(R.string.share_qq_title),
                getResources().getString(R.string.share_qq_summary),
                getResources().getString(R.string.share_qq_target_url),
                new ShareHelper.ShareLinstener() {
                    @Override
                    public void onShareSuccess() {
                        LogUtil.e("onShareSuccess");
                        readyShare();
                    }
                });
    }

    @Override
    public void responseShare(BaseResponse response) {
        showToast(R.string.share_success);
    }
}
