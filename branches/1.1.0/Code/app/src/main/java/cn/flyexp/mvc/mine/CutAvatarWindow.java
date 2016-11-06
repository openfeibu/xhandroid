package cn.flyexp.mvc.mine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.BitmapUtil;
import cn.flyexp.view.ClipImageLayout;

/**
 * Created by txy on 2016/8/6 0006.
 */
public class CutAvatarWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;
    private ClipImageLayout imgeLayout;
    private int mType;

    public CutAvatarWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_cutheadpic);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_comfirm).setOnClickListener(this);

        imgeLayout = (ClipImageLayout) findViewById(R.id.imgeLayout);
    }

    public void setImage(String imgUrl) {
        imgeLayout.setImageBitmap(BitmapFactory.decodeFile(imgUrl));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                hideWindow(true);
                break;
            case R.id.tv_comfirm:
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                List<File> files = new ArrayList<File>();
                files.add(saveCroppedImage(imgeLayout.clip()));
                callBack.uploadImageAvatar(token, files);
                break;
        }
    }


    private File saveCroppedImage(Bitmap bmp) {
        File file = new File("/sdcard/flyexp");
        if (!file.exists())
            file.mkdir();

        file = new File("/sdcard/temp.jpg".trim());
        String fileName = file.getName();
        String mName = fileName.substring(0, fileName.lastIndexOf("."));
        String sName = fileName.substring(fileName.lastIndexOf("."));

        String newFilePath = "/sdcard/flyexp" + "/" + mName + "_cropped" + sName;
        file = new File(newFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return BitmapUtil.compressBmpToFile(newFilePath);
    }
}

