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
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnExamineListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AssnExamineListResponse.AssnExamineListResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public AssnExamineListAdapter(Context context, ArrayList<AssnExamineListResponse.AssnExamineListResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AsssnExamineListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_assn_examinelist, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AssnExamineListResponse.AssnExamineListResponseData responseData = datas.get(position);
        AsssnExamineListViewHolder viewHolder = (AsssnExamineListViewHolder) holder;
        viewHolder.tvRealName.setText(responseData.getAr_username());
        viewHolder.tvCause.setText(responseData.getCauses());
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
        return datas == null ? 0 : datas.size();
    }

    public class AsssnExamineListViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.tv_realname)
        TextView tvRealName;
        @InjectView(R.id.tv_cause)
        TextView tvCause;

        public AsssnExamineListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
