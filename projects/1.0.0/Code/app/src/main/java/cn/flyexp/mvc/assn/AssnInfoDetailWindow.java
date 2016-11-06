package cn.flyexp.mvc.assn;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

import cn.flyexp.R;
import cn.flyexp.adapter.PicDisPlayAdapter;
import cn.flyexp.entity.AssnInfoResponse;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/31 0031.
 */
public class AssnInfoDetailWindow extends AbstractWindow implements View.OnClickListener {

    private AssnViewCallBack callBack;
    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_time;
    private TextView tv_viewnum;
    private TextView tv_assn;
    private RoundImageView iv_sculpture;
    private PicDisPlayAdapter picAdapter;
    private ArrayList<String> imgUrlList = new ArrayList<String>();
    private RecyclerView rv_pic;
    private int aid = -1;

    public AssnInfoDetailWindow(AssnViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_assn_info_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_avatar).setOnClickListener(this);
        findViewById(R.id.tv_assn).setOnClickListener(this);

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_viewnum = (TextView) findViewById(R.id.tv_viewnum);
        tv_assn = (TextView) findViewById(R.id.tv_assn);
        iv_sculpture = (RoundImageView) findViewById(R.id.iv_avatar);

        picAdapter = new PicDisPlayAdapter(getContext(), imgUrlList);
        picAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PicBrowserBean picBrowserBean = new PicBrowserBean();
                picBrowserBean.setImgUrl(imgUrlList);
                picBrowserBean.setCurSelectedIndex(position);
                picBrowserBean.setType(1);
                callBack.picBrowserEnter(picBrowserBean);
            }
        });
        rv_pic = (RecyclerView) findViewById(R.id.rv_pic);
        rv_pic.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_pic.setAdapter(picAdapter);
        rv_pic.setItemAnimator(new DefaultItemAnimator());
    }

    public void initDetail(AssnInfoResponse.AssnInfoResponseData responseData) {
        aid = responseData.getAid();
        Picasso.with(getContext()).load(responseData.getAvatar_url()).into(iv_sculpture);
        tv_title.setText(responseData.getTitle());
        tv_content.setText(responseData.getContent());
        tv_time.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData
                .getCreated_at())));
        tv_viewnum.setText("浏览" + responseData.getView_num());
        tv_assn.setText(responseData.getAname());

        if (responseData.getImg_url() == null || responseData.getImg_url().equals("")) {
            rv_pic.setVisibility(View.GONE);
        } else {
            String[] urls = CommonUtil.splitImageUrl(responseData.getImg_url());
            rv_pic.setVisibility(View.VISIBLE);
            imgUrlList.addAll(Arrays.asList(urls));
            picAdapter.notifyDataSetChanged();
        }
        aid = responseData.getAid();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_assn:
            case R.id.iv_avatar:
                if (aid == -1) {
                    return;
                }
                callBack.introduceEnter(aid);
                break;
        }
    }
}
