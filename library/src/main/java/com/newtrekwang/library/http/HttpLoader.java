package com.newtrekwang.library.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.newtrekwang.library.cache.ImageCache;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Newterk on 2018/4/20.
 * http 加载器 异步加载
 */

public class HttpLoader {
    // 图片加载策略
    private ImageCache mImageCache;
    // cpu核心数
    public static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // 核心线程
    public static final int CORE_POOL_SIZE = CPU_COUNT +1;
    // 最多线程
    public static final int MAXIMUM_POOL_SIZE = CPU_COUNT *2 +1;
    // 保持时间
    public static final long KEEP_ALIVE = 10L;
    // 缓冲区大小
    public static final int IO_BUFFER_SIZE = 8 * 1024;

    public HttpLoader(ImageCache mImageCache) {
        this.mImageCache = mImageCache;
    }

    // 一个简单的线程工厂
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(@NonNull Runnable runnable) {
            return new Thread(runnable,"ImageLoader#"+mCount.getAndIncrement());
        }
    };

    // 线程池
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<Runnable>(),
            sThreadFactory);
    /**
     * 主线程处理加载结果
     */
    private Handler mMainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 200){
                LoaderResult loaderResult = (LoaderResult) msg.obj;
                Bitmap bmp= loaderResult.bitmap;
                if (bmp != null){
                  loaderResult.imageView.setImageBitmap(bmp);
                }
            }
        }
    };

    /**
     * post 一个 网络加载任务
     * @param url
     * @param imageView
     */
    public void postLoadTask(final String url,final ImageView imageView){
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadBitmapFromUrl(url);
                mImageCache.put(url,bitmap);

                Message message = Message.obtain(null,200);
                LoaderResult loaderResult = new LoaderResult(imageView,url,bitmap);
                message.obj = loaderResult;
                mMainHandler.sendMessage(message);
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 下载图片
     * @param url
     * @return
     */
    private Bitmap downloadBitmapFromUrl (String url){
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL u = new URL(url);
            urlConnection = (HttpURLConnection) u.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 加载结果，跨线程通信消息载体
     */
    private static class LoaderResult{
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;

        public LoaderResult(ImageView imageView, String uri, Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}
