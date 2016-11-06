package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.WalletDetailResponse;

/**
 * Created by txy on 2016/6/5.
 */
public class WalletDetailAdapter extends RecyclerView.Adapter<WalletDetailAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<WalletDetailResponse.WalletDetailResponseData> data = new ArrayList<>();

    public WalletDetailAdapter(Context context, ArrayList<WalletDetailResponse.WalletDetailResponseData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_walletdetail, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        WalletDetailResponse.WalletDetailResponseData responseData = data.get(position);
        holder.tv_note.setText(responseData.getTrade_type());
        holder.tv_date.setText(responseData.getTime());
        holder.tv_reward.setText(responseData.getFee());
        holder.tv_servicefee.setText("服务费：" + responseData.getService_fee() + "元");
        holder.tv_orderid.setText("交易编号：" + responseData.getOut_trade_no());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_note;
        private TextView tv_date;
        private TextView tv_reward;
        private TextView tv_orderid;
        private TextView tv_servicefee;

        public ViewHolder(View view) {
            super(view);
            tv_note = (TextView) view.findViewById(R.id.tv_note);
            tv_orderid = (TextView) view.findViewById(R.id.tv_orderid);
            tv_servicefee = (TextView) view.findViewById(R.id.tv_servicefee);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_reward = (TextView) view.findViewById(R.id.tv_reward);
        }
    }
}
