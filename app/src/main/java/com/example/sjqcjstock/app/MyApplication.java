package com.example.sjqcjstock.app;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application {
    private ImageLoader imageLoader;


    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    public void init(Context context) {
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.
                Builder(context).threadPriority(5)
                .denyCacheImageMultipleSizesInMemory().//缓存显示不同大小的同一张图片
                diskCacheFileNameGenerator(new Md5FileNameGenerator()). //文件名字的加密策略
                tasksProcessingOrder(QueueProcessingType.LIFO).build();

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
    }
}
