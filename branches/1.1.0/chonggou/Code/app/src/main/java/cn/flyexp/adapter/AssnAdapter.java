package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.AssnResponse;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class AssnAdapter extends RecyclerView.Adapter<AssnAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private List<AssnResponse.DataBean.AssociationsBean> list=new ArrayList<>();

    public AssnAdapter(Context context, List<AssnResponse.DataBean.AssociationsBean> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list.addAll(list);
    }

    public List<AssnResponse.DataBean.AssociationsBean> getList(){
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_assn, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        AssnResponse.DataBean.AssociationsBean data=list.get(position);
        holder.mAssnName.setText(""+data.getAname());
        holder.mAssnLeaderName.setText(" "+data.getLeader());
        if(data.getIntroduction()!=null){
            holder.mAssnDescription.setText(""+data.getIntroduction());
        }else{
            holder.mAssnDescription.setText("这个家伙很懒，什么都没留下~");
        }
        holder.mAssnDetail.setText(data.getMember_number()+" 人活跃·"+data.getActivity_count()+" 个活动·#"+data.getLabel()+"#");
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
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RoundImageView mRoundImageView;
        TextView mAssnName;
        TextView mAssnLeaderName;
        TextView mAssnDescription;
        TextView mAssnDetail;

        public ViewHolder(View view) {
            super(view);
            mRoundImageView= (RoundImageView) view.findViewById(R.id.iv_avatar);
            mAssnName= (TextView) view.findViewById(R.id.tv_assn);
            mAssnLeaderName= (TextView) view.findViewById(R.id.tv_leader);
            mAssnDescription= (TextView) view.findViewById(R.id.tv_description);
            mAssnDetail= (TextView) view.findViewById(R.id.tv_detail);
        }
    }
}
