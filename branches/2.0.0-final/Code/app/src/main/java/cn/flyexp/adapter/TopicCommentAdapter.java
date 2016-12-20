package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/11/15.
 */
public class TopicCommentAdapter extends RecyclerView.Adapter<TopicCommentAdapter.TopicCommentViewHolder> {

    private Context context;
    private ArrayList<TopicResponseData.CommentResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);

        void onDelete(int position);
    }


    public TopicCommentAdapter(Context context, ArrayList<TopicResponseData.CommentResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public TopicCommentAdapter.TopicCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicCommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(TopicCommentAdapter.TopicCommentViewHolder holder, final int position) {
        TopicResponseData.CommentResponseData data = datas.get(position);
        String comment = "";
        if (data.getBe_review_id() != 0) {
            comment = "回复 " + data.getBe_review_username() + " : ";
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(comment + data.getContent());
            stringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.light_blue)),
                    3, data.getBe_review_username().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvComment.setText(stringBuilder);
        } else {
            holder.tvComment.setText(data.getContent());
        }
        holder.tvNickName.setText(data.getNickname());
        holder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(data.getCreated_at())));
        Glide.with(context).load(data.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.imgAvatar);
        String openId = SharePresUtil.getString(SharePresUtil.KEY_OPENID);
        if (TextUtils.equals(data.getOpenid(), openId)) {
            holder.tvDelete.setVisibility(View.VISIBLE);
            holder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickLinstener != null) {
                        onItemClickLinstener.onDelete(position);
                    }
                }
            });
        } else {
            holder.tvDelete.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLinstener != null) {
                    onItemClickLinstener.onItemClickLinstener(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class TopicCommentViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.tv_nickname)
        TextView tvNickName;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_comment)
        TextView tvComment;
        @InjectView(R.id.tv_delete)
        TextView tvDelete;

        public TopicCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
