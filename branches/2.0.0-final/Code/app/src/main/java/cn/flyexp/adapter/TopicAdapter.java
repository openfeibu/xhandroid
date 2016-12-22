package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private Context context;
    private ArrayList<TopicResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public TopicAdapter(Context context, ArrayList<TopicResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, final int position) {
        final TopicResponseData responseData = datas.get(position);
        holder.tvNickname.setText(responseData.getNickname());
        holder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
        holder.tvCommentNum.setText(String.valueOf(responseData.getComment_num()));
        holder.tvViewNum.setText(String.valueOf(responseData.getView_num()));
        holder.tvFavouritesCount.setText(String.valueOf(responseData.getFavourites_count()));
        holder.tvContent.setText(responseData.getContent());
        if (TextUtils.isEmpty(responseData.getThumb())) {
            holder.imgTopicPhoto.setVisibility(View.GONE);
        } else {
            holder.imgTopicPhoto.setVisibility(View.VISIBLE);
            Glide.with(context).load(Arrays.asList(splitImageUrl(responseData.getThumb())).get(0))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(holder.imgTopicPhoto);

        }
        Glide.with(context).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(holder.imgAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLinstener != null) {
                    onItemClickLinstener.onItemClickLinstener(view, position);
                }
            }
        });
    }

    private String[] splitImageUrl(String imgUrl) {
        return imgUrl.split("\\,");
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    protected static class TopicViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.img_avatar)
        CircleImageView imgAvatar;

        @InjectView(R.id.img_topicphoto)
        ImageView imgTopicPhoto;

        @InjectView(R.id.tv_nickname)
        TextView tvNickname;

        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_content)
        TextView tvContent;

        @InjectView(R.id.tv_favourites_count)
        TextView tvFavouritesCount;

        @InjectView(R.id.tv_commentnum)
        TextView tvCommentNum;

        @InjectView(R.id.tv_viewnum)
        TextView tvViewNum;

        public TopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
