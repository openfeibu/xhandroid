package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/6/5.
 */
public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MyTaskResponse.MyTaskResponseData> data = new ArrayList<MyTaskResponse.MyTaskResponseData>();
    private OnItemClickListener onItemClickListener;

    public MyTaskAdapter(Context context, ArrayList<MyTaskResponse.MyTaskResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_mytask, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MyTaskResponse.MyTaskResponseData responseData = data.get(position);
        if(responseData.getNickname().equals("")){
            holder.tv_nickname.setText("还没有人接");
            holder.tv_nickname.setTextColor(context.getResources().getColor(R.color.font_brown_light));
        }else{
            holder.tv_nickname.setText(responseData.getNickname());
            holder.tv_nickname.setTextColor(context.getResources().getColor(R.color.font_brown_dark));
        }
        holder.tv_state.setText(stateTrantf(responseData.getStatus()));
        holder.tv_description.setText(responseData.getDescription());
        holder.tv_date.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData
                .getCreated_at())));

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    private String stateTrantf(String status) {
        String sta = "";
        if (status.equals("accepted")) {
            sta = "进行中";
        } else if (status.equals("finish")) {
            sta = "待结算";
        } else if (status.equals("completed")) {
            sta = "已完成";
        }
        return sta;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_nickname;
        private TextView tv_state;
        private TextView tv_description;
        private TextView tv_date;

        public ViewHolder(View view) {
            super(view);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_state = (TextView) view.findViewById(R.id.tv_state);
            tv_description = (TextView) view.findViewById(R.id.tv_description);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
        }
    }
}
