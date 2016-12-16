package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class TTopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<TopicResponseData> datas;
    private OnTopicClickListener onTopicClickListener;

    public void setOnTopicClickListener(OnTopicClickListener onTopicClickListener) {
        this.onTopicClickListener = onTopicClickListener;
    }

    public interface OnTopicClickListener {
        void onLikeClick(int position);

        void onCommentClick(int position, int cpos, int commentid, String nickname, String openid);

        void onPicClick(int position, int picPosition);

        void onDeleteTopicClick(int position);

        void onLongClick();
    }

    public TTopicAdapter(Context context, ArrayList<TopicResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int itemPosition) {
        TopicViewHolder viewHolder = (TopicViewHolder) holder;
        final TopicResponseData responseData = datas.get(itemPosition);
        viewHolder.tvNickname.setText(responseData.getNickname());
        viewHolder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
        viewHolder.tvContent.setText(responseData.getContent());
        viewHolder.tvLikeNum.setText(String.format(context.getResources().getString(R.string.topic_like_format), responseData.getFavourites_count()));
        Glide.with(context).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(viewHolder.imgAvatar);

        String openId = SharePresUtil.getString(SharePresUtil.KEY_OPENID);
        if (!TextUtils.isEmpty(openId) && TextUtils.equals(openId, responseData.getOpenid())) {
            viewHolder.tvDelete.setVisibility(View.VISIBLE);
            viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onTopicClickListener != null) {
                        onTopicClickListener.onDeleteTopicClick(itemPosition);
                    }
                }
            });
        } else {
            viewHolder.tvDelete.setVisibility(View.GONE);
        }

        if (responseData.getFavorited() == 0) {
            viewHolder.tvLike.setText(context.getResources().getString(R.string.topic_like));
            Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_praise_nor);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tvLike.setCompoundDrawables(drawable, null, null, null);
        } else {
            viewHolder.tvLike.setText(context.getResources().getString(R.string.cancel));
            Drawable drawable = context.getResources().getDrawable(R.mipmap.icon_praise_sel);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.tvLike.setCompoundDrawables(drawable, null, null, null);
        }

        if (TextUtils.isEmpty(responseData.getThumb())) {
            viewHolder.rvPic.setVisibility(View.GONE);
        } else {
            ArrayList<String> imageUrl = new ArrayList<>();
            imageUrl.addAll(Arrays.asList(splitImageUrl(responseData.getThumb())));
            int spanCount = 3;
            if (imageUrl.size() == 1) {
                spanCount = 1;
            } else if (imageUrl.size() == 2 || imageUrl.size() == 4) {
                spanCount = 2;
            }
            TopicPicAdapter picAdapter = new TopicPicAdapter(context, imageUrl);
            picAdapter.setOnItemClickLinstener(new TopicPicAdapter.OnItemClickLinstener() {
                @Override
                public void onItemClickLinstener(int picPosition) {
                    if (onTopicClickListener != null) {
                        onTopicClickListener.onPicClick(itemPosition, picPosition);
                    }
                }
            });
            viewHolder.rvPic.setLayoutManager(new GridLayoutManager(context, spanCount));
            viewHolder.rvPic.setAdapter(picAdapter);
            viewHolder.rvPic.setHasFixedSize(false);
            viewHolder.rvPic.setNestedScrollingEnabled(false);
            viewHolder.rvPic.setVisibility(View.VISIBLE);
        }


        if (responseData.getComment() == null) {
            viewHolder.rvComment.setVisibility(View.GONE);
        } else {
            final ArrayList<TopicResponseData.CommentResponseData> comment = responseData.getComment();
            TopicCommentAdapter topicCommentAdapter = new TopicCommentAdapter(context, comment);
            topicCommentAdapter.setOnItemClickLinstener(new TopicCommentAdapter.OnItemClickLinstener() {
                @Override
                public void onItemClickLinstener(View view, int position) {
                    if (onTopicClickListener != null) {
                        onTopicClickListener.onCommentClick(itemPosition, position, comment.get(position).getTcid(),
                                comment.get(position).getNickname(), comment.get(position).getOpenid());
                    }
                }
            });
            viewHolder.rvComment.setLayoutManager(new LinearLayoutManager(context));
            viewHolder.rvComment.setAdapter(topicCommentAdapter);
            viewHolder.rvComment.setHasFixedSize(false);
            viewHolder.rvComment.setNestedScrollingEnabled(false);
            viewHolder.rvComment.setVisibility(View.VISIBLE);
        }


        viewHolder.tvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTopicClickListener != null) {
                    onTopicClickListener.onLikeClick(itemPosition);
                }
            }
        });
        viewHolder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTopicClickListener != null) {
                    onTopicClickListener.onCommentClick(itemPosition, -1, 0, "", "");
                }
            }
        });
        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onTopicClickListener != null) {
                    onTopicClickListener.onLongClick();
                }
                return false;
            }
        });
    }

    public String[] splitImageUrl(String imgUrl) {
        return imgUrl.split("\\,");
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;
        @InjectView(R.id.rv_pic)
        RecyclerView rvPic;
        @InjectView(R.id.rv_comment)
        RecyclerView rvComment;
        @InjectView(R.id.tv_nickname)
        TextView tvNickname;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_content)
        TextView tvContent;
        @InjectView(R.id.tv_likenum)
        TextView tvLikeNum;
        @InjectView(R.id.tv_like)
        TextView tvLike;
        @InjectView(R.id.tv_comment)
        TextView tvComment;
        @InjectView(R.id.tv_delete)
        TextView tvDelete;

        public TopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
