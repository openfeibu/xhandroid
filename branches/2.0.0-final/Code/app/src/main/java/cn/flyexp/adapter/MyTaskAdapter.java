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
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MyTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyTaskViewHolder(LayoutInflater.from(context).inflate(R.layout.item_task, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyTaskViewHolder viewHolder = (MyTaskViewHolder) holder;
        MyTaskResponse.MyTaskResponseData responseData = datas.get(position);
        viewHolder.tvAddress.setText(responseData.getDestination());
        viewHolder.tvContent.setText(responseData.getDescription());
        viewHolder.tvNickName.setText(responseData.getNickname());
        viewHolder.tvState.setText(tranfStateText(responseData.getStatus()));
        viewHolder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
//        viewHolder.tvMoney.setText(String.format(context.getResources().getString(R.string.hint_task_money), String.valueOf(responseData.getFee())));
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

    private int tranfStateColor(String status) {
        int color = 0;
        if (status.equals("new")) {
            color = context.getResources().getColor(R.color.task_green);
        } else if (status.equals("accepted")) {
            color = context.getResources().getColor(R.color.task_blue);
        } else {
            color = context.getResources().getColor(R.color.task_gray);
        }
        return color;
    }

    private String tranfStateText(String status) {
        String str = "";
        if (status.equals("new")) {
            str = context.getResources().getString(R.string.task_state_new);
        } else if (status.equals("finish")) {
            str = context.getResources().getString(R.string.task_state_finish);
        } else if (status.equals("accepted")) {
            str = context.getResources().getString(R.string.task_state_accepted);
        } else if (status.equals("completed")) {
            str = context.getResources().getString(R.string.task_state_completed);
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyTaskViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.tv_nickname)
        TextView tvNickName;
        @InjectView(R.id.tv_state)
        TextView tvState;
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
            ButterKnife.inject(this,itemView);
        }
    }

}
