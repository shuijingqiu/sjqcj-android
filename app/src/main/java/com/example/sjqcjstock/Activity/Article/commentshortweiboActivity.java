package com.example.sjqcjstock.Activity.Article;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import com.example.sjqcjstock.Activity.atfriendActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.facelibaryAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * 发送短微博的页面
 * mh 20170704 修改发布接口
 */
public class commentshortweiboActivity extends Activity implements ViewFactory,
        OnItemSelectedListener {

    // 获取@ 的好友
    private static final int REQUEST_CODE_UNAME = 4;
    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取
    // 图文混排
    private SpannableStringBuilder spannable;
    // 获取控件
    private RelativeLayout LinearLayout01;
    private EditText editcommentforweibo1;
    private ImageView facelibrary1;
    private LinearLayout goback1;
    private ImageView getimgtoalbum1;
    private EditText editcommentforweibo2;
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
    // post 短微博  postimage 图片微博
    private String type = "post";

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
    private LinearLayout imgLine;
    // 还能输入多少字
    private TextView inputNumberWords;
    // 接口返回数据
    private String feedStr;
    // 这个是集合了讨论区的发文 如果是15 就为讨论区的发文 20170724
    private String channelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 可以在主线程中使用http网络访问
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.addshortweibo);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        channelId = getIntent().getStringExtra("channelId");
        bitmaplist.clear();
        attach_idBuilder.append("|");
        inputNumberWords = (TextView) findViewById(R.id.input_number_words);
        // 微博图片管理
        // 添加定向删除的控件
        onedelete = (Button) findViewById(R.id.onedelete);
        imgLine = (LinearLayout) findViewById(R.id.img_line);
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
                CustomToast.makeText(commentshortweiboActivity.this, "position:", Toast.LENGTH_SHORT)
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
                CustomToast.makeText(commentshortweiboActivity.this, "position:", Toast.LENGTH_SHORT)
                        .show();
            }

        });
        LinearLayout01 = (RelativeLayout) findViewById(R.id.LinearLayout01);
        editcommentforweibo1 = (EditText) findViewById(R.id.editcommentforweibo1);
        // 绑定摘要输入状态的变化
        editcommentforweibo1.addTextChangedListener(watcher);
        facelibrary1 = (ImageView) findViewById(R.id.facelibrary1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        getimgtoalbum1 = (ImageView) findViewById(R.id.getimgtoalbum1);
        editcommentforweibo2 = (EditText) findViewById(R.id.editcommentforweibo2);
        addshortweibo1 = (LinearLayout) findViewById(R.id.addshortweibo1);
        atsb2 = (ImageView) findViewById(R.id.atsb2);
        getimgtotakephoto1 = (ImageView) findViewById(R.id.getimgtotakephoto1);
        tolongweibo1 = (Button) findViewById(R.id.tolongweibo1);

        editcommentforweibo1
                .setOnClickListener(new editcommentforweibo1_listener());
        LinearLayout01.setOnClickListener(new LinearLayout01_listener());
        // ImageView03.setOnClickListener(new ImageView03_listener());
        facelibrary1.setOnClickListener(new facelibrary1_listener());
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getimgtoalbum1.setOnClickListener(new getimgtoalbum1_listener());
        addshortweibo1.setOnClickListener(new addshortweibo1_listener());
        atsb2.setOnClickListener(new atsb2_listener());
        getimgtotakephoto1
                .setOnClickListener(new getimgtotakephoto1_listener());
        tolongweibo1.setOnClickListener(new tolongweibo1_listener());

        spannable = new SpannableStringBuilder(editcommentforweibo1.getText()
                .toString());

//        editcommentforweibo1.setOnKeyListener(new OnKeyListener() {
//
//            @Override
//            public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
//                if (arg1 == 66) { // 如果用户点击了回车键
//                    int index = editcommentforweibo1.getSelectionStart();
//                    Editable editable = editcommentforweibo1.getText();
//                    editable.insert(index, "");
//                }
//                return false;
//            }
//        });
//
//        editcommentforweibo1.addTextChangedListener(mTextWatcher);
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

                            }

                        });

                        // 如果图片已删除完毕，mSwitcher则不显示图片

                        if (bitmaplist.size() == 0) {
                            mSwitcher.setImageURI(null);
                            imgLine.setVisibility(View.GONE);
                        }

                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // 关闭alertDialog
                    }
                }).show();

    }

    class tolongweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if ("".equals(editcommentforweibo1.getText().toString().trim())) {
                Intent intent = new Intent(commentshortweiboActivity.this, commentlongweiboActivity.class);
                intent.putExtra("channelId",channelId);
                startActivity(intent);
                commentshortweiboActivity.this.finish();
                return;
            }
            new AlertDialog.Builder(commentshortweiboActivity.this)
                    .setMessage("要放弃发布吗！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(commentshortweiboActivity.this, commentlongweiboActivity.class);
                                    intent.putExtra("channelId",channelId);
                                    startActivity(intent);
                                    commentshortweiboActivity.this.finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss(); //关闭alertDialog
                                }
                            }).show();
        }

    }

    class getimgtotakephoto1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
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
            final String bodystr1 = editcommentforweibo1.getText() + "";

            if ("".equals(bodystr1.trim())) {
                CustomToast.makeText(getApplicationContext(), "正文不能为空", Toast.LENGTH_SHORT).show();
            } else if (bodystr1.length() > 140) {
                CustomToast.makeText(getApplicationContext(), "短微博过140字,不能发布", Toast.LENGTH_SHORT).show();
            } else {
//                new SendInfoTaskaddshortweibo()
//                        .execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
//                                new String[]{"mid", Constants.staticmyuidstr},
//                                new String[]{"login_password", Constants.staticpasswordstr},
//                                new String[]{"tokey", Constants.statictokeystr},
//                                new String[]{"app_name", "public"},
//                                new String[]{"body", bodystr1.replace("\n", "<br/>")},
//                                new String[]{"type", "postimage"},
//                                new String[]{"attach_id", attach_idBuilder.toString()}
//                        ));
                if(attach_idstrlist!=null && attach_idstrlist.size()>0){
                    type = "postimage";
                }else{
                    type = "post";
                }
                // 发布微博
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("app_name", "public"));
                        // 内容
//                        dataList.add(new BasicNameValuePair("body", bodystr1.replace("\n", "<br/>")));
                        dataList.add(new BasicNameValuePair("body", bodystr1));
                        // post短微博 long_post长微博 paid_post付费微博 repost转发微博 postimage图片微博
                        dataList.add(new BasicNameValuePair("type", type));
                        // 附件信息
                        dataList.add(new BasicNameValuePair("attach_id", attach_idBuilder.toString()));
                        if(channelId != null && "15".equals(channelId)){
                            // 讨论区的发文
                            dataList.add(new BasicNameValuePair("channel_id", channelId));
                        }
                        feedStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/feed/put", dataList);
                        handler.sendEmptyMessage(0);
                    }
                }).start();

            }
        }
    }

    class getimgtoalbum1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
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

    class editcommentforweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            LinearLayout01.setVisibility(View.VISIBLE);
            facegridView1.setVisibility(View.GONE);
        }

    }

    class LinearLayout01_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
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
            // 调用相机前先删除图片
            ImageUtil.scanFileAsync(this, ImageUtil.getSDPath());
            Intent getImageByCamera = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ImageUtil.getSDPath())));
            startActivityForResult(getImageByCamera,
                    REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            CustomToast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_SHORT).show();
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

                        double proportion = ImageUtil.getProportion(bmp.getWidth(), bmp.getHeight());
                        Bitmap bmpCompressed = Bitmap.createScaledBitmap(bmp,
                                (int) (bmp.getWidth() * proportion), (int) (bmp.getHeight() * proportion), true);

                        bitmaplist.add(bmpCompressed);
                        imgLine.setVisibility(View.VISIBLE);
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
            try {
                final ContentResolver resolver = getContentResolver();
                File file = new File(ImageUtil.getSDPath());
                if (file.exists()) {
                    Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, Uri.fromFile(file));
                    double proportion = ImageUtil.getProportion(photo.getWidth(), photo.getHeight());
                    Bitmap bmpCompressed = Bitmap.createScaledBitmap(photo,
                            (int) (photo.getWidth() * proportion), (int) (photo.getHeight() * proportion), true);
                    try {
                        sendData(bmpCompressed);
                        bitmaplist.add(bmpCompressed);
                        imgLine.setVisibility(View.VISIBLE);
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
                    }
                    // spath :生成图片取个名字和路径包含类型
                    String uuid = UUID.randomUUID() + "";
                    String envstr = Environment
                            .getExternalStorageDirectory()
                            + "/sglrBitmap/"
                            + uuid + ".jpg";
                    saveImage(photo, envstr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == REQUEST_CODE_UNAME) {
            Bundle bundle = data.getExtras();
            String Name = bundle.getString("unamestr");
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
    public void sendData(Bitmap bitmap) throws Exception {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
//            long sjc = System.currentTimeMillis();
//            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
//            HttpPost httpPost = new HttpPost(
//                    Constants.Url + "?app=public&mod=AppFeedList&act=AppUpload&sjc="
//                            + "1" + sjc + numcode);
            HttpPost httpPost = new HttpPost(
                    Constants.newUrl + "/api/upload/post");

            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("token", new StringBody(Constants.apptoken));
//            entity.addPart("attach_type", new StringBody("feed_image"));
//            entity.addPart("thumb", new StringBody("1"));
            entity.addPart("upload_type", new StringBody("image"));
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                    true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmpCompressed.compress(CompressFormat.PNG, 100, bos);
            byte[] data = bos.toByteArray();
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
            JSONObject jsonObject = new JSONObject(resstr);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                // 请求失败的情况
                CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject dataObject = new JSONObject(jsonObject.getString("data"));
            attach_idstrlist.add(dataObject.getString("attach_id"));
            return;

//            resstr = resstr.replace("\n ", "");
//            resstr = resstr.replace("\n", "");
//            resstr = resstr.replace(" ", "");
//            resstr = "[" + resstr + "]";
//            // 解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(resstr);
//
//            for (Map<String, Object> map : lists) {
//                String datastr = map.get("data") + "";
//                List<Map<String, Object>> resstrlists = JsonTools
//                        .listKeyMaps(resstr);
//                for (Map<String, Object> resstrmap : resstrlists) {
//                    String data2str = resstrmap.get("data") + "";
//                    List<Map<String, Object>> data2strlists = JsonTools
//                            .listKeyMaps("[" + data2str + "]");
//                    for (Map<String, Object> data2strmap : data2strlists) {
//                        String uidstr = data2strmap.get("uid") + "";
//                        String attach_idstr = data2strmap.get("attach_id")
//                                + "";
//                        // String datastr= map.get("data")+"";
//                        // String datastr= map.get("data")+"";
//
//                        // 将attach_id保存至链表
//                        attach_idstrlist.add(attach_idstr);
//
//                        // attach_idBuilder.append(attach_idstr);
//                        // attach_idBuilder.append("|");
//                    }
//                }
//
//            }
//            return resstr;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
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

            photo.compress(Bitmap.CompressFormat.JPEG, 100, iStream);// (0-100)压缩文件
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
            return bmplist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View converView,
                            ViewGroup parent) {

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
                                                // Toast.LENGTH_SHORT).show();
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
                // Toast.LENGTH_SHORT).show();
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
                // Toast.LENGTH_SHORT).show();
                // saveposition=position;
            }

        });

    }

//    // Edittext的监听
//    TextWatcher mTextWatcher = new TextWatcher() {
//        private CharSequence temp;
//        private int editStart;
//        private int editEnd;
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before,
//                                  int count) {
//            // TODO Auto-generated method stub
//            temp = s;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count,
//                                      int after) {
//            // TODO Auto-generated method stub
//            // mTextView.setText(s);//将输入的内容实时显示
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            // TODO Auto-generated method stub
//            // editStart = editcommentforweibo1.getSelectionStart();
//            // editEnd = editcommentforweibo1.getSelectionEnd();
//            // editcommentforweibo1.setText("您输入了" + temp.length() + "个字符");
//            if (temp.length() > 139) {
//
//                int outlenght = editcommentforweibo1.getText().toString()
//                        .length() - 140;
//                CustomToast.makeText(commentshortweiboActivity.this,
//                        "你输入的字数已经超过了140字限制！" + "已超出" + outlenght + "字",
//                        Toast.LENGTH_SHORT).show();
//                // s.delete(editStart-1, editEnd);
//                // int tempSelection = editStart;
//                // editcommentforweibo1.setText(s);
//                // editcommentforweibo1.setSelection(tempSelection);
//            }
//        }
//    };


//    private class SendInfoTaskaddshortweibo extends
//            AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "发短文失败,请重试", Toast.LENGTH_SHORT)
//                        .show();
//                return;
//            }
//            result = result.replace("\n ", "");
//            result = result.replace("\n", "");
//            result = result.replace(" ", "");
//            result = "[" + result + "]";
//            // 解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//            for (Map<String, Object> map : lists) {
//                String statusstr = map.get("status") + "";
//
//                if ("1".equals(statusstr)) {
//                    CustomToast.makeText(getApplicationContext(), "发短文成功", Toast.LENGTH_SHORT).show();
//                    // 返回给股吧页面标识
//                    Constants.intentFlag = "1";
//                    Constants.isreferforumlist = "0";
//                    finish();
//                } else {
//                    CustomToast.makeText(getApplicationContext(), "发短文失败,请重试", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        }
//    }

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
                        JSONObject jsonObject = new JSONObject(feedStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code)) {
                            // 返回给股吧页面标识
                            Constants.isreferforumlist = "0";
                            // 发布成功关闭页面
                            finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
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
