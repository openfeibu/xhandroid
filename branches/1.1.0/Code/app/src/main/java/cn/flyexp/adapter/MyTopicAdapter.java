package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.MyTopicResponse;
import cn.flyexp.entity.MyTopicResponseNew;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.mvc.topic.TopicViewCallBack;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/7/24.
 */
public class MyTopicAdapter extends RecyclerView.Adapter<MyTopicAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private TopicViewCallBack callBack;
    private Context context;

    private IDeletePosition iDeletePosition;

    public interface IDeletePosition {
        public void delete(int position);
    }

    private ArrayList<MyTopicResponseNew.DataBean> data = new ArrayList<>();

    public MyTopicAdapter(Context context, ArrayList<MyTopicResponseNew.DataBean> data, TopicViewCallBack callBack, IDeletePosition iDeletePosition) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.callBack = callBack;
        this.iDeletePosition = iDeletePosition;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_mytopic_new, parent,
                false));
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    MyTopicPicAdapter adapter;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.userName.setText(data.get(position).getNickname());
        if (TextUtils.isEmpty(data.get(position).getAvatar_url())) {
            holder.userIcon.setImageResource(R.mipmap.icon_defaultavatar_small);
        } else {
            Picasso.with(context).load(data.get(position).getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 32), CommonUtil.dip2px(context, 32))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.userIcon);
        }
        holder.lastTime.setText(""+data.get(position).getCreated_at());
        String content = "#" + data.get(position).getType() + "#";
        SpannableStringBuilder style = new SpannableStringBuilder(content + data.get(position).getContent());
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#5AC8FA")), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.content.setText(style);
        holder.userLove.setText("赞" + data.get(position).getFavourites_count());
        holder.commentContainer.removeAllViews();
        if(data.get(position).getComment()!=null&&data.get(position).getComment().size()>0){
            final List<MyTopicResponseNew.DataBean.CommentBean > list=data.get(position).getComment();
            for(int i=0;i<list.size();i++){
                TextView tv=new TextView(context);
                tv.setTag(R.id.tag_nickname,list.get(i).getNickname());
                tv.setTag(R.id.tag_commentId,list.get(i).getTcid());
                tv.setTag(R.id.tag_Tid,data.get(position).getTid());
                String reViewUser=list.get(i).getBe_review_username();
                TextSpan span=new TextSpan();
                if(TextUtils.isEmpty(reViewUser)){
                    SpannableStringBuilder builder=new SpannableStringBuilder(list.get(i).getNickname()+":"+list.get(i).getContent());
                    builder.setSpan(span,0,list.get(i).getNickname().length()+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                }else{
                    String value=list.get(i).getNickname()+" 回复 "+reViewUser+":";
                    SpannableStringBuilder builder=new SpannableStringBuilder(value+list.get(i).getContent());
                    builder.setSpan(span,0,value.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tv.setText(builder);
                }
                holder.commentContainer.addView(tv);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView tv= (TextView) v;
                        String nickName= (String) tv.getTag(R.id.tag_nickname);
                        int commentId= (int) tv.getTag(R.id.tag_commentId);
                        int topId= (int) tv.getTag(R.id.tag_Tid);
                        listener.onReview(tv,holder.getPosition(),nickName,commentId,topId);
                    }
                });
            }
        }
        holder.iv_bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iDeletePosition!=null){
                    iDeletePosition.delete(holder.getPosition());
                }
            }
        });


        final ArrayList<String> imgUrlList = new ArrayList<String>();
        ArrayList<String> thumbUrlList = new ArrayList<String>();
        adapter = new MyTopicPicAdapter(context, thumbUrlList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PicBrowserBean picBrowserBean = new PicBrowserBean();
                picBrowserBean.setImgUrl(imgUrlList);
                picBrowserBean.setCurSelectedIndex(position);
                picBrowserBean.setType(1);
                callBack.picBrowserEnter(picBrowserBean);
            }
        });
        holder.imgs.setAdapter(adapter);
        holder.imgs.setItemAnimator(new DefaultItemAnimator());

        if (data.get(position).getImg() == null || data.get(position).getImg().equals("")) {
            holder.imgs.setVisibility(View.GONE);
        } else {
            String[] thumbUrls = CommonUtil.splitImageUrl(data.get(position).getImg());
            String[] urls = CommonUtil.splitImageUrl(data.get(position).getImg());
            holder.imgs.setVisibility(View.VISIBLE);
            imgUrlList.addAll(Arrays.asList(urls));
            thumbUrlList.addAll(Arrays.asList(thumbUrls));
            adapter.notifyDataSetChanged();
        }
    }

    private CommentOnClickListener listener;
    public void setOnCommentClickListener(CommentOnClickListener listener){
        this.listener=listener;
    }

    //设置评论点击的回调
    public interface CommentOnClickListener{
        void onReview(TextView v,int position,String nickName,int commentId,int TopicId);
    }


    class TextSpan extends ClickableSpan{

        @Override
        public void onClick(View widget) {
            TextView tv= (TextView) widget;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#5AC8FA"));
            ds.setUnderlineText(false);
        }
    }

    private int typeImage(String type) {
        int imgId = -1;
        if (type.equals("随心写")) {
            imgId = R.mipmap.icon_random;
        } else if (type.equals("新鲜事")) {
            imgId = R.mipmap.icon_new;
        } else if (type.equals("一起约")) {
            imgId = R.mipmap.icon_appionment;
        } else if (type.equals("帮帮忙")) {
            imgId = R.mipmap.icon_help;
        } else if (type.equals("吐吐槽")) {
            imgId = R.mipmap.icon_teasing;
        } else if (type.equals("问一下")) {
            imgId = R.mipmap.icon_question;
        }
        return imgId;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_bin;
        private LinearLayout commentContainer;
        private ImageView userIcon;
        private TextView userName;
        private TextView lastTime;
        private TextView content;
        private RecyclerView imgs;
        private TextView userLove;

        public ViewHolder(View view) {
            super(view);
            iv_bin = (ImageView) view.findViewById(R.id.iv_bin);
            commentContainer = (LinearLayout) view.findViewById(R.id.commmentContainer);
            userIcon = (ImageView) view.findViewById(R.id.circle_usernameicon);
            userName = (TextView) view.findViewById(R.id.circle_username);
            lastTime = (TextView) view.findViewById(R.id.circle_time);
            content = (TextView) view.findViewById(R.id.circle_content);
            imgs = (RecyclerView) view.findViewById(R.id.rv_pic);
            userLove = (TextView) view.findViewById(R.id.circle_love);
            imgs.setLayoutManager(new GridLayoutManager(context, 3));
        }
    }
}
