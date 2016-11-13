package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cn.flyexp.R;
import cn.flyexp.entity.IntegralRecordResponse;
import cn.flyexp.entity.ItemTableBean;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/18.
 */
public class IntegralTableAdapter extends RecyclerView.Adapter<IntegralTableAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<IntegralRecordResponse> data;

    public IntegralTableAdapter(Context context, ArrayList<IntegralRecordResponse> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_integral_table, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView item1;
        private TextView item2;

        public ViewHolder(View view) {
            super(view);
            item1 = (TextView) view.findViewById(R.id.tv_item1);
            item2 = (TextView) view.findViewById(R.id.tv_item2);
        }
    }
}
