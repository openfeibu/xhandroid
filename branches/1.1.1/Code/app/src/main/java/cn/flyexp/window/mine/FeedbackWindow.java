package cn.flyexp.window.mine;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.mine.FeedbackCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.FeedbackRequest;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.mine.FeedbackPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/28.
 */
public class FeedbackWindow extends BaseWindow implements FeedbackCallback.ResponseCallback, TextWatcher {

    @InjectView(R.id.edt_content)
    EditText edtContent;
    @InjectView(R.id.edt_contact)
    EditText edtContact;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;
    @InjectView(R.id.btn_skip)
    Button btnSkip;
    @InjectView(R.id.img_back)
    ImageView imgBack;

    private String content;
    private FeedbackPresenter feedbackPresenter;
    private SweetAlertDialog feedbackDialog;
    private boolean isCrash;

    @Override
    protected int getLayoutId() {
        return R.layout.window_feedback;
    }

    public FeedbackWindow(Bundle bundle) {
        isCrash = bundle.getBoolean("isCrash");
        feedbackPresenter = new FeedbackPresenter(this);

        initView();
    }

    private void initView() {
        edtContent.addTextChangedListener(this);
        if (isCrash) {
            imgBack.setVisibility(GONE);
            btnSkip.setVisibility(VISIBLE);
            edtContent.setHint(R.string.hint_feedback_crash);
        } else {
            imgBack.setVisibility(VISIBLE);
            btnSkip.setVisibility(GONE);
            edtContent.setHint(R.string.hint_feedback);
        }
    }

    private boolean isBackInEdit() {
        String contact = edtContact.getText().toString().trim();
        if (TextUtils.isEmpty(content) && TextUtils.isEmpty(contact)) {
            return false;
        } else {
            DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_giveup_edit), getResources().getString(R.string.dialog_giveup), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                    hideWindow(true);
                }
            });
            return true;
        }
    }

    @OnClick({R.id.img_back, R.id.btn_confirm, R.id.btn_skip})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if (!isBackInEdit()) {
                    hideWindow(true);
                }
                break;
            case R.id.btn_skip:
                openWindow(WindowIDDefine.WINDOW_MAIN);
                break;
            case R.id.btn_confirm:
                String contact = edtContact.getText().toString().trim();
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                FeedbackRequest feedbackRequest = new FeedbackRequest();
                if (!TextUtils.isEmpty(token)) {
                    feedbackRequest.setToken(token);
                }
                feedbackRequest.setContent(content);
                feedbackRequest.setContact_way(contact);
                feedbackPresenter.requestFeedback(feedbackRequest);
                feedbackDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        content = edtContent.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            btnConfirm.setEnabled(false);
            btnConfirm.setAlpha(0.5f);
        } else {
            btnConfirm.setEnabled(true);
            btnConfirm.setAlpha(1f);
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(feedbackDialog);
        DialogHelper.showErrorDialog(getContext(), getResources().getString(R.string.request_failure));
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(feedbackDialog);
    }

    @Override
    public void responseFeedback(BaseResponse response) {
        if (isCrash) {
            openWindow(WindowIDDefine.WINDOW_MAIN);
        } else {
            hideWindow(true);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (isCrash) {
            getWindowManager().exitApp();
            return super.onBackPressed();
        }
        return isBackInEdit();
    }

}
