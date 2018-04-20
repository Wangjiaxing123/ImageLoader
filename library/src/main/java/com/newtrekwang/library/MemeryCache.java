package com.newtrekwang.library;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Newterk on 2018/4/19.
 * 内存缓存策略
 */

public class MemeryCache implements ImageCache {
    private LruCache<String,Bitmap> mMemeryCache;

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
}
