package cn.flyexp.window.assn;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.ImageCropBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.imageloader.rotate.RotateTransformation;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import cn.flyexp.R;
import cn.flyexp.callback.assn.AssnActiPublishCallback;
import cn.flyexp.entity.AssnActiPublishRequest;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ImgUrlResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.presenter.assn.AssnActiPublishPresenter;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.util.UploadFileHelper;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MultipartBody;

/**
 * Created by tanxinye on 2016/11/5.
 */
public class AssnActiPublishWindow extends BaseWindow implements NotifyManager.Notify, TextWatcher, AssnActiPublishCallback.ResponseCallback {

    @InjectView(R.id.edt_title)
    EditText edtTitle;
    @InjectView(R.id.edt_content)
    EditText edtContent;
    @InjectView(R.id.edt_place)
    EditText edtPlace;
    @InjectView(R.id.tv_date)
    TextView tvDate;
    @InjectView(R.id.img_photo)
    ImageView imgPhoto;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;
    @InjectView(R.id.activitylayout)
    View activityLayout;

    private AssnActiPublishPresenter assnActiPublishPresenter;
    private int aid;
    private SweetAlertDialog loadingDialog;
    private String imgPath;
    private String content;
    private String place;
    private String title;
    private View dateLayout;
    private AlertDialog dateDialog;
    private MaterialCalendarView calendarView;
    private String firstDate;
    private String lastDate;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_acti_publish;
    }

    public AssnActiPublishWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        assnActiPublishPresenter = new AssnActiPublishPresenter(this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.uploading));
        dateLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_calendar, null);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_GALLERY, this);
        initView();
    }

    private void initView() {
        edtTitle.addTextChangedListener(this);
        edtContent.addTextChangedListener(this);
        edtPlace.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.img_add, R.id.tv_date, R.id.btn_confirm})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                if (!isBackInEdit()) {
                    hideWindow(true);
                }
                break;
            case R.id.img_add:
                Bundle bundle = new Bundle();
                bundle.putInt("max", 1);
                openWindow(WindowIDDefine.WINDOW_GALLERY, bundle);
                break;
            case R.id.tv_date:
                showDateDialog();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(firstDate) || TextUtils.isEmpty(lastDate)) {
                    showToast(R.string.hint_select_date);
                    showDateDialog();
                    return;
                }
                if (TextUtils.isEmpty(imgPath)) {
                    showToast(R.string.hint_select_pic);
                    openWindow(WindowIDDefine.WINDOW_GALLERY);
                    return;
                }
                DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_assn_acti_publish), getResources().getString(R.string.comfirm), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        readyAssnActiImage();
                        dismissProgressDialog(sweetAlertDialog);
                    }
                });
                break;
        }
    }

    private boolean isBackInEdit() {
        if (TextUtils.isEmpty(content) && TextUtils.isEmpty(title)) {
            return false;
        } else {
            DialogHelper.showSelectDialog(getContext(), getResources().getString(R.string.hint_giveup_edit), getResources().getString(R.string.dialog_giveup), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    hideWindow(true);
                    dismissProgressDialog(sweetAlertDialog);
                }
            });
            return true;
        }
    }

    private void showDateDialog() {
        if (dateDialog == null) {
            dateDialog = new AlertDialog.Builder(getContext()).setView(dateLayout).create();
            calendarView = (MaterialCalendarView) dateLayout.findViewById(R.id.calendar_view);
            calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
            calendarView.setSelectionColor(getResources().getColor(R.color.light_blue));
            calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_RANGE);
            calendarView.setAllowClickDaysOutsideCurrentMonth(true);
            calendarView.setDateSelected(CalendarDay.today(), true);
            Calendar maxCalendar = Calendar.getInstance();
            maxCalendar.set(maxCalendar.get(Calendar.YEAR) + 1, Calendar.OCTOBER, 31);
            calendarView.state().edit()
                    .setMinimumDate(new Date())
                    .setMaximumDate(maxCalendar.getTime())
                    .commit();
            calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
                @Override
                public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                    if (dates.size() < 2) {
                        return;
                    }

                    firstDate = DateUtil.long2Date(dates.get(0).getDate().getTime(), "MM-dd");
                    lastDate = DateUtil.long2Date(dates.get(dates.size() - 1).getDate().getTime(), "MM-dd");
                    tvDate.setText(firstDate + " 至 " + lastDate);
                    firstDate = DateUtil.long2Date(dates.get(0).getDate().getTime(), "yyyy-MM-dd HH:mm");
                    lastDate = DateUtil.long2Date(dates.get(dates.size() - 1).getDate().getTime(), "yyyy-MM-dd HH:mm");
                    widget.clearSelection();
                    dateDialog.dismiss();
                }
            });
        }
        dateDialog.show();
    }

    private void readyAssnActiImage() {
        final String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            loadingDialog.show();
            new Thread() {
                @Override
                public void run() {
                    ArrayList<File> files = new ArrayList<>();
                    files.add(BitmapUtil.compressBmpToFile(imgPath));
                    MultipartBody multipartBody = UploadFileHelper.uploadMultipartFile(files);
                    assnActiPublishPresenter.requestUploadImageActi(multipartBody, new TokenRequest(token));
                }
            }.start();
        }
    }

    private void readyAssnActiPublish(String url) {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        AssnActiPublishRequest assnActiPublishRequest = new AssnActiPublishRequest();
        assnActiPublishRequest.setAid(aid);
        assnActiPublishRequest.setToken(token);
        assnActiPublishRequest.setTitle(title);
        assnActiPublishRequest.setContent(content);
        assnActiPublishRequest.setPlace(place);
        assnActiPublishRequest.setStart_time(firstDate);
        assnActiPublishRequest.setEnd_time(lastDate);
        assnActiPublishRequest.setImg_url(url);
        assnActiPublishPresenter.requestAssnActiPublish(assnActiPublishRequest);
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
    public void responseUploadImageActi(ImgUrlResponse response) {
        readyAssnActiPublish(response.getUrl());
    }

    @Override
    public void responseAssnActiPublish(BaseResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_MYTASK);
        getNotifyManager().notify(NotifyIDDefine.NOTIFY_ASSN_ACTIVITY);
        hideWindow(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        title = edtTitle.getText().toString().trim();
        content = edtContent.getText().toString().trim();
        place = edtPlace.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content) || TextUtils.isEmpty(place)) {
            btnConfirm.setAlpha(0.5f);
            btnConfirm.setEnabled(false);
        } else {
            btnConfirm.setAlpha(1f);
            btnConfirm.setEnabled(true);
        }
    }

    @Override
    public boolean onBackPressed() {
        return isBackInEdit();
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_GALLERY) {
            Bundle bundle = mes.getData();
            ArrayList<String> images = bundle.getStringArrayList("images");
            imgPath = images.get(0);
            Glide.with(getContext()).load(imgPath).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(imgPhoto);
        }
    }
}

