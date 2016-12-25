package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.TaskResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private ArrayList<TaskResponse.TaskResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public TaskAdapter(Context context, ArrayList<TaskResponse.TaskResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public TaskAdapter.TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskAdapter.TaskViewHolder holder, final int position) {
        final TaskResponse.TaskResponseData responseData = datas.get(position);
        holder.tvAddress.setText(String.format(context.getString(R.string.format_task_address), responseData.getDestination()));
        holder.tvContent.setText(responseData.getDescription());
        if(TextUtils.isEmpty(responseData.getNickname())){
            holder.tvNickName.setText("未被接");
        }else{
            holder.tvNickName.setText(responseData.getNickname());
        }
        holder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
        holder.tvMoney.setText(String.valueOf(responseData.getFee()));
        holder.layoutState.setBackgroundColor(tranfStateColor(responseData.getStatus()));
        holder.imgState.setImageDrawable(tranfStateDrawable(responseData.getStatus()));
        if(!TextUtils.isEmpty(responseData.getAvatar_url())){
            Glide.with(context).load(responseData.getAvatar_url())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(holder.imgAvatar);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLinstener != null) {
                    onItemClickLinstener.onItemClickLinstener(view, position);
                }

            }
        });
    }

    private int tranfStateColor(String status) {
        int color = Color.BLACK;
        if (TextUtils.isEmpty(status)) {
            return color;
        }
        if (status.equals("new")) {
            color = context.getResources().getColor(R.color.task_blue);
        } else if (status.equals("accepted")) {
            color = context.getResources().getColor(R.color.task_green);
        } else {
            color = context.getResources().getColor(R.color.task_gray);
        }
        return color;
    }

    private Drawable tranfStateDrawable(String status) {
        Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_task_end);
        if (TextUtils.isEmpty(status)) {
            return drawable;
        }
        if (TextUtils.equals(status,"new")) {
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_notstarted);
        } else if (TextUtils.equals(status,"accepted")) {
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_ongoing);
        } else if(TextUtils.equals(status,"completed")){
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_end);
        }else if(TextUtils.equals(status,"finish")){
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_bechecked);
        }else{
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_end);
        }
        return drawable;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.layout_state)
        View layoutState;
        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.tv_nickname)
        TextView tvNickName;
        @InjectView(R.id.img_state)
        ImageView imgState;
        @InjectView(R.id.tv_money)
        TextView tvMoney;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_content)
        TextView tvContent;
        @InjectView(R.id.tv_address)
        TextView tvAddress;

        public TaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
