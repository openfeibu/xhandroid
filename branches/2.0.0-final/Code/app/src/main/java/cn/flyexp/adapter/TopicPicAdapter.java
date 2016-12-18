package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.util.ScreenHelper;

/**
 * Created by tanxinye on 2016/11/14.
 */
public class TopicPicAdapter extends RecyclerView.Adapter<TopicPicAdapter.PicViewHolder> {

    private Context context;
    private ArrayList<String> datas;
    private OnItemClickLinstener onItemClickLinstener;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(int position);
    }

    public TopicPicAdapter(Context context, ArrayList<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public TopicPicAdapter.PicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(TopicPicAdapter.PicViewHolder holder, final int position) {
        Glide.with(context).load(datas.get(position)).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop().into(holder.imgPhoto);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickLinstener != null) {
                    onItemClickLinstener.onItemClickLinstener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    protected class PicViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_photo)
        ImageView imgPhoto;

        public PicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
