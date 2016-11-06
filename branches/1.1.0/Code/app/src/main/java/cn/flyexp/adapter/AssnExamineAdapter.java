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
import cn.flyexp.entity.AssnExamineListResponse;
import cn.flyexp.entity.AssnMemberResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class AssnExamineAdapter extends RecyclerView.Adapter<AssnExamineAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private ArrayList<AssnExamineListResponse.AssnExamineListResponseData> data;

    public AssnExamineAdapter(Context context,ArrayList<AssnExamineListResponse.AssnExamineListResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_assn_examine, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        AssnExamineListResponse.AssnExamineListResponseData responseData = data.get(position);
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 33), CommonUtil.dip2px(context, 33))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
        holder.tv_realname.setText(responseData.getAr_username());
        holder.tv_cause.setText(responseData.getCauses());
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
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView iv_avatar;
        private TextView tv_cause ;
        private TextView tv_realname;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_cause = (TextView) view.findViewById(R.id.tv_cause);
            tv_realname = (TextView) view.findViewById(R.id.tv_realname);
        }
    }
}
