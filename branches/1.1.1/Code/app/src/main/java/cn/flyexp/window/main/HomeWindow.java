package cn.flyexp.window.main;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.main.HomeCallback;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.ExtraResponse;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.main.HomePresenter;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.ScreenHelper;
import cn.flyexp.view.UPMarqueeView;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class HomeWindow extends BaseWindow implements HomeCallback.ResponseCallback {

    @InjectView(R.id.upmarqueeview)
    UPMarqueeView upMarqueeView;
    @InjectView(R.id.banner)
    ConvenientBanner banner;

    private ArrayList<String> imgUrls = new ArrayList<>();
    private ArrayList<AdResponse.AdResponseData> adResponseDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.window_home;
    }

    public HomeWindow() {
        HomePresenter homePresenter = new HomePresenter(this);

        initView();
        homePresenter.requestExtra();
        homePresenter.requestAd();
    }

    private void initView() {
        banner.setCanLoop(true);
        banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, imgUrls);
        banner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                WebBean webBean = new WebBean();
                webBean.setTitle(adResponseDatas.get(position).getTitle());
                webBean.setUrl(adResponseDatas.get(position).getAd_url());
                webBean.setRequest(false);
                Bundle bundle = new Bundle();
                bundle.putSerializable("webbean", webBean);
                openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
            }
        });
    }

    public class NetworkImageHolderView implements Holder<String> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
        }
    }

    @OnClick({R.id.tv_soup, R.id.tv_fault, R.id.tv_assn})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_assn:
                openWindow(WindowIDDefine.WINDOW_ASSN);
                break;
            case R.id.tv_soup:
                WebBean bean = new WebBean();
                bean.setRequest(true);
                bean.setTitle(getResources().getString(R.string.chicken_soup));
                bean.setName("soup");
                openWebWindow(bean);
                break;
            case R.id.tv_fault:
                WebBean bean2 = new WebBean();
                bean2.setRequest(true);
                bean2.setTitle(getResources().getString(R.string.net_fault));
                bean2.setName("fault");
                openWebWindow(bean2);
                break;
        }
    }

    private void openWebWindow(WebBean bean) {
        Bundle soupBundle = new Bundle();
        soupBundle.putSerializable("webbean", bean);
        openWindow(WindowIDDefine.WINDOW_WEBVIEW, soupBundle);
    }

    @Override
    public void onResume() {
        if (banner != null) {
            banner.startTurning(3000);
        }
    }

    @Override
    public void onStop() {
        if (banner != null) {
            banner.stopTurning();
        }
    }

    @Override
    public void responseAd(AdResponse response) {
        adResponseDatas.addAll(response.getData());
        for (int i = 0; i < adResponseDatas.size(); i++) {
            imgUrls.add(adResponseDatas.get(i).getAd_image_url());
        }
        banner.notifyDataSetChanged();
        banner.startTurning(3000);
    }

    @Override
    public void responseExtra(ExtraResponse response) {
        ArrayList<ExtraResponse.ExtraResponseData> responseData = response.getData();
        ArrayList<View> views = new ArrayList<>();
        for (int i = 0; i < responseData.size(); i++) {
            TextView textView = new TextView(getContext());
            textView.setText(responseData.get(i).getExtra());
            textView.setTextColor(getResources().getColor(R.color.font_dark));
            textView.setTextSize(ScreenHelper.dip2px(getContext(), 10));
            views.add(textView);
        }
        upMarqueeView.setViews(views);
    }

    @Override
    public void responseRecommendOrder(TaskResponse response) {

    }

    @Override
    public void responseHotActivity(AssnActivityResponse response) {

    }
}
