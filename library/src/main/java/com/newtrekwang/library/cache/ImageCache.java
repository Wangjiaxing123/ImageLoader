package com.newtrekwang.library.cache;

import android.graphics.Bitmap;

/**
 * Created by Newterk on 2018/4/19.
 * 缓存策略通用接口
 */

public interface ImageCache {
    /**
     * 由url获取缓存的bitmap
     * @param url key
     * @return bitmap
     */
    Bitmap get(String url);

    /**
     * 存入bitmap
     * @param url key
     * @param bmp value
     */
    void put(String url, Bitmap bmp);

    /**
     * 删除缓存的指定bimap
     * @param url key
     */
    void remove(String url);
}
