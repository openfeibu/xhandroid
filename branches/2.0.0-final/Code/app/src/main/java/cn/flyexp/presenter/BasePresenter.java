package cn.flyexp.presenter;

import cn.flyexp.callback.BaseRequestCallback;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.entity.EncodeData;
import cn.flyexp.util.GsonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.NetWorkUtil;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class BasePresenter implements BaseRequestCallback {

    private CompositeSubscription compositeSubscription;
    private BaseResponseCallback callback;

    protected interface ObservableCallback<T> {

        void onSuccess(T response);
    }

    public BasePresenter(BaseResponseCallback callback) {
        this.callback = callback;
    }

    protected <T> void execute(Observable<EncodeData> observable, final Class<T> cls, final ObservableCallback<T> observableCallback) {
        if (!NetWorkUtil.isNetWorkAvailable()) {
            callback.noConnected();
            return;
        }
        Subscription subscribe = observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<EncodeData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.requestFailure();
                LogUtil.e("onError Exception" + getClass().getSimpleName(), e.getClass().getSimpleName());
            }

            @Override
            public void onNext(EncodeData encodeData) {
                callback.requestFinish();
                T response =  GsonUtil.getInstance().decodeJson(encodeData.getData(), cls);
                LogUtil.e("parseData" + response.getClass().getSimpleName(),GsonUtil.getInstance().toJson(response));
                observableCallback.onSuccess(response);
            }
        });
        addSubscription(subscribe);
    }

    protected void addSubscription(Subscription s) {
        if (this.compositeSubscription == null) {
            this.compositeSubscription = new CompositeSubscription();
        }
        this.compositeSubscription.add(s);
    }

    @Override
    public void unsubcrible() {
        if (this.compositeSubscription != null) {
            this.compositeSubscription.unsubscribe();
        }
    }
}
