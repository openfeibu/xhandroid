package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.IntegralRecordResponse;
import cn.flyexp.entity.WalletDetailResponse;

/**
 * Created by txy on 2016/6/5.
 */
public class IntegralRecordAdapter extends RecyclerView.Adapter<IntegralRecordAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<IntegralRecordResponse.IntegralRecordResponseData> data = new ArrayList<>();

    public IntegralRecordAdapter(Context context, ArrayList<IntegralRecordResponse.IntegralRecordResponseData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_integralrecord, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        IntegralRecordResponse.IntegralRecordResponseData responseData = data.get(position);
        holder.tv_note.setText(responseData.getObtain_type());
        holder.tv_date.setText(responseData.getUpdated_at());
        holder.tv_reward.setText(responseData.getScore());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_note;
        private TextView tv_date;
        private TextView tv_reward;

        public ViewHolder(View view) {
            super(view);
            tv_note = (TextView) view.findViewById(R.id.tv_note);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_reward = (TextView) view.findViewById(R.id.tv_reward);
        }
    }
}
