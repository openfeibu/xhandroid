package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.MyTaskViewHolder> {

    private Context context;
    private ArrayList<MyTaskResponse.MyTaskResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public MyTaskAdapter(Context context, ArrayList<MyTaskResponse.MyTaskResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyTaskAdapter.MyTaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(MyTaskAdapter.MyTaskViewHolder holder, final int position) {
        MyTaskResponse.MyTaskResponseData responseData = datas.get(position);
        holder.tvAddress.setText(responseData.getDestination());
        holder.tvContent.setText(responseData.getDescription());
        holder.tvNickName.setText(responseData.getNickname());
        holder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
        holder.tvMoney.setText(String.valueOf(responseData.getFee()));
        holder.layoutState.setBackgroundColor(tranfStateColor(responseData.getStatus()));
        holder.imgState.setImageDrawable(tranfStateDrawable(responseData.getStatus()));
        Glide.with(context).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(holder.imgAvatar);
        if (onItemClickLinstener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLinstener.onItemClickLinstener(view, position);
                }
            });
        }
    }


    private Drawable tranfStateDrawable(String status) {
        Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_task_end);
        if (TextUtils.isEmpty(status)) {
            return drawable;
        }
        if (status.equals("new")) {
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_notstarted);
        } else if (status.equals("accepted")) {
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_ongoing);
        } else {
            drawable = context.getResources().getDrawable(R.mipmap.icon_task_end);
        }
        return drawable;
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

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyTaskViewHolder extends RecyclerView.ViewHolder {

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

        public MyTaskViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

}
