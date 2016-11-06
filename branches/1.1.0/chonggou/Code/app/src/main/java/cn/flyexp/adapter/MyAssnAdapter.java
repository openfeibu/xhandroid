package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.MyAssnResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class MyAssnAdapter extends RecyclerView.Adapter<MyAssnAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private ArrayList<MyAssnResponse.MyAssnResponseData> data;

    public MyAssnAdapter(Context context, ArrayList<MyAssnResponse.MyAssnResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_assn, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MyAssnResponse.MyAssnResponseData responseData = data.get(position);

        holder.tv_assn.setText(responseData.getAname());
        holder.tv_leader.setText(" · " + responseData.getLeader());
        holder.tv_description.setText(responseData.getAssociation_level().trim());
        holder.tv_detail.setText(responseData.getMember_number() + "人活跃 · " + responseData.getActivity_count() + "个活动" + responseData.getLabel());
        if (!TextUtils.isEmpty(responseData.getAvatar_url())) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 50), CommonUtil.dip2px(context, 50))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
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
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView iv_avatar;
        private TextView tv_assn;
        private TextView tv_leader;
        private TextView tv_description;
        private TextView tv_detail;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_assn = (TextView) view.findViewById(R.id.tv_assn);
            tv_leader = (TextView) view.findViewById(R.id.tv_leader);
            tv_description = (TextView) view.findViewById(R.id.tv_description);
            tv_detail = (TextView) view.findViewById(R.id.tv_detail);
        }
    }
}
