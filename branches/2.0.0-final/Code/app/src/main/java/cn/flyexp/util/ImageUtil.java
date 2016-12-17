package cn.flyexp.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.entity.BucketBean;
import cn.flyexp.entity.ImageBean;

/**
 * Created by tanxinye on 2016/12/15.
 */
public class ImageUtil {

    /**
     * 获取所有图片文件夹
     *
     * @return
     */
    public static ArrayList<BucketBean> getAllImageBucket() {
        ArrayList<BucketBean> list = new ArrayList<>();
        BucketBean allBucketBean = new BucketBean();
        allBucketBean.setBucketName(MainActivity.getContext().getString(R.string.gallery_all_images));
        allBucketBean.setBucketId(String.valueOf(Integer.MIN_VALUE));
        list.add(allBucketBean);

        ContentResolver contentResolver = MainActivity.getContext().getContentResolver();
        String[] projection = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.ORIENTATION,
        };
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(uri, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC");
        } catch (Exception e) {
            LogUtil.e(e);
        }
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String bucketKey = MediaStore.Images.Media.BUCKET_ID;
                String bucketId = cursor.getString(cursor.getColumnIndex(bucketKey));
                BucketBean bucketBean = new BucketBean();
                bucketBean.setBucketId(bucketId);
                bucketBean.setBucketName(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                bucketBean.setOrientation(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)));
                String cover = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                if (TextUtils.isEmpty(allBucketBean.getCover())) {
                    allBucketBean.setCover(cover);
                }
                if (list.contains(bucketBean)) {
                    continue;
                }
                Cursor c = contentResolver.query(uri, projection, bucketKey + "=?", new String[]{bucketId}, null);
                if (c != null && c.getCount() > 0) {
                    bucketBean.setImageCount(c.getCount());
                }
                bucketBean.setCover(cover);
                if (c != null && !c.isClosed()) {
                    c.close();
                }
                list.add(bucketBean);
            }
        }
        return list;
    }

    public static ArrayList<ImageBean> getImageList(String bucketId, int page, int limit) {
        ArrayList<ImageBean> list = new ArrayList<>();
        int offset = (page - 1) * limit;
        ContentResolver contentResolver = MainActivity.getContext().getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Images.Media._ID);
        projection.add(MediaStore.Images.Media.TITLE);
        projection.add(MediaStore.Images.Media.DATA);
        projection.add(MediaStore.Images.Media.BUCKET_ID);
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Images.Media.MIME_TYPE);
        projection.add(MediaStore.Images.Media.DATE_ADDED);
        projection.add(MediaStore.Images.Media.DATE_MODIFIED);
        projection.add(MediaStore.Images.Media.LATITUDE);
        projection.add(MediaStore.Images.Media.LONGITUDE);
        projection.add(MediaStore.Images.Media.ORIENTATION);
        projection.add(MediaStore.Images.Media.SIZE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Images.Media.WIDTH);
            projection.add(MediaStore.Images.Media.HEIGHT);
        }
        String selection = null;
        String[] selectionArgs = null;
        if (!TextUtils.equals(bucketId, String.valueOf(Integer.MIN_VALUE))) {
            selection = MediaStore.Images.Media.BUCKET_ID + "=?";
            selectionArgs = new String[]{bucketId};
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), selection,
                selectionArgs, MediaStore.Images.Media.DATE_ADDED +" DESC LIMIT " + limit +" OFFSET " + offset);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                ImageBean imageBean = parseImageCursorAndCreateThumImage(cursor);
                list.add(imageBean);
            }
        }
        return list;
    }

    /**
     * 解析图片cursor 并且创建缩略图
     * @param cursor
     * @return
     */
    private static ImageBean parseImageCursorAndCreateThumImage(Cursor cursor) {
        ImageBean imageBean = new ImageBean();
        long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        imageBean.setId(id);
        String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
        imageBean.setTitle(title);
        String originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        imageBean.setOriginalPath(originalPath);
        String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
        imageBean.setBucketId(bucketId);
        String bucketDisplayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        imageBean.setBucketDisplayName(bucketDisplayName);
        String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
        imageBean.setMimeType(mimeType);
        long createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
        imageBean.setCreateDate(createDate);
        long modifiedDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));
        imageBean.setModifiedDate(modifiedDate);
        imageBean.setThumbnailBigPath(createThumbnailBigFileName(originalPath).getAbsolutePath());
        imageBean.setThumbnailSmallPath(createThumbnailSmallFileName(originalPath).getAbsolutePath());
        int width = 0, height = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
        } else {
            try {
                ExifInterface exifInterface = new ExifInterface(originalPath);
                width = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_WIDTH, 0);
                height = exifInterface.getAttributeInt(ExifInterface.TAG_IMAGE_LENGTH, 0);
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }
        imageBean.setWidth(width);
        imageBean.setHeight(height);
        double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
        imageBean.setLatitude(latitude);
        double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
        imageBean.setLongitude(longitude);
        int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION));
        imageBean.setOrientation(orientation);
        long length = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
        imageBean.setLength(length);
        return imageBean;
    }

    public static File createThumbnailBigFileName(String originalPath) {
        File storeFile = StorageUtil.getCacheDirectory(MainActivity.getContext());
        File bigThumFile = new File(storeFile, "big_" +originalPath);
        return bigThumFile;
    }

    public static File createThumbnailSmallFileName(String originalPath) {
        File storeFile = StorageUtil.getCacheDirectory(MainActivity.getContext());
        File smallThumFile = new File(storeFile, "small_" +originalPath);
        return smallThumFile;
    }

    /**
     * 根据原图获取图片相关信息
     * @param context
     * @param originalPath
     * @return
     */
    public static ImageBean getImageBeanWithImage(Context context, String originalPath) {
        ContentResolver contentResolver = context.getContentResolver();
        List<String> projection = new ArrayList<>();
        projection.add(MediaStore.Images.Media._ID);
        projection.add(MediaStore.Images.Media.TITLE);
        projection.add(MediaStore.Images.Media.DATA);
        projection.add(MediaStore.Images.Media.BUCKET_ID);
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        projection.add(MediaStore.Images.Media.MIME_TYPE);
        projection.add(MediaStore.Images.Media.DATE_ADDED);
        projection.add(MediaStore.Images.Media.DATE_MODIFIED);
        projection.add(MediaStore.Images.Media.LATITUDE);
        projection.add(MediaStore.Images.Media.LONGITUDE);
        projection.add(MediaStore.Images.Media.ORIENTATION);
        projection.add(MediaStore.Images.Media.SIZE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            projection.add(MediaStore.Images.Media.WIDTH);
            projection.add(MediaStore.Images.Media.HEIGHT);
        }
        Cursor cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection.toArray(new String[projection.size()]), MediaStore.Images.Media.DATA +"=?",
                new String[]{originalPath}, null);
        ImageBean imageBean = null;
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            imageBean =  parseImageCursorAndCreateThumImage(cursor);
        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }
        cursor = null;
        return imageBean;
    }
}
