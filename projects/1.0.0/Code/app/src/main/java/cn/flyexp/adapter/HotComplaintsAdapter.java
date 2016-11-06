package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/6/5.
 */
public class HotComplaintsAdapter extends RecyclerView.Adapter<HotComplaintsAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<OrderResponse.OrderResponseData> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public HotComplaintsAdapter(Context context, ArrayList<OrderResponse.OrderResponseData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_hottopic, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

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
        return 6;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }
}
