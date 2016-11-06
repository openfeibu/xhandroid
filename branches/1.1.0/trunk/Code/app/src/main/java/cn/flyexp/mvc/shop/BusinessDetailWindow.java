package cn.flyexp.mvc.shop;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import cn.flyexp.R;
import cn.flyexp.adapter.PicDisPlayAdapter;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/18 0018.
 */
public class BusinessDetailWindow extends AbstractWindow implements View.OnClickListener{


    private PicDisPlayAdapter picAdapter;

    public BusinessDetailWindow(WindowCallBack callBack) {
        super(callBack);
        initView();
    }

    private void initView() {
        View layout = getView(R.layout.window_business_detail);
        layout.findViewById(R.id.iv_back).setOnClickListener(this);
        picAdapter = new PicDisPlayAdapter(getContext(), null);
        picAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        final RecyclerView rv_pic = (RecyclerView) layout.findViewById(R.id.rv_pic);
        rv_pic.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_pic.setAdapter(picAdapter);
        rv_pic.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(false);
                break;
        }
    }
}
