package cn.flyexp.presenter.other;

import java.util.ArrayList;

import cn.flyexp.callback.other.GalleryCallback;
import cn.flyexp.entity.BucketBean;
import cn.flyexp.entity.ImageBean;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.ImageUtil;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by tanxinye on 2016/12/15.
 */
public class GalleryPresenter implements GalleryCallback {

    private GalleryCallback.OnGenerateCallback callback;

    public GalleryPresenter(GalleryCallback.OnGenerateCallback callback) {
        this.callback = callback;
    }

    @Override
    public void getBuckets() {
        Observable.create(new Observable.OnSubscribe<ArrayList<BucketBean>>() {
            @Override
            public void call(Subscriber<? super ArrayList<BucketBean>> subscriber) {
                ArrayList<BucketBean> bucketBeanList = null;
                bucketBeanList = ImageUtil.getAllImageBucket();
                subscriber.onNext(bucketBeanList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<BucketBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.generateBuckets(null);
                LogUtil.e(e.getMessage());
            }

            @Override
            public void onNext(ArrayList<BucketBean> list) {
                callback.generateBuckets(list);
            }
        });
    }

    @Override
    public void getImages(final String bucketId, final int page, final int limit) {
        Observable.create(new Observable.OnSubscribe<ArrayList<ImageBean>>() {
            @Override
            public void call(Subscriber<? super ArrayList<ImageBean>> subscriber) {
                ArrayList<ImageBean> imageList = null;
                imageList = ImageUtil.getImageList(bucketId,page,limit);
                subscriber.onNext(imageList);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<ImageBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.generateBuckets(null);
                LogUtil.e(e.getMessage());
            }

            @Override
            public void onNext(ArrayList<ImageBean> list) {
                callback.generateImages(list);
            }
        });
    }

}
