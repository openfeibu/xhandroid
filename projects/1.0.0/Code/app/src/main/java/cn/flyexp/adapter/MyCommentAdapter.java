package cn.flyexp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.DeleteCommentRequest;
import cn.flyexp.entity.MyCommentResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.mvc.topic.TopicViewCallBack;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;

/**
 * Created by txy on 2016/7/24.
 */
public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MyCommentResponse.MyCommentResponseData> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private TopicViewCallBack callBack;

    private IDeletePosition iDeletePosition;

    public interface IDeletePosition {
        public void delete(int position);
    }

    public MyCommentAdapter(Context context, ArrayList<MyCommentResponse.MyCommentResponseData> data, TopicViewCallBack callBack, IDeletePosition iDeletePosition) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.callBack = callBack;
        this.iDeletePosition = iDeletePosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_mycomment, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final MyCommentResponse.MyCommentResponseData responseData = data.get(position);
        holder.iv_bin.setVisibility(View.VISIBLE);
        holder.iv_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iDeletePosition.delete(position);
            }
        });
        if(responseData.getCid_username().equals("")){
            holder.tv_commentName.setText("评论话题" );
        }else{
            holder.tv_commentName.setText("评论：" + responseData.getCid_username());
        }
        holder.tv_content.setText(responseData.getContent());
        holder.tv_time.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData
                .getCreated_at())));
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
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_bin;
        private TextView tv_content;
        private TextView tv_time;
        private TextView tv_commentName;

        public ViewHolder(View view) {
            super(view);
            iv_bin = (ImageView) view.findViewById(R.id.iv_bin);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_commentName = (TextView) view.findViewById(R.id.tv_commentName);
        }
    }
}
