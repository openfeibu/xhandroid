package cn.flyexp.window.assn;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.callback.assn.MyAssnActivityCallback;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.MyAssnActivityRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.assn.MyAssnActivityPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class MyAssnActivityWindow extends BaseWindow implements NotifyManager.Notify, MyAssnActivityCallback.ResponseCallback {

    @InjectView(R.id.rv_myassnacti)
    LoadMoreRecyclerView rvMyAssnActi;
    @InjectView(R.id.img_publish)
    ImageView imgPulish;

    private int page = 1;
    private int aid;
    private int level;
    private MyAssnActivityPresenter myAssnActivityPresenter;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> datas = new ArrayList<>();
    private AssnActivityAdapter assnActivityAdapter;
    private SweetAlertDialog loadingDialog;
    private boolean isRefresh;

    @Override
    protected int getLayoutId() {
        return R.layout.window_myassn_activity;
    }

    public MyAssnActivityWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        level = bundle.getInt("level");
        myAssnActivityPresenter = new MyAssnActivityPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_ASSN_ACTIVITY, this);
        loadingDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.loading));

        initView();
        readyMyAssnActivity();
    }

    private void initView() {
        if (level == 0) {
            imgPulish.setVisibility(GONE);
        } else {
            imgPulish.setVisibility(VISIBLE);
        }
        assnActivityAdapter = new AssnActivityAdapter(getContext(), datas);
        assnActivityAdapter.setOnItemClickLinstener(new AssnActivityAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("assnacti", datas.get(position));
                openWindow(WindowIDDefine.WINDOW_ASSNACTI_DETAIL, bundle);
            }
        });
        rvMyAssnActi.setAdapter(assnActivityAdapter);
        rvMyAssnActi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyAssnActi.setLoadMoreLinstener(new LoadMoreRecyclerView.LoadMoreLinstener() {
            @Override
            public void onLoadMore() {
                page++;
                readyMyAssnActivity();
            }
        });
    }

    private void readyMyAssnActivity() {
        MyAssnActivityRequest myAssnActivityRequest = new MyAssnActivityRequest();
        myAssnActivityRequest.setPage(page);
        myAssnActivityRequest.setAssociation_id(aid);
        myAssnActivityPresenter.requestMyAssnActivity(myAssnActivityRequest);
        loadingDialog.show();
    }

    @OnClick({R.id.img_back, R.id.img_publish})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.img_publish:
                Bundle bundle = new Bundle();
                bundle.putInt("aid", aid);
                openWindow(WindowIDDefine.WINDOW_ASSNACTI_PUBLISH, bundle);
                break;
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
    public void responseMyAssnActivity(AssnActivityResponse response) {
        if (isRefresh) {
            datas.clear();
        }
        datas.addAll(response.getData());
        assnActivityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {
        isRefresh = true;
        readyMyAssnActivity();
    }
}
