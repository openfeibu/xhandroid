package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by guo on 2016/10/15.
 */

public class MyTopicPicAdapter extends RecyclerView.Adapter<MyTopicPicAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imgUrl = new ArrayList<String>();
    private OnItemClickListener onItemClickListener;

    public void setImgUrl(List<String> list) {
        imgUrl.addAll(list);
    }

    public MyTopicPicAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.imgUrl = data;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public MyTopicPicAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_pic, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.iv_delete.setVisibility(View.GONE);
        Picasso.with(context).load(imgUrl.get(position)).config(Bitmap.Config.RGB_565)
                .resize(CommonUtil.dip2px(context, 100), CommonUtil.dip2px(context, 100))
                .memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.iv_img);
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
        return imgUrl.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        private ImageView iv_delete;

        public ViewHolder(View view) {
            super(view);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }
}

