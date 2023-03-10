package cn.flyexp.window.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.flyexp.window.mine.MineWindow;
import cn.flyexp.window.store.StoreWindow;
import cn.flyexp.window.task.TaskWindow;
import cn.flyexp.window.topic.TopicWindow;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class MainWindow extends BaseWindow implements NotifyManager.Notify {

    @InjectView(R.id.frameLayout)
    FrameLayout frameLayout;
    @InjectViews({R.id.tv_home, R.id.tv_task, R.id.tv_topic, R.id.tv_store, R.id.tv_mine})
    List<TextView> tvTabs;

    private Drawable[][] tabDrawable = new Drawable[5][2];
    private View[] views = new View[5];

    @Override
    protected int getLayoutId() {
        return R.layout.window_main;
    }

    public MainWindow() {
        getNotifyManager().register(NotifyIDDefine.NOTICE_MAIN_HOME, this);
        getNotifyManager().register(NotifyIDDefine.NOTIFY_MAIN_TASK, this);
        initView();
        loadPushData();
    }

    private void initView() {
        tabDrawable[0][0] = getResources().getDrawable(R.mipmap.icon_campus_nor);
        tabDrawable[0][1] = getResources().getDrawable(R.mipmap.icon_campus_sel);
        tabDrawable[1][0] = getResources().getDrawable(R.mipmap.icon_campustask_nor);
        tabDrawable[1][1] = getResources().getDrawable(R.mipmap.icon_campustask_sel);
        tabDrawable[2][0] = getResources().getDrawable(R.mipmap.icon_topic_nor);
        tabDrawable[2][1] = getResources().getDrawable(R.mipmap.icon_topic_sel);
        tabDrawable[3][0] = getResources().getDrawable(R.mipmap.icon_store_nor);
        tabDrawable[3][1] = getResources().getDrawable(R.mipmap.icon_store_sel);
        tabDrawable[4][0] = getResources().getDrawable(R.mipmap.icon_mine_nor);
        tabDrawable[4][1] = getResources().getDrawable(R.mipmap.icon_mine_sel);

        views[0] = new HomeWindow();
        views[1] = new TaskWindow();
        views[2] = new TopicWindow();
        views[3] = new StoreWindow();
        views[4] = new MineWindow();
        switchWindow(0);
    }

    private void loadPushData() {
        int windowId = SharePresUtil.getInt(SharePresUtil.KEY_WINDOW_ID);
        String data = SharePresUtil.getString(SharePresUtil.KEY_PUSH_DATA);
        if (!(windowId == 0 && TextUtils.isEmpty(data))) {
            Bundle bundle = new Bundle();
            bundle.putString("pushdata", data);
            SharePresUtil.putInt(SharePresUtil.KEY_WINDOW_ID, 0);
            SharePresUtil.putString(SharePresUtil.KEY_PUSH_DATA, "");
            openWindow(windowId, bundle);
        }
    }

    private void switchWindow(int index) {
        frameLayout.removeAllViews();
        frameLayout.addView(views[index]);
        for (int i = 0; i < 5; i++) {
            if (i == index) {
                tvTabs.get(i).setTextColor(getResources().getColor(R.color.light_blue));
                tabDrawable[i][1].setBounds(0, 0, tabDrawable[i][1].getMinimumWidth(), tabDrawable[i][1].getMinimumHeight());
                tvTabs.get(i).setCompoundDrawables(null, tabDrawable[i][1], null, null);
                ((BaseWindow) views[index]).onResume();
            } else {
                tvTabs.get(i).setTextColor(getResources().getColor(R.color.font_dark));
                tabDrawable[i][0].setBounds(0, 0, tabDrawable[i][0].getMinimumWidth(), tabDrawable[i][0].getMinimumHeight());
                tvTabs.get(i).setCompoundDrawables(null, tabDrawable[i][0], null, null);
                ((BaseWindow) views[index]).onStop();
            }
        }
    }


    @OnClick({R.id.tv_home, R.id.tv_task, R.id.tv_topic, R.id.tv_store, R.id.tv_mine})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
                switchWindow(0);
                break;
            case R.id.tv_task:
                switchWindow(1);
                break;
            case R.id.tv_topic:
                switchWindow(2);
                break;
            case R.id.tv_store:
                switchWindow(3);
                break;
            case R.id.tv_mine:
                String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
                if (TextUtils.isEmpty(token)) {
                    openWindow(WindowIDDefine.WINDOW_LOGIN);
                } else {
                    switchWindow(4);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        ((BaseWindow) views[0]).onResume();
    }

    @Override
    public void onStop() {
        ((BaseWindow) views[0]).onStop();
    }

    @Override
    public void onNotify(Message mes) {
        if (mes.what == NotifyIDDefine.NOTICE_MAIN_HOME) {
            switchWindow(0);
        } else if (mes.what == NotifyIDDefine.NOTIFY_MAIN_TASK) {
            switchWindow(1);
        }
    }
}
