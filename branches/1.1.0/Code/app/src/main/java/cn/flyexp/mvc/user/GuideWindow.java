package cn.flyexp.mvc.user;

import android.content.pm.ActivityInfo;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class GuideWindow extends AbstractWindow {

    private UserViewCallBack callBack;
    private ViewPager viewPager;
    private View[] views;
    private CircleIndicator indicator;

    public GuideWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_guide);
        views = new View[3];
        views[0] = inflate(getContext(), R.layout.layout_guide1, null);
        views[1] = inflate(getContext(), R.layout.layout_guide2, null);
        views[2] = inflate(getContext(), R.layout.layout_guide3, null);
        views[2].findViewById(R.id.btn_into).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainEnter();
            }
        });
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.vp_guide);
        viewPager.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }

            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        indicator.setViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    indicator.setVisibility(GONE);
                } else {
                    indicator.setVisibility(VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP ) {
            mainEnter();
        }
        return super.dispatchKeyEvent(event);
    }

    private void mainEnter() {
        callBack.mainEnter();
        WindowHelper.putBooleanByPreference("isNoFirstRun", true);
        System.gc();
    }
}
