package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.BucketBean;
import cn.flyexp.util.LogUtil;

/**
 * Created by tanxinye on 2016/12/15.
 */
public class BucketsAdapter extends RecyclerView.Adapter<BucketsAdapter.BucketViewHolder> {

    private Context context;
    private ArrayList<BucketBean> datas;
    private OnItemClickLinstener onItemClickLinstener;
    private int selPosition = 0;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public BucketsAdapter(Context context, ArrayList<BucketBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public BucketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BucketViewHolder(LayoutInflater.from(context).inflate(R.layout.item_buckets, parent, false));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(BucketViewHolder holder, final int position) {
        BucketBean bucketBean = datas.get(position);
        holder.tvName.setText(bucketBean.getBucketName());
        holder.tvNum.setText(bucketBean.getImageCount()+"张");
        Glide.with(context).load(bucketBean.getCover()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(holder.imgCover);
        if (position == 0) {
            holder.tvNum.setVisibility(View.GONE);
        }else {
            holder.tvNum.setVisibility(View.VISIBLE);
        }
        if (selPosition == position) {
            holder.imgSel.setVisibility(View.VISIBLE);
        } else {
            holder.imgSel.setVisibility(View.GONE);
        }
        if (onItemClickLinstener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLinstener.onItemClickLinstener(view, position);
                    selPosition = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

     class BucketViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_cover)
        ImageView imgCover;
        @InjectView(R.id.img_sel)
        ImageView imgSel;
        @InjectView(R.id.tv_name)
        TextView tvName;
        @InjectView(R.id.tv_num)
        TextView tvNum;

        public BucketViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
