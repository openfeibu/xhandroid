package cn.flyexp.window.other;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/12/20.
 */
public class EditWindow extends BaseWindow implements TextWatcher {

    @InjectView(R.id.edt_value)
    EditText edtValue;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    private String value;
    private String name;
    private int length;
    private String result;

    @Override
    protected int getLayoutId() {
        return R.layout.window_edit;
    }

    public EditWindow(Bundle bundle) {
        length = bundle.getInt("length");
        name = bundle.getString("name");
        value = bundle.getString("value");

        tvTitle.setText(name);
        edtValue.setText(value);
        edtValue.setMaxEms(length);
        edtValue.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.tv_save})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_save:
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                bundle.putString("name", name);
                Message mes = Message.obtain();
                mes.setData(bundle);
                mes.what = NotifyIDDefine.NOTIFY_EDIT_RESULT;
                getNotifyManager().notify(mes);
                hideWindow(true);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        result = edtValue.getText().toString().trim();
        if (TextUtils.isEmpty(result) || TextUtils.equals(result, value)) {
            tvSave.setEnabled(false);
            tvSave.setAlpha(0.5f);
        } else {
            tvSave.setEnabled(true);
            tvSave.setAlpha(1f);
        }
    }
}
