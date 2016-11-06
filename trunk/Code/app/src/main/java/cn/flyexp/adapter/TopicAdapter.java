package cn.flyexp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.entity.PicBrowserBean;
import cn.flyexp.entity.TopicResponseData;
import cn.flyexp.mvc.topic.TopicViewCallBack;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.DateUtil;
import cn.flyexp.util.OnItemClickListener;
import cn.flyexp.view.RoundImageView;

/**
 * Created by txy on 2016/6/5.
 * revise by guo on 2016/9/24.
 * modify by zlk on 2016/10/23 添加itemcallback
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private List<TopicResponseData> data = new ArrayList<>();
    private TopicViewCallBack callBack;


    private ArrayList<String> imgUrlList = new ArrayList<String>();
    private ArrayList<String> thumbUrlList = new ArrayList<String>();

    public TopicAdapter(Context context, List<TopicResponseData> data, TopicViewCallBack callBack) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.callBack = callBack;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_xh_circle, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TopicResponseData responseData = data.get(position);
        if (!responseData.getAvatar_url().equals("")) {
            Picasso.with(context).load(responseData.getAvatar_url()).config(Bitmap.Config.RGB_565)
                    .resize(CommonUtil.dip2px(context, 32), CommonUtil.dip2px(context, 32))
                    .memoryPolicy(MemoryPolicy.NO_CACHE).error(context.getResources().getDrawable(R.mipmap.icon_defaultavatar_small)).centerCrop().into(holder.circleUserIcon);
        } else {
            holder.circleUserIcon.setImageResource(R.mipmap.icon_defaultavatar_small);
        }
        if (responseData.getFavorited() == 0) {
            holder.circleLove.setText(context.getResources().getString(R.string.send_love));
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_praise_nor);
            holder.circleLove.setCompoundDrawablePadding(CommonUtil.dip2px(context, 5));
            drawable.setBounds(0, 0, CommonUtil.dip2px(context, 16), CommonUtil.dip2px(context, 12));
            holder.circleLove.setCompoundDrawables(drawable, null, null, null);
        } else {
            holder.circleLove.setText(context.getResources().getString(R.string.cancel_love));
            Drawable drawable = context.getResources().getDrawable(R.drawable.icon_praise_sel);
            holder.circleLove.setCompoundDrawablePadding(CommonUtil.dip2px(context, 5));
            drawable.setBounds(0, 0, CommonUtil.dip2px(context, 16), CommonUtil.dip2px(context, 12));
            holder.circleLove.setCompoundDrawables(drawable, null, null, null);
        }

        holder.circleLaud.setText("赞 " + responseData.getFavourites_count());
        holder.circleUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.taWindowEnter(responseData.getOpenid());
            }
        });

        holder.circleUserName.setText(responseData.getNickname());
        holder.circleUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.taWindowEnter(responseData.getOpenid());
            }
        });
        String content = "#" + responseData.getType() + "#";
        SpannableStringBuilder style = new SpannableStringBuilder(content + responseData.getContent());
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#5AC8FA")), 0, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.circleContent.setText(style);
        holder.circleContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemCallback != null) {
                    onItemCallback.onContentLongClicked(position, data.get(position).getTid());
                }
                return false;
            }
        });
        holder.circleTime.setText(DateUtil.getStandardDate(DateUtil.date2Long(responseData
                .getCreated_at())));
        holder.circleLove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentListener != null) {
                    commentListener.onLaudClick(v, position,data.get(position).getTid());
                }
            }
        });

        holder.circleComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentListener != null) {
                    commentListener.onCommentClick(v, position);
                }
            }
        });
        holder.circleCommentContainer.removeAllViews();
        if (responseData.getComment() != null && responseData.getComment().size() > 0) {
            for (TopicResponseData.CommentBean commentData : responseData.getComment()) {
                String nickName = commentData.getNickname();
                String reViewUserName = commentData.getBe_review_username();
                String contents = commentData.getContent();
                LinearLayout layout = new LinearLayout(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 0, CommonUtil.dip2px(context, 10), 0);
                layout.setLayoutParams(lp);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                TextView nickNmae = new TextView(context);
                TextView commentContent = new TextView(context);
                if(!reViewUserName.isEmpty()&&!reViewUserName.equals("")){
                    nickNmae.setText(nickName+" 回复 "+reViewUserName+":");
                }else{
                    nickNmae.setText(nickName + " : ");
                }
                nickNmae.setTextColor(Color.parseColor("#5AC8FA"));
                commentContent.setText(contents);
                nickNmae.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                commentContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                layout.addView(nickNmae, 0);
                layout.addView(commentContent, 1);
                layout.setTag(R.id.tag_nickname,commentData.getNickname());
                layout.setTag(R.id.tag_commentId,commentData.getTcid());
                layout.setTag(R.id.tag_Tid,responseData.getTid());
                holder.circleCommentContainer.addView(layout);
                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout layout= (LinearLayout) v;
                        String nickName= (String) layout.getTag(R.id.tag_nickname);
                        int commentId= (int) layout.getTag(R.id.tag_commentId);
                        int topId= (int) layout.getTag(R.id.tag_Tid);
                        commentListener.onReviewClick(layout,holder.getPosition(),nickName,commentId,topId);
                    }
                });
            }
        }

        final ArrayList<String> imgUrlList = new ArrayList<String>();
        ArrayList<String> thumbUrlList = new ArrayList<String>();
        PicDisPlayAdapter picAdapter = new PicDisPlayAdapter(context, thumbUrlList);
        picAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                PicBrowserBean picBrowserBean = new PicBrowserBean();
                picBrowserBean.setImgUrl(imgUrlList);
                picBrowserBean.setCurSelectedIndex(position);
                picBrowserBean.setType(1);
                callBack.picBrowserEnter(picBrowserBean);
            }
        });
        holder.rv_pic.setAdapter(picAdapter);
        holder.rv_pic.setItemAnimator(new DefaultItemAnimator());

        if (responseData.getImg() == null || responseData.getImg().equals("") || responseData.getThumb() == null || responseData.getThumb().equals("")) {
            holder.rv_pic.setVisibility(View.GONE);
        } else {
            String[] thumbUrls = CommonUtil.splitImageUrl(responseData.getThumb());
            String[] urls = CommonUtil.splitImageUrl(responseData.getImg());
            holder.rv_pic.setVisibility(View.VISIBLE);
            imgUrlList.addAll(Arrays.asList(urls));
            thumbUrlList.addAll(Arrays.asList(thumbUrls));
            picAdapter.notifyDataSetChanged();
        }
    }

    public List<TopicResponseData> getData(){
        return data;
    }

    public interface OnCommentClickListener {
        void onCommentClick(View v, int position);

        void onLaudClick(View v, int position,int topic_id);

        void onReviewClick(LinearLayout v,int position,String nickName,int commentId,int TopicId);
    }

    private OnCommentClickListener commentListener;

    public void setOnCommentClickListener(OnCommentClickListener commentListener) {
        this.commentListener = commentListener;
    }

    public interface OnItemCallback {
        void onContentLongClicked(int position, int topic_id);
//        void onIconClicked(int position,int topic_id);//后面点击用户头像查看等

    }

    private OnItemCallback onItemCallback;

    public void setOnItemCallback(OnItemCallback onItemCallback) {
        this.onItemCallback = onItemCallback;
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
        } else if (type.equals("物招领")) {
            imgId = R.mipmap.icon_thelost;
        }
        return imgId;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private RoundImageView circleUserIcon;
        private TextView circleUserName;
        private TextView circleTime;
        private TextView circleContent;
        private TextView circleLaud;
        private TextView circleLove;
        private TextView circleComment;
        private LinearLayout circleCommentContainer;
        private RecyclerView rv_pic;

        public ViewHolder(View view) {
            super(view);
            circleUserIcon = (RoundImageView) view.findViewById(R.id.circle_usernameicon);
            circleUserName = (TextView) view.findViewById(R.id.circle_username);
            circleTime = (TextView) view.findViewById(R.id.circle_time);
            circleContent = (TextView) view.findViewById(R.id.circle_content);
            circleLaud = (TextView) view.findViewById(R.id.circle_love);
            circleLove = (TextView) view.findViewById(R.id.love);
            circleComment = (TextView) view.findViewById(R.id.comment);
            circleCommentContainer = (LinearLayout) view.findViewById(R.id.commmentContainer);
            rv_pic= (RecyclerView) view.findViewById(R.id.rv_pic);
            rv_pic.setLayoutManager(new GridLayoutManager(context, 3));
        }
    }
}
