package cn.flyexp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by tanxinye on 2016/12/16.
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {


    @Override
    public TopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TopicViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected static class TopicViewHolder extends RecyclerView.ViewHolder {



        public TopicViewHolder(View itemView) {
            super(itemView);
        }
    }
}
