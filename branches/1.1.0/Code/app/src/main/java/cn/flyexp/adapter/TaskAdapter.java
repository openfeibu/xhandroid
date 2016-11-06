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

import cn.flyexp.R;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<OrderResponse.OrderResponseData> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private Context context;

    public TaskAdapter(Context context, ArrayList<OrderResponse.OrderResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_task, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OrderResponse.OrderResponseData responseData = data.get(position);
        holder.tv_nickname.setText(responseData.getNickname());
        holder.tv_surplustime.setText(DateUtil.countDown(DateUtil.addOneDay(DateUtil.date2Long(responseData.getCreated_at()))));
        holder.tv_description.setText(responseData.getDescription());
        holder.tv_fee.setText("￥" + responseData.getFee());
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 32), CommonUtil.dip2px(context, 32))
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
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView iv_avatar;
        private TextView tv_nickname;
        private TextView tv_surplustime;
        private TextView tv_description;
        private TextView tv_fee;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_surplustime = (TextView) view.findViewById(R.id.tv_surplustime);
            tv_description = (TextView) view.findViewById(R.id.tv_description);
            tv_fee = (TextView) view.findViewById(R.id.tv_fee);
        }
    }
}
