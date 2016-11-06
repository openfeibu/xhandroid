package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.AssnMemberResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class AssnMemberAdapter extends RecyclerView.Adapter<AssnMemberAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private ArrayList<AssnMemberResponse.AssnMemberResponseData> data;

    public AssnMemberAdapter(Context context, ArrayList<AssnMemberResponse.AssnMemberResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_assn_member, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        AssnMemberResponse.AssnMemberResponseData responseData = data.get(position);
        holder.tv_nickname.setText(responseData.getNickname());
        holder.tv_position.setText("·" + toLevel(responseData.getLevel()));
        holder.tv_realname.setText("(" + responseData.getRealname() + ")");

        if (!TextUtils.isEmpty(responseData.getAvatar_url())) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 32), CommonUtil.dip2px(context, 32))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.icon_defaultavatar_small);
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

    private String toLevel(int level) {
        String lev = "";
        switch (level) {
            case 0:
                lev = "成员";
                break;
            case 1:
                lev = "会长";
                break;
            case 2:
                lev = "副会长";
                break;
        }
        return lev;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView iv_avatar;
        private TextView tv_nickname;
        private TextView tv_position;
        private TextView tv_realname;

        public ViewHolder(View view) {
            super(view);
            iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_position = (TextView) view.findViewById(R.id.tv_position);
            tv_realname = (TextView) view.findViewById(R.id.tv_realname);
        }
    }
}
