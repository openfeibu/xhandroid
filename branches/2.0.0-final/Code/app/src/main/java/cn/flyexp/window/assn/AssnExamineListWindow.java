package cn.flyexp.window.assn;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.assn.AssnExamineListCallback;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.adapter.AssnExamineListAdapter;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.assn.AssnExamineListPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class AssnExamineListWindow extends BaseWindow implements AssnExamineListCallback.ResponseCallback, NotifyManager.Notify {

    @InjectView(R.id.rv_assnexamine)
    RecyclerView rvAssnExamine;

    private int aid;
    private AssnExamineListPresenter assnExamineListPresenter;
    private ArrayList<AssnExamineListResponse.AssnExamineListResponseData> datas = new ArrayList<>();
    private AssnExamineListAdapter assnExamineListAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_examine_list;
    }

    public AssnExamineListWindow(Bundle bundle) {
        aid = bundle.getInt("aid");
        assnExamineListPresenter = new AssnExamineListPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTICE_ASSN_EXAMINE_LIST, this);

        initView();
        readyAssnExamineList();
    }

    private void initView() {
        assnExamineListAdapter = new AssnExamineListAdapter(getContext(), datas);
        assnExamineListAdapter.setOnItemClickLinstener(new AssnExamineListAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("examine", datas.get(position));
                openWindow(WindowIDDefine.WINDOW_ASSN_EXAMINE_DETAIL, bundle);
            }
        });
        rvAssnExamine.setAdapter(assnExamineListAdapter);
        rvAssnExamine.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAssnExamine.addItemDecoration(new DividerItemDecoration(getContext()));
    }

    private void readyAssnExamineList() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
        } else {
            AssnExamineListRequest assnExamineListRequest = new AssnExamineListRequest();
            assnExamineListRequest.setToken(token);
            assnExamineListRequest.setAssociation_id(aid);
            assnExamineListPresenter.requestAssnExamineList(assnExamineListRequest);
        }
    }

    @OnClick({R.id.img_back})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
        }
    }

    @Override
    public void responseAssnExamineList(AssnExamineListResponse response) {
        getNotifyManager().notify(NotifyIDDefine.NOTICE_MYASSN_DETAIL);
        datas.addAll(response.getData());
        assnExamineListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNotify(Message mes) {
        readyAssnExamineList();
    }
}
