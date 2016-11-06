package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.TopicCommentResponse;
import cn.flyexp.mvc.topic.TopicViewCallBack;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/21.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<TopicCommentResponse.TopicCommentResponseData> data;
    private OnItemClickListener onItemClickListener;
    private TopicViewCallBack callBack;

    public CommentAdapter(Context context, ArrayList<TopicCommentResponse.TopicCommentResponseData> data, TopicViewCallBack callBack) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.callBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_topic_comment, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final TopicCommentResponse.TopicCommentResponseData topicCommentResponseData = data.get(position);
        data.get(position);
        if (!topicCommentResponseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(topicCommentResponseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 32), CommonUtil.dip2px(context, 32))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.taWindowEnter(topicCommentResponseData.getOpenid());
            }
        });

        String beCommentName = topicCommentResponseData.getBe_review_username();
        if (beCommentName == null || beCommentName.equals("")) {
            holder.tv_name.setText(topicCommentResponseData.getNickname());
            holder.tv_comm.setVisibility(View.GONE);
            holder.tv_becomm.setVisibility(View.GONE);
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.taWindowEnter(topicCommentResponseData.getOpenid());
                }
            });
        } else {
            holder.tv_name.setText(topicCommentResponseData.getNickname());
            holder.tv_comm.setVisibility(View.VISIBLE);
            holder.tv_becomm.setVisibility(View.VISIBLE);
            holder.tv_becomm.setText(topicCommentResponseData.getBe_review_username());
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.taWindowEnter(topicCommentResponseData.getOpenid());
                }
            });
        }


        holder.tv_time.setText(DateUtil.getStandardDate(DateUtil.date2Long(topicCommentResponseData.getCreated_at())));
        holder.tv_content.setText(topicCommentResponseData.getContent());
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
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_content;
        private TextView tv_becomm;
        private TextView tv_comm;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_becomm = (TextView) view.findViewById(R.id.tv_becomm);
            tv_comm = (TextView) view.findViewById(R.id.tv_comm);
        }
    }
}
