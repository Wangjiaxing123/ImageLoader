package com.newtrekwang.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by Newterk on 2018/4/19.
 */

public class NewtrekLoader {
    private ImageResizer mImageResizer;
    private ImageCache mImageCache;
    private HttpLoader mHttpLoader;
    private NewtrekLoader(){
        mImageResizer = new ImageResizer();
        mImageCache = new MemeryCache();// 默认使用内存缓存
        mHttpLoader = new HttpLoader(mImageCache);
    }

    private static class NewtrekLoaderHolder{
         private static final NewtrekLoader instance= new NewtrekLoader();
    }

    public static NewtrekLoader getInstance(){
        return NewtrekLoaderHolder.instance;
    }

    public  void displayImage(String url,ImageView imageView){
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        mHttpLoader.postLoadTask(url,imageView);
    }

    public void displayImage(Context context,int resId,ImageView imageView){
        Bitmap bitmap = mImageResizer.decodeSampledBitmapFromResource(context.getResources(),
                resId,
                imageView.getMeasuredWidth(),
                imageView.getMeasuredHeight());
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }

    }
}
