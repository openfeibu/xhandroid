package cn.flyexp.mvc.shop;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.adapter.ShopAdapter;
import cn.flyexp.entity.ShopResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.LoadMoreRecyclerView;

/**
 * Created by txy on 2016/8/9 0009.
 */
public class ShopWindow extends AbstractWindow implements View.OnClickListener {

    private ShopViewCallBack callBack;

    public ShopWindow(ShopViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_shop);
        findViewById(R.id.call).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call:
            Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "15986309335");
                intent.setData(data);
                try {
                    getContext().startActivity(intent);
                } catch (Exception e) {
//                    showToast("无法使用电话");
                    Log.e("test","无法进行电话联系啊");
                }
                break;
        }
    }
}
