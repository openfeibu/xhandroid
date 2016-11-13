package cn.flyexp.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.flyexp.R;

/**
 * Created by tanxinye on 2016/10/6.
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private static int default_width = 64; // 默认宽度
    private static int default_height = 64;// 默认高度

    public LoadingDialog(Context context, int layout, int style,String mes) {
        this(context, default_width, default_height, layout, style,mes);
    }

    public LoadingDialog(Context context, int width, int height, int layout,
                        int style,String mes) {
        super(context, style);

        setContentView(layout);
        TextView tv_mes = (TextView) findViewById(R.id.tv_mes);
        tv_mes.setText(mes);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        float density = getDensity(context);
        params.width = (int) (width * density);
        params.height = (int) (height * density);
        params.gravity = Gravity.CENTER;
        params.dimAmount = 0; // 去背景遮盖
        params.alpha = 1.0f;

        window.setAttributes(params);
    }

    private float getDensity(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }
}
