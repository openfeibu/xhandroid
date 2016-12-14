package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/12/9.
 */
public class RecommendTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TaskResponse.TaskResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public RecommendTaskAdapter(Context context, ArrayList<TaskResponse.TaskResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recommend_task, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TaskResponse.TaskResponseData responseData = datas.get(position);
        RecommendTaskViewHolder viewHolder = (RecommendTaskViewHolder) holder;
        viewHolder.tvContent.setText(responseData.getDescription());
        viewHolder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
        viewHolder.tvAddress.setText(responseData.getDestination());
        viewHolder.tvMoney.setText(responseData.getFee() + "元");
        Glide.with(context).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(viewHolder.imgAvatar);
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
        return datas.size();
    }

    class RecommendTaskViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.tv_content)
        TextView tvContent;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_address)
        TextView tvAddress;
        @InjectView(R.id.tv_money)
        TextView tvMoney;

        public RecommendTaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
