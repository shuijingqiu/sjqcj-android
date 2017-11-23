package com.example.sjqcjstock.netutil;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
     * 获取图片的设置加载处理信息(头像用)
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
     * TextView 显示图片的加载器 (只用于显示表情的)
     *
     * @param resources
     * @return
     */
    public static Html.ImageGetter getImageGetter(final Resources resources) {
        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                // 如果为http 开头  并且以.gif 结尾的 就当表情处理
                if(source.indexOf("http")!=-1 && source.indexOf(".gif")!=-1){
                    source = source.substring(source.lastIndexOf("/" )+1,source.indexOf(".gif"));
                }

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

    /**
     * 根据传入的宽高来计算图片缩放比例
     */
    public static double getProportion(int width ,int height){
        Double proportion = 1.0d;

        if (width>720){
            proportion = 720.0/width;
        }
        if (proportion*height>1280){
            proportion = 1280.0/height;
        }
        if (proportion == 0){
            proportion = 0.0d;
        }
        return proportion;
    }

    /**
     * 获取sd卡的根目录
     * @return
     */
    public static String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
        if(sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString()+"/sjqimg.jpg";
    }

    /**
     * 删除保存的图片
     * @param ctx
     * @param filePath
     */
    public static void scanFileAsync(Context ctx, String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
                // 发送广播刷新缩虐图
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(Uri.fromFile(new File(filePath)));
                ctx.sendBroadcast(scanIntent);
        }
    }


    // 将图片上传到服务器
    public static String sendData(Bitmap bitmap) throws Exception {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(
                    Constants.newUrls + "/api/upload/post");
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("token", new StringBody(Constants.apptoken));
            entity.addPart("upload_type", new StringBody("image"));
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                    true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmpCompressed.compress(Bitmap.CompressFormat.PNG, 80, bos);
            byte[] data = bos.toByteArray();
            entity.addPart("mylmage", new ByteArrayBody(data, "temp.png"));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            String resstr = HttpUtil.changeInputStream(in);
            Log.e("mhimg",resstr);
            JSONObject jsonObject = new JSONObject(resstr);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                return "";
            }
            JSONObject dataObject = new JSONObject(jsonObject.getString("data"));
            return dataObject.getString("attach_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
