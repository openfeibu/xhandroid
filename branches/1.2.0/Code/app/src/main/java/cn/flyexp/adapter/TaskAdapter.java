package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.MyTaskResponse;
import cn.flyexp.entity.OrderResponse;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<OrderResponse.OrderResponseData> taskData = new ArrayList<>();
    private ArrayList<MyTaskResponse.MyTaskResponseData> myTaskData = new ArrayList<MyTaskResponse.MyTaskResponseData>();
    private OnItemClickListener onItemClickListener;
    private Context context;
    private boolean isMyTaskData;

    public TaskAdapter(Context context, List<OrderResponse.OrderResponseData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.taskData = data;
    }

    public TaskAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setMyTaskData(ArrayList<MyTaskResponse.MyTaskResponseData> myTaskData) {
        this.myTaskData = myTaskData;
        isMyTaskData = true;
    }

    public List<OrderResponse.OrderResponseData> getTaskData() {
        return this.taskData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_school_task, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        float round = context.getResources().getDimensionPixelSize(R.dimen.task_item_round);
        float[] outerR = new float[] { 0, 0, round, round, round, round, 0, 0 };
        RoundRectShape rr = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawableMoney = new ShapeDrawable(rr);

        round = context.getResources().getDimensionPixelSize(R.dimen.task_item_state_round);
        outerR = new float[] { round, round, round, round, round, round, round, round };
        rr = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawableState = new ShapeDrawable(rr);

        // 指定填充模式
        drawableMoney.getPaint().setStyle(Paint.Style.FILL);
        drawableState.getPaint().setStyle(Paint.Style.FILL);

        if (!isMyTaskData) {
            OrderResponse.OrderResponseData responseData = taskData.get(position);
            holder.senderName.setText(responseData.getNickname());
            holder.orderTime.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData.getCreated_at())));
            holder.orderMoney.setText("" + responseData.getFee());
            holder.orderContent.setText(responseData.getDescription());
            holder.orderState.setText(responseData.getOrder_status());
            holder.orderAddress.setText(responseData.getDestination());
            if (!responseData.getAvatar_url().equals("")) {
                Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                        .resize(CommonUtil.dip2px(context, 33), CommonUtil.dip2px(context, 33))
                        .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.senderIcon);
            } else {
                holder.senderIcon.setImageResource(R.mipmap.icon_defaultavatar_small);
            }
            if (responseData.getStatus().equals("new")) {
                drawableMoney.getPaint().setColor(context.getResources().getColor(R.color.task_new));
                drawableState.getPaint().setColor(context.getResources().getColor(R.color.task_new));
                holder.stateView.setBackgroundDrawable(drawableMoney);
                //指定填充颜色
                holder.orderState.setBackgroundDrawable(drawableState);
            } else if (responseData.getStatus().equals("accepted")) {
                drawableMoney.getPaint().setColor(context.getResources().getColor(R.color.task_comped));
                drawableState.getPaint().setColor(context.getResources().getColor(R.color.task_comped));
                holder.stateView.setBackgroundDrawable(drawableMoney);
                holder.orderState.setBackgroundDrawable(drawableState);
            } else {
                drawableMoney.getPaint().setColor(context.getResources().getColor(R.color.task_finish));
                drawableState.getPaint().setColor(context.getResources().getColor(R.color.task_finish));
                holder.stateView.setBackgroundDrawable(drawableMoney);
                holder.orderState.setBackgroundDrawable(drawableState);
            }
        } else {
            MyTaskResponse.MyTaskResponseData myTaskResponseData = myTaskData.get(position);
            holder.senderName.setText(myTaskResponseData.getNickname());
            holder.orderTime.setText(DateUtil.getStandardDate(DateUtil.date2Long(myTaskResponseData.getCreated_at())));
            holder.orderMoney.setText("" + (int)myTaskResponseData.getFee());
            holder.orderContent.setText(myTaskResponseData.getDescription());
            holder.orderState.setText(myTaskResponseData.getOrder_status());
            holder.orderAddress.setText(myTaskResponseData.getDestination());
            if (!myTaskResponseData.getAvatar_url().equals("")) {
                Picasso.with(context).load(myTaskResponseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                        .resize(CommonUtil.dip2px(context, 33), CommonUtil.dip2px(context, 33))
                        .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.senderIcon);
            } else {
                holder.senderIcon.setImageResource(R.mipmap.icon_defaultavatar_small);
            }
            if (myTaskResponseData.getStatus().equals("new")) {
                drawableMoney.getPaint().setColor(context.getResources().getColor(R.color.task_new));
                drawableState.getPaint().setColor(context.getResources().getColor(R.color.task_new));
                holder.stateView.setBackgroundDrawable(drawableMoney);
                holder.orderState.setBackgroundDrawable(drawableMoney);
            } else if (myTaskResponseData.getStatus().equals("accepted")) {
                drawableMoney.getPaint().setColor(context.getResources().getColor(R.color.task_comped));
                drawableState.getPaint().setColor(context.getResources().getColor(R.color.task_comped));
                holder.stateView.setBackgroundDrawable(drawableMoney);
                holder.orderState.setBackgroundDrawable(drawableState);
            } else {
                drawableMoney.getPaint().setColor(context.getResources().getColor(R.color.task_finish));
                drawableState.getPaint().setColor(context.getResources().getColor(R.color.task_finish));
                holder.stateView.setBackgroundDrawable(drawableMoney);
                holder.orderState.setBackgroundDrawable(drawableState);
            }
        }

        int padding = context.getResources().getDimensionPixelSize(R.dimen.task_item_padding);
        holder.stateView.setPadding(padding, 0, padding, 0);
        String textStr = holder.orderMoney.getText().toString() + "元";
        float size = holder.orderMoney.getTextSize();
        SpannableStringBuilder builder = new SpannableStringBuilder(textStr);

        builder.setSpan(new AbsoluteSizeSpan((int)(size / 2)), textStr.length() - 1, textStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.orderMoney.setText(builder);

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
        return isMyTaskData ? myTaskData.size() : taskData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView senderIcon;
        private TextView senderName;
        private TextView orderState;
        private TextView orderTime;
        private TextView orderMoney;
        private TextView orderContent;
        private TextView orderAddress;
        private View stateView;

        public ViewHolder(View view) {
            super(view);
            senderIcon = (RoundImageView) view.findViewById(R.id.sender_icon);
            senderName = (TextView) view.findViewById(R.id.sender_name);
            orderState = (TextView) view.findViewById(R.id.order_state);
            orderTime = (TextView) view.findViewById(R.id.order_time);
            orderMoney = (TextView) view.findViewById(R.id.order_money);
            orderContent = (TextView) view.findViewById(R.id.order_content);
            orderAddress = (TextView) view.findViewById(R.id.order_address);
            stateView = view.findViewById(R.id.task_state);
        }
    }
}
