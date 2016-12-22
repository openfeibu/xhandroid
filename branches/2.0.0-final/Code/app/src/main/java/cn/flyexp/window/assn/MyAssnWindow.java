package cn.flyexp.window.assn;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.adapter.MyAssnAdapter;
import cn.flyexp.callback.assn.MyAssnCallback;
import cn.flyexp.entity.MyAssnResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.assn.MyAssnPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.LoadMoreRecyclerView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class MyAssnWindow extends BaseWindow implements MyAssnCallback.ResponseCallback, NotifyManager.Notify {

    @InjectView(R.id.rv_myassn)
    RecyclerView rvMyAssn;
    @InjectView(R.id.layout_hint)
    LinearLayout layoutHint;

    private MyAssnPresenter myAssnPresenter;
    private ArrayList<MyAssnResponse.MyAssnResponseData> datas = new ArrayList<>();
    private MyAssnAdapter myAssnAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_myassn;
    }

    public MyAssnWindow() {
        myAssnPresenter = new MyAssnPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTICE_MYASSN, this);

        initView();
        readyMyAssn();
    }

    private void readyMyAssn() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            myAssnPresenter.requestMyAssn(new TokenRequest(token));
        }
    }

    private void initView() {
        myAssnAdapter = new MyAssnAdapter(getContext(), datas);
        myAssnAdapter.setOnItemClickLinstener(new MyAssnAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("aid", datas.get(position).getAid());
                bundle.putInt("level", datas.get(position).getLevel());
                openWindow(WindowIDDefine.WINDOW_MYASSN_DETAIL, bundle);
            }
        });
        rvMyAssn.setAdapter(myAssnAdapter);
        rvMyAssn.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @OnClick({R.id.img_back, R.id.btn_myassn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_myassn:
                hideWindow(true);
                openWindow(WindowIDDefine.WINDOW_ASSN);
                break;
        }
    }

    @Override
    public void responseMyAssn(MyAssnResponse response) {
        ArrayList<MyAssnResponse.MyAssnResponseData> responseData = response.getData();
        if (responseData.size() == 0) {
            rvMyAssn.setVisibility(GONE);
            layoutHint.setVisibility(VISIBLE);
        } else {
            datas.clear();
            datas.addAll(responseData);
            myAssnAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTICE_MYASSN) {
            readyMyAssn();
        }
    }
}
