package com.example.sjqcjstock.Activity.discussarea;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.atfriendActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.facelibaryAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.richeditor.RichTextEditor;
import com.example.sjqcjstock.richeditor.RichTextEditor.EditData;
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

public class commentlongdiscussareaweiboActivity extends Activity {

    //获取@ 的好友
    private static final int REQUEST_CODE_UNAME = 4;

    //照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; //相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; //照相获取


    //图文混排
    SpannableStringBuilder spannable;

    //长微博body字符串组合
    private SpannableStringBuilder titlebuilder;
    private StringBuilder longweibobody;

    private SpannableStringBuilder hidelongweibobody;


    //获取控件
    private RelativeLayout LinearLayout01;
    private EditText editcommentforweibo1;
    //private ImageView ImageView03;
    private LinearLayout LinearLayoutgetimg;
    private ImageView facelibrary1;
    private LinearLayout goback1;
    private ImageView getimgtoalbum1;
    private LinearLayout addshortweibo1;
    private EditText editcommentforweibohide1;
    private LinearLayout addlongweibo1;
    private EditText editcomtitleforweibo1;
    private RichTextEditor editor;
    private ImageView getimgtotakephoto1;

    private ImageView atsb2;
    private Button toshortweibo1;

    //定义适配器
    facelibaryAdapter facelibaryAdapter;
    private GridView facegridView1;
    //定义于数据库同步的字段集合
    //private String[] name;
    ArrayList<HashMap<String, Object>> listData;

    //判断弹出获取图片操作的标识  0代表显示，1代表隐藏
    private String isdisplaygetimg = "0";

    //判断弹出表情库的标识  0代表显示，1代表隐藏
    private String isdisplayfacelibrary = "0";

    //用于RichTextEditor回调的方法

    public void getRichTextEditorforced() {
        facegridView1.setVisibility(View.GONE);

    }

    //用于RichTextEditor回调 ScrollView到达顶部

    public void editortotop() {
        //editor.fullScroll(33);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.addlongweibo);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {

        editor = (RichTextEditor) findViewById(R.id.richEditor);

        //往RichTextEditor里传入当前Activity的参数
//			editor.senddiscussareaweActivity(commentlongdiscussareaweiboActivity.this);

        longweibobody = new StringBuilder();


        LinearLayout01 = (RelativeLayout) findViewById(R.id.LinearLayout01);
        //editcommentforweibo1=(EditText)findViewById(R.id.editcommentforweibo1);
        //ImageView03=(ImageView)findViewById(R.id.ImageView03);
        LinearLayoutgetimg = (LinearLayout) findViewById(R.id.LinearLayoutgetimg);
        facelibrary1 = (ImageView) findViewById(R.id.facelibrary1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        getimgtoalbum1 = (ImageView) findViewById(R.id.getimgtoalbum1);
        addlongweibo1 = (LinearLayout) findViewById(R.id.addlongweibo1);
        //editcommentforweibohide1=(EditText)findViewById(R.id.editcommentforweibohide1);
        editcomtitleforweibo1 = (EditText) findViewById(R.id.editcomtitleforweibo1);
        getimgtotakephoto1 = (ImageView) findViewById(R.id.getimgtotakephoto1);
        atsb2 = (ImageView) findViewById(R.id.atsb2);
        toshortweibo1 = (Button) findViewById(R.id.toshortweibo1);


        editor.setOnClickListener(new editor_listener());


        //editcommentforweibo1.setOnClickListener(new editcommentforweibo1_listener());
        LinearLayout01.setOnClickListener(new LinearLayout01_listener());
        //ImageView03.setOnClickListener(new ImageView03_listener());
        facelibrary1.setOnClickListener(new facelibrary1_listener());
        goback1.setOnClickListener(new goback1_listener());
        getimgtoalbum1.setOnClickListener(new getimgtoalbum1_listener());
        addlongweibo1.setOnClickListener(new addlongweibo1_listener());
        getimgtotakephoto1.setOnClickListener(new getimgtotakephoto1_listener());
        atsb2.setOnClickListener(new atsb2_listener());
        toshortweibo1.setOnClickListener(new toshortweibo1_listener());
        editcomtitleforweibo1.setOnClickListener(new editcomtitleforweibo1_listener());
        //spannable = new SpannableStringBuilder(editcommentforweibo1.getText().toString());


        /**
         editcommentforweibo1.setOnFocusChangeListener(new OnFocusChangeListener() {

        @Override public void onFocusChange(View arg0, boolean arg1) {
        // TODO Auto-generated method stub
        if(arg1){
        LinearLayout01.setVisibility(View.VISIBLE);
        }else{
        LinearLayout01.setVisibility(View.GONE);
        }

        }
        });
         */
        facegridView1 = (GridView) findViewById(R.id.facegridView1);


        listData = new ArrayList<HashMap<String, Object>>();
        facelibaryAdapter = new facelibaryAdapter(commentlongdiscussareaweiboActivity.this, listData);
        final int[] imagestr = {R.drawable.aoman, R.drawable.baiyan, R.drawable.bishi,
                R.drawable.bizui, R.drawable.cahan, R.drawable.caidao, R.drawable.chajin,
                R.drawable.cheer, R.drawable.chong, R.drawable.ciya, R.drawable.da, R.drawable.dabian,
                R.drawable.dabing, R.drawable.dajiao, R.drawable.daku, R.drawable.dangao, R.drawable.danu,
                R.drawable.dao, R.drawable.deyi, R.drawable.diaoxie, R.drawable.e, R.drawable.fadai,
                R.drawable.fadou, R.drawable.fan, R.drawable.fanu, R.drawable.feiwen, R.drawable.fendou,
                R.drawable.gangga, R.drawable.geili, R.drawable.gouyin, R.drawable.guzhang, R.drawable.haha,
                R.drawable.haixiu, R.drawable.haqian, R.drawable.hua, R.drawable.huaixiao, R.drawable.hufen,
                R.drawable.huishou, R.drawable.huitou, R.drawable.jidong, R.drawable.jingkong, R.drawable.jingya,
                R.drawable.kafei, R.drawable.keai, R.drawable.kelian, R.drawable.ketou, R.drawable.kiss,
                R.drawable.ku, R.drawable.kuaikule, R.drawable.kulou, R.drawable.kun, R.drawable.lanqiu,
                R.drawable.lenghan, R.drawable.liuhan, R.drawable.liulei, R.drawable.liwu,
                R.drawable.love, R.drawable.ma, R.drawable.meng, R.drawable.nanguo, R.drawable.no,
                R.drawable.ok, R.drawable.peifu, R.drawable.pijiu, R.drawable.pingpang, R.drawable.pizui,
                R.drawable.qiang, R.drawable.qinqin, R.drawable.qioudale, R.drawable.qiu, R.drawable.quantou,
                R.drawable.ruo, R.drawable.se, R.drawable.shandian, R.drawable.shengli, R.drawable.shenma,
                R.drawable.shuai, R.drawable.shuijiao, R.drawable.taiyang, R.drawable.tiao, R.drawable.tiaopi,
                R.drawable.tiaosheng, R.drawable.tiaowu, R.drawable.touxiao, R.drawable.tu, R.drawable.tuzi,
                R.drawable.wabi, R.drawable.weiqu, R.drawable.weixiao, R.drawable.wen, R.drawable.woshou,
                R.drawable.xia, R.drawable.xianwen, R.drawable.xigua, R.drawable.xinsui, R.drawable.xu,
                R.drawable.yinxian, R.drawable.yongbao, R.drawable.youhengheng, R.drawable.youtaiji, R.drawable.yueliang,
                R.drawable.yun, R.drawable.zaijian, R.drawable.zhadan, R.drawable.zhemo, R.drawable.zhuakuang,
                R.drawable.zhuanquan, R.drawable.zhutou, R.drawable.zuohengheng, R.drawable.zuotaiji, R.drawable.zuqiu,

        };


        final String[] facestrs = {"[aoman]", "[baiyan]", "[bishi]",
                "[bizui]", "[cahan]", "[caidao]", "[chajin]",
                "[cheer]", "[chong]", "[ciya]", "[da]", "[dabian]",
                "[dabing]", "[dajiao]", "[daku]", "[dangao]", "[danu]",
                "[dao]", "[deyi]", "[diaoxie]", "[e]", "[fadai]",
                "[fadou]", "[fan]", "[fanu]", "[feiwen]", "[fendou]",
                "[gangga]", "[geili]", "[gouyin]", "[guzhang]", "[haha]",
                "[haixiu]", "[haqian]", "[hua]", "[huaixiao]", "[hufen]",
                "[huishou]", "[huitou]", "[jidong]", "[jingkong]", "[jingya]",
                "[kafei]", "[keai]", "[kelian]", "[ketou]", "[kiss]",
                "[ku]", "[kuaikule]", "[kulou]", "[kun]", "[lanqiu]",
                "[lenghan]", "[liuhan]", "[liulei]", "[liwu]",
                "[love]", "[ma]", "[meng]", "[nanguo]", "[no]",
                "[ok]", "[peifu]", "[pijiu]", "[pingpang]", "[pizui]",
                "[qiang]", "[qinqin]", "[qioudale]", "[qiu]", "[quantou]",
                "[ruo]", "[se]", "[shandian]", "[shengli]", "[shenma]",
                "[shuai]", "[shuijiao]", "[taiyang]", "[tiao]", "[tiaopi]",
                "[tiaosheng]", "[tiaowu]", "[touxiao]", "[tu]", "[tuzi]",
                "[wabi]", "[weiqu]", "[weixiao]", "[wen]", "[woshou]",
                "[xia]", "[xianwen]", "[xigua]", "[xinsui]", "[xu]",
                "[yinxian]", "[yongbao]", "[youhengheng]", "[youtaiji]", "[yueliang]",
                "[yun]", "[zaijian]", "[zhadan]", "[zhemo]", "[zhuakuang]",
                "[zhuanquan]", "[zhutou]", "[zuohengheng]", "[zuotaiji]", "[zuqiu]",

        };

//	    	int[] arrowimagestr={R.drawable.twodimension1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,
//	    			R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1
//	    			,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,
//	    			R.mipmap.gracearrow1,};
        for (int i = 0; i < 110; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("friend_image", imagestr[i]);

            map.put("facestr", facestrs[i]);

            //map.put("friend_username", name[i]);
            //map.put("arrow_image", arrowimagestr[i]);
            // map.put("friend_id",id[i]);

            //添加数据
            listData.add(map);
        }


        facegridView1.setAdapter(facelibaryAdapter);


        facegridView1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {


                // 下面的代码可以上传、或者保存，请自行实现
                int editerlen = editor.editerviewscounts();

                //editor.addEditTextAtIndex(editerlen,"@"+Name+" ");

                //editor.createEditText("@"+Name+" ",1);
                editor.editeratinfoforlastview(facestrs[position]);

//	                int index = editcommentforweibo1.getSelectionStart();  
//	                Editable editable = editcommentforweibo1.getText();  
//	                editable.insert(index, "@"+Name+" "); 


                // TODO Auto-generated method stub
//						               ImageGetter imageGetter = new ImageGetter() {
//						               public Drawable getDrawable(String source) {
//						               int id = Integer.parseInt(source);
//						                      Drawable d = getResources().getDrawable(id);
//						                      d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//						                      return d;
//						                     }
//						                  };
//						           
//						                 CharSequence cs = Html.fromHtml("<img src='"+ imagestr[position]+ "'/>",imageGetter, null);
//						                 editcommentforweibo1.getText().append(cs);
//						              String   faceContent =FilterHtml(Html.toHtml(editcommentforweibo1.getText()));
//						              
//

            }

        });

    }

    public static String FilterHtml(String str) {
        str = str.replaceAll("<(?!br|img)[^>]+>", "").trim();
        return str;
    }


    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<EditData> editList) {
        for (EditData itemData : editList) {
            if (itemData.inputStr != null) {
                String str = itemData.imagePath;
                String str2 = itemData.inputStr;

                longweibobody.append("<p>");
                longweibobody.append(str2.replace("\n", "<br/>"));
                longweibobody.append("</p>");

            } else if (itemData.imagePath != null) {
                String str = itemData.imagePath;

                longweibobody.append("<img src=\"");
                longweibobody.append(str);
                longweibobody.append("\"  />");

            }

        }
    }


    /**
     * 获取当前编辑器的长度
     */
    protected int getediterslength(List<EditData> editList) {
        StringBuilder srb = new StringBuilder();
        for (EditData itemData : editList) {
            if (itemData.inputStr != null) {
                String str = itemData.imagePath;
                String str2 = itemData.inputStr;

                //longweibobody.append("<p>");
                srb.append(str2);
                //longweibobody.append("</p>");

                Log.d("RichEditor", "commit inputStr=" + itemData.inputStr);
            } else if (itemData.imagePath != null) {
                String str = itemData.imagePath;

                //longweibobody.append("<img src=\"");
                srb.append(str);
                //longweibobody.append("\"  />");
            }

        }

        return srb.length();
    }


    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath, String serverimagepath, Bitmap bmpCompressed) {
        editor.insertImage(imagePath, serverimagepath, bmpCompressed);
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    class editcomtitleforweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            facegridView1.setVisibility(View.GONE);
        }

    }

    class toshortweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub


            //sda
            Intent intent = new Intent(commentlongdiscussareaweiboActivity.this, commentshortdisscussareaweiboActivity.class);
            startActivity(intent);
            commentlongdiscussareaweiboActivity.this.finish();
        }

    }


    class editor_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            facegridView1.setVisibility(View.GONE);
        }

    }

    class atsb2_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(commentlongdiscussareaweiboActivity.this, atfriendActivity.class);
            //startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UNAME);
        }

    }

    class addlongweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
                /*
				titlebuilder=new SpannableStringBuilder();
						//(SpannableStringBuilder)editcomtitleforweibo1.getText();
				titlebuilder.append("<feed-title style='display:none'>");
				titlebuilder.append(editcomtitleforweibo1.getText().toString());
				titlebuilder.append("</feed-title>");
				
				titlebuilder.append(hidelongweibobody.toString());
				
				String titlestr=titlebuilder+"";

				
				*/

            List<EditData> editList = editor.buildEditData();
            //List<com.example.sjqcjstock.richeditor.RichTextEditor.EditData> editList=editor.buildEditData();
            // 下面的代码可以上传、或者保存，请自行实现
            dealEditData(editList);
            String title;
            if ("".equals(editcomtitleforweibo1.getText().toString()) || editcomtitleforweibo1.getText() == null) {
                //title="【】";
                CustomToast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_LONG).show();
            } else {
                title = editcomtitleforweibo1.getText() + "";


                titlebuilder = new SpannableStringBuilder();
                //(SpannableStringBuilder)editcomtitleforweibo1.getText();
                titlebuilder.append("<feed-title style='display:none'>");
                titlebuilder.append(title);
                titlebuilder.append("</feed-title>");

                titlebuilder.append(longweibobody.toString());

                String titlestr = titlebuilder + "";

                //发送长微博

                new SendInfoTaskaddlongweibo().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
                                //new String[] { "login_email", "1061550505@qq.com" },
                                //new String[] { "login_password", "12345678" },
                                new String[]{"mid", Constants.staticmyuidstr},
                                new String[]{"login_password", Constants.staticpasswordstr},
                                new String[]{"tokey", Constants.statictokeystr},

                                new String[]{"app_name", "public"},
                                new String[]{"body", titlebuilder.toString()},
                                new String[]{"channel_id", "15"},

                                new String[]{"type", "long_post"}
                                //new String[] { "attach_id", attach_idBuilder.toString()}


                        )

                );

                //CustomToast.makeText(getApplicationContext(),"已发送长文",1).show();

                //返回给股吧页面标识
                Intent intent = new Intent();
                Bundle unameBundle = new Bundle();
                //unameBundle.putString("unamestr", (String)listatfrientData.get(arg2-1).get("unamestr"));
                //zhuceIntent.putExtra("username", zhuceEdit.getText().toString());
                //intent.putExtras(unameBundle);
                setResult(1, intent);

                finish();
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

    class getimgtotakephoto1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //从拍照获取图片
            getImageFromCamera();

        }

    }

    class getimgtoalbum1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //从相册获取图片
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

                //InputMethodManager imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(editcommentforweibo1.getWindowToken(), 0);
                //InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.hideSoftInputFromWindow(editcommentforweibo1.getWindowToken(), 0);

//					int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM; 
//					getWindow().addFlags(flags); 


                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            } else {

                facegridView1.setVisibility(View.GONE);
                isdisplayfacelibrary = "0";
            }

        }

    }

//		class ImageView03_listener implements OnClickListener{
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if("0".equals(isdisplaygetimg)){
//					LinearLayoutgetimg.setVisibility(View.VISIBLE);
//					isdisplaygetimg="1";
//					
//					
//				}else{
//
//					LinearLayoutgetimg.setVisibility(View.GONE);
//					isdisplaygetimg="0";
//				}
//
//			}
//			
//		}

    class editcommentforweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub


            LinearLayout01.setVisibility(View.VISIBLE);


        }

    }


    class LinearLayout01_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            LinearLayout01.setVisibility(View.VISIBLE);

        }

    }


    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }


    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            CustomToast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        //从相册获取图片
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {

                Uri uri = data.getData();
                if (uri != null) {
                    //Bitmap bmp=BitmapFactory.decodeFile(uri.toString());
                    //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                    ContentResolver resolver = getContentResolver();
                    Bitmap bmp;
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
                        //imgportrait.setImageBitmap(bmp);
                        Bitmap bmpCompressed = Bitmap.createScaledBitmap(bmp, 200, 200, true);

                        //将长微博图片上传的服务器

                        String imgurl = sendData(bmpCompressed);

                        insertBitmap(getRealFilePath(uri), imgurl, bmpCompressed);


                        /**

                         longweibobody=(SpannableStringBuilder)editcommentforweibo1.getText();
                         longweibobody.append("1");
                         //要让图片替代指定的文字就要用ImageSpan
                         ImageSpan span = new ImageSpan(bmpCompressed, ImageSpan.ALIGN_BASELINE);

                         int length= editcommentforweibo1.getText().length();
                         longweibobody.setSpan(span, length-1,
                         length,
                         Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                         //editcommentforweibo1.setText(longweibobody);
                         //String str= editcommentforweibo1.getText()+"";


                         //转义数据给隐藏Edittext
                         //editcommentforweibohide1.setText(longweibobody);
                         //String hidelongweibo  =editcommentforweibohide1.getText()+"";

                         hidelongweibobody=new SpannableStringBuilder();

                         editcommentforweibo1.getText()+"";

                         hidelongweibobody.append("<img src=\"");
                         hidelongweibobody.append(imgurl);
                         hidelongweibobody.append("\"  />");

                         String str= editcommentforweibo1.getText()+"";
                         String str2= editcommentforweibohide1.getText()+"";

                         //editcommentforweibohide1.setText(hidelongweibobody);

                         */
                        /**
                         //需要处理的文本，[smile]是需要被替代的文本
                         //spannable = new SpannableStringBuilder(editcommentforweibo1.getText().toString()+"1");
                         SpannableStringBuilder spanStr = (SpannableStringBuilder) editcommentforweibo1.getText();
                         spanStr.append("1");
                         //editcommentforweibo1.setText(editcommentforweibo1.getText().toString()+"1");
                         //要让图片替代指定的文字就要用ImageSpan
                         ImageSpan span = new ImageSpan(bmpCompressed, ImageSpan.ALIGN_BASELINE);

                         //开始替换，注意第2和第3个参数表示从哪里开始替换到哪里替换结束（start和end）
                         //最后一个参数类似数学中的集合,[5,12)表示从5到12，包括5但不包括12
                         //						        spannable.setSpan(span, editcommentforweibo1.getText().length(),
                         //						        		editcommentforweibo1.getText().length(),
                         //						        		Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                         //setText(spannable);
                         int length= editcommentforweibo1.getText().length();
                         spanStr.setSpan(span, length-1,
                         length,
                         Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                         editcommentforweibo1.setText(spanStr);

                         //SpannableString spannable2 = new SpannableString(spannable.toString()+"1");

                         //editcommentforweibo1.setText(editcommentforweibo1.getText().toString()+"1");

                         String fullpicurlstr="";
                         String picheightstr="";
                         String picurlstr="";
                         String picwidthstr="";
                         */


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
                    //use bundle to get data
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap photo = (Bitmap) bundle.get("data"); //get bitmap
                        //imgportrait.setImageBitmap(photo);


                        Bitmap bmpCompressed = Bitmap.createScaledBitmap(photo, 200, 200, true);

                        try {
                            //将长微博图片上传的服务器

                            String imgurl = sendData(bmpCompressed);

                            insertBitmap(getRealFilePath(uri), imgurl, bmpCompressed);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }


                        //spath :生成图片取个名字和路径包含类型
                        String uuid = UUID.randomUUID() + "";
                        String envstr = Environment.getExternalStorageDirectory() + "/sglrBitmap/" + uuid + ".jpg";
                        saveImage(photo, envstr);
                    } else {
                        CustomToast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    //to do find the path of pic by uri
                }

            }
        } else if (resultCode == REQUEST_CODE_UNAME) {
            Bundle bundle = data.getExtras();
            String Name = bundle.getString("unamestr");
            //myTextView.setText("恭喜您，注册成功。您的用户名是："+Name);
            List<EditData> editList = editor.buildEditData();
            //List<com.example.sjqcjstock.richeditor.RichTextEditor.EditData> editList=editor.buildEditData();
            // 下面的代码可以上传、或者保存，请自行实现
            int editerlen = editor.editerviewscounts();

            //editor.addEditTextAtIndex(editerlen,"@"+Name+" ");

            //editor.createEditText("@"+Name+" ",1);
            editor.editeratinfoforlastview("@" + Name + "  ");
        }

    }


    private class SendInfoTaskaddlongweibo extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //CustomToast.makeText(supermanlistActivity.this, result, 1).show();

            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            //解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";

                if ("1".equals(statusstr)) {
                    CustomToast.makeText(getApplicationContext(), "发长文成功", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.makeText(getApplicationContext(), "发长文失败,请重试", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();


//			        if (isineditarea(editcommentforweibo1, ev)) {  
//			        	//CustomToast.makeText(getApplicationContext(), "事情edit焦点", 1)
//						 //    .show();
//			        	LinearLayout01.setVisibility(View.GONE);
//			        		
//			        }

            //if(isineditarea(LinearLayout01, ev)==false){
            //LinearLayout01.setVisibility(View.VISIBLE);

            //}

            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }


    //判断是否离开edit区域的方法
    public boolean isineditarea(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
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


    //将图片上传到服务器
    public String sendData(Bitmap bitmap) throws Exception {
        try {


            HttpClient httpClient = new DefaultHttpClient();

            HttpContext localContext = new BasicHttpContext();


            /**

             HttpClient httpClient=new DefaultHttpClient();

             HttpPost httpPost=new HttpPost("http://123.56.163.113:8080/GraceSys/getHomeRmd.do");
             //HttpGet httpGet=new HttpGet("http://123.56.163.113:8080/GraceSys/getHomeRmd.do");

             //MultipartEntity entity=new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


             try {
             List<NameValuePair> paramss = new ArrayList<NameValuePair>();
             paramss.add(new BasicNameValuePair("longitude", "104.068163"));
             paramss.add(new BasicNameValuePair("latitude", "30.582042"));
             paramss.add(new BasicNameValuePair("page", "0"));

             //entity.addPart("longitude",new StringBody("104.068163"));
             //entity.addPart("latitude",new StringBody("30.582042"));
             //entity.addPart("page",new StringBody("0"));
             httpPost.setEntity(new UrlEncodedFormEntity(paramss, HTTP.UTF_8));

             //httpPost.setEntity(entity);

             //HttpResponse response=httpClient.execute(httpPost,localContext);
             HttpResponse response=httpClient.execute(httpPost);
             int code=response.getStatusLine().getStatusCode();
             //if (response.getStatusLine().getStatusCode() == 200) {
             String result = EntityUtils.toString(response.getEntity());
             System.out.println("result:" + result);


             * */

            HttpPost httpPost = new HttpPost(Constants.Url + "?app=public&mod=AppFeedList&act=saveEditorImg&dir=image");

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("login_password", new StringBody(Constants.staticpasswordstr));
            entity.addPart("tokey", new StringBody(Constants.statictokeystr));

            //entity.addPart("attach_type",new StringBody("feed_image"));
            //entity.addPart("thumb",new StringBody("1"));
            //entity.addPart("upload_type",new StringBody("image"));

            //Bitmap bmpCompressed=Bitmap.createScaledBitmap(bitmap, 640, 480, true);
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //bmpCompressed.compress(CompressFormat.JPEG,100, bos);
            //bmpCompressed.compress(CompressFormat.PNG,100, bos);

            bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
            //imgportrait.setImageBitmap(bmpCompressed);
            byte[] data = bos.toByteArray();

            // sending a String param;

            //entity.addPart("myParam",new StringBody("my value"));

            // sending a Image;

            //entity.addPart("mylmage",new ByteArrayBody(data,"temp.jpg"));
            entity.addPart("mylmage", new ByteArrayBody(data, "temp.jpg"));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            int code = response.getStatusLine().getStatusCode();

            //BufferedReader reader=
            //		new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
            InputStream in = response.getEntity().getContent();
            //String sResponse=reader.readLine();
            String resstr = HttpUtil.changeInputStream(in);
            //saveheadimg1.setVisibility(View.VISIBLE);

            resstr = resstr.replace("\n ", "");
            resstr = resstr.replace("\n", "");
            resstr = resstr.replace(" ", "");
            resstr = "[" + resstr + "]";
            String urlstr = "";
            //解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(resstr);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                urlstr = map.get("url") + "";

            }


            //返回图片url
            return urlstr;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }


    public static void saveImage(Bitmap photo, String spath) {
        try {
            File fImage = null;
            FileOutputStream iStream = null;

            //BufferedOutputStream bos = new BufferedOutputStream(
            //        new FileOutputStream(spath, false));
            //photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);

            fImage = new File(Environment.getExternalStorageDirectory() + "/sglrBitmap/");
            if (!fImage.exists()) {
                fImage.mkdir();
            }

            fImage = new File(spath);
            iStream = new FileOutputStream(fImage);

            photo.compress(Bitmap.CompressFormat.JPEG, 75, iStream);// (0-100)压缩文件
            //bos.flush();
            //bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            //return false;
        }
        //return true;
    }
}
