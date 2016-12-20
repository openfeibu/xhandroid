package cn.flyexp.window.other;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.util.ColorShades;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.ScreenHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/26.
 */
public class GuideWindow extends BaseWindow {

    @InjectView(R.id.viewpager)
    ViewPager viewPager;
    @InjectView(R.id.guideLayout)
    View guideLayout;
    @InjectView(R.id.layout_info)
    View intoLayout;

    private ViewGroup[] views = new ViewGroup[4];
    private ColorShades colorShades = new ColorShades();
//    private int[] guideColor = new int[4];
    private int lastPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.window_guide;
    }

    public GuideWindow() {
//        guideColor[0] = getResources().getColor(R.color.light_blue);
//        guideColor[1] = getResources().getColor(R.color.light_cyan);
//        guideColor[2] = getResources().getColor(R.color.light_yellow);
        views[0] = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_guide1, null);
        views[1] = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_guide2, null);
        views[2] = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_guide3, null);
        views[3] = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_guide4, null);
        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views[position]);
                return views[position];
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);
            }

        };
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                colorShades.setFromColor(guideColor[position % guideColor.length])
//                        .setToColor(guideColor[(position + 1) % guideColor.length])
//                        .setShade(positionOffset);
//                guideLayout.setBackgroundColor(colorShades.generate());
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    ObjectAnimator upAnim = ObjectAnimator.ofFloat(intoLayout, "y", intoLayout.getY(), intoLayout.getY() - intoLayout.getHeight());
                    upAnim.setDuration(200);
                    upAnim.setInterpolator(new AccelerateInterpolator());
                    upAnim.start();
                } else if (position == 2 && lastPosition == 3) {
                    ObjectAnimator upAnim = ObjectAnimator.ofFloat(intoLayout, "y", intoLayout.getY(), intoLayout.getY() + intoLayout.getHeight());
                    upAnim.setDuration(200);
                    upAnim.setInterpolator(new AccelerateInterpolator());
                    upAnim.start();
                }
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {

            private float parallaxCoefficient = 0.4f;
            private float distanceCoefficient = 0.2f;

            @Override
            public void transformPage(View page, float position) {
                float scrollXOffset = page.getWidth() * parallaxCoefficient;
                int[] layer = new int[]{R.id.img_guide, R.id.img_font};
                for (int id : layer) {
                    View view = page.findViewById(id);
                    if (view != null) {
                        view.setTranslationX(scrollXOffset * position);
                    }
                    scrollXOffset *= distanceCoefficient;
                }
            }
        });
        viewPager.setAdapter(pagerAdapter);
    }

    @OnClick({R.id.btn_into, R.id.btn_login})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_into:
                openWindow(WindowIDDefine.WINDOW_MAIN);
                break;
            case R.id.btn_login:
                Bundle bundle = new Bundle();
                bundle.putInt("openwindow", WindowIDDefine.WINDOW_LOGIN);
                openWindow(WindowIDDefine.WINDOW_MAIN, bundle);
                break;
        }
        SharePresUtil.putBoolean(SharePresUtil.KEY_FIRST_RUN, true);
    }

    @Override
    protected boolean isEnabledSwipeBack() {
        return false;
    }

    @Override
    public boolean onBackPressed() {
        getWindowManager().exitApp();
        return super.onBackPressed();
    }
}
