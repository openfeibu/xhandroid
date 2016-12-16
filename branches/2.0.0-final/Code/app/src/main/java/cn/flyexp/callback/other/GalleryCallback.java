package cn.flyexp.callback.other;

import android.content.Context;

import java.util.ArrayList;

import cn.flyexp.entity.BucketBean;
import cn.flyexp.entity.ImageBean;

/**
 * Created by tanxinye on 2016/12/15.
 */
public interface GalleryCallback {

    void getBuckets();

    void getImages(String bucketId,int page,int limit);

     interface OnGenerateCallback {
        void generateBuckets(ArrayList<BucketBean> list);

        void generateImages(ArrayList<ImageBean> list);
    }
}
