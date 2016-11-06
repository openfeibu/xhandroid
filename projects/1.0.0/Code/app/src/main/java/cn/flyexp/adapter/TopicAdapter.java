package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.ThumbUpRequest;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.mvc.topic.TopicViewCallBack;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private List<TopicResponseData> data = new ArrayList<>();
    private TopicViewCallBack callBack;

    public TopicAdapter(Context context, List<TopicResponseData> data, TopicViewCallBack callBack) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.callBack = callBack;
    }

    public interface OnLike {
        public void onlike(int pos);
    }

    public OnLike onLike;

    public void setOnLike(OnLike onLike) {
        this.onLike = onLike;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_topic, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TopicResponseData responseData = data.get(position);
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 32), CommonUtil.dip2px(context, 32))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.taWindowEnter(responseData.getOpenid());
            }
        });
        holder.tv_name.setText(responseData.getNickname());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.taWindowEnter(responseData.getOpenid());
            }
        });
        holder.tv_time.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData
                .getCreated_at())));
        holder.tv_content.setText(responseData.getContent());
        if (responseData.getThumb() == null || responseData.getThumb().equals("")) {
            holder.iv_cover.setVisibility(View.GONE);
        } else {
            String[] urls = CommonUtil.splitImageUrl(responseData.getThumb());
            holder.iv_cover.setVisibility(View.VISIBLE);
            Picasso.with(context).load(urls[0]).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 100), CommonUtil.dip2px(context, 100))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.iv_cover);
        }
        holder.tv_content.setText(responseData.getContent());
        holder.tv_look.setText("浏览" + responseData.getView_num());
        holder.tv_comment.setText("评论" + responseData.getComment_num());
        holder.tv_good.setText("点赞" + responseData.getFavourites_count());
        holder.iv_labeltype.setImageResource(typeImage(responseData.getType()));
        final int curLike = responseData.getFavorited();
        holder.iv_like.setVisibility(View.VISIBLE);
        holder.iv_like.setImageResource(curLike == 1 ? R.mipmap.icon_like_sel : R.mipmap.icon_like_nor);
        if (curLike == 0) {
            holder.iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.iv_like.setImageResource(R.mipmap.icon_like_sel);
                    holder.tv_good.setText("点赞" + (responseData.getFavourites_count() + 1));
                    v.setEnabled(false);
                    onLike.onlike(position);
                }
            });
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

    private int typeImage(String type) {
        int imgId = -1;
        if (type.equals("随心写")) {
            imgId = R.mipmap.icon_random;
        } else if (type.equals("新鲜事")) {
            imgId = R.mipmap.icon_new;
        } else if (type.equals("一起约")) {
            imgId = R.mipmap.icon_appionment;
        } else if (type.equals("帮帮忙")) {
            imgId = R.mipmap.icon_help;
        } else if (type.equals("吐吐槽")) {
            imgId = R.mipmap.icon_teasing;
        } else if (type.equals("问一下")) {
            imgId = R.mipmap.icon_question;
        }
        return imgId;
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
        private RoundImageView iv_cover;
        private ImageView iv_labeltype;
        private TextView tv_look;
        private TextView tv_comment;
        private TextView tv_good;
        private ImageView iv_like;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            iv_cover = (RoundImageView) view.findViewById(R.id.iv_cover);
            iv_labeltype = (ImageView) view.findViewById(R.id.iv_labeltype);
            tv_look = (TextView) view.findViewById(R.id.tv_look);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
            tv_good = (TextView) view.findViewById(R.id.tv_good);
            iv_like = (ImageView) view.findViewById(R.id.iv_like);
        }
    }
}
