package cn.flyexp.window.task;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.ShareHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class TaskDetailWindow extends BaseWindow implements TaskDetailCallback.ResponseCallback {

    @InjectView(R.id.tv_nickname)
    TextView tvNickname;
    @InjectView(R.id.tv_state)
    TextView tvState;
    @InjectView(R.id.tv_fee)
    TextView tvFee;
    @InjectView(R.id.tv_destination)
    TextView tvDestination;
    @InjectView(R.id.tv_description)
    TextView tvDescription;
    @InjectView(R.id.tasklayout)
    View taskLayout;
    @InjectView(R.id.btn_claim)
    Button btnClaim;

    private TaskResponse.TaskResponseData data;
    private PopupWindow picPopupWindow;
    private TaskDetailPresenter taskDetailPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_task_detail;
    }

    public TaskDetailWindow(Bundle bundle) {
        data = (TaskResponse.TaskResponseData) bundle.getSerializable("taskdetail");
        taskDetailPresenter = new TaskDetailPresenter(this);

        initView();
    }

    private void initView() {
        tvNickname.setText(String.format(getResources().getString(R.string.task_sender), data.getNickname()));
        tvFee.setText(String.format(getResources().getString(R.string.hint_task_money), data.getFee()));
        tvDestination.setText(data.getDestination());
        tvDescription.setText(data.getDescription());
        tvState.setText(tranfStateText(data.getStatus()));

        View popShareLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_share, null);
        popShareLayout.findViewById(R.id.tv_qq).setOnClickListener(shareOnClickListener);
        popShareLayout.findViewById(R.id.tv_wxf).setOnClickListener(shareOnClickListener);
        popShareLayout.findViewById(R.id.tv_wxq).setOnClickListener(shareOnClickListener);
        picPopupWindow = new PopupWindow(popShareLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        picPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        picPopupWindow.setFocusable(true);
        picPopupWindow.setOutsideTouchable(true);
        picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);

        toggleHideButton();
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

    private String tranfStateText(String status) {
        if (TextUtils.isEmpty(status)) {
            return getResources().getString(R.string.dialog_backfire);
        }
        String str = "";
        if (status.equals("new")) {
            str = getResources().getString(R.string.task_state_new);
        } else if (status.equals("finish")) {
            str = getResources().getString(R.string.task_state_finish);
        } else if (status.equals("accepted")) {
            str = getResources().getString(R.string.task_state_accepted);
        } else if (status.equals("completed")) {
            str = getResources().getString(R.string.task_state_completed);
        }
        return str;
    }

    private OnClickListener shareOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            picPopupWindow.dismiss();
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
                picPopupWindow.showAtLocation(taskLayout, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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
