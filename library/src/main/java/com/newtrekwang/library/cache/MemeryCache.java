package com.newtrekwang.library.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.newtrekwang.library.cache.ImageCache;

/**
 * Created by Newterk on 2018/4/19.
 * 内存缓存策略
 */

public class MemeryCache implements ImageCache {
    // 使用LruCache
    private LruCache<String,Bitmap> mMemeryCache;

    /**
     * 初始化
     * 默认缓存空间大小 为可用空间的 八分之一
     */
    public MemeryCache(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;
        mMemeryCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        return mMemeryCache.get(url);
    }

    @Override
    public void put(String url, Bitmap bmp) {
        mMemeryCache.put(url,bmp);
    }

    @Override
    public void remove(String url) {
        mMemeryCache.remove(url);
    }
}
