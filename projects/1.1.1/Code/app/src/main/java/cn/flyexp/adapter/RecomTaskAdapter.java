package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.AssnResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class RecomTaskAdapter extends RecyclerView.Adapter<RecomTaskAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<OrderResponse.OrderResponseData> taskData;
    private OnItemClickListener onItemClickListener;

    public RecomTaskAdapter(Context context, ArrayList<OrderResponse.OrderResponseData> taskData) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.taskData = taskData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_recom_task, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderResponse.OrderResponseData responseData = taskData.get(position);
        holder.tv_content.setText(responseData.getDescription());
        holder.tv_fee.setText("￥" + responseData.getFee());
        holder.tv_pleace.setText(responseData.getDestination());
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 33), CommonUtil.dip2px(context, 33))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
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
        return taskData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView iv_avatar;
        TextView tv_fee;
        TextView tv_content;
        TextView tv_pleace;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_fee = (TextView) view.findViewById(R.id.tv_fee);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_pleace = (TextView) view.findViewById(R.id.tv_pleace);
        }
    }
}
