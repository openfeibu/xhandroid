package cn.flyexp.mvc.assn;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.AssnExamineAdapter;
import cn.flyexp.entity.AssnExamineListRequest;
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by tanxinye on 2016/10/3.
 */
public class AssnExamineWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private RecyclerView rv_assnexamine;
    private AssnExamineAdapter assnExamineAdapter;
    private ArrayList<AssnExamineListResponse.AssnExamineListResponseData> data = new ArrayList<>();

    public AssnExamineWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    public void requestMemberList(int aid) {
        String token = WindowHelper.getStringByPreference("token");
        if (TextUtils.isEmpty(token)) {
            return;
        }
        AssnExamineListRequest assnExamineRequest = new AssnExamineListRequest();
        assnExamineRequest.setAssociation_id(aid);
        assnExamineRequest.setToken(token);
        callBack.examineMembarList(assnExamineRequest);
    }

    private void initView() {
        setContentView(R.layout.window_assn_examine);
        findViewById(R.id.iv_back).setOnClickListener(this);

        assnExamineAdapter = new AssnExamineAdapter(getContext(),data);
        assnExamineAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.assnExamineDetailEnter(data.get(position));
            }
        });
        rv_assnexamine = (RecyclerView) findViewById(R.id.rv_assnexamine);
        rv_assnexamine.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_assnexamine.setAdapter(assnExamineAdapter);
    }

    public void examineListResponse(ArrayList<AssnExamineListResponse.AssnExamineListResponseData> responseData) {
        this.data.addAll(responseData);
        assnExamineAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }
}
