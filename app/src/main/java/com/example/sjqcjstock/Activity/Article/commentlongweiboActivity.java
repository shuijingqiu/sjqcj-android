package com.example.sjqcjstock.Activity.Article;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
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
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.atfriendActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.adapter.facelibaryAdapter;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
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
 * 写长微博的页面
 */
public class commentlongweiboActivity extends Activity {

    // 获取@ 的好友
    private static final int REQUEST_CODE_UNAME = 4;
    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取
    // 长微博body字符串组合
//    private SpannableStringBuilder titlebuilder;
    // 文章标题
    private String title;
    // 文章内容
    private String longweibobodystr;
    private StringBuilder longweibobody;
    // 获取控件
    private RelativeLayout LinearLayout01;
    private ImageView facelibrary1;
    private LinearLayout goback1;
    private ImageView getimgtoalbum1;
    private LinearLayout addlongweibo1;
    private EditText editcomtitleforweibo1;
    private RichTextEditor editor;
    private ImageView getimgtotakephoto1;
    private ImageView atsb2;
    private Button toshortweibo1;
    // 定义适配器
    private facelibaryAdapter facelibaryAdapter;
    private GridView facegridView1;
    private ArrayList<HashMap<String, Object>> listData;
    // 判断弹出表情库的标识 0代表显示，1代表隐藏
    private String isdisplayfacelibrary = "0";
    // 接口返回数据
    private String feedStr;
    // 这个是集合了讨论区的发文 如果是15 就为讨论区的发文 20170724
    private String channelId;

    public void editortodown() {
        // editor.fullScroll(asd);
        editor.fullScroll(ScrollView.FOCUS_DOWN);
    }

    public void getRichTextEditorforced() {
        facegridView1.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 可以在主线程中使用http网络访问
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addlongweibo);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        channelId = getIntent().getStringExtra("channelId");
        editor = (RichTextEditor) findViewById(R.id.richEditor);
        // 往RichTextEditor里传入当前Activity的参数
        editor.sendActivity(commentlongweiboActivity.this);
        longweibobody = new StringBuilder();
        LinearLayout01 = (RelativeLayout) findViewById(R.id.LinearLayout01);
        facelibrary1 = (ImageView) findViewById(R.id.facelibrary1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        getimgtoalbum1 = (ImageView) findViewById(R.id.getimgtoalbum1);
        addlongweibo1 = (LinearLayout) findViewById(R.id.addlongweibo1);
        editcomtitleforweibo1 = (EditText) findViewById(R.id.editcomtitleforweibo1);
        getimgtotakephoto1 = (ImageView) findViewById(R.id.getimgtotakephoto1);
        atsb2 = (ImageView) findViewById(R.id.atsb2);
        toshortweibo1 = (Button) findViewById(R.id.toshortweibo1);
        editor.setOnClickListener(new editor_listener());
        LinearLayout01.setOnClickListener(new LinearLayout01_listener());
        facelibrary1.setOnClickListener(new facelibrary1_listener());
        goback1.setOnClickListener(new goback1_listener());
        getimgtoalbum1.setOnClickListener(new getimgtoalbum1_listener());
        addlongweibo1.setOnClickListener(new addlongweibo1_listener());
        getimgtotakephoto1
                .setOnClickListener(new getimgtotakephoto1_listener());
        atsb2.setOnClickListener(new atsb2_listener());
        toshortweibo1.setOnClickListener(new toshortweibo1_listener());
        editcomtitleforweibo1
                .setOnClickListener(new editcomtitleforweibo1_listener());
        facegridView1 = (GridView) findViewById(R.id.facegridView1);
        listData = new ArrayList<HashMap<String, Object>>();
        facelibaryAdapter = new facelibaryAdapter(
                commentlongweiboActivity.this, listData);
        final int[] imagestr = {R.drawable.img0, R.drawable.img1,
                R.drawable.img2, R.drawable.img3, R.drawable.img4,
                R.drawable.img5, R.drawable.img6, R.drawable.img7,
                R.drawable.img8, R.drawable.img9, R.drawable.img10,
                R.drawable.img11, R.drawable.img12, R.drawable.img13,
                R.drawable.img14, R.drawable.img15, R.drawable.img16,
                R.drawable.img17, R.drawable.img18, R.drawable.img19,
                R.drawable.img20, R.drawable.img21, R.drawable.img22,
                R.drawable.img23, R.drawable.img24, R.drawable.img25,
                R.drawable.img26, R.drawable.img27, R.drawable.img28,
                R.drawable.img29, R.drawable.img30, R.drawable.img31,
                R.drawable.img32, R.drawable.img33, R.drawable.img34,
                R.drawable.img35, R.drawable.img36, R.drawable.img37,
                R.drawable.img38, R.drawable.img39, R.drawable.img40,
                R.drawable.img41, R.drawable.img42, R.drawable.img43,
                R.drawable.img44, R.drawable.img45, R.drawable.img46,
                R.drawable.img47, R.drawable.img48, R.drawable.img49,
                R.drawable.img50, R.drawable.img51, R.drawable.img52,
                R.drawable.img53, R.drawable.img54, R.drawable.img55,
                R.drawable.img56, R.drawable.img57, R.drawable.img58,
                R.drawable.img59, R.drawable.img60, R.drawable.img61,
                R.drawable.img62, R.drawable.img63, R.drawable.img64,
                R.drawable.img65, R.drawable.img66, R.drawable.img67,
                R.drawable.img68, R.drawable.img69, R.drawable.img70,
                R.drawable.img71, R.drawable.img72, R.drawable.img73,
                R.drawable.img74, R.drawable.img75, R.drawable.img76,
                R.drawable.img77, R.drawable.img78, R.drawable.img79,
                R.drawable.img80, R.drawable.img81, R.drawable.img82,
                R.drawable.img83, R.drawable.img84, R.drawable.img85,
                R.drawable.img86, R.drawable.img87, R.drawable.img88,
                R.drawable.img89, R.drawable.img90, R.drawable.img91,
                R.drawable.img92, R.drawable.img93, R.drawable.img94,
                R.drawable.img95, R.drawable.img96, R.drawable.img97,
                R.drawable.img98, R.drawable.img99, R.drawable.img100,
                R.drawable.img101, R.drawable.img102, R.drawable.img103,
                R.drawable.img104, R.drawable.img105, R.drawable.img106,
                R.drawable.img107, R.drawable.img108, R.drawable.img109,
                R.drawable.img110, R.drawable.img111, R.drawable.img112,
                R.drawable.img113, R.drawable.img114, R.drawable.img115,
                R.drawable.img116, R.drawable.img117, R.drawable.img118,
                R.drawable.img119, R.drawable.img120, R.drawable.img121,
                R.drawable.img122, R.drawable.img123, R.drawable.img124,
                R.drawable.img125, R.drawable.img126, R.drawable.img127,
                R.drawable.img128, R.drawable.img129, R.drawable.img130,
                R.drawable.img131, R.drawable.img132, R.drawable.img133,
                R.drawable.img134

        };

        final String[] facestrs = {"[1]", "[2]", "[3]", "[4]", "[5]", "[6]",
                "[7]", "[8]", "[9]", "[10]", "[11]", "[12]", "[13]", "[14]",
                "[15]", "[16]", "[17]", "[18]", "[19]", "[20]", "[21]", "[22]",
                "[23]", "[24]", "[25]", "[26]", "[27]", "[28]", "[29]", "[30]",
                "[31]", "[32]", "[33]", "[34]", "[35]", "[36]", "[37]", "[38]",
                "[39]", "[40]", "[41]", "[42]", "[43]", "[44]", "[45]", "[46]",
                "[47]", "[48]", "[49]", "[50]", "[51]", "[52]", "[53]", "[54]",
                "[55]", "[56]", "[57]", "[58]", "[59]", "[60]", "[61]", "[62]",
                "[63]", "[64]", "[65]", "[66]", "[67]", "[68]", "[69]", "[70]",
                "[71]", "[72]", "[73]", "[74]", "[75]", "[76]", "[77]", "[78]",
                "[79]", "[80]", "[81]", "[82]", "[83]", "[84]", "[85]", "[86]",
                "[87]", "[88]", "[89]", "[90]", "[91]", "[92]", "[93]", "[94]",
                "[95]", "[96]", "[97]", "[98]", "[99]", "[100]", "[101]",
                "[102]", "[103]", "[104]", "[105]", "[106]", "[107]", "[108]",
                "[109]", "[110]", "[111]", "[112]", "[113]", "[114]", "[115]",
                "[116]", "[117]", "[118]", "[119]", "[120]", "[121]", "[122]",
                "[123]", "[124]", "[125]", "[126]", "[127]", "[128]", "[129]",
                "[130]", "[131]", "[132]", "[133]", "[134]"

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
                editor.editeratinfoforlastview(facestrs[position]);
            }

        });

    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<EditData> editList) {
        for (EditData itemData : editList) {
            if (itemData.inputStr != null) {
                String str2 = itemData.inputStr;
//                longweibobody.append("<p>");
                longweibobody.append(str2.replace("\n", "<br/>"));
//                longweibobody.append("</p>");
            } else if (itemData.imagePath != null) {
                String str = itemData.imagePath;
                longweibobody.append("<img src=\"");
                longweibobody.append(str);
                longweibobody.append("\"  />");
            }
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath, String serverimagepath,
                              Bitmap bmpCompressed) {
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
            if ("".equals(editcomtitleforweibo1.getText().toString().trim()) && "".equals(longweibobody.toString().trim())) {
                if(channelId != null && "15".equals(channelId)){
                    // 跳转到发短文
                    Intent intent = new Intent(commentlongweiboActivity.this,
                            commentshortweiboActivity.class);
                    intent.putExtra("channelId",channelId);
                    startActivity(intent);
                    commentlongweiboActivity.this.finish();
                }else {
                    // 打赏文
                    Intent intent = new Intent(commentlongweiboActivity.this,
                            commentrewardweiboActivity.class);
                    startActivity(intent);
                    commentlongweiboActivity.this.finish();
                }
                return;
            }
            // 放弃发布
            new AlertDialog.Builder(commentlongweiboActivity.this)
                    .setMessage("要放弃发布吗！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if(channelId != null && "15".equals(channelId)){
                                        // 跳转到发短文
                                        Intent intent = new Intent(commentlongweiboActivity.this,
                                                commentshortweiboActivity.class);
                                        intent.putExtra("channelId",channelId);
                                        startActivity(intent);
                                        commentlongweiboActivity.this.finish();
                                    }else{
                                        // 打赏文
                                        Intent intent = new Intent(commentlongweiboActivity.this,
                                                commentrewardweiboActivity.class);
                                        startActivity(intent);
                                        commentlongweiboActivity.this.finish();
                                    }

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

    class editor_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            CustomToast.makeText(getApplicationContext(), "点击输入内容", Toast.LENGTH_SHORT).show();
            facegridView1.setVisibility(View.GONE);
            editor.firstclickgetfource();

        }

    }

    class atsb2_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent(commentlongweiboActivity.this,
                    atfriendActivity.class);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UNAME);
        }

    }

    class addlongweibo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            //隐藏键盘
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(toshortweibo1.getWindowToken(), 0);
            List<EditData> editList = editor.buildEditData();
            dealEditData(editList);
            title = editcomtitleforweibo1.getText() + "";
            longweibobodystr = longweibobody + "";
            if ("".equals(title.trim())) {
                CustomToast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();

            } else if ("".equals(longweibobodystr.trim())) {
                CustomToast.makeText(getApplicationContext(), "正文不能为空", Toast.LENGTH_SHORT).show();
            } else {
                Constants.isreferforumlist = "0";
//                titlebuilder = new SpannableStringBuilder();
//                titlebuilder.append("<feed-title style='display:none'>");
//                titlebuilder.append(title);
//                titlebuilder.append("</feed-title>");
                longweibobodystr = longweibobodystr
                        .replace(
                                "[",
                                "<img border='0' alt='' src='http://www.sjqcj.com/addons/plugin/LongText/editor/kindeditor-4.1.4/plugins/emoticons/images/");
                longweibobodystr = longweibobodystr.replace("]", ".gif' />");

//                titlebuilder.append(longweibobodystr);
//
//                String titlestr = titlebuilder + "";
                // 发送长微博

//                new SendInfoTaskaddlongweibo()
//                        .execute(new TaskParams(
//                                Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
//                                new String[]{"mid", Constants.staticmyuidstr},
//                                new String[]{"login_password", Constants.staticpasswordstr},
//                                new String[]{"tokey", Constants.statictokeystr},
//                                new String[]{"app_name", "public"},
//                                new String[]{"body", titlebuilder.toString()},
//                                new String[]{"type", "long_post"}
//                        ));

                // 发送长微博
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List dataList = new ArrayList();
                        dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                        dataList.add(new BasicNameValuePair("app_name", "public"));
                        // 标题
                        dataList.add(new BasicNameValuePair("title", title));
                        // 内容
                        dataList.add(new BasicNameValuePair("body", longweibobodystr));
                        // post短微博 long_post长微博 paid_post付费微博 repost转发微博 postimage图片微博
                        dataList.add(new BasicNameValuePair("type", "long_post"));
//                        // 附件信息
//                        dataList.add(new BasicNameValuePair("attach_id", attach_idBuilder.toString()));
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
            // 从拍照获取图片
            getImageFromCamera();

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
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(getCurrentFocus()
                                .getApplicationWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            } else {

                facegridView1.setVisibility(View.GONE);
                isdisplayfacelibrary = "0";
            }

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

                        double proportion = ImageUtil.getProportion(bmp.getWidth(), bmp.getHeight());
                        Bitmap bmpCompressed = Bitmap.createScaledBitmap(bmp,
                                (int) (bmp.getWidth() * proportion), (int) (bmp.getHeight() * proportion), true);

                        // 将长微博图片上传的服务器
                        String imgurl = sendData(bmpCompressed);

                        insertBitmap(getRealFilePath(uri), imgurl,
                                bmpCompressed);
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
                        // 将长微博图片上传的服务器

                        String imgurl = sendData(bmpCompressed);

                        insertBitmap(getRealFilePath(Uri.fromFile(file)), imgurl,
                                bmpCompressed);
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
//            if (data != null) {
//                Uri uri = data.getData();
//                if (uri == null) {
//                    // use bundle to get data
//                    Bundle bundle = data.getExtras();
//                    if (bundle != null) {
//                        Bitmap photo = (Bitmap) bundle.get("data"); // get
//
//                        double proportion= ImageUtil.getProportion(photo.getWidth(),photo.getHeight());
//                        Bitmap bmpCompressed = Bitmap.createScaledBitmap(photo,
//                                (int)(photo.getWidth()*proportion), (int)(photo.getHeight()*proportion), true);
//                        try {
//                            // 将长微博图片上传的服务器
//
//                            String imgurl = sendData(bmpCompressed);
//
//                            insertBitmap(getRealFilePath(uri), imgurl,
//                                    bmpCompressed);
//                        } catch (Exception e) {
//                        }
//                        // spath :生成图片取个名字和路径包含类型
//                        String uuid = UUID.randomUUID() + "";
//                        String envstr = Environment
//                                .getExternalStorageDirectory()
//                                + "/sglrBitmap/"
//                                + uuid + ".jpg";
//                        saveImage(photo, envstr);
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), "err****",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//            }
        } else if (resultCode == REQUEST_CODE_UNAME) {
            Bundle bundle = data.getExtras();
            String Name = bundle.getString("unamestr");
            editor.editeratinfoforlastview("@" + Name + "  ");
        }

    }

//    private class SendInfoTaskaddlongweibo extends
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
//                CustomToast.makeText(getApplicationContext(), "发长文失败,请重试", Toast.LENGTH_SHORT)
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
//                    CustomToast.makeText(getApplicationContext(), "发长文成功", Toast.LENGTH_SHORT).show();
//                    // 返回给股吧页面标识
//                    Constants.intentFlag = "1";
//                    Constants.isreferforumlist = "0";
//                    finish();
//                } else {
//                    CustomToast.makeText(getApplicationContext(), "发长文失败,请重试", Toast.LENGTH_SHORT)
//                            .show();
//                }
//            }
//        }
//
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isineditarea(editcomtitleforweibo1, ev) == true) {
                editor.firstclickgetfource();
            }
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
//            long sjc = System.currentTimeMillis();
//            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
//            HttpPost httpPost = new HttpPost(
//                    Constants.Url + "?app=public&mod=AppFeedList&act=saveEditorImg&dir=image&sjc="
//                            + "1" + sjc + numcode);
            HttpPost httpPost = new HttpPost(
                    Constants.newUrl + "/api/upload/longPost");
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("step", new StringBody("upload"));
//            entity.addPart("login_password", new StringBody(
//                    Constants.staticpasswordstr));
            entity.addPart("token", new StringBody(Constants.apptoken));
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                    true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            entity.addPart("mylmage", new ByteArrayBody(data, "temp.jpg"));
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            InputStream in = response.getEntity().getContent();
            String resstr = HttpUtil.changeInputStream(in);
            JSONObject jsonObject = new JSONObject(resstr);
            if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                // 请求失败的情况
                CustomToast.makeText(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                return null;
            }
            JSONObject dataObject = new JSONObject(jsonObject.getString("data"));
            // 返回图片url
            return dataObject.getString("url");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveImage(Bitmap photo, String spath) {
        try {
            File fImage = null;
            FileOutputStream iStream = null;

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

}
