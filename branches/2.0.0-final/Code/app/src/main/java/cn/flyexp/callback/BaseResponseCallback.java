package cn.flyexp.callback;

/**
 * Created by tanxinye on 2016/10/23.
 */
public interface BaseResponseCallback {

    void requestFailure();

    void requestFinish();

    void noConnected();

    void renewLogin();

    void showDetail(String detail);

    void showDetail(int strid);
}
