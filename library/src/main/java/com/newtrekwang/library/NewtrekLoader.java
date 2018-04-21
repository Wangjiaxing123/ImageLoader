package com.newtrekwang.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.UiThread;
import android.widget.ImageView;

import com.newtrekwang.library.cache.ImageCache;
import com.newtrekwang.library.cache.MemeryCache;
import com.newtrekwang.library.http.HttpLoader;


/**
 * Created by Newterk on 2018/4/19.
 * 加载器管理类
 */

public class NewtrekLoader {

    // 图片压缩器
    private ImageResizer mImageResizer;
    // 缓存策略加载器
    private ImageCache mImageCache;
    // http加载器
    private HttpLoader mHttpLoader;
    // 开启http加载
    private boolean openHttpLoader = true;
    /**
     * 初始化
     * 默认使用MemeryCache，内存加载策略
     * 默认开启http加载
     */
    private NewtrekLoader(){
        mImageResizer = new ImageResizer();
        mImageCache = new MemeryCache();// 默认使用内存缓存
        mHttpLoader = new HttpLoader(mImageCache);
    }

    /**
     * 静态内部类，便于实现NewtrekLoader单例
     */
    private static class NewtrekLoaderHolder{
         private static final NewtrekLoader instance= new NewtrekLoader();
    }

    /**
     * 获取NewtrekLoader单例
     * @return
     */
    public static NewtrekLoader getInstance(){
        return NewtrekLoaderHolder.instance;
    }

    /**
     * 展示图片
     * @param url 图片url
     * @param imageView 图片控件
     */
    @UiThread
    public  void displayImage(String url,ImageView imageView){
        Bitmap bitmap = mImageCache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        if (openHttpLoader){
            // 异步过程
            mHttpLoader.postLoadTask(url,imageView);
        }
    }

    /**
     * 展示图片，是同步过程
     * @param context 上下文，便于获取多媒体资源
     * @param resId 图片资源id
     * @param imageView 图片控件
     */
    @UiThread
    public void displayImage(Context context, @DrawableRes int resId, ImageView imageView){
        Bitmap bitmap = mImageResizer.decodeSampledBitmapFromResource(context.getResources(),
                resId,
                imageView.getMeasuredWidth(),
                imageView.getMeasuredHeight());
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
    }


    /**
     * 加载器是否开启http加载
     * @return
     */
    public boolean isOpenHttpLoader() {
        return openHttpLoader;
    }


    /**
     * 设置是否开启http加载
     * @param openHttpLoader
     */
    public void setOpenHttpLoader(boolean openHttpLoader) {
        this.openHttpLoader = openHttpLoader;
    }
}
