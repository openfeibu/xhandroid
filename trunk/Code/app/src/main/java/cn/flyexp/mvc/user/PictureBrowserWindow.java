package cn.flyexp.mvc.user;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.net.NetWork;
import cn.flyexp.permission.PermissionHandler;
import cn.flyexp.permission.PermissionTools;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.view.ZoomImageView;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by txy on 2016/8/5 0005.
 */
public class PictureBrowserWindow extends AbstractWindow implements View.OnClickListener {

    private UserViewCallBack callBack;
    private ViewPager viewPager;
    private ArrayList<String> imgUrl = new ArrayList<String>();
    private ArrayList<View> views = new ArrayList<View>();
    private PagerAdapter pagerAdapter;
    private int type;
    private int max;
    private TextView tv_index;
    private TextView tv_save;

    public PictureBrowserWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_picbrowser);
        tv_save = (TextView) findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);

        tv_index = (TextView) findViewById(R.id.tv_index);
        viewPager = (ViewPager) findViewById(R.id.vp_browser);
        viewPager.setAdapter(pagerAdapter = new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View layout = views.get(position);
                final ZoomImageView image = (ZoomImageView) layout.findViewById(R.id.img);
                final ContentLoadingProgressBar progressBar = (ContentLoadingProgressBar) layout.findViewById(R.id.progressBar);
                switch (type) {
                    case 0:
                        Picasso.with(getContext()).load(new File(imgUrl.get(position))).config(Bitmap.Config.RGB_565).resize(CommonUtil.getScreenWidth(getContext()), CommonUtil.dip2px(getContext(), 300)).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
                        break;
                    case 1:
                        progressBar.setVisibility(VISIBLE);
                        Picasso picasso = new Picasso.Builder(getContext()).downloader(new OkHttp3Downloader(NetWork.getInstance().getClient())).build();
                        picasso.with(getContext()).load(imgUrl.get(position)).config(Bitmap.Config.RGB_565).memoryPolicy(MemoryPolicy.NO_CACHE).into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(GONE);
                                tv_save.setVisibility(VISIBLE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(GONE);
                                tv_save.setVisibility(GONE);
                                WindowHelper.showToast(getContext().getString(R.string.loading_pic_failure));
                            }
                        });
                        break;
                }
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
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
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                tv_index.setText(position + 1 + "/" + max);
                tv_save.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View layout = views.get(position);
                        final ZoomImageView image = (ZoomImageView) layout.findViewById(R.id.img);
                        PermissionTools.requestPermission(getContext(), new PermissionHandler.PermissionCallback() {
                            public void onSuccess() {
                                WindowHelper.showAlertDialog(getContext().getString(R.string.save_pic), getContext().getString(R.string.cancel), getContext().getString(R.string.save), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (BitmapUtil.saveImageToGallery(((BitmapDrawable) image.getDrawable()).getBitmap())) {
                                            WindowHelper.showToast(getContext().getString(R.string.save_success));
                                        } else {
                                            WindowHelper.showToast(getContext().getString(R.string.save_failure));
                                        }
                                    }
                                });
                            }

                            public void onFail(int[] ids) {
                            }

                            public void onCancel() {
                            }

                            public void goSetting() {
                            }
                        }, new int[]{PermissionHandler.PERMISSION_FILE});
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initData(PicBrowserBean picBrowserBean) {
        if (picBrowserBean == null) {
            return;
        }
        type = picBrowserBean.getType();
        imgUrl.addAll(picBrowserBean.getImgUrl());
        max = imgUrl.size();
        for (int i = 0; i < imgUrl.size(); i++) {
            View layout = LayoutInflater.from(getContext()).inflate(R.layout.layout_picbrowser, null);
            views.add(layout);
        }
        tv_index.setText(picBrowserBean.getCurSelectedIndex() + 1 + "/" + max);
        viewPager.setCurrentItem(picBrowserBean.getCurSelectedIndex());
        pagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }
}
