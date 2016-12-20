package cn.flyexp.window.task;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.task.TaskDetailCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.TaskClaimRequest;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.task.TaskDetailPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.ShareHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class TaskDetailWindow extends BaseWindow implements TaskDetailCallback.ResponseCallback {

    @InjectView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @InjectView(R.id.tv_nickname)
    TextView tvNickname;
    @InjectView(R.id.tv_fee)
    TextView tvFee;
    @InjectView(R.id.img_state)
    ImageView imgState;
    @InjectView(R.id.tv_destination)
    TextView tvDestination;
    @InjectView(R.id.tv_description)
    TextView tvDescription;
    @InjectView(R.id.btn_claim)
    Button btnClaim;

    private TaskResponse.TaskResponseData data;
    private TaskDetailPresenter taskDetailPresenter;
    private PopupWindow popupWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.window_task_detail;
    }

    public TaskDetailWindow(Bundle bundle) {
        data = (TaskResponse.TaskResponseData) bundle.getSerializable("taskDetail");
        if (data == null) {
            showToast(R.string.data_unable);
            return;
        }
        taskDetailPresenter = new TaskDetailPresenter(this);
        initView();
    }

    private void initView() {
        tvNickname.setText(data.getNickname());
        SpannableStringBuilder feeStr = new SpannableStringBuilder("赏金： "
                + String.format(getResources().getString(R.string.format_task_money),
                String.valueOf(data.getFee())));
        feeStr.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_dark)),
                0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvFee.setText(feeStr);
        imgState.setImageDrawable(tranfStateDrawable(data.getStatus()));
        tvDestination.setText(String.format(getResources().getString(R.string.format_destination),
                String.valueOf(data.getDestination().trim())));
        tvDescription.setText(data.getDescription().trim());
        Glide.with(getContext()).load(data.getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imgAvatar);

        View popShareLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_share, null);
        popShareLayout.findViewById(R.id.tv_qq).setOnClickListener(shareOnClickListener);
        popShareLayout.findViewById(R.id.tv_wxf).setOnClickListener(shareOnClickListener);
        popShareLayout.findViewById(R.id.tv_wxq).setOnClickListener(shareOnClickListener);
        popupWindow = new PopupWindow(popShareLayout,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1f);
            }
        });

        toggleHideButton();
    }


    private Drawable tranfStateDrawable(String status) {
        Drawable drawable = getResources().getDrawable(R.mipmap.icon_task_end);
        if (TextUtils.isEmpty(status)) {
            return drawable;
        }
        if (TextUtils.equals(status, "new")) {
            drawable = getResources().getDrawable(R.mipmap.icon_task_notstarted);
        } else if (TextUtils.equals(status, "accepted")) {
            drawable = getResources().getDrawable(R.mipmap.icon_task_ongoing);
        } else if (TextUtils.equals(status, "completed")) {
            drawable = getResources().getDrawable(R.mipmap.icon_task_end);
        } else if (TextUtils.equals(status, "finish")) {
            drawable = getResources().getDrawable(R.mipmap.icon_task_bechecked);
        } else {
            drawable = getResources().getDrawable(R.mipmap.icon_task_end);
        }
        return drawable;
    }

    private void changeWindowAlpha(float v) {
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    @Override
    public void onResume() {
        toggleHideButton();
    }

    private void toggleHideButton() {
        String openid = SharePresUtil.getString(SharePresUtil.KEY_OPENID);
        if (openid.equals(data.getOpenid())) {
            btnClaim.setVisibility(GONE);
        } else {
            btnClaim.setVisibility(VISIBLE);
        }
    }

    private OnClickListener shareOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
            switch (view.getId()) {
                case R.id.tv_qq:
                    ShareHelper.shareQQ(getContext(), getResources().getString(R.string.share_task_title),
                            data.getDescription(), data.getShare_url(), null);
                    break;
                case R.id.tv_wxf:
                    ShareHelper.shareWX(getContext(), ShareHelper.SHARE_WX_FIREND, getResources().getString(R.string.share_task_title),
                            data.getDescription(), data.getShare_url(), null);
                    break;
                case R.id.tv_wxq:
                    ShareHelper.shareWX(getContext(), ShareHelper.SHARE_WX_CIRCLE, getResources().getString(R.string.share_task_title),
                            data.getDescription(), data.getShare_url(), null);
                    break;
            }
        }
    };

    @OnClick({R.id.img_back, R.id.img_share, R.id.tv_report, R.id.tv_taskStatement, R.id.btn_claim})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.img_share:
                popupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
                changeWindowAlpha(0.7f);
                break;
            case R.id.tv_report:
                Bundle bundle = new Bundle();
                bundle.putInt("oid", data.getOid());
                openWindow(WindowIDDefine.WINDOW_TASK_REPORT, bundle);
                break;
            case R.id.tv_taskStatement:
                WebBean webBean = new WebBean();
                webBean.setRequest(true);
                webBean.setTitle(getResources().getString(R.string.task_statement));
                webBean.setName("taskStatement");
                Bundle bundle2 = new Bundle();
                bundle2.putSerializable("webbean", webBean);
                openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle2);
                break;
            case R.id.btn_claim:
                final String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                    return;
                }
                int auth = SharePresUtil.getInt(SharePresUtil.KEY_AUTH);
                if (auth == 0) {
                    showToast(R.string.go_auth);
                    openWindow(WindowIDDefine.WINDOW_CERTIFITION);
                } else if (auth == 2) {
                    showToast(R.string.authing);
                } else if (auth == 1) {
                    DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_claim_task),
                            getContext().getResources().getString(R.string.hint_claim_task_confirm),
                            getContext().getResources().getString(R.string.hint_claim_task_cancel), new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    readyTaskClaim(token);
                                    dismissProgressDialog(sweetAlertDialog);
                                }
                            });
                }
                break;
        }
    }

    private void readyTaskClaim(String token) {
        TaskClaimRequest taskClaimRequest = new TaskClaimRequest();
        taskClaimRequest.setToken(token);
        taskClaimRequest.setOrder_id(data.getOid());
        taskDetailPresenter.requestTaskClaim(taskClaimRequest);
    }

    @Override
    public void responseTaskClaim(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_TASK_REFRESH);
        showToast(R.string.accpet_task_success);
        hideWindow(false);
        openWindow(WindowIDDefine.WINDOW_MYTASK);
    }
}
