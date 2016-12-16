package cn.flyexp.window.topic;

import android.text.TextUtils;
import android.view.View;

import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class TopicWindow extends BaseWindow {

    @Override
    protected int getLayoutId() {
        return R.layout.window_topic;
    }

    public TopicWindow(){

    }

    @OnClick({R.id.img_publish})
    void onClick(View view){
        switch (view.getId()){
            case R.id.img_publish:
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    renewLogin();
                } else {
                    openWindow(WindowIDDefine.WINDOW_TOPIC_PUBLISH);
                }
                break;
        }
    }
}
