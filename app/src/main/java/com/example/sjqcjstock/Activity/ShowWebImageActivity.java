package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.view.ImageControlView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 点击图片放大的Activity
 * Created by Administrator on 2016/7/29.
 */
public class ShowWebImageActivity extends Activity {
    private String imagePath = null;
    private ImageControlView imageView = null;
    private TextView black = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_webimage);
        this.imagePath = getIntent().getStringExtra("image");
        imageView = (ImageControlView) findViewById(R.id.show_webimage_imageview);
        black = (TextView) findViewById(R.id.black);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            Bitmap bitmap = ((BitmapDrawable) ShowWebImageActivity.loadImageFromUrl(this.imagePath)).getBitmap();
            imageView.setImageBitmap(bitmap);

            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;

            int screenW = this.getWindowManager().getDefaultDisplay().getWidth();
            int screenH = this.getWindowManager().getDefaultDisplay().getHeight()
                    - statusBarHeight;
            if (bitmap != null) {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
