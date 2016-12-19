package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.view.CircleImageView;

/**
 * Created by tanxinye on 2016/12/1.
 */
public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private Context context;
    private ArrayList<String> urls;

    public AvatarAdapter(Context context, ArrayList<String> urls) {
        this.context = context;
        this.urls = urls;
    }

    @Override
    public AvatarAdapter.AvatarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AvatarViewHolder(LayoutInflater.from(context).inflate(R.layout.item_avatar, parent, false));
    }

    @Override
    public void onBindViewHolder(AvatarAdapter.AvatarViewHolder holder, int position) {
        String url = urls.get(position);
        Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

     class AvatarViewHolder extends RecyclerView.ViewHolder {

         @InjectView(R.id.img_avatar)
         CircleImageView imgAvatar;

        public AvatarViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this,itemView);
        }
    }
}
