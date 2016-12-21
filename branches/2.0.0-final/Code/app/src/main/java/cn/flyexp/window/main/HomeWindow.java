package cn.flyexp.window.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import cn.flyexp.adapter.AssnActivityAdapter;
import cn.flyexp.adapter.RecommendTaskAdapter;
import cn.flyexp.callback.main.HomeCallback;
import cn.flyexp.entity.AdResponse;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.entity.UpdateRequest;
import cn.flyexp.entity.UpdateResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.main.HomePresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.PackageUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.DividerItemDecoration;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class HomeWindow extends BaseWindow implements HomeCallback.ResponseCallback {

    @InjectView(R.id.banner)
    ConvenientBanner banner;
    @InjectView(R.id.rv_recommend_task)
    RecyclerView rvRecommendTask;
    @InjectView(R.id.rv_assn_acti)
    RecyclerView rvAssnActi;
    @InjectView(R.id.layout_recommend_task)
    View layoutRecommendTask;
    @InjectView(R.id.layout_assn_acti)
    View layoutAssnActi;

    private View updateLayout;
    private ArrayList<String> imgUrls = new ArrayList<>();
    private ArrayList<AdResponse.AdResponseData> adResponseDatas = new ArrayList<>();
    private ArrayList<TaskResponse.TaskResponseData> recommendTaskDatas = new ArrayList<>();
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> hotActiDatas = new ArrayList<>();
    private HomePresenter homePresenter;
    private RecommendTaskAdapter recommendTaskAdapter;
    private AssnActivityAdapter assnActivityAdapter;
    private UpdateResponse.UpdateResponseData updateData;
    private AlertDialog dialog;
    private TextView tvMsg;

    @Override
    protected int getLayoutId() {
        return R.layout.window_home;
    }

    public HomeWindow() {
        homePresenter = new HomePresenter(this);
        initView();
    }

    @Override
    public void onStart() {
        homePresenter.requestAd();
        homePresenter.requestRecommendTask();
        homePresenter.requestHotActivity();
        long lastTime = SharePresUtil.getLong(SharePresUtil.KEY_LAST_TIME_UPDATE);
        //超过一个星期检查更新
        if (System.currentTimeMillis() - lastTime > 1000 * 60 * 60 * 24 * 7) {
            LogUtil.e("last");
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.setPlatform("and");
            homePresenter.requestCheckUpdate(updateRequest);
            SharePresUtil.putLong(SharePresUtil.KEY_LAST_TIME_UPDATE,System.currentTimeMillis());
        }else{
            LogUtil.e("last no");
        }
    }

    @Override
    public void onRenew() {
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
        banner.setPageIndicator(new int[]{R.mipmap.icon_carousel_white, R.mipmap.icon_carousel_black});

        recommendTaskAdapter = new RecommendTaskAdapter(getContext(), recommendTaskDatas);
        recommendTaskAdapter.setOnItemClickLinstener(new RecommendTaskAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskDetail", recommendTaskDatas.get(position));
                openWindow(WindowIDDefine.WINDOW_TASK_DETAIL, bundle);
            }
        });
        rvRecommendTask.setAdapter(recommendTaskAdapter);
        rvRecommendTask.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvRecommendTask.setHasFixedSize(true);
        rvRecommendTask.setNestedScrollingEnabled(false);

        assnActivityAdapter = new AssnActivityAdapter(getContext(), hotActiDatas);
        assnActivityAdapter.setOnItemClickLinstener(new AssnActivityAdapter.OnItemClickLinstener() {
            @Override
            public void onItemClickLinstener(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("assnacti", hotActiDatas.get(position));
                openWindow(WindowIDDefine.WINDOW_ASSNACTI_DETAIL, bundle);
            }
        });
        rvAssnActi.setAdapter(assnActivityAdapter);
        rvAssnActi.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAssnActi.addItemDecoration(new DividerItemDecoration(getContext()));
        rvAssnActi.setHasFixedSize(true);
        rvAssnActi.setNestedScrollingEnabled(false);
        updateLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update, null);
        updateLayout.findViewById(R.id.tv_yes).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(updateData.getDownload());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                getContext().startActivity(intent);
            }
        });
        updateLayout.findViewById(R.id.tv_no).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        tvMsg = (TextView) updateLayout.findViewById(R.id.tv_msg);
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
            Glide.with(context).load(data).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
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
    protected boolean isEnabledSwipeBack() {
        return false;
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void requestFailure() {
        super.requestFailure();
    }

    @Override
    public void requestFinish() {
        super.requestFinish();
    }

    @Override
    public void responseCheckUpdate(UpdateResponse response) {
        final UpdateResponse.UpdateResponseData responseData = response.getData();
        int downloadVersionCode = responseData.getCode();
        int versionCode = PackageUtil.getVersionCode(getContext());
        if (downloadVersionCode > versionCode) {
            updateData = response.getData();
            showDialog();
        }else{
            dialog = null;
            updateLayout = null;
        }
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new AlertDialog.Builder(getContext()).setView(updateLayout).create();
        }
        tvMsg.setText(updateData.getDetail());
        dialog.show();
    }

    @Override
    public void responseAd(AdResponse response) {
        adResponseDatas.clear();
        adResponseDatas.addAll(response.getData());
        for (int i = 0; i < adResponseDatas.size(); i++) {
            imgUrls.add(adResponseDatas.get(i).getAd_image_url());
        }
        banner.notifyDataSetChanged();
        banner.startTurning(3000);
    }

    @Override
    public void responseRecommendTask(TaskResponse response) {
        recommendTaskDatas.clear();
        if (response.getData().isEmpty()) {
            layoutRecommendTask.setVisibility(GONE);
            rvRecommendTask.setVisibility(GONE);
        } else {
            layoutRecommendTask.setVisibility(VISIBLE);
            rvRecommendTask.setVisibility(VISIBLE);
            recommendTaskDatas.addAll(response.getData());
            recommendTaskAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void responseHotActivity(AssnActivityResponse response) {
        hotActiDatas.clear();
        if (response.getData().isEmpty()) {
            layoutAssnActi.setVisibility(GONE);
            rvAssnActi.setVisibility(GONE);
        } else {
            layoutAssnActi.setVisibility(VISIBLE);
            rvAssnActi.setVisibility(VISIBLE);
            hotActiDatas.addAll(response.getData());
            assnActivityAdapter.notifyDataSetChanged();
        }
    }
}
