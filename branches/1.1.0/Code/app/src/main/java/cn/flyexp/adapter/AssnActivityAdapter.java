package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import cn.flyexp.R;
import cn.flyexp.entity.AssnActivityResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/18.
 */
public class AssnActivityAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private Context context;
    private ArrayList<AssnActivityResponse.AssnActivityResponseData> data;

    public AssnActivityAdapter(Context context, ArrayList<AssnActivityResponse.AssnActivityResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(inflater.inflate(R.layout.item_assn_activitiy, parent, false));
        } else {
            return new PicViewHolder(inflater.inflate(R.layout.item_assn_activitiy_pic, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AssnActivityResponse.AssnActivityResponseData responseData = data.get(position);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
                viewHolder.iv_state.setImageResource(R.mipmap.icon_activity_waiting);
            } else if (DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()) {
                viewHolder.iv_state.setImageResource(R.mipmap.icon_activity_end);
            } else {
                viewHolder.iv_state.setImageResource(R.mipmap.icon_activity_ongoing);
            }
            viewHolder.tv_title.setText(responseData.getTitle().trim());
            viewHolder.tv_content.setText(responseData.getContent().trim());
            viewHolder.tv_place.setText("地点：" + responseData.getPlace());
            viewHolder.tv_stime.setText("开始时间：" + responseData.getStart_time());
            viewHolder.tv_etime.setText("结束时间：" + responseData.getEnd_time());
            viewHolder.tv_assn.setText(responseData.getAname());
            viewHolder.tv_viewnum.setText("浏览" + responseData.getView_num());
        } else if (holder instanceof PicViewHolder) {
            PicViewHolder viewHolder = (PicViewHolder) holder;
            if (DateUtil.date2Long(responseData.getStart_time()) > new Date().getTime()) {
                viewHolder.iv_state.setImageResource(R.mipmap.icon_activity_waiting);
                viewHolder.tv_stime.setTextColor(context.getResources().getColor(R.color.light_blue));
            } else if (DateUtil.date2Long(responseData.getEnd_time()) < new Date().getTime()) {
                viewHolder.iv_state.setImageResource(R.mipmap.icon_activity_end);
                viewHolder.tv_stime.setTextColor(context.getResources().getColor(R.color.light_gray));
            } else {
                viewHolder.iv_state.setImageResource(R.mipmap.icon_activity_ongoing);
                viewHolder.tv_stime.setTextColor(context.getResources().getColor(R.color.light_red));
            }
            viewHolder.tv_title.setText(responseData.getTitle());
            viewHolder.tv_content.setText(responseData.getContent());
            viewHolder.tv_place.setText(responseData.getPlace());
            viewHolder.tv_stime.setText("开始时间：" + DateUtil.long2Date(DateUtil.date2Long(responseData.getStart_time()), "yy-MM-dd HH:mm"));
            viewHolder.tv_etime.setText("结束时间：" + DateUtil.long2Date(DateUtil.date2Long(responseData.getEnd_time()), "yy-MM-dd HH:mm"));
            viewHolder.tv_assn.setText(responseData.getAname());
            viewHolder.tv_viewnum.setText("浏览" + responseData.getView_num());
            if (!TextUtils.isEmpty(responseData.getImg_url())) {
                Picasso.with(context).load(responseData.getImg_url()).config(Bitmap.Config.RGB_565)
                        .resize(CommonUtil.getScreenWidth(context), CommonUtil.dip2px(context, 150))
                        .memoryPolicy(MemoryPolicy.NO_CACHE).into(viewHolder.iv_img);
            }
        }
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
    public int getItemViewType(int position) {
        String imgUrl = data.get(position).getImg_url();
        if (TextUtils.isEmpty(imgUrl)) {
            return 0;
        } else {
            return 1;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_state;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_place;
        private TextView tv_stime;
        private TextView tv_etime;
        private TextView tv_assn;
        private TextView tv_viewnum;

        public ViewHolder(View view) {
            super(view);
            iv_state = (ImageView) view.findViewById(R.id.iv_state);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_place = (TextView) view.findViewById(R.id.tv_place);
            tv_stime = (TextView) view.findViewById(R.id.tv_stime);
            tv_etime = (TextView) view.findViewById(R.id.tv_etime);
            tv_assn = (TextView) view.findViewById(R.id.tv_assn);
            tv_viewnum = (TextView) view.findViewById(R.id.tv_viewnum);
        }
    }

    class PicViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_img;
        private ImageView iv_state;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_place;
        private TextView tv_stime;
        private TextView tv_etime;
        private TextView tv_assn;
        private TextView tv_viewnum;

        public PicViewHolder(View view) {
            super(view);
            iv_img = (ImageView) view.findViewById(R.id.iv_img);
            iv_state = (ImageView) view.findViewById(R.id.iv_state);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_place = (TextView) view.findViewById(R.id.tv_place);
            tv_stime = (TextView) view.findViewById(R.id.tv_stime);
            tv_etime = (TextView) view.findViewById(R.id.tv_etime);
            tv_assn = (TextView) view.findViewById(R.id.tv_assn);
            tv_viewnum = (TextView) view.findViewById(R.id.tv_viewnum);
        }
    }
}
