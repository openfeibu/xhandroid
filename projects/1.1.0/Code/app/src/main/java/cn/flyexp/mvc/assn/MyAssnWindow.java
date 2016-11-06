package cn.flyexp.mvc.assn;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnAdapter;
import cn.flyexp.adapter.MyAssnAdapter;
import cn.flyexp.entity.MyAssnResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by tanxinye on 2016/9/29.
 */
public class MyAssnWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private ArrayList<MyAssnResponse.MyAssnResponseData> data = new ArrayList<>();
    private MyAssnAdapter myAssnAdapter;
    private View hintLayout;

    public MyAssnWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        String token = WindowHelper.getStringByPreference("token");
        if (!TextUtils.isEmpty(token)) {
            callBack.getMyAssnList(token);
        } else {
            callBack.loginWindowEnter();
        }
    }

    private void initView() {
        setContentView(R.layout.window_myassn);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_myassn).setOnClickListener(this);
        hintLayout = findViewById(R.id.layout_hint);

        myAssnAdapter = new MyAssnAdapter(getContext(), data);
        myAssnAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.myAssnDetailEnter(data.get(position).getAid(), data.get(position).getLevel());
            }
        });
        RecyclerView rv_myassn = (RecyclerView) findViewById(R.id.rv_myassn);
        rv_myassn.setAdapter(myAssnAdapter);
        rv_myassn.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void reponseData(ArrayList<MyAssnResponse.MyAssnResponseData> reponseData) {
        if (reponseData.size() == 0) {
            hintLayout.setVisibility(VISIBLE);
        } else {
            data.addAll(reponseData);
            myAssnAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_myassn:
                hideWindow(true);
                callBack.assnEnter();
                break;
        }
    }
}
