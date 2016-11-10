package cn.flyexp.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import cn.flyexp.util.LogUtil;

/**
 * Created by tanxinye on 2016/10/31.
 */
public class LoadMoreRecyclerView extends RecyclerView {

    private Context context;
    private LoadingMoreFooter loadingMoreFooter;
    private Adapter adapter;
    private LoadMoreFootAdapter loadMoreFootAdapter;
    private boolean canloadMore = true;
    private LoadMoreLinstener loadMoreLinstener;
    private boolean isLoading;
    private int itemCount;
    private int currItemCount;

    public LoadMoreRecyclerView(Context context) {
        super(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
    }

    public void setCanloadMore(boolean flag) {
        canloadMore = flag;
    }

    private void initView() {
        loadingMoreFooter = new LoadingMoreFooter(context);
        loadingMoreFooter.setVisibility(GONE);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        loadMoreFootAdapter = new LoadMoreFootAdapter(this, loadingMoreFooter, adapter);
        super.setAdapter(loadMoreFootAdapter);
        this.adapter.registerAdapterDataObserver(dataObserver);
        currItemCount = adapter.getItemCount();
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        LogUtil.e("onScrolled");
        if (dy > 0 && loadMoreLinstener != null && canloadMore && !isLoading) {
            LogUtil.e("onScrolled dy");
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = last(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1) {
                if (loadingMoreFooter != null) {
                    loadingMoreFooter.showLoading();
                }
                loadMoreLinstener.onLoadMore();
                isLoading = true;
                LogUtil.e("show more");
            }
        }
    }

    private int last(int[] lastPositions) {
        int last = lastPositions[0];
        for (int value : lastPositions) {
            if (value > last) {
                last = value;
            }
        }
        return last;
    }


    public interface LoadMoreLinstener {
        void onLoadMore();
    }

    public void setLoadMoreLinstener(LoadMoreLinstener loadMoreLinstener) {
        this.loadMoreLinstener = loadMoreLinstener;
    }

    private void loadMoreComplete() {
        if (loadingMoreFooter != null) {
            loadingMoreFooter.setVisibility(View.GONE);
        }
    }

    private void loadMoreEnd() {
        if (loadingMoreFooter != null) {
            loadingMoreFooter.showEnd();
        }
    }

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            loadMoreFootAdapter.notifyDataSetChanged();
            isLoading = false;
            itemCount = adapter.getItemCount();
            if (itemCount == currItemCount) {
                loadMoreEnd();
            } else {
                loadMoreComplete();
            }
            currItemCount = itemCount;
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            loadMoreFootAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            loadMoreFootAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            loadMoreFootAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            loadMoreFootAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            loadMoreFootAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

}
