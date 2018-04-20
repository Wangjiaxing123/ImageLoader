package com.newtrekwang.library;

import android.graphics.Bitmap;

/**
 * Created by Newterk on 2018/4/19.
 * 缓存策略
 */

public interface ImageCache {
    Bitmap get(String url);
    void put(String url, Bitmap bmp);
}
