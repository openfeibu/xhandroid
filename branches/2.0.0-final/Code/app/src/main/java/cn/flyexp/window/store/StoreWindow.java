package cn.flyexp.window.store;

import android.view.View;
import android.widget.Button;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/24.
 */
public class StoreWindow extends BaseWindow {

    @InjectView(R.id.btn_Ta_Topic)
    Button btn_Ta_Topic;

    @Override
    protected int getLayoutId() {
        return R.layout.window_store;
    }

    public StoreWindow(){

    }

    @OnClick({R.id.btn_Ta_Topic})
        void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_Ta_Topic:
                //openWindow(WindowIDDefine.WINDOW_OTHERS);
                break;
        }
    }


    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

}
