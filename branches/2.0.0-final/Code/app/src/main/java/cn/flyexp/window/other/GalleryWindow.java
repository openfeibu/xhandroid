package cn.flyexp.window.other;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.BucketsAdapter;
import cn.flyexp.adapter.GalleryAdapter;
import cn.flyexp.constants.Constants;
import cn.flyexp.entity.BucketBean;
import cn.flyexp.entity.ImageBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.presenter.other.GalleryPresenter;
import cn.flyexp.util.ImageUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.ScreenHelper;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tanxinye on 2016/12/15.
 */
public class GalleryWindow extends BaseWindow implements NotifyManager.Notify, GalleryPresenter.OnGenerateCallback {

    @InjectView(R.id.rv_gallery)
    LoadMoreRecyclerView rvGallery;
    @InjectView(R.id.tv_bucket)
    TextView tvBucket;
    @InjectView(R.id.tv_num)
    TextView tvNum;

    private ArrayList<BucketBean> buckets = new ArrayList<>();
    private ArrayList<ImageBean> images = new ArrayList<>();
    private GalleryPresenter galleryPresenter;
    private RecyclerView rvBuckets;
    private BucketsAdapter bucketsAdapter;
    private PopupWindow picPopupWindow;
    private GalleryAdapter galleryAdapter;
    private int page = 1;
    private boolean isRefresh;
    private String bucketId;
    private int max;
    private ArrayList<String> selImages = new ArrayList<>();
    private final String IMAGE_STORE_FILE_NAME = "xhImg_%s.jpg";
    private String imagePath;
    private MediaScannerConnection scannerConnection;

    @Override
    protected int getLayoutId() {
        return R.layout.window_gallery;
    }

    public GalleryWindow(Bundle bundle) {
        max = bundle.getInt("max");
        initView();
        galleryPresenter = new GalleryPresenter(this);
        galleryPresenter.getBuckets();
        scanner();
        getNotifyManager().register(NotifyIDDefine.NOTIFY_CAMERA_RESULT, this);
    }

    private void initView() {
        tvNum.setText("0/" + max);
        galleryAdapter = new GalleryAdapter(getContext(), images, max);
        galleryAdapter.setOnItemClickLinstener(new GalleryAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(String path, int position) {
                Bundle bundle = new Bundle();
                ArrayList<String> uri = new ArrayList<>();
                uri.add(path);
                bundle.putStringArrayList("uri", uri);
                bundle.putInt("position", position);
                bundle.putString("type", Constants.GALLERY);
                openWindow(WindowIDDefine.WINDOW_PICBROWSER, bundle);
            }

            @Override
            public void onSelectedLinstener(int curSelNum, ArrayList<String> selImg) {
                tvNum.setText(curSelNum + "/" + max);
                selImages.clear();
                selImages.addAll(selImg);
            }
        });
        rvGallery.setAdapter(galleryAdapter);
        rvGallery.setLayoutManager(new GridLayoutManager(getContext(), 4));
        rvGallery.setHasFixedSize(true);
        rvGallery.setShowEnd(false);
        rvGallery.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                isRefresh = false;
                getImages();
            }
        });

        bucketsAdapter = new BucketsAdapter(getContext(), buckets);
        bucketsAdapter.setOnItemClickLinstener(new BucketsAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                isRefresh = true;
                tvBucket.setText(buckets.get(position).getBucketName());
                page = 1;
                bucketId = buckets.get(position).getBucketId();
                getImages();
                picPopupWindow.dismiss();
            }
        });
        rvBuckets = new RecyclerView(getContext());
        rvBuckets.setBackgroundColor(Color.WHITE);
        rvBuckets.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                ScreenHelper.dip2px(getContext(), 400)));
        rvBuckets.setAdapter(bucketsAdapter);
        rvBuckets.setHasFixedSize(true);
        rvBuckets.setLayoutManager(new LinearLayoutManager(getContext()));

        picPopupWindow = new PopupWindow(rvBuckets,
                ViewGroup.LayoutParams.MATCH_PARENT, ScreenHelper.dip2px(getContext(), 400));
        picPopupWindow.setFocusable(true);
        picPopupWindow.setTouchable(true);
        picPopupWindow.setOutsideTouchable(true);
        picPopupWindow.setBackgroundDrawable(new ColorDrawable());
        picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        picPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1f);
            }
        });
    }

    @OnClick({R.id.img_back, R.id.tv_complete, R.id.tv_bucket, R.id.tv_photograph})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_complete:
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", selImages);
                Message msg = Message.obtain();
                msg.what = NotifyIDDefine.NOTIFY_GALLERY;
                msg.setData(bundle);
                getNotifyManager().notify(msg);
                hideWindow(true);
                break;
            case R.id.tv_bucket:
                picPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
                changeWindowAlpha(0.7f);
                break;
            case R.id.tv_photograph:
                PermissionHandler.PermissionCallback permissionCallback = new PermissionHandler.PermissionCallback() {
                    public void onSuccess() {
                        openCamera();
                    }

                    public void onFail(int[] ids) {
                    }

                    public void onCancel() {
                    }

                    public void goSetting() {
                    }
                };
                PermissionTools.requestPermission(getContext(), permissionCallback, new int[]{PermissionHandler.PERMISSION_FILE, PermissionHandler.PERMISSION_CAMERA});
                break;
        }
    }

    private void openCamera() {
        File imageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/XiaoHui/");
        if (!imageStoreDir.exists()) {
            imageStoreDir.mkdir();
        }
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (captureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            String filename = String.format(IMAGE_STORE_FILE_NAME, dateFormat.format(new Date()));
            imagePath = new File(imageStoreDir, filename).getAbsolutePath();
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
            ((Activity) getContext()).startActivityForResult(captureIntent, Constants.CAMERA_RESULT);
        } else {
            showToast(R.string.gallery_device_camera_unable);
        }
    }

    private void scanner() {
        scannerConnection = new MediaScannerConnection(getContext(), new MediaScannerConnection.MediaScannerConnectionClient() {
            @Override
            public void onMediaScannerConnected() {
                if (!TextUtils.isEmpty(imagePath)) {
                    scannerConnection.scanFile(imagePath, "image/jpeg");
                }
            }

            @Override
            public void onScanCompleted(String s, Uri uri) {
                Observable.create(new Observable.OnSubscribe<ImageBean>() {
                    @Override
                    public void call(Subscriber<? super ImageBean> subscriber) {
                        ImageBean imageBean = ImageUtil.getImageBeanWithImage(getContext(), imagePath);
                        subscriber.onNext(imageBean);
                        subscriber.onCompleted();
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<ImageBean>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onNext(ImageBean imageBean) {
                                if (imageBean != null) {
                                    images.add(0, imageBean);
                                    galleryAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                scannerConnection.disconnect();
            }
        });
    }

    private void changeWindowAlpha(float v) {
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    private void getImages() {
        galleryPresenter.getImages(bucketId, page, 32);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        scannerConnection.disconnect();
    }

    @Override
    public void generateBuckets(ArrayList<BucketBean> list) {
        buckets.addAll(list);
        bucketsAdapter.notifyDataSetChanged();
        bucketId = list.get(0).getBucketId();
        page = 1;
        getImages();
    }

    @Override
    public void generateImages(ArrayList<ImageBean> list) {
        if (isRefresh) {
            images.clear();
            isRefresh = false;
        }
        images.addAll(list);
        galleryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTIFY_CAMERA_RESULT) {
            LogUtil.e("connect");
            scannerConnection.connect();
        }
    }
}
