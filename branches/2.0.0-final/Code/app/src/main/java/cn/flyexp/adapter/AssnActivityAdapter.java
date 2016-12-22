package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/11/1.
 */
public class AssnActivityAdapter extends RecyclerView.Adapter<AssnActivityAdapter.AssnActivityViewHolder> {

    private Context context;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public AssnActivityAdapter(Context context, ArrayList<AssnActivityResponse.AssnActivityResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public AssnActivityAdapter.AssnActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AssnActivityViewHolder(LayoutInflater.from(context).inflate(R.layout.item_assnacti, parent, false));
    }

    @Override
    public void onBindViewHolder(AssnActivityAdapter.AssnActivityViewHolder holder, final int position) {
        AssnActivityResponse.AssnActivityResponseData responseData = datas.get(position);
        holder.tvTitle.setText(responseData.getTitle().trim());
        holder.tvAssnName.setText(responseData.getAname());
        holder.tvViewNum.setText(String.format(context.getResources().getString(R.string.assnactiv_viewnum), responseData.getView_num()));
        holder.tvDate.setText(DateUtil.dateFormat(responseData.getEnd_time(), "MM-dd"));
        if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
            holder.imgState.setImageResource(R.mipmap.icon_activities_notstarted);
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.light_blue));
        } else if (DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()) {
            holder.imgState.setImageResource(R.mipmap.icon_activities_end);
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.font_light));
        } else {
            holder.imgState.setImageResource(R.mipmap.icon_activities_ongoing);
            holder.tvDate.setTextColor(context.getResources().getColor(R.color.light_red));
        }
        Glide.with(context).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(holder.imgAvatar);
        Glide.with(context).load(responseData.getImg_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).fitCenter().into(holder.imgActi);
        if (onItemClickLinstener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLinstener.onItemClickLinstener(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

     class AssnActivityViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.img_state)
        ImageView imgState;
        @InjectView(R.id.img_acti)
        ImageView imgActi;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_assnname)
        TextView tvAssnName;
        @InjectView(R.id.tv_viewnum)
        TextView tvViewNum;

        public AssnActivityViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
