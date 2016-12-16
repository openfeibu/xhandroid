package cn.flyexp.window.other;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.constants.Constants;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.util.LogUtil;
import cn.flyexp.window.BaseWindow;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by tanxinye on 2016/11/15.
 */
public class PicBrowserWindow extends BaseWindow {

    @InjectView(R.id.vp_pic)
    ViewPager vpPic;
    @InjectView(R.id.tv_num)
    TextView tvNum;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.tv_delete)
    TextView tvDelete;

    private ArrayList<String> uri;
    private ArrayList<View> views = new ArrayList<>();
    private int position;
    private String type;
    private int currPosition;
    private PagerAdapter pagerAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_picbrowser;
    }

    public PicBrowserWindow(Bundle bundle) {
        Config config = new Config();
        config.setAnimStyle(config.ANIMSTYLE_FADE);
        setConfig(config);

        uri = bundle.getStringArrayList("uri");
        position = bundle.getInt("position");
        type = bundle.getString("type");
        initView();
    }

    private void initView() {
        if (TextUtils.equals(type, Constants.LOCAL)) {
            tvSave.setVisibility(GONE);
            tvDelete.setVisibility(VISIBLE);
        } else if(TextUtils.equals(type, Constants.NET)){
            tvSave.setVisibility(VISIBLE);
            tvDelete.setVisibility(GONE);
        } else if(TextUtils.equals(type, Constants.GALLERY)){
            tvSave.setVisibility(GONE);
            tvDelete.setVisibility(GONE);
            tvNum.setVisibility(GONE);
        }

        for (int i = 0; i < uri.size(); i++) {
            views.add(LayoutInflater.from(getContext()).inflate(R.layout.layout_pic, null));
        }

        pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView photoView = (PhotoView) views.get(position).findViewById(R.id.pv_pic);
                Glide.with(getContext()).load(uri.get(position)).diskCacheStrategy(DiskCacheStrategy.ALL).into(photoView);
                container.addView(views.get(position));
                return views.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getItemPosition(Object object) {
                return PagerAdapter.POSITION_NONE;
            }
        };
        vpPic.setAdapter(pagerAdapter);
        vpPic.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currPosition = position;
                tvNum.setText(position + 1 + "/" + uri.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvNum.setText(position + 1 + "/" + uri.size());
        vpPic.setCurrentItem(position, false);
    }

    @OnClick({R.id.img_back,R.id.tv_save, R.id.tv_delete})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_save:
                PhotoView photoView = (PhotoView) views.get(position).findViewById(R.id.pv_pic);
                break;
            case R.id.tv_delete:
                Message message = Message.obtain();
                message.what = NotifyIDDefine.NOTICE_DELETE_PHOTO;
                message.arg1 = currPosition;
                getNotifyManager().notify(message);
                if (uri.size() == 0) {
                    hideWindow(true);
                } else {
                    views.remove(currPosition);
                    pagerAdapter.notifyDataSetChanged();
                    tvNum.setText(vpPic.getCurrentItem() + 1 + "/" + uri.size());
                    currPosition = vpPic.getCurrentItem();
                }
                break;
        }
    }

}
