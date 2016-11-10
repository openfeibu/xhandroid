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
import cn.flyexp.entity.WalletDetailResponse;

/**
 * Created by tanxinye on 2016/10/31.
 */
public class WalletDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<WalletDetailResponse.WalletDetailResponseData> datas;
    private Context context;

    public WalletDetailAdapter(Context context, ArrayList<WalletDetailResponse.WalletDetailResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WalletDetailViewHolder(LayoutInflater.from(context).inflate(R.layout.item_walletdetail, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        WalletDetailViewHolder viewHolder = (WalletDetailViewHolder) holder;
        WalletDetailResponse.WalletDetailResponseData responseData = datas.get(position);
        viewHolder.tvNote.setText(responseData.getTrade_type());
        viewHolder.tvReward.setText(responseData.getFee());
        viewHolder.tvTransactionid.setText(String.format(context.getString(R.string.servicefee), responseData.getOut_trade_no()));
        viewHolder.tvDate.setText(responseData.getTime());
        viewHolder.tvServicefee.setText(String.format(context.getString(R.string.servicefee), responseData.getService_fee()));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class WalletDetailViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.tv_note)
        TextView tvNote;
        @InjectView(R.id.tv_reward)
        TextView tvReward;
        @InjectView(R.id.tv_transactionid)
        TextView tvTransactionid;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_servicefee)
        TextView tvServicefee;

        public WalletDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
