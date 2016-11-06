package cn.flyexp.mvc.shop;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/21 0021.
 */
public class ShopSearchWindow extends AbstractWindow implements View.OnClickListener {

    private ShopViewCallBack callBack;
    private SearchView searchView;

    public ShopSearchWindow(ShopViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        hideSoftInput();
    }

    private void initView() {
        View layout = getView(R.layout.window_shop_search);
        layout.findViewById(R.id.iv_back).setOnClickListener(this);

        searchView = (SearchView) layout.findViewById(R.id.searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(true);
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View v = ((Activity) getContext()).getCurrentFocus();
            if (v == null) {
                return;
            }
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }

}
