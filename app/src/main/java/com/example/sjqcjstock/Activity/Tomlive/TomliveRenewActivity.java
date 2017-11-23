package com.example.sjqcjstock.Activity.Tomlive;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.RechargeActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.tomlive.PriceAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.Tomlive.SpinnerData;
import com.example.sjqcjstock.entity.Tomlive.TomliveRoomEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Md5Util;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.SoListView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播间续费页面
 * Created by Administrator on 2017/2/22.
 */
public class TomliveRenewActivity extends Activity{

    // 网络请求提示
    private CustomProgress dialog;
    // 头像
    private ImageView headImg;
    // 名称
    private TextView nameTv;
    // 个人简介
    private TextView presentTv;
    // 房间简介
    private TextView introduceTv;
    // 价格选择的List
    private SoListView priceLv;
    // 价格选择的Adapter
    private PriceAdapter priceAdapter;

    // 房间ID
    private String roomId;
    // 房间接口返回的信息
    private String resstr;
    // 房主的Uid
    private String uid;
    // 房间信息JSON
    private String roomJson;
    // 支付的水晶币个数
    private String sjbCount = "0";
    // 订阅的月份
    private String desertTime;
    // 价格数据
    private ArrayList<SpinnerData> pricelist;
    // 订阅的接口返回
    private String desertRst;

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_tomlive_renew);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
        getData();
    }

    private void findView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headImg = (ImageView) findViewById(R.id.head_iv);
        nameTv = (TextView) findViewById(R.id.name_tv);
        presentTv = (TextView) findViewById(R.id.present_tv);
        presentTv.setOnClickListener(new View.OnClickListener() {
            Boolean flag = true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    presentTv.setEllipsize(null); // 展开
                    presentTv.setSingleLine(flag);
                }else{
                    flag = true;
                    presentTv.setLines(2);
                    presentTv.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    presentTv.setSingleLine(flag);
                }
            }
        });
        introduceTv = (TextView) findViewById(R.id.introduce_tv);
        presentTv.setText(getIntent().getStringExtra("intro"));
        nameTv.setText(getIntent().getStringExtra("name"));
        uid = getIntent().getStringExtra("uid");
        roomId = getIntent().getStringExtra("roomId");
        if (uid != null) {
            ImageLoader.getInstance().displayImage(Md5Util.getuidstrMd5(Md5Util
                            .getMd5(uid)),
                    headImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
        }

        // 显示加载价格数据
        priceLv = (SoListView) findViewById(R.id.price_lv);
        priceAdapter = new PriceAdapter(TomliveRenewActivity.this);
        priceLv.setAdapter(priceAdapter);
        priceLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sjbCount = pricelist.get(position).getPrice();
                desertTime = pricelist.get(position).getTime();
                priceAdapter.setItem(desertTime);
            }
        });
    }


    /**
     * 获取直播间数据
     */
    public void getData() {
        // 获取直播间信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resstr = HttpUtil.restHttpGet(Constants.moUrl+"/live/room/info&id="+roomId);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 续订按钮的单机事件
     * @param view
     */
    public void PayCrystalCoin(View view){

        final View viewSang = LayoutInflater.from(TomliveRenewActivity.this).inflate(R.layout.dialog_pay, null);
        RelativeLayout rl_dialogDismiss = (RelativeLayout) viewSang.findViewById(R.id.rl_dialogDismiss);

        //显示需要支付水晶币数量的控件
        TextView inputCount = (TextView) viewSang.findViewById(R.id.tv_inputCount);
        inputCount.setText("需要打赏："+sjbCount+"水晶币");

        // 显示水晶币数量的控件
        TextView restCount = (TextView) viewSang.findViewById(R.id.tv_restCount);
        restCount.setText(Constants.shuijinbiCount);
        Button bt_ok = (Button) viewSang.findViewById(R.id.bt_ok);

        final AlertDialog alertDialog = new AlertDialog.Builder(TomliveRenewActivity.this).setView(viewSang).show();
        rl_dialogDismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                alertDialog.dismiss();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Integer.valueOf(Constants.shuijinbiCount) < Integer.valueOf(sjbCount)) {
                    CustomToast.makeText(getApplicationContext(), "对不起你水晶币不足。", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (!((CheckBox) viewSang.findViewById(R.id.sang_agreement_ck)).isChecked()) {
                    CustomToast.makeText(getApplicationContext(), "请阅读《付费阅读协议》", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.showDialog();
                // 确认支付订阅的接口
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        // 用户ID
                        dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("price_uid", uid));
                        dataList.add(new BasicNameValuePair("desert_time",desertTime));
                        dataList.add(new BasicNameValuePair("type", "2"));
                        dataList.add(new BasicNameValuePair("payment", "6"));
                        desertRst = HttpUtil.restHttpPost(Constants.moUrl+"/desert/order",dataList);
                        handler.sendEmptyMessage(1);
                    }
                }).start();

                alertDialog.dismiss();
            }
        });

        viewSang.findViewById(R.id.sang_agreement_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到水晶币协议页面
                Intent intent = new Intent(TomliveRenewActivity.this, AgreementActivity.class);
                intent.putExtra("type","15");
                startActivity(intent);
            }
        });
        viewSang.findViewById(R.id.bt_cz).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到充值页面
                Intent intent = new Intent(TomliveRenewActivity.this, RechargeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(resstr);
                        if ("failed".equals(jsonObject.getString("status"))){
                            dialog.dismissDlog();
                            return;
                        }
                        roomJson = jsonObject.getString("data");
                        TomliveRoomEntity tomliveRoom = JSON.parseObject(roomJson,TomliveRoomEntity.class);
                        presentTv.setText(tomliveRoom.getIntro());
                        nameTv.setText(tomliveRoom.getUsername());
                        introduceTv.setText(tomliveRoom.getRemark());
                        // 价格数据
                        pricelist = new ArrayList<SpinnerData>();
                        SpinnerData spinnerData;
                        if (tomliveRoom.getPrices()!=null && tomliveRoom.getPrices().size()>0){
                            for (int i=0;i<tomliveRoom.getPrices().size();i++){
                                spinnerData = new SpinnerData(tomliveRoom.getPrices().get(i).getExp_time(),tomliveRoom.getPrices().get(i).getPrice());
                                pricelist.add(spinnerData);
                            }
                            sjbCount = pricelist.get(0).getPrice();
                            desertTime = pricelist.get(0).getTime();
                            priceAdapter.setItem(desertTime);
                            // 加载数据价格
                            priceAdapter.setlistData(pricelist);
                            Utils.setListViewHeightBasedOnChildren(priceLv);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismissDlog();
                    break;
                case 1:
                    dialog.dismissDlog();
                    try {
                        JSONObject jsonObject = new JSONObject(desertRst);
                        if ("failed".equals(jsonObject.getString("status"))) {
                            Toast.makeText(getApplicationContext(), "续订失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Toast.makeText(getApplicationContext(), "续订成功", Toast.LENGTH_SHORT).show();
                        Constants.shuijinbiCount = Integer.valueOf(Constants.shuijinbiCount) - Integer.valueOf(sjbCount)+"";
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
