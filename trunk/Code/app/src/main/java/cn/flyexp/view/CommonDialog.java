package cn.flyexp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.flyexp.R;

/**
 * Created by guo on 2016/9/19.
 * modify by zlk
 * 2016/10/23 cancleDialog变成commonDialog
 */
public class CommonDialog extends Dialog {
    private Context context;

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }


        public CommonDialog create() {
            return create(null);
        }

        public CommonDialog create(CharSequence text) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommonDialog dialog = new CommonDialog(context, R.style.cancelDialog);
            View layout = inflater.inflate(R.layout.layout_cancel_topic, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                    , android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);

            TextView textView= (TextView) layout.findViewById(R.id.cancel_topic);
            if(text != null && !"".equals(text)) {
                textView.setText(text);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.contentClick();
                }
            });
            return dialog;
        }
    }

    public void contentClick(){
        if(listenter!=null){
            listenter.onContentClick();
        }
    }

    private ContentClickListenter listenter;

    public void setContentListener(ContentClickListenter listener) {
        this.listenter = listener;
    }

    public interface ContentClickListenter {
        void onContentClick();
    }

}
