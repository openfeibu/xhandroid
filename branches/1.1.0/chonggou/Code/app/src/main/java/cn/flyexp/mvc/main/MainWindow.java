package cn.flyexp.mvc.main;

import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.campus.CampusController;
import cn.flyexp.mvc.shop.ShopController;
import cn.flyexp.mvc.task.TaskController;
import cn.flyexp.mvc.topic.TopicController;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.mvc.mine.MineController;
import cn.flyexp.view.NoSlidingViewPager;

/**
 * Created by txy on 2016/7/16 0016.
 */
public class MainWindow extends AbstractWindow implements View.OnClickListener {

    public static NoSlidingViewPager vp_main;
    private TextView tv_mine;
    private TextView tv_task;
    private TextView tv_topic;
    private TextView tv_shop;
    private TextView tv_campus;
    private AbstractWindow[] windows = new AbstractWindow[5];
    private TextView[] tabText = new TextView[5];
    private int[][] tabImgId = new int[5][2];
    private MainViewCallBack callBack;
    private int currentWindowIndex = 0;

    public MainWindow(MainViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_main);
        tv_campus = (TextView) findViewById(R.id.tv_campus);
        tv_task = (TextView) findViewById(R.id.tv_task);
        tv_topic = (TextView) findViewById(R.id.tv_topic);
        tv_shop = (TextView) findViewById(R.id.tv_shop);
        tv_mine = (TextView) findViewById(R.id.tv_mine);
        tv_campus.setOnClickListener(this);
        tv_task.setOnClickListener(this);
        tv_topic.setOnClickListener(this);
        tv_shop.setOnClickListener(this);
        tv_mine.setOnClickListener(this);

        tabText[0] = tv_campus;
        tabText[1] = tv_task;
        tabText[2] = tv_topic;
        tabText[3] = tv_shop;
        tabText[4] = tv_mine;

        tabImgId[0][0] = R.mipmap.icon_campus_nor;
        tabImgId[0][1] = R.mipmap.icon_campus_sel;
        tabImgId[1][0] = R.mipmap.icon_campustask_nor;
        tabImgId[1][1] = R.mipmap.icon_campustask_sel;
        tabImgId[2][0] = R.mipmap.icon_topic_nor;
        tabImgId[2][1] = R.mipmap.icon_topic_sel;
        tabImgId[3][0] = R.mipmap.icon_store_nor;
        tabImgId[3][1] = R.mipmap.icon_store_sel;
        tabImgId[4][0] = R.mipmap.icon_mine_nor;
        tabImgId[4][1] = R.mipmap.icon_mine_sel;

        windows[0] = new CampusController().campusWindow;
        windows[1] = new TaskController().taskWindow;
        windows[2] = new TopicController().topicWindow;
        windows[3] = new ShopController().shopWindow;
        windows[4] = new MineController().mineWindow;

        vp_main = (NoSlidingViewPager) findViewById(R.id.vp_main);
        vp_main.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return windows.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(windows[position]);
                return windows[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(windows[position]);
            }

        });
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void selectTab(int index) {
        this.currentWindowIndex = index;
        for (int i = 0; i < 5; i++) {
            if (i == index) {
                tabText[i].setTextColor(getResources().getColor(R.color.light_blue));
                Drawable drawable = getResources().getDrawable(tabImgId[i][1]);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tabText[i].setCompoundDrawables(null, drawable, null, null);
            } else {
                tabText[i].setTextColor(getResources().getColor(R.color.font_brown_dark));
                Drawable drawable = getResources().getDrawable(tabImgId[i][0]);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tabText[i].setCompoundDrawables(null, drawable, null, null);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String token = WindowHelper.getStringByPreference("token");
        switch (v.getId()) {
            case R.id.tv_campus:
                vp_main.setCurrentItem(0, false);
                break;
            case R.id.tv_task:
                vp_main.setCurrentItem(1, false);
                break;
            case R.id.tv_topic:
                vp_main.setCurrentItem(2, false);
                break;
            case R.id.tv_shop:
                vp_main.setCurrentItem(3, false);
                break;
            case R.id.tv_mine:
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                } else {
                    vp_main.setCurrentItem(4, false);
                }
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (windows[currentWindowIndex].dispatchKeyEvent(event)) {
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_UP) {
            int keyCode = event.getKeyCode();
            if (keyCode == event.KEYCODE_BACK) {
                exitApp();
            }
            return true;
        }
        return false;
    }
}
