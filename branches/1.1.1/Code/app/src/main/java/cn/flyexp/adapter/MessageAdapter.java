package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.MessageResponse;
import cn.flyexp.util.DateUtil;

/**
 * Created by tanxinye on 2016/11/4.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MessageResponse.MessageResponseData> datas;

    public MessageAdapter(Context context, ArrayList<MessageResponse.MessageResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageViewHolder viewHolder = (MessageViewHolder) holder;
        MessageResponse.MessageResponseData responseData = datas.get(position);
        viewHolder.tvName.setText(responseData.getName());
        viewHolder.tvContent.setText(responseData.getContent());
        viewHolder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_content)
        TextView tvContent;
        @InjectView(R.id.tv_date)
        TextView tvDate;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
