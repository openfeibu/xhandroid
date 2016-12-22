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
import cn.flyexp.entity.AssnListResponse;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/10/29.
 */
public class AssnListAdapter extends RecyclerView.Adapter<AssnListAdapter.AssnListViewHolder> {

    private Context context;
    private ArrayList<AssnListResponse.AssnListResponseData.AssnResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public AssnListAdapter(Context context, ArrayList<AssnListResponse.AssnListResponseData.AssnResponseData> assnListDatas) {
        this.context = context;
        this.datas = assnListDatas;
    }

    @Override
    public AssnListAdapter.AssnListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AssnListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_assnlist, parent, false));
    }

    @Override
    public void onBindViewHolder(AssnListAdapter.AssnListViewHolder holder, final int position) {
        AssnListResponse.AssnListResponseData.AssnResponseData responseData = datas.get(position);
        holder.tvAssnName.setText(responseData.getAname());
        holder.tvLeader.setText(responseData.getLeader());
        holder.tvDescription.setText(responseData.getIntroduction());
        holder.tvDetail.setText(String.format(context.getResources().getString(R.string.assnlist_detail), responseData.getMember_number(), responseData.getActivity_count(), responseData.getLabel()));
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

    @Override
    public int getItemCount() {
        return datas.size();
    }

     class AssnListViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.tv_assnname)
        TextView tvAssnName;
        @InjectView(R.id.tv_leader)
        TextView tvLeader;
        @InjectView(R.id.tv_description)
        TextView tvDescription;
        @InjectView(R.id.tv_detail)
        TextView tvDetail;

        public AssnListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
