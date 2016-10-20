package com.example.sjqcjstock.netutil;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 图片处理的共通类
 * Created by Administrator on 2016/5/7.
 */
public class ImageUtil {

    private static AnimateFirstDisplayListener animateFirstDisplayListener;
    private static DisplayImageOptions displayImageOptions;
    private static DisplayImageOptions displayImageOptions1 = null;

    public static AnimateFirstDisplayListener getAnimateFirstDisplayListener() {
        if (animateFirstDisplayListener == null) {
            animateFirstDisplayListener = new AnimateFirstDisplayListener();
            return animateFirstDisplayListener;
        } else {
            return animateFirstDisplayListener;
        }
    }

    private static class AnimateFirstDisplayListener extends
            SimpleImageLoadingListener {

        List<String> displayedImages = Collections
                .synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view,
                                      Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /**
     * 获取ImageView视图的同时加载显示url
     *
     * @param
     * @return
     */
    public static ImageView getImageView(Context context, String url) {
        ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(
                R.layout.view_banner, null);
        ImageLoader.getInstance().displayImage(url, imageView, getOptionLocal(), getAnimateFirstDisplayListener());
        return imageView;
    }

    /**
     * 获取图片的设置加载处理信息
     *
     * @return
     */
    public static DisplayImageOptions getOption() {
        if (displayImageOptions == null) {
            displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory()
                    .showImageOnLoading(R.mipmap.portrait2)//设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.portrait2)
                    .showImageOnFail(R.mipmap.portrait2)
                    .cacheInMemory(true)//启用内存缓存
                    .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                    .displayer(new RoundedBitmapDisplayer(0)). // 是否设置为方形或椭圆
                    imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 图片显示方式
                    .bitmapConfig(Bitmap.Config.RGB_565).build();// 設置圖片配置信息
        }
        return displayImageOptions;
    }

    /**
     * 获取图片的设置加载处理信息（图片缓存在本地 滚动图片的时候用）
     *
     * @return
     */
    public static DisplayImageOptions getOptionLocal() {
        if (displayImageOptions1 == null) {
            displayImageOptions1 = new DisplayImageOptions.Builder().cacheInMemory()
                    .showImageOnLoading(R.mipmap.nullimg)//设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.mipmap.nullimg)//设置图片Uri为空或是错误的时候显示的图  显示这个图片不对
                    .showImageOnFail(R.mipmap.nullimg)//设置图片加载/解码过程中错误时候显示的图片  显示这个图片不对
                    .cacheInMemory(true)//启用内存缓存
                    .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                    .displayer(new RoundedBitmapDisplayer(1)). // 是否设置为方形或椭圆
                    imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 图片显示方式
                    .bitmapConfig(Bitmap.Config.ARGB_8888).build();// 設置圖片配置信息
//                    .bitmapConfig(Bitmap.Config.RGB_565).build();// 設置圖片配置信息
        }
        return displayImageOptions1;
    }

    /**
     * TextView 显示图片的加载器
     *
     * @param resources
     * @return
     */
    public static Html.ImageGetter getImageGetter1(final Resources resources) {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                if (Constants.facemap2.get(source) != null) {
                    int id = (Integer) Constants.facemap2.get(source);
                    Drawable d = resources.getDrawable(id);
                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    return d;
                }
                return null;
            }
        };
        return imageGetter;
    }

    /**
     * TextView 显示图片的加载器
     *
     * @param resources
     * @return
     */
    public static Html.ImageGetter getImageGetter(final Resources resources) {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                if (Constants.facemap2.get(source) != null) {
                    int id = (Integer) Constants.facemap2.get(source);
                    // int id=id2;
                    // int id = Integer.parseInt(source);
                    Drawable d = resources.getDrawable(id);
                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                    return d;
                }
                return null;
            }
        };
        return imageGetter;
    }

    /**
     * TextView 显示图片的加载器
     *
     * @return
     */
    public static Html.ImageGetter getImageGetter1() {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String source) {
                InputStream is = null;
                try {
                    is = (InputStream) new URL(source).getContent();
                    Drawable d = Drawable.createFromStream(is, "src");
                    d.setBounds(0, 0, d.getIntrinsicWidth() * 2,
                            d.getIntrinsicHeight() * 2);
                    is.close();
                    return d;
                } catch (Exception e) {
                    return null;
                }
            }
        };
        return imageGetter;
    }


    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        int width = 0;
        if (width == 0) {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            width = display.getWidth();
        }
        return width;
    }
}
