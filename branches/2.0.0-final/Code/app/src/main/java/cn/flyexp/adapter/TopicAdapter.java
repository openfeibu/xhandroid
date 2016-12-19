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
        return new TopicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic,parent,false));
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, final int position) {
        TopicViewHolder viewHolder = (TopicViewHolder)holder;
        final TopicResponseData responseData = datas.get(position);
        viewHolder.tvNickname.setText(responseData.getNickname());
        viewHolder.tvDate.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
        viewHolder.tvCommentNum.setText(String.valueOf(responseData.getComment_num()));
        viewHolder.tvViewNum.setText(String.valueOf(responseData.getView_num()));
        viewHolder.tvFavouritesCount.setText(String.valueOf(responseData.getFavourites_count()));
        viewHolder.tvContent.setText(responseData.getContent());
        if(TextUtils.isEmpty(responseData.getImg())) {
            viewHolder.imgtopicphoto.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(responseData.getImg()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(viewHolder.imgtopicphoto);
        }
        Glide.with(context).load(responseData.getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(viewHolder.imgavatar);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLinstener != null) {
                    onItemClickLinstener.onItemClickLinstener(view,position);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    protected static class TopicViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.img_avatar)
        CircleImageView imgavatar;

        @InjectView(R.id.img_topicphoto)
        ImageView imgtopicphoto;

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
            ButterKnife.inject(this,itemView);
        }
    }
}
