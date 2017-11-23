package com.example.sjqcjstock.Activity.stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.ParseException;

/**
 * 订阅订单详情
 * Created by Administrator on 2016/12/26.
 */
public class DynamicOrderDetailsActivity extends Activity {

    // 头像
    private ImageView headIv;
    // 姓名
    private TextView nameTv;
    // 到期天数
    private TextView timeDaysTv;
    // 有效期至
    private TextView expTimeTv;
    // 订单编号
    private TextView orderNumberTv;
    // 创建时间
    private TextView timeTv;
    // 用户uid
    private String uid;
    // 名称
    private String name;
    // 到期时间
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dynamic_order_details);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    /**
     * 页面控件的绑定
     */

    private void findView() {
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headIv = (ImageView) findViewById(R.id.head_iv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        timeDaysTv = (TextView) findViewById(R.id.time_days);
        expTimeTv = (TextView) findViewById(R.id.exp_time);
        orderNumberTv = (TextView) findViewById(R.id.order_number);
        timeTv = (TextView) findViewById(R.id.time);
    }

    /**
     * 接口数据的获取
     */
    private void getData(){
        uid = getIntent().getStringExtra("price_uid");
        name = getIntent().getStringExtra("price_username");
        time = getIntent().getStringExtra("exp_time");

        nameTv.setText(name);
        try {
            timeDaysTv.setText("还有"+Utils.CalculatedDays(time)+"天到期");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        expTimeTv.setText(time);
        timeTv.setText(getIntent().getStringExtra("time"));
        orderNumberTv.setText(getIntent().getStringExtra("order_number"));
        ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                        .getMd5(uid)),
                headIv, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
//        intent.putExtra("price_uid",desertEntity.getPrice_uid());
//        intent.putExtra("price_username",desertEntity.getPrice_username());
//        intent.putExtra("exp_time",desertEntity.getExp_time());
//        intent.putExtra("time",desertEntity.getTime());
//        intent.putExtra("order_number",desertEntity.getOrder_number());

    }

    /**
     * 延长订阅的单击事件
     */
    public void extendedClick(View view){
        // 跳转到订阅确认页面
        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("time",time);
        intent.putExtra("uid",uid);
        intent.putExtra("type","1");
        intent.setClass(DynamicOrderDetailsActivity.this,SubscribeConfirmActivity.class);
        startActivity(intent);
    }

    /**
     *  阅读协议的单机事件
     */
    public void serviceClick(View view){
        Intent intent = new Intent(DynamicOrderDetailsActivity.this, AgreementActivity.class);
        intent.putExtra("type", "12");
        startActivity(intent);
    }

}
