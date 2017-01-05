package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.ImageControlView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 点击图片放大的Activity
 * Created by Administrator on 2016/7/29.
 */
public class ShowWebImageActivity extends Activity {

    private String imagePath = "";
    private ImageControlView imageView = null;
    private TextView black = null;
    private Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_webimage);
        imagePath = getIntent().getStringExtra("image");
        imageView = (ImageControlView) findViewById(R.id.show_webimage_imageview);
        black = (TextView) findViewById(R.id.black);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 开线程获取网络数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    bitmap = ((BitmapDrawable) ShowWebImageActivity.loadImageFromUrl(imagePath)).getBitmap();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
                Rect frame = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                int statusBarHeight = frame.top;
                int screenW = getWindowManager().getDefaultDisplay().getWidth();
                int screenH = getWindowManager().getDefaultDisplay().getHeight()
                        - statusBarHeight;
                    imageView.imageInit(bitmap, screenW, screenH, statusBarHeight,
                            new ImageControlView.ICustomMethod() {
                                @Override
                                public void customMethod(Boolean currentStatus) {
                                    // 当图片处于放大或缩小状态时，控制标题是否显示
                                    if (currentStatus) {
                                        black.setVisibility(View.GONE);
                                    } else {
                                        black.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                }
        }
    };

    public static Drawable loadImageFromUrl(String url) throws IOException {
        URL m = new URL(url);
        InputStream i = (InputStream) m.getContent();
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                imageView.mouseDown(event);
                break;
            /**
             * 非第一个点按下
             */
            case MotionEvent.ACTION_POINTER_DOWN:
                imageView.mousePointDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                imageView.mouseMove(event);
                break;
            case MotionEvent.ACTION_UP:
                imageView.mouseUp();
                break;
        }
        return super.onTouchEvent(event);
    }
}
