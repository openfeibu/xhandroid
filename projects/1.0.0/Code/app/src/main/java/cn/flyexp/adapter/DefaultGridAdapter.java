package cn.flyexp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.entity.MineBean;

/**
 * Created by guo on 2016/8/26 0026.
 */
public class DefaultGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MineBean> mineBeanArrayList = new ArrayList<>();


    public DefaultGridAdapter(Context mContext, ArrayList<MineBean> mineBeanArrayList) {
        super();
        this.mContext = mContext;
        this.mineBeanArrayList = mineBeanArrayList;
    }

    @Override
    public int getCount() {
        return mineBeanArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View converView, ViewGroup viewGroup) {
        if (converView == null) {
            converView = LayoutInflater.from(mContext).inflate(R.layout.item_gridview_mine, viewGroup, false);
        }
        TextView tv = BaseViewHolder.get(converView, R.id.textView);
        ImageView iv = BaseViewHolder.get(converView, R.id.imageView);
        iv.setBackgroundResource(mineBeanArrayList.get(i).getImg());
        tv.setText(mineBeanArrayList.get(i).getName());
        return converView;
    }
}
