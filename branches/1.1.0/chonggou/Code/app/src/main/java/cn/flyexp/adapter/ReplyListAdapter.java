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
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.AssnResponse;
import cn.flyexp.entity.ReplyListResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class ReplyListAdapter extends RecyclerView.Adapter<ReplyListAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private ArrayList<ReplyListResponse.ReplyListResponseData> data;

    public ReplyListAdapter(Context context, ArrayList<ReplyListResponse.ReplyListResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_replylist, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ReplyListResponse.ReplyListResponseData responseData = data.get(position);
        holder.tv_nickname.setText(responseData.getNickname());
        holder.tv_date.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
        holder.tv_comment.setText(responseData.getContent());
        holder.tv_content.setText(responseData.getObject_content());
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 33), CommonUtil.dip2px(context, 33))
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
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView iv_avatar;
        TextView tv_nickname;
        TextView tv_date;
        TextView tv_comment;
        TextView tv_reply;
        TextView tv_content;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_reply = (TextView) view.findViewById(R.id.tv_reply);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
