package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.ImageBean;
import cn.flyexp.util.LogUtil;
import cn.flyexp.view.RotateTransformation;

/**
 * Created by tanxinye on 2016/12/15.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Context context;
    private ArrayList<ImageBean> images;
    private ArrayList<String> selImages = new ArrayList<>();
    private OnItemClickLinstener onItemClickLinstener;
    private int maxSelNum;
    private int curSelNum;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(String path, int position);

        void onSelectedLinstener(int curSelNum, ArrayList<String> selImages);
    }

    public GalleryAdapter(Context context, ArrayList<ImageBean> images, int max) {
        this.context = context;
        this.images = images;
        this.maxSelNum = max;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {
        ImageBean imageBean = images.get(position);
        String path = imageBean.getThumbnailSmallPath();
        if (TextUtils.isEmpty(path)) {
            path = imageBean.getThumbnailBigPath();
        }
        if (TextUtils.isEmpty(path)) {
            path = imageBean.getOriginalPath();
        }
        final String finalPath = path;
        holder.image.setBackgroundColor(context.getResources().getColor(R.color.light_gray));
        Glide.with(context).load(path).crossFade().centerCrop()
                .transform(new RotateTransformation(context, imageBean.getOrientation()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.image);
        holder.imgSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sel = holder.imgSel.isSelected();
                if (!sel) {
                    if (curSelNum >= maxSelNum) {
                        holder.imgSel.setSelected(sel);
                        return;
                    }
                    selImages.add(finalPath);
                    curSelNum++;
                } else {
                    selImages.remove(finalPath);
                    curSelNum--;
                }
                holder.imgSel.setSelected(!sel);
                if (onItemClickLinstener != null) {
                    onItemClickLinstener.onSelectedLinstener(curSelNum,selImages);
                }
            }
        });

        if (onItemClickLinstener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLinstener.onItemClickLinstener(finalPath, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.image)
        ImageView image;
        @InjectView(R.id.img_sel)
        ImageView imgSel;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
