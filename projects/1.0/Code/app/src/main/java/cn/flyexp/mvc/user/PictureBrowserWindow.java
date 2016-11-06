package cn.flyexp.mvc.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.view.ZoomImageView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by txy on 2016/8/5 0005.
 */
public class PictureBrowserWindow extends AbstractWindow {

    private UserViewCallBack callBack;
    private ViewPager viewPager;
    private ArrayList<String> imgUrl = new ArrayList<String>();
    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private CircleIndicator indicator;
    private PagerAdapter pagerAdapter;
    private int type;

    public PictureBrowserWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_picbrowser);
        findViewById(R.id.picbrowserLayout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideWindow(true);
            }
        });

        indicator = (CircleIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.vp_browser);
        viewPager.setAdapter(pagerAdapter = new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {

                switch (type) {
                    case 0:
                        Picasso.with(getContext()).load(new File(imgUrl.get(position))).config(Bitmap.Config.RGB_565).resize(CommonUtil.getScreenWidth(getContext()), CommonUtil.dip2px(getContext(), 300)).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageViews.get(position));
//                        Picasso.with(getContext()).load(new File(imgUrl.get(position))).config(Bitmap.Config.RGB_565).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView);
                        break;
                    case 1:
                        Picasso.with(getContext()).load(imgUrl.get(position)).config(Bitmap.Config.RGB_565).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageViews.get(position));
                        break;
                }
                container.addView(imageViews.get(position));
                return imageViews.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(imageViews.get(position));
            }

            @Override
            public int getCount() {
                return imgUrl.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
    }

    public void initData(PicBrowserBean picBrowserBean) {
        if (picBrowserBean == null) {
            return;
        }
        type = picBrowserBean.getType();
        imgUrl.addAll(picBrowserBean.getImgUrl());
        for (int i = 0; i < imgUrl.size(); i++) {
            ZoomImageView imageView = new ZoomImageView(getContext());
            imageViews.add(imageView);
        }
        viewPager.setCurrentItem(picBrowserBean.getCurSelectedIndex());
        indicator.setViewPager(viewPager);
        pagerAdapter.notifyDataSetChanged();
    }
}
