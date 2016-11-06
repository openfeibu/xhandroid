package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.flyexp.R;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/18.
 */
public class PicPublishAdapter extends RecyclerView.Adapter<PicPublishAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<PhotoInfo> photoInfos = new ArrayList<PhotoInfo>();
    private OnItemClickListener onItemClickListener;
    private Context context;

    public PicPublishAdapter(Context context, ArrayList<PhotoInfo> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.photoInfos = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_pic, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.with(context).load(new File(photoInfos.get(position).getPhotoPath())).config(Bitmap.Config.RGB_565).resize(CommonUtil.dip2px(context, 100), CommonUtil.dip2px(context, 100)).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.iv_img);
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoInfos.remove(position);
                notifyDataSetChanged();
            }
        });

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
        return photoInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView iv_img;
        private ImageView iv_delete;

        public ViewHolder(View view) {
            super(view);
            iv_img = (RoundImageView) view.findViewById(R.id.iv_img);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }
}
