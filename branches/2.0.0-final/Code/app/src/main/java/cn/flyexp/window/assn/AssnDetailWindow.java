package cn.flyexp.window.assn;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.assn.AssnDetailCallback;
import cn.flyexp.entity.AssnDetailRequest;
import cn.flyexp.entity.AssnDetailResponse;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.assn.AssnDetailPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/2.
 */
public class AssnDetailWindow extends BaseWindow implements AssnDetailCallback.ResponseCallback {

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.tv_assnname)
    TextView tvAssnName;
    @InjectView(R.id.tv_detail)
    TextView tvDetail;
    @InjectView(R.id.tv_intro)
    TextView tvIntro;
    @InjectView(R.id.btn_join)
    Button btnJoin;

    private AssnDetailPresenter assnDetailPresenter;
    private final SweetAlertDialog assnDetailDialog;
    private final int aid;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_detail;
    }

    public AssnDetailWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        String aname = bundle.getString("aname");
        assnDetailPresenter = new AssnDetailPresenter(this);
        assnDetailDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));

        tvTitle.setText(aname);
        AssnDetailRequest assnDetailRequest = new AssnDetailRequest();
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            assnDetailRequest.setToken(token);
        }
        assnDetailRequest.setAssociation_id(aid);
        assnDetailPresenter.requestAssnDetail(assnDetailRequest);
        assnDetailDialog.show();
    }

    @OnClick({R.id.img_back, R.id.btn_join})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_join:
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("aid", aid);
                    openWindow(WindowIDDefine.WINDOW_ASSN_JOIN, bundle);
                }
                break;
        }
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(assnDetailDialog);
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(assnDetailDialog);
    }

    @Override
    public void responseAssnDetail(AssnDetailResponse response) {
        AssnDetailResponse.AssnDetailResponseData responseData = response.getData();
        tvAssnName.setText(responseData.getAname());
        tvIntro.setText(responseData.getIntroduction());
        tvDetail.setText(String.format(getResources().getString(R.string.assnlist_detail), responseData.getMember_number(), responseData.getActivity_count(), responseData.getLabel()));
        Glide.with(getContext()).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(imgAvatar);
        if (responseData.getUid() == 0) {
            btnJoin.setVisibility(VISIBLE);
        }
    }
}
