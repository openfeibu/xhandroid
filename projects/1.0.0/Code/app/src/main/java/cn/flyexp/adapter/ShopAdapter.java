package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.ShopResponse;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/18.
 */
public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<ShopResponse.ShopResponseData> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public ShopAdapter(Context context, ArrayList<ShopResponse.ShopResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_shop, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        ShopResponse.ShopResponseData responseData = data.get(position);
//        Picasso.with(context).load(responseData.getShop_image()).into(holder.shopbg);
//        holder.tv_shopname.setText(responseData.getShop_name());
//        holder.tv_introduction.setText("主营："+responseData.getShop_intruduction());
//        holder.tv_viewnum.setText("逛逛"+responseData.getShop_view_num());
//        holder.tv_collect.setText("收藏"+responseData.getShop_favorite());

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
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RoundImageView shopbg;
        private TextView tv_shopname;
        private TextView tv_introduction;
        private TextView tv_viewnum;
        private TextView tv_collect;
        private TextView tv_tag1;
        private TextView tv_tag2;
        private TextView tv_tag3;
        private TextView tv_tag4;


        public ViewHolder(View view) {
            super(view);
            shopbg = (RoundImageView) view.findViewById(R.id.shopbg);
            tv_shopname = (TextView) view.findViewById(R.id.tv_shopname);
            tv_introduction = (TextView) view.findViewById(R.id.tv_introduction);
            tv_viewnum = (TextView) view.findViewById(R.id.tv_viewnum);
            tv_collect = (TextView) view.findViewById(R.id.tv_collect);
            tv_tag1 = (TextView) view.findViewById(R.id.tv_tag1);
            tv_tag2 = (TextView) view.findViewById(R.id.tv_tag2);
            tv_tag3 = (TextView) view.findViewById(R.id.tv_tag3);
            tv_tag4 = (TextView) view.findViewById(R.id.tv_tag4);
        }
    }
}
