package cn.flyexp.window.assn;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/11/1.
 */
public class AssnActiDetailWindow extends BaseWindow {


    @Override
    protected int getLayoutId() {
        return R.layout.window_assn_acti_detail;
    }

    public AssnActiDetailWindow(Bundle bundle) {
        AssnActivityResponse.AssnActivityResponseData data = (AssnActivityResponse.AssnActivityResponseData) bundle.getSerializable("assnacti");
    }

    private void initView(AssnActivityResponse.AssnActivityResponseData data) {
        CollapsingToolbarLayout a;
    }
//
//    @OnClick({R.id.img_back})
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.img_back:
//                hideWindow(true);
//                break;
//        }
//    }
}
