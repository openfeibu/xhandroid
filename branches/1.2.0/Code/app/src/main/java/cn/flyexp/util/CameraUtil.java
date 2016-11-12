package cn.flyexp.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateFormat;

import cn.flyexp.wxapi.Util;

public final class CameraUtil {

    private static final String TAG = "MicroMsg.SDK.CameraUtil";

    private static String filePath = null;

    private CameraUtil() {
        // can't be instantiated
    }

    public static boolean takePhoto(final Activity activity, final String dir, final String filename, final int cmd) {
        filePath = dir + filename;

        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final File cameraDir = new File(dir);
        if (!cameraDir.exists()) {
            return false;
        }

        final File file = new File(filePath);
        final Uri outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        try {
            activity.startActivityForResult(intent, cmd);

        } catch (final ActivityNotFoundException e) {
            return false;
        }
        return true;
    }

    public static String getResultPhotoPath(Context context, final Intent intent, final String dir) {
        if (filePath != null && new File(filePath).exists()) {
            return filePath;
        }

        return resolvePhotoFromIntent(context, intent, dir);
    }

    public static String resolvePhotoFromIntent(final Context ctx, final Intent data, final String dir) {
        if (ctx == null || data == null || dir == null) {
            return null;
        }

        String filePath = null;

        final Uri uri = Uri.parse(data.toURI());
        Cursor cu = ctx.getContentResolver().query(uri, null, null, null, null);
        if (cu != null && cu.getCount() > 0) {
            try {
                cu.moveToFirst();
                final int pathIndex = cu.getColumnIndex(MediaColumns.DATA);
                filePath = cu.getString(pathIndex);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (data.getData() != null) {
            filePath = data.getData().getPath();
            if (!(new File(filePath)).exists()) {
                filePath = null;
            }

        } else if (data.getAction() != null && data.getAction().equals("inline-data")) {

            try {
                final String fileName = DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()).toString();
                filePath = dir + fileName;

                final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                final File file = new File(filePath);
                if (!file.exists()) {
                    file.createNewFile();
                }

                BufferedOutputStream out;
                out = new BufferedOutputStream(new FileOutputStream(file));
                final int cQuality = 100;
                bitmap.compress(Bitmap.CompressFormat.PNG, cQuality, out);
                out.close();

            } catch (final Exception e) {
                e.printStackTrace();
            }

        } else {
            if (cu != null) {
                cu.close();
                cu = null;
            }
            return null;
        }
        if (cu != null) {
            cu.close();
            cu = null;
        }
        return filePath;
    }

}
