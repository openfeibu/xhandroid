package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.flyexp.R;

/**
 * Created by tanxinye on 2016/10/27.
 */
public class TopicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;

    public TopicAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(context).inflate(R.layout.item_topic, parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class TopicViewHolder extends RecyclerView.ViewHolder {

        public TopicViewHolder(View itemView) {
            super(itemView);
        }
    }
}
