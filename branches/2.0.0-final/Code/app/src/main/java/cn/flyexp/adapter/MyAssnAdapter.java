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
import cn.flyexp.entity.MyAssnResponse;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class MyAssnAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<MyAssnResponse.MyAssnResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public MyAssnAdapter(Context context, ArrayList<MyAssnResponse.MyAssnResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAssnViewHolder(LayoutInflater.from(context).inflate(R.layout.item_assnlist, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyAssnResponse.MyAssnResponseData responseData = datas.get(position);
        MyAssnViewHolder viewHolder = (MyAssnViewHolder) holder;
        viewHolder.tvAssnName.setText(responseData.getAname());
        viewHolder.tvLeader.setText(responseData.getLeader());
        viewHolder.tvDescription.setText(responseData.getIntroduction());
        viewHolder.tvDetail.setText(String.format(context.getResources().getString(R.string.assnlist_detail), responseData.getMember_number(), responseData.getActivity_count(), responseData.getLabel()));
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

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class MyAssnViewHolder extends RecyclerView.ViewHolder {

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

        public MyAssnViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
