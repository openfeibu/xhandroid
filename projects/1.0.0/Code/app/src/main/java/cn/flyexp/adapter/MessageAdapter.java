package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/6/5.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MessageResponse.MessageResponseData> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public MessageAdapter(Context context, ArrayList<MessageResponse.MessageResponseData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_message, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MessageResponse.MessageResponseData responseData = data.get(position);
        holder.tv_name.setText(responseData.getName());
        holder.tv_content.setText(responseData.getContent());
        holder.tv_time.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
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

        private TextView tv_name;
        private TextView tv_content;
        private TextView tv_time;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}
