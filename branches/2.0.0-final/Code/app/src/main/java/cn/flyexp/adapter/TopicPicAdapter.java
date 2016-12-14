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
public class TopicPicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<String> datas;
    private boolean isLocal;
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

    public TopicPicAdapter(Context context, ArrayList<String> datas, boolean isLocal) {
        this.context = context;
        this.datas = datas;
        this.isLocal = isLocal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PicViewHolder viewHolder = (PicViewHolder) holder;
        int width = ScreenHelper.dip2px(context, 100);
        int height = ScreenHelper.dip2px(context, 100);
        if (!isLocal) {
            if (datas.size() == 1) {
                width = ScreenHelper.dip2px(context, 336);
                height = ScreenHelper.dip2px(context, 190);
            } else if (datas.size() == 2 || datas.size() == 4) {
                width = ScreenHelper.dip2px(context, 100);
                height = ScreenHelper.dip2px(context, 100);
            }
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(0, 0, ScreenHelper.dip2px(context, 6), ScreenHelper.dip2px(context, 6));
        viewHolder.imgPhoto.setLayoutParams(params);
        Glide.with(context).load(datas.get(position)).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(viewHolder.imgPhoto);
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

    public class PicViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_photo)
        ImageView imgPhoto;

        public PicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
