package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.flyexp.R;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.util.DateUtil;

/**
 * Created by tanxinye on 2016/11/1.
 */
public class AssnActivityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> datas;
    private OnItemClickLinstener onItemClickLinstener;
    private static final int NORMAL = -1;
    private static final int PICTURE = 1;

    public void setOnItemClickLinstener(OnItemClickLinstener onItemClickLinstener) {
        this.onItemClickLinstener = onItemClickLinstener;
    }

    public interface OnItemClickLinstener {
        void onItemClickLinstener(View view, int position);
    }

    public AssnActivityAdapter(Context context, ArrayList<AssnActivityResponse.AssnActivityResponseData> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NORMAL) {
            return new AssnActivityNormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_assnacti_normal, parent, false));
        } else {
            return new AssnActivityPicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_assnacti_pic, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AssnActivityResponse.AssnActivityResponseData responseData = datas.get(position);
        if (holder instanceof AssnActivityPicViewHolder) {
            bindPictureViewHolder((AssnActivityPicViewHolder) holder, responseData);
        } else if (holder instanceof AssnActivityNormalViewHolder) {
            bindNormalViewHolder((AssnActivityNormalViewHolder) holder, responseData);
        }
        if (onItemClickLinstener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickLinstener.onItemClickLinstener(view, position);
                }
            });
        }
    }

    private void bindPictureViewHolder(AssnActivityPicViewHolder holder, AssnActivityResponse.AssnActivityResponseData responseData) {
        final AssnActivityPicViewHolder viewHolder = holder;
        viewHolder.tvTitle.setText(responseData.getTitle().trim());
        viewHolder.tvContent.setText(responseData.getContent().trim());
        viewHolder.tvAssnName.setText(responseData.getAname());
        viewHolder.tvPlace.setText(String.format(context.getResources().getString(R.string.assnacti_place), responseData.getPlace()));
        viewHolder.tvViewNum.setText(String.format(context.getResources().getString(R.string.assnactiv_viewnum), responseData.getView_num()));
        viewHolder.tvDate.setText(DateUtil.dateFormat(responseData.getStart_time(), "MM-dd") + " 至 " + DateUtil.dateFormat(responseData.getEnd_time(), "MM-dd"));
        if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
            viewHolder.imgState.setImageResource(R.mipmap.icon_activity_waiting);
            viewHolder.tvDate.setTextColor(context.getResources().getColor(R.color.light_blue));
        } else if (DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()) {
            viewHolder.imgState.setImageResource(R.mipmap.icon_activity_end);
            viewHolder.tvDate.setTextColor(context.getResources().getColor(R.color.font_light));
        } else {
            viewHolder.imgState.setImageResource(R.mipmap.icon_activity_ongoing);
            viewHolder.tvDate.setTextColor(context.getResources().getColor(R.color.light_red));
        }
        Glide.with(context).load(responseData.getImg_url()).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (resource != null) {
                    Palette.Builder builder = Palette.from(resource);
                    builder.generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch swatch = palette.getLightVibrantSwatch();
                            if (swatch != null) {
                                viewHolder.viewBg.setBackgroundColor(swatch.getRgb());
                                viewHolder.tvTitle.setTextColor(swatch.getTitleTextColor());
                                viewHolder.tvContent.setTextColor(swatch.getBodyTextColor());
                            }
                        }
                    });
                }
                return false;
            }
        }).diskCacheStrategy(DiskCacheStrategy.SOURCE).centerCrop().into(viewHolder.img);
    }

    private void bindNormalViewHolder(AssnActivityNormalViewHolder holder, AssnActivityResponse.AssnActivityResponseData responseData) {
        AssnActivityNormalViewHolder viewHolder = holder;
        viewHolder.tvTitle.setText(responseData.getTitle().trim());
        viewHolder.tvContent.setText(responseData.getContent().trim());
        viewHolder.tvAssnName.setText(responseData.getAname());
        viewHolder.tvPlace.setText(String.format(context.getResources().getString(R.string.assnacti_place), responseData.getPlace()));
        viewHolder.tvViewNum.setText(String.format(context.getResources().getString(R.string.assnactiv_viewnum), responseData.getView_num()));
        viewHolder.tvDate.setText(DateUtil.dateFormat(responseData.getStart_time(), "MM-dd") + "至" + DateUtil.dateFormat(responseData.getEnd_time(), "MM-dd"));
        if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
            viewHolder.imgState.setImageResource(R.mipmap.icon_activity_waiting);
            viewHolder.tvDate.setTextColor(context.getResources().getColor(R.color.light_blue));
        } else if (DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()) {
            viewHolder.imgState.setImageResource(R.mipmap.icon_activity_end);
            viewHolder.tvDate.setTextColor(context.getResources().getColor(R.color.font_light));
        } else {
            viewHolder.imgState.setImageResource(R.mipmap.icon_activity_ongoing);
            viewHolder.tvDate.setTextColor(context.getResources().getColor(R.color.light_red));
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        String imgUrl = datas.get(position).getImg_url();
        if (TextUtils.isEmpty(imgUrl)) {
            return NORMAL;
        } else {
            return PICTURE;
        }
    }

    public class AssnActivityNormalViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.img_state)
        ImageView imgState;
        @InjectView(R.id.tv_title)
        TextView tvTitle;
        @InjectView(R.id.tv_content)
        TextView tvContent;
        @InjectView(R.id.tv_place)
        TextView tvPlace;
        @InjectView(R.id.tv_date)
        TextView tvDate;
        @InjectView(R.id.tv_assnname)
        TextView tvAssnName;
        @InjectView(R.id.tv_viewnum)
        TextView tvViewNum;

        public AssnActivityNormalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class AssnActivityPicViewHolder extends AssnActivityNormalViewHolder {

        @InjectView(R.id.img)
        ImageView img;
        @InjectView(R.id.view_bg)
        View viewBg;

        public AssnActivityPicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
