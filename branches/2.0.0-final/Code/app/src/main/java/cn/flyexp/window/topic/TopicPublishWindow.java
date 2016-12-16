package cn.flyexp.window.topic;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.ImageCropBean;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.flyexp.R;
import cn.flyexp.adapter.TopicPicAdapter;
import cn.flyexp.callback.topic.TopicPublishCallback;
import cn.flyexp.constants.Constants;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.entity.TopicPublishRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.presenter.topic.TopicPublishPresenter;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.util.UploadFileHelper;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/11/17.
 */
public class TopicPublishWindow extends BaseWindow implements NotifyManager.Notify, TextWatcher, TopicPublishCallback.ResponseCallback {

    @InjectView(R.id.edt_content)
    EditText edtContent;
    @InjectView(R.id.rv_pic)
    RecyclerView rvPic;
    @InjectView(R.id.tv_publish)
    TextView tvPublish;
    @InjectView(R.id.tv_tnum)
    TextView tvTnum;
    @InjectView(R.id.img_add)
    ImageView imgAdd;

    private TopicPublishPresenter topicPublishPresenter;
    private SweetAlertDialog loadingDialog;
    private String content;
    private ArrayList<String> imgPaths = new ArrayList<>();
    private TopicPicAdapter topicPicAdapter;
    private String reponseUrl;
    private String reponseThumbUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.window_topic_publish;
    }

    public TopicPublishWindow() {
        topicPublishPresenter = new TopicPublishPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTICE_DELETE_PHOTO, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_GALLERY, this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));
        initView();
    }

    private void initView() {
        topicPicAdapter = new TopicPicAdapter(getContext(), imgPaths, true);
        topicPicAdapter.setOnItemClickLinstener(new TopicPicAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(int position) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("uri", imgPaths);
                bundle.putInt("position", position);
                bundle.putString("type", Constants.LOCAL);
                openWindow(WindowIDDefine.WINDOW_PICBROWSER, bundle);
            }
        });
        rvPic.setAdapter(topicPicAdapter);
        rvPic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        edtContent.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.tv_publish, R.id.img_add})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_publish:
                if (imgPaths.size() == 0) {
                    readyTopicPublish();
                } else {
                    readyUploadImageTopic();
                }
                break;
            case R.id.img_add:
                Bundle bundle = new Bundle();
                bundle.putInt("max", 9);
                openWindow(WindowIDDefine.WINDOW_GALLERY, bundle);
                break;
        }
    }

    private void readyTopicPublish() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            TopicPublishRequest topicPublishRequest = new TopicPublishRequest();
            topicPublishRequest.setToken(token);
            topicPublishRequest.setTopic_type("随心写");
            topicPublishRequest.setTopic_content(content);
            topicPublishRequest.setImg(reponseUrl);
            topicPublishRequest.setThumb(reponseThumbUrl);
            topicPublishPresenter.requestTopicPublish(topicPublishRequest);
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
        }
    }

    private void readyUploadImageTopic() {
        final String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            loadingDialog.show();
            new Thread() {
                @Override
                public void run() {
                    ArrayList<File> files = new ArrayList<>();
                    for (String path : imgPaths) {
                        files.add(BitmapUtil.compressBmpToFile(path));
                    }
                    MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(files);
                    topicPublishPresenter.requestUploadImageTopic(multipartBody, new TokenRequest(token));
                }
            }.start();
        }
    }

    @Override
    public void requestFailure() {
        dismissProgressDialog(loadingDialog);
    }

    @Override
    public void requestFinish() {
        dismissProgressDialog(loadingDialog);
    }


    @Override
    public void responseUploadImageTopic(ImgUrlResponse response) {
        reponseUrl = response.getUrl();
        reponseThumbUrl = response.getThumb_url();
        readyTopicPublish();
    }

    @Override
    public void responseTopicPublish(BaseResponse response) {
        hideWindow(true);
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_TOPIC);
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
        tvTnum.setText(String.valueOf(140 - content.length()));
        if (TextUtils.isEmpty(content)) {
            tvPublish.setEnabled(false);
            tvPublish.setAlpha(0.5f);
        } else {
            tvPublish.setEnabled(true);
            tvPublish.setAlpha(1f);
        }
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTICE_DELETE_PHOTO) {
            int position = mes.arg1;
            imgPaths.remove(position);
            topicPicAdapter.notifyDataSetChanged();
            if (imgPaths.size() == 9) {
                imgAdd.setEnabled(false);
                imgAdd.setAlpha(0.5f);
            } else {
                imgAdd.setEnabled(true);
                imgAdd.setAlpha(1f);
            }
        } else if (mes.what == NotifyIDDefine.NOTIFY_GALLERY) {
            Bundle bundle = mes.getData();
            imgPaths.clear();
            imgPaths.addAll(bundle.getStringArrayList("images"));
            topicPicAdapter.notifyDataSetChanged();
        }
    }

    private boolean isBackInEdit() {
        if (TextUtils.isEmpty(content)) {
            return false;
        } else {
            DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_giveup_edit), getResources().getString(R.string.dialog_giveup), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    dismissProgressDialog(sweetAlertDialog);
                    hideWindow(true);
                }
            });
            return true;
        }
    }

    @Override
    public boolean onBackPressed() {
        return isBackInEdit();
    }
}
