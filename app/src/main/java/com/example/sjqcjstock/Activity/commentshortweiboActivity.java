package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.facelibaryAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 发送短微博的页面
 */
public class commentshortweiboActivity extends Activity implements ViewFactory,
        OnItemSelectedListener {

    // 获取@ 的好友
    private static final int REQUEST_CODE_UNAME = 4;

    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取

    // 图文混排
    SpannableStringBuilder spannable;

    // 获取控件
    private RelativeLayout LinearLayout01;
    private EditText editcommentforweibo1;
    private ImageView facelibrary1;
    private LinearLayout goback1;
    private ImageView getimgtoalbum1;
    private EditText editcommentforweibo2;
    private TextView hideweiboimaid1;
    private LinearLayout addshortweibo1;
    private ImageView atsb2;
    private ImageView getimgtotakephoto1;
    private Button tolongweibo1;

    // 图片idString
    private StringBuilder attach_idBuilder = new StringBuilder();
    // 定义适配器
    private facelibaryAdapter facelibaryAdapter;
    private GridView facegridView1;
    // 定义于数据库同步的字段集合
    private ArrayList<HashMap<String, Object>> listData;
    // 判断弹出表情库的标识 0代表显示，1代表隐藏
    private String isdisplayfacelibrary = "0";

    /**
     * 短微博图片管理
     */
    // 保存Bitmap的列表
    private static List<Bitmap> bitmaplist = new ArrayList<Bitmap>();
    private List<String> attach_idstrlist = new ArrayList<String>();
    // 获取当前选择图片的路径
    private static String photoURL = "";
    // 添加定向删除的控件
    private Button onedelete;
    // 图片控件
    private Bitmap[] list;
    private ImageSwitcher mSwitcher;
    private Gallery g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.addshortweibo);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        bitmaplist.clear();
        attach_idBuilder.append("|");
        // 微博图片管理
        // 添加定向删除的控件
        onedelete = (Button) findViewById(R.id.onedelete);

        // 图片控件

        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher4);

        list = bitmaplist.toArray(new Bitmap[bitmaplist.size()]);

        mSwitcher.setFactory(this);

        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
                commentshortweiboActivity.this, android.R.anim.fade_in));

        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
                commentshortweiboActivity.this, android.R.anim.fade_out));

        mSwitcher.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                CustomToast.makeText(commentshortweiboActivity.this, "position:", Toast.LENGTH_LONG)
                        .show();
            }
        });

        // 加载画框控件
        g = (Gallery) findViewById(R.id.mygallery2);

        g.setAdapter(new ImageAdapter(commentshortweiboActivity.this,
                commentshortweiboActivity.this, bitmaplist));

        g.setOnItemSelectedListener(commentshortweiboActivity.this);

        g.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
                // TODO Auto-generated method stub
                CustomToast.makeText(commentshortweiboActivity.this, "position:", Toast.LENGTH_LONG)
                        .show();

                // CustomToast.makeText(makephotoActivity.this,
                // "dianjiale,"+"listsize:"+saveImapathList.size()+"listview:"+saveImapathList.get(position),
                // Toast.LENGTH_LONG).show();
                // saveposition=position;
                // CustomToast.makeText(commentshortweiboActivity.this,
                // "position:"+position, Toast.LENGTH_LONG).show();
                // 弹出消息提示框

            }

        });

        LinearLayout01 = (RelativeLayout) findViewById(R.id.LinearLayout01);
        editcommentforweibo1 = (EditText) findViewById(R.id.editcommentforweibo1);
        facelibrary1 = (ImageView) findViewById(R.id.facelibrary1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        getimgtoalbum1 = (ImageView) findViewById(R.id.getimgtoalbum1);
        editcommentforweibo2 = (EditText) findViewById(R.id.editcommentforweibo2);
        hideweiboimaid1 = (TextView) findViewById(R.id.hideweiboimaid1);
        addshortweibo1 = (LinearLayout) findViewById(R.id.addshortweibo1);
        atsb2 = (ImageView) findViewById(R.id.atsb2);
        getimgtotakephoto1 = (ImageView) findViewById(R.id.getimgtotakephoto1);
        tolongweibo1 = (Button) findViewById(R.id.tolongweibo1);

        editcommentforweibo1
                .setOnClickListener(new editcommentforweibo1_listener());
        LinearLayout01.setOnClickListener(new LinearLayout01_listener());
        // ImageView03.setOnClickListener(new ImageView03_listener());
        facelibrary1.setOnClickListener(new facelibrary1_listener());
        goback1.setOnClickListener(new goback1_listener());
        getimgtoalbum1.setOnClickListener(new getimgtoalbum1_listener());
        addshortweibo1.setOnClickListener(new addshortweibo1_listener());
        atsb2.setOnClickListener(new atsb2_listener());
        getimgtotakephoto1
                .setOnClickListener(new getimgtotakephoto1_listener());
        tolongweibo1.setOnClickListener(new tolongweibo1_listener());

        spannable = new SpannableStringBuilder(editcommentforweibo1.getText()
                .toString());

        editcommentforweibo1.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (arg1 == 66) { // 如果用户点击了回车键
                    int index = editcommentforweibo1.getSelectionStart();
                    Editable editable = editcommentforweibo1.getText();
                    editable.insert(index, "");
                }
                return false;
            }
        });

        editcommentforweibo1.addTextChangedListener(mTextWatcher);
        /**
         * editcommentforweibo1.setOnFocusChangeListener(new
         * OnFocusChangeListener() {
         *
         * @Override public void onFocusChange(View arg0, boolean arg1) { //
         *           TODO Auto-generated method stub if(arg1){
         *           LinearLayout01.setVisibility(View.VISIBLE); }else{
         *           LinearLayout01.setVisibility(View.GONE); }
         *
         *           } });
         */
        facegridView1 = (GridView) findViewById(R.id.facegridView1);

        listData = new ArrayList<HashMap<String, Object>>();
        facelibaryAdapter = new facelibaryAdapter(
                commentshortweiboActivity.this, listData);
        final int[] imagestr = {R.drawable.aoman, R.drawable.baiyan,
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
                R.drawable.liuhan, R.drawable.liulei, R.drawable.liwu,
                R.drawable.love, R.drawable.ma, R.drawable.meng,
                R.drawable.nanguo, R.drawable.no, R.drawable.ok,
                R.drawable.peifu, R.drawable.pijiu, R.drawable.pingpang,
                R.drawable.pizui, R.drawable.qiang, R.drawable.qinqin,
                R.drawable.qioudale, R.drawable.qiu, R.drawable.quantou,
                R.drawable.ruo, R.drawable.se, R.drawable.shandian,
                R.drawable.shengli, R.drawable.shenma, R.drawable.shuai,
                R.drawable.shuijiao, R.drawable.taiyang, R.drawable.tiao,
                R.drawable.tiaopi, R.drawable.tiaosheng, R.drawable.tiaowu,
                R.drawable.touxiao, R.drawable.tu, R.drawable.tuzi,
                R.drawable.wabi, R.drawable.weiqu, R.drawable.weixiao,
                R.drawable.wen, R.drawable.woshou, R.drawable.xia,
                R.drawable.xianwen, R.drawable.xigua, R.drawable.xinsui,
                R.drawable.xu, R.drawable.yinxian, R.drawable.yongbao,
                R.drawable.youhengheng, R.drawable.youtaiji,
                R.drawable.yueliang, R.drawable.yun, R.drawable.zaijian,
                R.drawable.zhadan, R.drawable.zhemo, R.drawable.zhuakuang,
                R.drawable.zhuanquan, R.drawable.zhutou,
                R.drawable.zuohengheng, R.drawable.zuotaiji, R.drawable.zuqiu,

        };

        final String[] facestrs = {"[aoman]", "[baiyan]", "[bishi]",
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
        for (int i = 0; i < 110; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("friend_image", imagestr[i]);
            map.put("facestr", facestrs[i]);
            // 添加数据
            listData.add(map);
        }

        facegridView1.setAdapter(facelibaryAdapter);

        facegridView1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub

                int index = editcommentforweibo1.getSelectionStart();
                Editable editable = editcommentforweibo1.getText();
                editable.insert(index, facestrs[position]);

            }

        });

    }

    // 删除shortweiboimage图像 供adapter回调
    public void deleteweiboimage(final int position) {
        // TODO Auto-generated method stub
        // 弹出消息提示框

        new AlertDialog.Builder(commentshortweiboActivity.this)
                .setTitle("删除图片")
                .setMessage("确定要要删除图片吗")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        // TODO Auto-generated method stub
                        File imafilepath = new File(photoURL);
                        imafilepath.delete();
                        bitmaplist.remove(bitmaplist.get(position));

                        attach_idstrlist.remove(attach_idstrlist.get(position));
                        // 重新刷新相框的适配器与显示

                        list = bitmaplist.toArray(new Bitmap[bitmaplist.size()]);

                        g.setAdapter(new ImageAdapter(
                                commentshortweiboActivity.this,
                                commentshortweiboActivity.this, bitmaplist));

                        g.setOnItemSelectedListener(commentshortweiboActivity.this);

                        g.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View v, int position, long id) {
                                // TODO Auto-generated method stub
                                // CustomToast.makeText(commentshortweiboActivity.this,
                                // "dianjiale,"+"listsize:"+saveImapathList.size()+"listview:"+saveImapathList.get(position),
                                // Toast.LENGTH_LONG).show();
                                // saveposition=position;
                            }

                        });

                        // 如果图片已删除完毕，mSwitcher则不显示图片

                        if (bitmaplist.size() == 0) {
                            mSwitcher.setImageURI(null);
                        }

                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss(); // 关闭alertDialog
                    }
                }).show();

    }

    class tolongweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if ("".equals(editcommentforweibo1.getText().toString().trim())) {
                Intent intent = new Intent(commentshortweiboActivity.this, commentlongweiboActivity.class);
                startActivity(intent);
                commentshortweiboActivity.this.finish();
                return;
            }
            // 删除微博评论
            new AlertDialog.Builder(commentshortweiboActivity.this)
                    .setMessage("要放弃发布吗！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(commentshortweiboActivity.this, commentlongweiboActivity.class);
                                    startActivity(intent);
                                    commentshortweiboActivity.this.finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    dialog.dismiss(); //关闭alertDialog
                                }
                            }).show();
        }

    }

    class getimgtotakephoto1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 从相机获取图片
            getImageFromCamera();
        }
    }

    class atsb2_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(commentshortweiboActivity.this, atfriendActivity.class);
            startActivityForResult(intent, REQUEST_CODE_UNAME);
        }
    }

    class addshortweibo1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(tolongweibo1.getWindowToken(), 0);
            if (Utils.isFastDoubleClick()) {
                return;
            }
            // 组合attach_idBuilder
            for (String attach_idstr : attach_idstrlist) {
                attach_idBuilder.append(attach_idstr);
                attach_idBuilder.append("|");
            }
            String bodystr1 = editcommentforweibo1.getText() + "";

            if ("".equals(bodystr1.trim())) {
                CustomToast.makeText(getApplicationContext(), "正文不能为空", Toast.LENGTH_SHORT).show();
            } else if (bodystr1.length() > 140) {
                CustomToast.makeText(getApplicationContext(), "短微博过140字,不能发布", Toast.LENGTH_SHORT).show();
            } else {
                new SendInfoTaskaddshortweibo()
                        .execute(new TaskParams(
                                Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
                                new String[]{"mid", Constants.staticmyuidstr},
                                new String[]{"login_password", Constants.staticpasswordstr},
                                new String[]{"tokey", Constants.statictokeystr},
                                new String[]{"app_name", "public"},
                                new String[]{"body", bodystr1.replace("\n", "<br/>")},
                                new String[]{"type", "postimage"},
                                new String[]{"attach_id", attach_idBuilder.toString()}

                        ));
            }
        }
    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
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

                // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        editcommentforweibo1.getWindowToken(), 0);
            } else {

                facegridView1.setVisibility(View.GONE);
                isdisplayfacelibrary = "0";
            }

        }

    }

    // class ImageView03_listener implements OnClickListener{
    //
    // @Override
    // public void onClick(View arg0) {
    // // TODO Auto-generated method stub
    // if("0".equals(isdisplaygetimg)){
    // LinearLayoutgetimg.setVisibility(View.VISIBLE);
    // isdisplaygetimg="1";
    //
    // }else{
    //
    // LinearLayoutgetimg.setVisibility(View.GONE);
    // isdisplaygetimg="0";
    // }
    //
    // }
    //
    // }

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
//			LinearLayout01.setVisibility(View.VISIBLE);
            facegridView1.setVisibility(View.GONE);

        }

    }

    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera,
                    REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            CustomToast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_LONG).show();
        }
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

                        bitmaplist.add(bmpCompressed);

                        sendData(bmpCompressed);
                        // 需要处理的文本，[smile]是需要被替代的文本
                        // spannable = new
                        // SpannableStringBuilder(editcommentforweibo1.getText().toString()+"1");
                        SpannableStringBuilder spanStr = (SpannableStringBuilder) editcommentforweibo2
                                .getText();
                        spanStr.append("1");
                        // editcommentforweibo1.setText(editcommentforweibo1.getText().toString()+"1");
                        // 要让图片替代指定的文字就要用ImageSpan
                        ImageSpan span = new ImageSpan(bmpCompressed,
                                ImageSpan.ALIGN_BASELINE);
                        int length = editcommentforweibo2.getText().length();
                        spanStr.setSpan(span, length - 1, length,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                        editcommentforweibo2.setText(spanStr);

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
            if (data != null) {
                Uri uri = data.getData();
                if (uri == null) {
                    // use bundle to get data
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data"); // get
                        // bitmap
                        // imgportrait.setImageBitmap(photo);

                        Bitmap bmpCompressed = Bitmap.createScaledBitmap(photo,
                                200, 200, true);

                        try {
                            sendData(bmpCompressed);

                            bitmaplist.add(bmpCompressed);

                            // 需要处理的文本，[smile]是需要被替代的文本
                            // spannable = new
                            // SpannableStringBuilder(editcommentforweibo1.getText().toString()+"1");
                            SpannableStringBuilder spanStr = (SpannableStringBuilder) editcommentforweibo2
                                    .getText();
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
                            int length = editcommentforweibo2.getText()
                                    .length();
                            spanStr.setSpan(span, length - 1, length,
                                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                            editcommentforweibo2.setText(spanStr);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        /**
                         * try { String fullpicurlstr=""; String
                         * picheightstr=""; String picurlstr=""; String
                         * picwidthstr="";
                         *
                         *
                         * String result=sendData(photo); /**解析返回的数据再次访问网络保存图片
                         * result=result.replace("\n ","");
                         * result=result.replace("\n","");
                         * result=result.replace(" ",""); result="["+result+"]";
                         * //解析json字符串获得List<Map<String,Object>>
                         * List<Map<String,Object>>
                         * lists=JsonTools.listKeyMaps(result);
                         * for(Map<String,Object> map:lists){ String statusstr=
                         * map.get("status")+""; String datastr=
                         * map.get("data")+""; List<Map<String,Object>>
                         * datastrlists=JsonTools.listKeyMaps("["+datastr+"]");
                         *
                         *
                         * for(Map<String,Object> datastrmap:datastrlists){
                         * fullpicurlstr=
                         * datastrmap.get("fullpicurl")+"";
                         * picheightstr= datastrmap.get("picheight")+"";
                         * picurlstr= datastrmap.get("picurl")+"";
                         * picwidthstr= datastrmap.get("picwidth")+"";
                         *
                         * }
                         *
                         *
                         * }
                         *
                         * new SendInfoTasksaveheadimg().execute(new TaskParams(
                         * Constants.Url+"?app=public&mod=AppFeedList&act=AppSaveAvatar&step=save&sjc="
                         * +"1"+sjc+numcode, new String[] { "mid",
                         * Constants.staticmyuidstr }, new String[] {
                         * "login_password", Constants.staticpasswordstr }, new
                         * String[] { "picurl",picurlstr}, new String[] {
                         * "picwidth",picwidthstr}, new String[] {
                         * "fullpicurl",fullpicurlstr}, new String[] {
                         * "x1","0"}, new String[] { "y1","0"}, new String[] {
                         * "x2","0"}, new String[] { "y2","0"}, new String[] {
                         * "w",picwidthstr}, new String[] { "h",picheightstr}
                         *
                         *
                         *
                         * // picurl //图片路径 // picwidth // 图片宽度 // fullpicurl
                         * //图片绝路径 // x1 // 选择区域左上角x轴坐标 // y1 // 选择区域左上角y轴坐标 //
                         * x2 // 选择区域右下角x轴坐标 // y2 // 选择区域右下角y轴坐标 // w // 选择区的宽度
                         * // h // 选择区的高度
                         *
                         * )
                         *
                         * );
                         *
                         * } catch (Exception e) { // TODO Auto-generated catch
                         * block e.printStackTrace(); }
                         */
                        // spath :生成图片取个名字和路径包含类型
                        String uuid = UUID.randomUUID() + "";
                        String envstr = Environment
                                .getExternalStorageDirectory()
                                + "/sglrBitmap/"
                                + uuid + ".jpg";
                        saveImage(photo, envstr);
                    } else {
                        CustomToast.makeText(getApplicationContext(), "err****",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    // to do find the path of pic by uri
                }

            }
        } else if (resultCode == REQUEST_CODE_UNAME) {
            Bundle bundle = data.getExtras();
            String Name = bundle.getString("unamestr");
            int index = editcommentforweibo1.getSelectionStart();
            Editable editable = editcommentforweibo1.getText();
            editable.insert(index, "@" + Name + " ");

            // editcommentforweibo1.setText("@"+Name+" ");
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
//			facegridView1.setVisibility(View.GONE);
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

    // 将图片上传到服务器
    public String sendData(Bitmap bitmap) throws Exception {
        try {

            HttpClient httpClient = new DefaultHttpClient();

            HttpContext localContext = new BasicHttpContext();

            /**
             * HttpClient httpClient=new DefaultHttpClient();
             *
             * HttpPost httpPost=new
             * HttpPost("http://123.56.163.113:8080/GraceSys/getHomeRmd.do");
             * //HttpGet httpGet=new
             * HttpGet("http://123.56.163.113:8080/GraceSys/getHomeRmd.do");
             *
             * //MultipartEntity entity=new
             * MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
             *
             *
             * try { List<NameValuePair> paramss = new
             * ArrayList<NameValuePair>(); paramss.add(new
             * BasicNameValuePair("longitude", "104.068163")); paramss.add(new
             * BasicNameValuePair("latitude", "30.582042")); paramss.add(new
             * BasicNameValuePair("page", "0"));
             *
             * //entity.addPart("longitude",new StringBody("104.068163"));
             * //entity.addPart("latitude",new StringBody("30.582042"));
             * //entity.addPart("page",new StringBody("0"));
             * httpPost.setEntity(new UrlEncodedFormEntity(paramss,
             * HTTP.UTF_8));
             *
             * //httpPost.setEntity(entity);
             *
             * //HttpResponse
             * response=httpClient.execute(httpPost,localContext); HttpResponse
             * response=httpClient.execute(httpPost); int
             * code=response.getStatusLine().getStatusCode(); //if
             * (response.getStatusLine().getStatusCode() == 200) { String result
             * = EntityUtils.toString(response.getEntity());
             * System.out.println("result:" + result);
             *
             *
             * */

            long sjc = System.currentTimeMillis();
            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
            HttpPost httpPost = new HttpPost(
                    Constants.Url + "?app=public&mod=AppFeedList&act=AppUpload&sjc="
                            + "1" + sjc + numcode);

            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("login_password", new StringBody(
                    Constants.staticpasswordstr));
            entity.addPart("tokey", new StringBody(Constants.statictokeystr));

            entity.addPart("attach_type", new StringBody("feed_image"));
            entity.addPart("thumb", new StringBody("1"));
            entity.addPart("upload_type", new StringBody("image"));

            // Bitmap bmpCompressed=Bitmap.createScaledBitmap(bitmap, 640, 480,
            // true);
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 500, 500,
                    true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // bmpCompressed.compress(CompressFormat.JPEG,100, bos);
            bmpCompressed.compress(CompressFormat.PNG, 100, bos);
            // imgportrait.setImageBitmap(bmpCompressed);
            byte[] data = bos.toByteArray();

            // sending a String param;

            // entity.addPart("myParam",new StringBody("my value"));

            // sending a Image;

            // entity.addPart("mylmage",new ByteArrayBody(data,"temp.jpg"));
            entity.addPart("mylmage", new ByteArrayBody(data, "temp.png"));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            int code = response.getStatusLine().getStatusCode();

            // BufferedReader reader=
            // new BufferedReader(new
            // InputStreamReader(response.getEntity().getContent(),"utf-8"));
            InputStream in = response.getEntity().getContent();
            // String sResponse=reader.readLine();
            String resstr = HttpUtil.changeInputStream(in);
            // saveheadimg1.setVisibility(View.VISIBLE);

            resstr = resstr.replace("\n ", "");
            resstr = resstr.replace("\n", "");
            resstr = resstr.replace(" ", "");
            resstr = "[" + resstr + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(resstr);

            for (Map<String, Object> map : lists) {
                String datastr = map.get("data") + "";
                List<Map<String, Object>> resstrlists = JsonTools
                        .listKeyMaps(resstr);
                for (Map<String, Object> resstrmap : resstrlists) {
                    String data2str = resstrmap.get("data") + "";
                    List<Map<String, Object>> data2strlists = JsonTools
                            .listKeyMaps("[" + data2str + "]");
                    for (Map<String, Object> data2strmap : data2strlists) {
                        String uidstr = data2strmap.get("uid") + "";
                        String attach_idstr = data2strmap.get("attach_id")
                                + "";
                        // String datastr= map.get("data")+"";
                        // String datastr= map.get("data")+"";

                        // 将attach_id保存至链表
                        attach_idstrlist.add(attach_idstr);

                        // attach_idBuilder.append(attach_idstr);
                        // attach_idBuilder.append("|");
                    }
                }

            }
            return resstr;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

    private class SendInfoTaskaddshortweibo extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";

                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "发短文成功", Toast.LENGTH_LONG).show();
                    // 返回给股吧页面标识
                    Constants.intentFlag = "1";
                    Constants.isreferforumlist = "0";
                    finish();
                } else {
                    CustomToast.makeText(getApplicationContext(), "发短文失败,请重试", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

    }

    public static void saveImage(Bitmap photo, String spath) {
        try {
            File fImage = null;
            FileOutputStream iStream = null;

            // BufferedOutputStream bos = new BufferedOutputStream(
            // new FileOutputStream(spath, false));
            // photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            fImage = new File(Environment.getExternalStorageDirectory()
                    + "/sglrBitmap/");
            if (!fImage.exists()) {
                fImage.mkdir();
            }

            fImage = new File(spath);
            iStream = new FileOutputStream(fImage);

            photo.compress(Bitmap.CompressFormat.JPEG, 75, iStream);// (0-100)压缩文件
            // bos.flush();
            // bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        // return true;
    }

    // 设置mSwitcher中的图片
    @Override
    public View makeView() {
        // TODO Auto-generated method stub
        ImageView i = new ImageView(this);
        // i.setBackgroundColor(0xFFA9A9A9);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(
                LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        return i;
    }

    // 定义适配器类

    public class ImageAdapter extends BaseAdapter {

        int mGalleryItemBackground;
        private Context mContext;
        private List<String> lis;
        private List<Bitmap> bmplist;
        private commentshortweiboActivity commentshortweiboActivity2;

        // public ImageAdapter(Context c,List<String> li){
        // mContext =c;
        // lis=li;
        //
        // TypedArray a=obtainStyledAttributes(R.styleable.Gallery);
        //
        // mGalleryItemBackground=a.getResourceId(R.styleable.Gallery_android_galleryItemBackground,
        // 0);
        //
        // a.recycle();
        //
        // }

        public ImageAdapter(Context c, List<Bitmap> bmpintolist) {
            mContext = c;
            bmplist = bmpintolist;

            TypedArray a = obtainStyledAttributes(R.styleable.Gallery);

            mGalleryItemBackground = a.getResourceId(
                    R.styleable.Gallery_android_galleryItemBackground, 0);

            a.recycle();

        }

        public ImageAdapter(Context c,
                            commentshortweiboActivity commentshortweiboActivity,
                            List<Bitmap> bmpintolist) {
            mContext = c;
            bmplist = bmpintolist;
            commentshortweiboActivity2 = commentshortweiboActivity;
            TypedArray a = obtainStyledAttributes(R.styleable.Gallery);

            mGalleryItemBackground = a.getResourceId(
                    R.styleable.Gallery_android_galleryItemBackground, 0);

            a.recycle();

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return bmplist.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View converView,
                            ViewGroup parent) {
            // TODO Auto-generated method stub

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            converView = mInflater.inflate(R.layout.list_item_shortweiboimage,
                    null);

            // ImageView i=new ImageView(mContext);
            ImageView i = (ImageView) converView
                    .findViewById(R.id.shortweiboimage1);
            ImageView deleteimg1 = (ImageView) converView
                    .findViewById(R.id.deleteimg1);

            deleteimg1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    CustomToast.makeText(mContext, "delete1", Toast.LENGTH_LONG).show();

                    commentshortweiboActivity2.deleteweiboimage(position);
                }
            });

            // Bitmap bm=BitmapFactory.decodeFile(lis.get(position).toString());

            i.setImageBitmap(bmplist.get(position));

            i.setScaleType(ImageView.ScaleType.FIT_XY);

            // i.setLayoutParams(new Gallery.LayoutParams(136, 88));
            i.setLayoutParams(new RelativeLayout.LayoutParams(136, 88));

            i.setBackgroundResource(mGalleryItemBackground);

            converView.setLayoutParams(new Gallery.LayoutParams(170, 120));

            return converView;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               final int position, long id) {
        // TODO Auto-generated method stub

        Bitmap bmp = list[position];
        // Log.i("A", String.valueOf(position));
        Drawable drawable = new BitmapDrawable(bmp);
        // 现实当前选中的图片选项
        mSwitcher.setImageURI(Uri.parse(photoURL));
        mSwitcher.setImageDrawable(drawable);
        // 添加定向删除功能
        onedelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // 弹出消息提示框

                new AlertDialog.Builder(commentshortweiboActivity.this)
                        .setTitle("删除图片")
                        .setMessage("确定要要删除图片吗")
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        // TODO Auto-generated method stub
                                        File imafilepath = new File(photoURL);
                                        imafilepath.delete();
                                        bitmaplist.remove(bitmaplist
                                                .get(position));

                                        attach_idstrlist
                                                .remove(attach_idstrlist
                                                        .get(position));
                                        // 重新刷新相框的适配器与显示

                                        list = bitmaplist
                                                .toArray(new Bitmap[bitmaplist
                                                        .size()]);

                                        g.setAdapter(new ImageAdapter(
                                                commentshortweiboActivity.this,
                                                commentshortweiboActivity.this,
                                                bitmaplist));

                                        g.setOnItemSelectedListener(commentshortweiboActivity.this);

                                        g.setOnItemClickListener(new OnItemClickListener() {

                                            @Override
                                            public void onItemClick(
                                                    AdapterView<?> parent,
                                                    View v, int position,
                                                    long id) {
                                                // TODO Auto-generated method
                                                // stub
                                                // CustomToast.makeText(commentshortweiboActivity.this,
                                                // "dianjiale,"+"listsize:"+saveImapathList.size()+"listview:"+saveImapathList.get(position),
                                                // Toast.LENGTH_LONG).show();
                                                // saveposition=position;
                                            }

                                        });

                                        // 如果图片已删除完毕，mSwitcher则不显示图片

                                        if (bitmaplist.size() == 0) {
                                            mSwitcher.setImageURI(null);
                                        }

                                    }
                                })
                        .setNegativeButton("否",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dialog.dismiss(); // 关闭alertDialog
                                    }
                                }).show();

            }

        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        // 重新刷新相框的适配器与显示
        list = bitmaplist.toArray(new Bitmap[bitmaplist.size()]);

        g.setAdapter(new ImageAdapter(commentshortweiboActivity.this,
                commentshortweiboActivity.this, bitmaplist));

        g.setOnItemSelectedListener(commentshortweiboActivity.this);

        g.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // CustomToast.makeText(commentshortweiboActivity.this,
                // "dianjiale,"+"listsize:"+saveImapathList.size()+"listview:"+saveImapathList.get(position),
                // Toast.LENGTH_LONG).show();
                // saveposition=position;
            }

        });

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        // 重新刷新相框的适配器与显示
        list = bitmaplist.toArray(new Bitmap[bitmaplist.size()]);

        g.setAdapter(new ImageAdapter(commentshortweiboActivity.this,
                commentshortweiboActivity.this, bitmaplist));

        g.setOnItemSelectedListener(commentshortweiboActivity.this);

        g.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // CustomToast.makeText(commentshortweiboActivity.this,
                // "dianjiale,"+"listsize:"+saveImapathList.size()+"listview:"+saveImapathList.get(position),
                // Toast.LENGTH_LONG).show();
                // saveposition=position;
            }

        });

    }

    // Edittext的监听
    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;
        private int editStart;
        private int editEnd;

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            temp = s;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
            // mTextView.setText(s);//将输入的内容实时显示
        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            // editStart = editcommentforweibo1.getSelectionStart();
            // editEnd = editcommentforweibo1.getSelectionEnd();
            // editcommentforweibo1.setText("您输入了" + temp.length() + "个字符");
            if (temp.length() > 139) {

                int outlenght = editcommentforweibo1.getText().toString()
                        .length() - 140;
                CustomToast.makeText(commentshortweiboActivity.this,
                        "你输入的字数已经超过了140字限制！" + "已超出" + outlenght + "字",
                        Toast.LENGTH_SHORT).show();
                // s.delete(editStart-1, editEnd);
                // int tempSelection = editStart;
                // editcommentforweibo1.setText(s);
                // editcommentforweibo1.setSelection(tempSelection);
            }
        }
    };

}
