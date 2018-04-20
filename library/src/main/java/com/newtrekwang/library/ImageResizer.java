package com.newtrekwang.library;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by Newterk on 2018/4/19.
 *
 * 图片压缩器
 */

public class ImageResizer {
    private static final String TAG = "ImageResizer>";
    public ImageResizer(){}

    /**
     * 从资源包加载出合适的bitmap
     * @param res 资源对象
     * @param resId 图片资源id
     * @param reqWidth 所需图片宽度
     * @param reqHeight 所需图片高度
     * @return
     */
    public Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
        // 首先BitmapFactory.Options 的inJustDecodeBounds设为true，只用于检测出图片原始尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res,resId,options);
        // 计算出用于显示的合适尺寸
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    /**
     * 从文件描述符加载出合适的bitmap
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd,int reqWidth,int reqHeight){
        // 测原始尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd,null,options);
        // 计算尺寸
        options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }

    /**
     * 计算用于显示的合适尺寸
     * @param options 用于取得图片原始尺寸
     * @param reqWidth 用于显示的宽
     * @param reqHeight 用于显示的高
     * @return 合适缩放级别
     */
    private int calculateInSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        if (reqWidth == 0 || reqHeight == 0){
            return 1;//原始尺寸
        }

        final int rawHeight = options.outHeight;
        final int rawWidth = options.outWidth;
        int inSampleSize = 1;

        if (rawHeight > reqHeight || rawWidth > reqWidth){
            final int halfHeight = rawHeight /2;
            final int halfWidth = rawWidth /2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth){
                inSampleSize = inSampleSize << 1;
            }
        }
        Log.i(TAG, "calculateInSampleSize: >>"+inSampleSize);
        return inSampleSize;
    }


}
