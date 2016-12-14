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
import cn.flyexp.entity.AssnMemberListResponse;

/**
 * Created by tanxinye on 2016/11/3.
 */
public class AssnMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AssnMemberListResponse.AssnMemberResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public AssnMemberAdapter(Context context, ArrayList<AssnMemberListResponse.AssnMemberResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AssnMemberViewHolder(LayoutInflater.from(context).inflate(R.layout.item_assnmember, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AssnMemberViewHolder viewHolder = (AssnMemberViewHolder) holder;
        AssnMemberListResponse.AssnMemberResponseData responseData = datas.get(position);
        viewHolder.tvNickName.setText(responseData.getNickname());
        viewHolder.tvPosition.setText(tranfLevel(responseData.getLevel()));
        viewHolder.tvRealName.setText(responseData.getRealname());
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

    private String tranfLevel(int level) {
        int rid = 0;
        switch (level) {
            case 0:
                rid = R.string.member;
                break;
            case 1:
                rid = R.string.president;
                break;
            case 2:
                rid = R.string.vice_president;
                break;
        }
        if (rid != 0) {
            return context.getResources().getString(rid);
        } else {
            return "";
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class AssnMemberViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        ImageView imgAvatar;
        @InjectView(R.id.tv_nickname)
        TextView tvNickName;
        @InjectView(R.id.tv_position)
        TextView tvPosition;
        @InjectView(R.id.tv_realname)
        TextView tvRealName;


        public AssnMemberViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
