package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.TopicResponseData;

/**
 * Created by tanxinye on 2016/11/15.
 */
public class TopicCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TopicResponseData.CommentResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }


    public TopicCommentAdapter(Context context, ArrayList<TopicResponseData.CommentResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicCommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TopicCommentViewHolder viewHolder = (TopicCommentViewHolder) holder;
        TopicResponseData.CommentResponseData commentResponseData = datas.get(position);
        String nickname = "";
        boolean flag;
        if (commentResponseData.getBe_review_id() != 0) {
            nickname = commentResponseData.getNickname() + " 回复 " + commentResponseData.getBe_review_username() + " : ";
            flag = true;
        } else {
            nickname = commentResponseData.getNickname() + " : ";
            flag = false;
        }
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(nickname + commentResponseData.getContent());
        stringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.light_blue)),
                0, nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (flag) {
            stringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_dark)),
                    commentResponseData.getNickname().length(), commentResponseData.getNickname().length() + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_dark)),
                    nickname.length() - 3, nickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            stringBuilder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_dark)),
                    commentResponseData.getNickname().length(), commentResponseData.getNickname().length() + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        viewHolder.tvComment.setText(stringBuilder);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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

        @InjectView(R.id.tv_comment)
        TextView tvComment;

        public TopicCommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
