package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.facelibaryAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转发微博页面
 */
public class transpondweiboActivity extends Activity {

    //表情图片
    private final int[] imagestr = {R.drawable.aoman, R.drawable.baiyan,
            R.drawable.bishi, R.drawable.bizui, R.drawable.cahan,
            R.drawable.caidao, R.drawable.chajin, R.drawable.cheer,
            R.drawable.chong, R.drawable.ciya, R.drawable.da,
            R.drawable.dabian, R.drawable.dabing, R.drawable.dajiao,
            R.drawable.daku, R.drawable.dangao, R.drawable.danu,
            R.drawable.dao, R.drawable.deyi, R.drawable.diaoxie,
            R.drawable.e, R.drawable.fadai, R.drawable.fadou,
            R.drawable.fan, R.drawable.fanu, R.drawable.feiwen,
            R.drawable.fendou, R.drawable.gangga, R.drawable.geili,
            R.drawable.gouyin, R.drawable.guzhang, R.drawable.haha,
            R.drawable.haixiu, R.drawable.haqian, R.drawable.hua,
            R.drawable.huaixiao, R.drawable.hufen, R.drawable.huishou,
            R.drawable.huitou, R.drawable.jidong, R.drawable.jingkong,
            R.drawable.jingya, R.drawable.kafei, R.drawable.keai,
            R.drawable.kelian, R.drawable.ketou, R.drawable.kiss,
            R.drawable.ku, R.drawable.kuaikule, R.drawable.kulou,
            R.drawable.kun, R.drawable.lanqiu, R.drawable.lenghan,
            R.drawable.line, R.drawable.liuhan, R.drawable.liulei,
            R.drawable.liwu, R.drawable.love, R.drawable.ma,
            R.drawable.meng, R.drawable.nanguo, R.drawable.no,
            R.drawable.ok, R.drawable.peifu, R.drawable.pijiu,
            R.drawable.pingpang, R.drawable.pizui, R.drawable.qiang,
            R.drawable.qinqin, R.drawable.qioudale, R.drawable.qiu,
            R.drawable.quantou, R.drawable.ruo, R.drawable.se,
            R.drawable.shandian, R.drawable.shengli, R.drawable.shenma,
            R.drawable.shuai, R.drawable.shuijiao, R.drawable.taiyang,
            R.drawable.tiao, R.drawable.tiaopi, R.drawable.tiaosheng,
            R.drawable.tiaowu, R.drawable.touxiao, R.drawable.tu,
            R.drawable.tuzi, R.drawable.wabi, R.drawable.weiqu,
            R.drawable.weixiao, R.drawable.wen, R.drawable.woshou,
            R.drawable.xia, R.drawable.xianwen, R.drawable.xigua,
            R.drawable.xinsui, R.drawable.xu, R.drawable.yinxian,
            R.drawable.yongbao, R.drawable.youhengheng,
            R.drawable.youtaiji, R.drawable.yueliang, R.drawable.yun,
            R.drawable.zaijian, R.drawable.zhadan, R.drawable.zhemo,
            R.drawable.zhuakuang, R.drawable.zhuanquan, R.drawable.zhutou,
            R.drawable.zuohengheng, R.drawable.zuotaiji, R.drawable.zuqiu,

    };

    //表情的描述文字
    private final String[] facestrs = {"[aoman]", "[baiyan]", "[bishi]",
            "[bizui]", "[cahan]", "[caidao]", "[chajin]", "[cheer]",
            "[chong]", "[ciya]", "[da]", "[dabian]", "[dabing]",
            "[dajiao]", "[daku]", "[dangao]", "[danu]", "[dao]", "[deyi]",
            "[diaoxie]", "[e]", "[fadai]", "[fadou]", "[fan]", "[fanu]",
            "[feiwen]", "[fendou]", "[gangga]", "[geili]", "[gouyin]",
            "[guzhang]", "[haha]", "[haixiu]", "[haqian]", "[hua]",
            "[huaixiao]", "[hufen]", "[huishou]", "[huitou]", "[jidong]",
            "[jingkong]", "[jingya]", "[kafei]", "[keai]", "[kelian]",
            "[ketou]", "[kiss]", "[ku]", "[kuaikule]", "[kulou]", "[kun]",
            "[lanqiu]", "[lenghan]", "[liuhan]", "[liulei]", "[liwu]",
            "[love]", "[ma]", "[meng]", "[nanguo]", "[no]", "[ok]",
            "[peifu]", "[pijiu]", "[pingpang]", "[pizui]", "[qiang]",
            "[qinqin]", "[qioudale]", "[qiu]", "[quantou]", "[ruo]",
            "[se]", "[shandian]", "[shengli]", "[shenma]", "[shuai]",
            "[shuijiao]", "[taiyang]", "[tiao]", "[tiaopi]", "[tiaosheng]",
            "[tiaowu]", "[touxiao]", "[tu]", "[tuzi]", "[wabi]", "[weiqu]",
            "[weixiao]", "[wen]", "[woshou]", "[xia]", "[xianwen]",
            "[xigua]", "[xinsui]", "[xu]", "[yinxian]", "[yongbao]",
            "[youhengheng]", "[youtaiji]", "[yueliang]", "[yun]",
            "[zaijian]", "[zhadan]", "[zhemo]", "[zhuakuang]",
            "[zhuanquan]", "[zhutou]", "[zuohengheng]", "[zuotaiji]",
            "[zuqiu]",

    };

    // 获取@ 的好友
    private static final int REQUEST_CODE_UNAME = 4;

    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取

    // 图文混排
    private SpannableStringBuilder spannable;

    // 右下角@标记，表情标记
    private RelativeLayout LinearLayout01;

    //评论编辑框
    private EditText editcommentforweibo1;

    private LinearLayout LinearLayoutgetimg;

    //表情包
    private ImageView facelibrary1;

    //@标记
    private ImageView atsbicon1;

    //返回键
    private LinearLayout goback1;

    //图片选取，暂时未使用
    private ImageView getimgtoalbum1;

    //发送按钮
    private LinearLayout addtranspond1;

    //复选框
    private CheckBox selected;

    // 定义适配器
    private facelibaryAdapter facelibaryAdapter;
    private GridView facegridView1;
    private ArrayList<HashMap<String, Object>> listData;

    // 判断弹出获取图片操作的标识 0代表显示，1代表隐藏
    private String isdisplaygetimg = "0";

    // 判断弹出表情库的标识 0代表显示，1代表隐藏
    private String isdisplayfacelibrary = "0";
    // 写摘要还能输入多少字
    private TextView inputNumberWords;

    // 获得微博id
    private String feedidstr;

    // 是否同时评论
    private String iscommentstr = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 4表示禁言用户不能转发微博
        if (Constants.userType.equals("4")) {
            finish();
        }
        setContentView(R.layout.transpondweibo);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub
        // 获取intent的数据
        Intent intent = getIntent();
        feedidstr = intent.getStringExtra("feed_id");
//		feeduidstr = intent.getStringExtra("feeduid");
//		content = intent.getCharSequenceExtra("content");
//		uname = intent.getStringExtra("uname");
        LinearLayout01 = (RelativeLayout) findViewById(R.id.LinearLayout01);
        editcommentforweibo1 = (EditText) findViewById(R.id.editcommentforweibo1);
        LinearLayoutgetimg = (LinearLayout) findViewById(R.id.LinearLayoutgetimg);
        addtranspond1 = (LinearLayout) findViewById(R.id.addtranspond1);
        facelibrary1 = (ImageView) findViewById(R.id.facelibrary1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        getimgtoalbum1 = (ImageView) findViewById(R.id.getimgtoalbum1);
        atsbicon1 = (ImageView) findViewById(R.id.atsbicon1);
        selected = (CheckBox) findViewById(R.id.selected);

        //所有监听事件的处理
        atsbicon1.setOnClickListener(new atsbicon1_listener());
        editcommentforweibo1.setOnClickListener(new editcommentforweibo1_listener());
        LinearLayout01.setOnClickListener(new LinearLayout01_listener());
        facelibrary1.setOnClickListener(new facelibrary1_listener());
        goback1.setOnClickListener(new goback1_listener());
        getimgtoalbum1.setOnClickListener(new getimgtoalbum1_listener());
        addtranspond1.setOnClickListener(new addtranspond1_listener());
        spannable = new SpannableStringBuilder(editcommentforweibo1.getText().toString());

        inputNumberWords = (TextView) findViewById(R.id.input_number_words);
        // 绑定摘要输入状态的变化
        editcommentforweibo1.addTextChangedListener(watcher);

        //表情包--->控件、数据、适配器
        facegridView1 = (GridView) findViewById(R.id.facegridView1);
        listData = new ArrayList<HashMap<String, Object>>();
        facelibaryAdapter = new facelibaryAdapter(transpondweiboActivity.this, listData);
        //表情包的数据
        for (int i = 0; i < 110; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("friend_image", imagestr[i]);
            map.put("facestr", facestrs[i]);
            listData.add(map);
        }
        facegridView1.setAdapter(facelibaryAdapter);
        facegridView1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                //在光标处添加文本
                int index = editcommentforweibo1.getSelectionStart();
                Editable editable = editcommentforweibo1.getText();
                editable.insert(index, facestrs[position]);
            }
        });
    }

    public static String FilterHtml(String str) {
        str = str.replaceAll("<(?!br|img)[^>]+>", "").trim();
        return str;
    }

    class addtranspond1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (editcommentforweibo1.getText().toString().length() > 140) {
                CustomToast.makeText(getApplicationContext(), "评论超长了", Toast.LENGTH_LONG).show();
                return;
            }
            //是否同时评论
            if (selected.isChecked()) {
                iscommentstr = "1";
            }

            String bodystr;
            //没有内容，默认为“//”。
            if ("".equals(editcommentforweibo1.getText().toString())) {
//				try{
//					if(content.equals("") || content == null){
                bodystr = "//";
//					} else {
//						bodystr = "//" + "@" + uname + ":" + content;
//					}
//				}catch(Exception e){
//					bodystr = "//";
//				}
            } else {
                bodystr = "//" + editcommentforweibo1.getText() + "";
//				try{
//					if(content.equals("") || content == null){
//						bodystr = editcommentforweibo1.getText()+"";
//					} else {
//						bodystr = editcommentforweibo1.getText().toString() + "//" + "@" + uname + ":" + content;
//					}
//				}catch(Exception e){
//					bodystr = editcommentforweibo1.getText()+"";
//				}
            }

//			// 评论这条微博
//			if ("1".equals(iscommentstr)) {
//				new SendInfoTaskaddcommentweibo().execute(new TaskParams(Constants.Url+"?app=public&mod=AppFeedList&act=Appaddcomment",
//								new String[] { "mid", Constants.staticmyuidstr },
//								new String[] { "login_password",Constants.staticpasswordstr },
//								new String[] { "app_name", "public" },
//								new String[] { "table_name", "feed" },
//								new String[] { "app_uid", feeduidstr },
//								new String[] { "ifShareFeed", "0" },
//								new String[] { "content", bodystr },
//								new String[] { "row_id", feedidstr }
//						));
//			}
            //转发微博，自己观点
            new SendInfoTasktranspond().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppForward",
                    new String[]{"mid", Constants.staticmyuidstr},
                    new String[]{"feed_id", feedidstr},
                    new String[]{"login_password", Constants.staticpasswordstr},
                    new String[]{"tokey", Constants.statictokeystr},
                    new String[]{"comment", iscommentstr},
                    new String[]{"body", bodystr}
            ));

        }
    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }

    class atsbicon1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(transpondweiboActivity.this,
                    atfriendActivity.class);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UNAME);
        }

    }

    class getimgtoalbum1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 从相册获取图片
            getImageFromAlbum();

        }

    }

    class facelibrary1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if ("0".equals(isdisplayfacelibrary)) {
                facegridView1.setVisibility(View.VISIBLE);
                isdisplayfacelibrary = "1";

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        editcommentforweibo1.getWindowToken(), 0);

            } else {

                facegridView1.setVisibility(View.GONE);
                isdisplayfacelibrary = "0";
            }
        }
    }

    class editcommentforweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            LinearLayout01.setVisibility(View.VISIBLE);
            facegridView1.setVisibility(View.GONE);
        }
    }

    class LinearLayout01_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // CustomToast.makeText(getApplicationContext(), "hahaha", 1)
            // .show();
            LinearLayout01.setVisibility(View.VISIBLE);

        }

    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // 从相册获取图片
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {

                Uri uri = data.getData();
                if (uri != null) {
                    // Bitmap bmp=BitmapFactory.decodeFile(uri.toString());
                    // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                    ContentResolver resolver = getContentResolver();
                    Bitmap bmp;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
                        // imgportrait.setImageBitmap(bmp);
                        Bitmap bmpCompressed = Bitmap.createScaledBitmap(bmp,
                                200, 200, true);

                        // 需要处理的文本，[smile]是需要被替代的文本
                        // spannable = new
                        // SpannableStringBuilder(editcommentforweibo1.getText().toString()+"1");
                        SpannableStringBuilder spanStr = (SpannableStringBuilder) editcommentforweibo1.getText();
                        spanStr.append("1");
                        // editcommentforweibo1.setText(editcommentforweibo1.getText().toString()+"1");
                        // 要让图片替代指定的文字就要用ImageSpan
                        ImageSpan span = new ImageSpan(bmpCompressed,
                                ImageSpan.ALIGN_BASELINE);

                        // 开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
                        // 最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
                        // spannable.setSpan(span,
                        // editcommentforweibo1.getText().length(),
                        // editcommentforweibo1.getText().length(),
                        // Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        // setText(spannable);
                        int length = editcommentforweibo1.getText().length();
                        spanStr.setSpan(span, length - 1, length,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                        editcommentforweibo1.setText(spanStr);

                        // SpannableString spannable2 = new
                        // SpannableString(spannable.toString()+"1");

                        // editcommentforweibo1.setText(editcommentforweibo1.getText().toString()+"1");

                        String fullpicurlstr = "";
                        String picheightstr = "";
                        String picurlstr = "";
                        String picwidthstr = "";

                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }

        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {

        } else if (resultCode == REQUEST_CODE_UNAME) {
            Bundle bundle = data.getExtras();
            String Name = bundle.getString("unamestr");
            // myTextView.setText("恭喜您，注册成功。您的用户名是："+Name);

            int index = editcommentforweibo1.getSelectionStart();
            Editable editable = editcommentforweibo1.getText();
            editable.insert(index, "@" + Name + " ");

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            // if (isineditarea(editcommentforweibo1, ev)) {
            // //CustomToast.makeText(getApplicationContext(), "事情edit焦点", 1)
            // // .show();
            // LinearLayout01.setVisibility(View.GONE);
            //
            // }

            // if(isineditarea(LinearLayout01, ev)==false){
            // LinearLayout01.setVisibility(View.VISIBLE);

            // }

            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    // 判断是否离开edit区域的方法
    public boolean isineditarea(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private class SendInfoTaskaddcommentweibo extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();

            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";

                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "发评论成功",
                            Toast.LENGTH_SHORT).show();
                } else {
                    CustomToast.makeText(getApplicationContext(), "发评论失败,请重试",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class SendInfoTasktranspond extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
            super.onPostExecute(result);
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "转发成功", Toast.LENGTH_LONG).show();
                    // 返回股吧页面刷新
                    Constants.isreferforumlist = "0";
                    finish();
                } else {
                    CustomToast.makeText(getApplicationContext(), "转发失败,请重试", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * 绑定摘要输入字数事件
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int number = 140 - s.length();
            if (number >= 0) {
                if (number == 140) {
                    inputNumberWords.setText("你可以输入" + number + "字");
                } else {
                    inputNumberWords.setText("你还可以输入" + number + "字");
                }
                inputNumberWords.setTextColor(inputNumberWords.getResources().getColor(R.color.color_999999));
            } else {
                inputNumberWords.setTextColor(inputNumberWords.getResources().getColor(R.color.red));
                inputNumberWords.setText("已经超出" + Math.abs(number) + "字");
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }
    };

}
