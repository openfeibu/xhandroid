package cn.flyexp.mvc.mine;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.ShopAdapter;
import cn.flyexp.entity.ShopResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreRecyclerView;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class CollectionShopWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private LoadMoreRecyclerView shopRecyclerView;
    private ArrayList<ShopResponse.ShopResponseData> shopData = new ArrayList<>();
    private ShopAdapter shopAdapter;

    public CollectionShopWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_collectionshop);
        findViewById(R.id.iv_back).setOnClickListener(this);

        shopAdapter = new ShopAdapter(getContext(), shopData);
        shopAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                callBack.shopDetailEnter();
            }
        });
        shopRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.rv_shop);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shopRecyclerView.setItemAnimator(new DefaultItemAnimator());
        shopRecyclerView.setAdapter(shopAdapter);
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
