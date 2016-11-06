package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import cn.flyexp.R;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.mvc.assn.AssnViewCallBack;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/18.
 */
public class AssnActivityAdapter extends RecyclerView.Adapter<AssnActivityAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private AssnViewCallBack callBack;
    private Context context;

    public AssnActivityAdapter(Context context, ArrayList<AssnActivityResponse.AssnActivityResponseData> data, AssnViewCallBack callBack) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.callBack = callBack;
    }

    public AssnActivityAdapter(Context context, ArrayList<AssnActivityResponse.AssnActivityResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_assn_activitiy, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AssnActivityResponse.AssnActivityResponseData responseData = this.data.get
                (position);
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 24), CommonUtil.dip2px(context, 24))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
        if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
            holder.iv_state.setImageResource(R.mipmap.icon_activity_waiting);
        } else if(DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()){
            holder.iv_state.setImageResource(R.mipmap.icon_activity_end);
        }else {
            holder.iv_state.setImageResource(R.mipmap.icon_activity_ongoing);
        }
        holder.tv_title.setText(responseData.getTitle().trim());
        holder.tv_content.setText(responseData.getContent().trim());
        holder.tv_time.setText(DateUtil.long2Date(DateUtil.date2Long((responseData.getStart_time
                ())), "MM-dd HH:mm") + "至" + DateUtil.long2Date(DateUtil.date2Long(
                (responseData.getEnd_time())), "MM-dd HH:mm"));
        holder.tv_assn.setText(responseData.getAname());
        holder.tv_viewnum.setText("围观：" + responseData.getView_num());
        if (responseData.getImg_url() == null || responseData.getImg_url().equals("")) {
            holder.activitybg.setVisibility(View.GONE);
        } else {
            String[] urls = CommonUtil.splitImageUrl(responseData.getImg_url());
            holder.activitybg.setVisibility(View.VISIBLE);
            Picasso.with(context).load(urls[0]).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 100), CommonUtil.dip2px(context, 100))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().into(holder.activitybg);
        }
        if (callBack != null) {
            holder.tv_assn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.introduceEnter(responseData.getAid());
                }
            });
            holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.introduceEnter(responseData.getAid());
                }
            });
        } else {
            holder.tv_assn.setVisibility(View.GONE);
            holder.iv_avatar.setVisibility(View.GONE);
        }

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_state;
        private TextView tv_title;
        private RoundImageView activitybg;
        private TextView tv_content;
        private TextView tv_time;
        private RoundImageView iv_avatar;
        private TextView tv_assn;
        private TextView tv_viewnum;

        public ViewHolder(View view) {
            super(view);
            iv_state = (ImageView) view.findViewById(R.id.iv_state);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            activitybg = (RoundImageView) view.findViewById(R.id.activitybg);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_assn = (TextView) view.findViewById(R.id.tv_assn);
            tv_viewnum = (TextView) view.findViewById(R.id.tv_viewnum);
        }
    }
}
