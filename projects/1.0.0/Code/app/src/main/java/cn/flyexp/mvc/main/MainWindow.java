package cn.flyexp.mvc.main;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.oushangfeng.marqueelayout.MarqueeLayout;
import com.oushangfeng.marqueelayout.MarqueeLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.entity.NoResponse;
import cn.flyexp.mvc.campus.CampusController;
import cn.flyexp.mvc.topic.TopicController;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.mvc.message.MessageController;
import cn.flyexp.mvc.mine.MineController;
import cn.flyexp.util.LogUtil;
import cn.flyexp.view.NoSlidingViewPager;
import cn.flyexp.view.VerticalMarqueeView;

/**
 * Created by txy on 2016/7/16 0016.
 */
public class MainWindow extends AbstractWindow implements View.OnClickListener {

    public static NoSlidingViewPager vp_main;
    private TextView tv_mine;
    private TextView tv_message;
    private TextView tv_topic;
    private TextView tv_campus;
    private AbstractWindow[] windows = new AbstractWindow[4];
    private TextView[] tabText = new TextView[4];
    private int[][] tabImgId = new int[4][2];
    private MainViewCallBack callBack;
    private VerticalMarqueeView mMarqueeView;
    private ArrayList<NoResponse.NoResponseData> mSrcList = new ArrayList<>();
    private MarqueeLayoutAdapter mSrcAdapter;

    public MainWindow(MainViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        callBack.getNo();
    }

    private void initView() {
        setContentView(R.layout.window_main);
        tv_campus = (TextView) findViewById(R.id.tv_campus);
        tv_topic = (TextView) findViewById(R.id.tv_topic);
        tv_message = (TextView) findViewById(R.id.tv_message);
        tv_mine = (TextView) findViewById(R.id.tv_mine);
        tv_campus.setOnClickListener(this);
        tv_topic.setOnClickListener(this);
        tv_message.setOnClickListener(this);
        tv_mine.setOnClickListener(this);

        tabText[0] = tv_campus;
        tabText[1] = tv_topic;
        tabText[2] = tv_message;
        tabText[3] = tv_mine;

        tabImgId[0][0] = R.mipmap.icon_campus_nor;
        tabImgId[0][1] = R.mipmap.icon_campus_sel;
        tabImgId[1][0] = R.mipmap.icon_topic_nor;
        tabImgId[1][1] = R.mipmap.icon_topic_sel;
        tabImgId[2][0] = R.mipmap.icon_papermessage_nor;
        tabImgId[2][1] = R.mipmap.icon_papermessage_sel;
        tabImgId[3][0] = R.mipmap.icon_mine_nor;
        tabImgId[3][1] = R.mipmap.icon_mine_sel;

        windows[0] = new CampusController().campusWindow;
        windows[1] = new TopicController().topicWindow;
        windows[2] = new MessageController().messageWindow;
        windows[3] = new MineController().mineWindow;

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

        mMarqueeView = (VerticalMarqueeView) windows[0].findViewById(R.id.marqueeView);
    }

    //森彬改
    private int sp2px(Context context, int sp) {
        float density = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * density + 0.5f);
    }

    public void stopMarquee() {
        mMarqueeView.stoScroll();
    }

    private void selectTab(int index) {
        for (int i = 0; i < 4; i++) {
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
        String statistics = "";
        switch (index) {
            case 0:
                statistics = "campus";
                break;
            case 1:
                statistics = "topic";
                break;
            case 2:
                statistics = "message";
                break;
            case 3:
                statistics = "mine";
                break;
        }
        callBack.userCount(statistics);
    }

    private String[] changeDataToArray(ArrayList<NoResponse.NoResponseData> data) {
        if (data.size() == 1) {
            NoResponse nrd = new NoResponse();
            NoResponse.NoResponseData rd = nrd.new NoResponseData();
            rd.setExtra("");
            data.add(rd);
        }
        String[] datas = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            NoResponse.NoResponseData item = data.get(i);
            datas[i] = item.getExtra();
        }
        return datas;
    }

    public void responseData(ArrayList<NoResponse.NoResponseData> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        String[] datas = changeDataToArray(data);
        mMarqueeView.color(getResources().getColor(R.color.light_red))
                .textSize(sp2px(getContext(), 14)).datas(datas).commit();
        mMarqueeView.setData();
        mMarqueeView.startScroll();
    }


    @Override
    public void onClick(View v) {
        String token = getStringByPreference("token");
        switch (v.getId()) {
            case R.id.tv_campus:
                vp_main.setCurrentItem(0, false);
                break;
            case R.id.tv_topic:
                vp_main.setCurrentItem(1, false);
                break;
            case R.id.tv_message:
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                } else {
                    vp_main.setCurrentItem(2, false);
                }
                break;
            case R.id.tv_mine:
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                } else {
                    vp_main.setCurrentItem(3, false);
                }
                break;
        }
    }
}
