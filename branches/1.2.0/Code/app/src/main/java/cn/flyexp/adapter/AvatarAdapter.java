package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.view.RoundImageView;

/**
 * Created by tanxinye on 2016/12/1.
 */
public class AvatarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<String> urls;

    public AvatarAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvatarViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_avatar, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String url = urls.get(position);
        AvatarViewHolder viewHolder = (AvatarViewHolder) holder;
        if (!TextUtils.isEmpty(url)) {
            Picasso.with(context).load(url)
                    .resize(CommonUtil.dip2px(context, 33), CommonUtil.dip2px(context, 33))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).centerCrop().into(viewHolder.imgAvatar);
        }
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView imgAvatar;

        public AvatarViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (RoundImageView) itemView.findViewById(R.id.img_avatar);
        }
    }
}
