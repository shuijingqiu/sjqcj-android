package com.example.sjqcjstock.Activity;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * 写打赏文的页面
 */
public class commentrewardweiboActivity extends Activity {

    // 获取@ 的好友
    private static final int REQUEST_CODE_UNAME = 4;
    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取
    // 长微博body字符串组合
    private SpannableStringBuilder titlebuilder;
    private StringBuilder longweibobody;
    private SpannableStringBuilder hidelongweibobody;
    // 获取控件
    private RelativeLayout LinearLayout01;
    private LinearLayout LinearLayoutgetimg;
    private ImageView facelibrary1;
    private LinearLayout goback1;
    private ImageView getimgtoalbum1;
    private LinearLayout addlongweibo1;
    private RichTextEditor editor;
    private ImageView getimgtotakephoto1;
    private ImageView atsb2;
    private Button toshortweibo1;
    // 定义适配器
    private facelibaryAdapter facelibaryAdapter;
    private GridView facegridView1;
    // 定义于数据库同步的字段集合
    // private String[] name;
    private ArrayList<HashMap<String, Object>> listData;
    // 判断弹出表情库的标识 0代表显示，1代表隐藏
    private String isdisplayfacelibrary = "0";
    //写文章类型的标题
    private TextView bowenTitleTv;
    //写文章的标题输入框
    private EditText editcomtitleforweibo1;
    // 写摘要的输入框
    private RelativeLayout summaryRel;
    private EditText summaryEdi;
    // 写摘要还能输入多少字
    private TextView inputNumberWords;
    // 富文本编辑器里面显示的说明文字
    public TextView shareFreshTingsTx;
    // 打赏水晶币的文本输入框
    private LinearLayout downlinear3;
    private EditText rystalCoinEdi;
    // 协议的checkbox
    private CheckBox weiboAgreementCk;
    // 协议的跳转链接
    private TextView weiboAgreementTv;

    // 用于RichTextEditor回调的方法
    public void getRichTextEditorforced() {
        facegridView1.setVisibility(View.GONE);
    }

    public void editortodown() {
        editor.fullScroll(ScrollView.FOCUS_DOWN);
    }

    // 用于RichTextEditor回调 ScrollView到达顶部
    public void editortotop() {
        // editor.fullScroll(33);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.addrewardweibo);
        // Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        bowenTitleTv = (TextView) findViewById(R.id.bowen_title_tv);
        editcomtitleforweibo1 = (EditText) findViewById(R.id.editcomtitleforweibo1);
        summaryRel = (RelativeLayout) findViewById(R.id.summary_rel);
        summaryEdi = (EditText) findViewById(R.id.summary_edi);
        inputNumberWords = (TextView) findViewById(R.id.input_number_words);
        shareFreshTingsTx = (TextView) findViewById(R.id.share_fresh_tings_tx);
        downlinear3 = (LinearLayout) findViewById(R.id.downlinear3);
        rystalCoinEdi = (EditText) findViewById(R.id.rystal_coin_edi);
        // 绑定摘要输入状态的变化
        summaryEdi.addTextChangedListener(watcher);
        weiboAgreementCk = (CheckBox) findViewById(R.id.weibo_agreement_ck);
        weiboAgreementTv = (TextView) findViewById(R.id.weibo_agreement_tv);
        weiboAgreementTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到协议页面
                startActivity(new Intent(commentrewardweiboActivity.this, AgreementActivity.class));
            }
        });
        editor = (RichTextEditor) findViewById(R.id.richEditor);
        // 往RichTextEditor里传入当前Activity的参数
        editor.senddiscussareaweActivity(commentrewardweiboActivity.this);
        longweibobody = new StringBuilder();
        LinearLayout01 = (RelativeLayout) findViewById(R.id.LinearLayout01);
        // editcommentforweibo1=(EditText)findViewById(R.id.editcommentforweibo1);
        // ImageView03=(ImageView)findViewById(R.id.ImageView03);
        LinearLayoutgetimg = (LinearLayout) findViewById(R.id.LinearLayoutgetimg);
        facelibrary1 = (ImageView) findViewById(R.id.facelibrary1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        getimgtoalbum1 = (ImageView) findViewById(R.id.getimgtoalbum1);
        addlongweibo1 = (LinearLayout) findViewById(R.id.addlongweibo1);
        // editcommentforweibohide1=(EditText)findViewById(R.id.editcommentforweibohide1);
        editcomtitleforweibo1 = (EditText) findViewById(R.id.editcomtitleforweibo1);

        getimgtotakephoto1 = (ImageView) findViewById(R.id.getimgtotakephoto1);
        atsb2 = (ImageView) findViewById(R.id.atsb2);
        toshortweibo1 = (Button) findViewById(R.id.toshortweibo1);

        // editcomtitleforweibo1.setOnFocusChangeListener(new
        // OnFocusChangeListener() {
        //
        // public void onFocusChange(View v, boolean hasFocus) {
        // if(!hasFocus){
        // CustomToast.makeText(getApplicationContext(), "点击输入内容",1).show();
        //
        // }
        // //do job here owhen Edittext lose focus
        // }
        // });

        editor.setOnClickListener(new editor_listener());
        // editerlay.setOnClickListener(new editerlay_listener());

        // editcommentforweibo1.setOnClickListener(new
        // editcommentforweibo1_listener());
        LinearLayout01.setOnClickListener(new LinearLayout01_listener());
        // ImageView03.setOnClickListener(new ImageView03_listener());
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
        // spannable = new
        // SpannableStringBuilder(editcommentforweibo1.getText().toString());

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
                commentrewardweiboActivity.this, listData);
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

        // int[]
        // arrowimagestr={R.drawable.twodimension1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,
        // R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1
        // ,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,R.mipmap.gracearrow1,
        // R.mipmap.gracearrow1,};
        for (int i = 0; i < 110; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("friend_image", imagestr[i]);

            map.put("facestr", facestrs[i]);

            // map.put("friend_username", name[i]);
            // map.put("arrow_image", arrowimagestr[i]);
            // map.put("friend_id",id[i]);

            // 添加数据
            listData.add(map);
        }

        facegridView1.setAdapter(facelibaryAdapter);

        facegridView1.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                // 下面的代码可以上传、或者保存，请自行实现
                int editerlen = editor.editerviewscounts();

                // editor.addEditTextAtIndex(editerlen,"@"+Name+" ");

                // editor.createEditText("@"+Name+" ",1);
                editor.editeratinfoforlastview(facestrs[position]);

                // int index = editcommentforweibo1.getSelectionStart();
                // Editable editable = editcommentforweibo1.getText();
                // editable.insert(index, "@"+Name+" ");

                // TODO Auto-generated method stub
                // ImageGetter imageGetter = new ImageGetter() {
                // public Drawable getDrawable(String source) {
                // int id = Integer.parseInt(source);
                // Drawable d = getResources().getDrawable(id);
                // d.setBounds(0, 0, d.getIntrinsicWidth(),
                // d.getIntrinsicHeight());
                // return d;
                // }
                // };
                //
//                 CharSequence cs = Html.fromHtml("<img src='"+
//                 imagestr[position]+ "'/>",imageGetter, null);
//                 editcommentforweibo1.getText().append(cs);
                // String faceContent
                // =FilterHtml(Html.toHtml(editcommentforweibo1.getText()));
                //
                //

            }

        });

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

                // longweibobody.append("<p>");
                srb.append(str2);
                // longweibobody.append("</p>");
            } else if (itemData.imagePath != null) {
                String str = itemData.imagePath;
                String str2 = itemData.inputStr;

                // longweibobody.append("<img src=\"");
                srb.append(str);
                // longweibobody.append("\"  />");

            }

        }

        return srb.length();
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

    /**
     * 变化写文类型的事件
     */
    class toshortweibo1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {

            if ("".equals(editcomtitleforweibo1.getText().toString().trim()) && "".equals(summaryEdi.getText().toString().trim()) && "".equals(longweibobody.toString().trim())) {
                // 打赏文
                Intent intent = new Intent(commentrewardweiboActivity.this, commentshortweiboActivity.class);
                startActivity(intent);
                commentrewardweiboActivity.this.finish();
                return;
            }
            // 删除微博评论
            new AlertDialog.Builder(commentrewardweiboActivity.this)
                    .setMessage("要放弃发布吗！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Intent intent = new Intent(commentrewardweiboActivity.this, commentshortweiboActivity.class);
                                    startActivity(intent);
                                    commentrewardweiboActivity.this.finish();
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

    class editor_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            facegridView1.setVisibility(View.GONE);
            editor.firstclickgetfource();
            shareFreshTingsTx.setText("");
        }
    }

    class atsb2_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(commentrewardweiboActivity.this,
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
            // 标题
            String title = editcomtitleforweibo1.getText() + "";
            // 简介
            String summary = summaryEdi.getText() + "";
            //打赏的水晶币
            String rystalCoin = rystalCoinEdi.getText().toString().trim();
            if ("".equals(title)) {
                CustomToast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_LONG).show();
                return;
            }
            if ("".equals(summary)) {
                CustomToast.makeText(getApplicationContext(), "摘要不能为空", Toast.LENGTH_LONG).show();
                return;
            } else if (summary.length() > 140) {
                CustomToast.makeText(getApplicationContext(), "摘要超长了", Toast.LENGTH_LONG).show();
                return;
            }
            Object zhengwen = editor.lastFocusEdit.getText();
            if (zhengwen == null || "".equals(zhengwen.toString().trim())) {
                CustomToast.makeText(getApplicationContext(), "请输入正文", Toast.LENGTH_LONG).show();
                return;
            }
            if ("".equals(rystalCoin)) {
                CustomToast.makeText(getApplicationContext(), "请输入需打赏水晶数量", Toast.LENGTH_LONG).show();
                return;
            }
            if (!weiboAgreementCk.isChecked()) {
                CustomToast.makeText(getApplicationContext(), "请阅读并同意协议", Toast.LENGTH_LONG).show();
                return;
            }

            titlebuilder = new SpannableStringBuilder();
            // (SpannableStringBuilder)editcomtitleforweibo1.getText();
            titlebuilder.append("<feed-title style='display:none'>");
            titlebuilder.append(title);
            titlebuilder.append("</feed-title>");
            String longweibobodystr = longweibobody + "";
            longweibobodystr = longweibobodystr
                    .replace(
                            "[",
                            "<img border='0' alt='' src='http://www.sjqcj.com/addons/plugin/LongText/editor/kindeditor-4.1.4/plugins/emoticons/images/");
            longweibobodystr = longweibobodystr.replace("]", ".gif' />");

            titlebuilder.append(longweibobodystr);

            Constants.isreferforumlist = "0";
            // 发送打赏微博
            new SendInfoTaskaddlongweibo()
                    .execute(new TaskParams(
                            Constants.Url + "?app=public&mod=AppFeedList&act=AppPostFeed",
                            new String[]{"mid", Constants.staticmyuidstr},
                            new String[]{"login_password", Constants.staticpasswordstr},
                            new String[]{"tokey", Constants.statictokeystr},
                            new String[]{"app_name", "public"},
                            new String[]{"body", titlebuilder.toString()},
                            new String[]{"type", "long_post"},
                            new String[]{"introduction", summary},
                            new String[]{"reward", rystalCoin},
                            new String[]{"state", "1"}
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

    // class ImageView03_listener implements OnClickListener{
    //
    // @Override
    // public void onClick(View arg0) {
    // // TODO Auto-generated method stub
    // if("0".equals(isdisplaygetimg)){
    // LinearLayoutgetimg.setVisibility(View.VISIBLE);
    // isdisplaygetimg="1";
    //
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
//
//	class editcommentforweibo1_listener implements OnClickListener {
//
//		@Override
//		public void onClick(View arg0) {
//			// TODO Auto-generated method stub
//
//			LinearLayout01.setVisibility(View.VISIBLE);
//
//		}
//
//	}

    class LinearLayout01_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
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
                            // 将长微博图片上传的服务器

                            String imgurl = sendData(bmpCompressed);

                            insertBitmap(getRealFilePath(uri), imgurl,
                                    bmpCompressed);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

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
            // myTextView.setText("恭喜您，注册成功。您的用户名是："+Name);
            List<EditData> editList = editor.buildEditData();
            // List<com.example.sjqcjstock.richeditor.RichTextEditor.EditData>
            // editList=editor.buildEditData();
            // 下面的代码可以上传、或者保存，请自行实现
            int editerlen = editor.editerviewscounts();

            // editor.addEditTextAtIndex(editerlen,"@"+Name+" ");

            // editor.createEditText("@"+Name+" ",1);
            editor.editeratinfoforlastview("@" + Name + "  ");
        }

    }

    private class SendInfoTaskaddlongweibo extends
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
                    CustomToast.makeText(getApplicationContext(), "发长文成功", Toast.LENGTH_LONG).show();
                    // 返回给股吧页面标识
                    Constants.intentFlag = "1";
                    Constants.isreferforumlist = "0";
                    finish();
                } else {
                    CustomToast.makeText(getApplicationContext(), "发长文失败,请重试", Toast.LENGTH_LONG)
                            .show();
                }
            }
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

            if (isineditarea(editcomtitleforweibo1, ev) == true) {
                // loginusername1.setVisibility(View.VISIBLE);
                // oast.makeText(getApplicationContext(), "已离开",1).show();

                // InputMethodManager imm =
                // (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                // imm.hideSoftInputFromWindow(loginusername1.getWindowToken(),
                // 0);
                // CustomToast.makeText(getApplicationContext(), "点击输入内容",1).show();
//                 facegridView1.setVisibility(View.GONE);
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

    /**
     * @Override public boolean dispatchTouchEvent(MotionEvent ev) { // TODO
     * Auto-generated method stub
     * <p/>
     * if (ev.getAction() == MotionEvent.ACTION_DOWN) { View v =
     * getCurrentFocus();
     * <p/>
     * <p/>
     * // if (isineditarea(editcommentforweibo1, ev)) { //
     * //CustomToast.makeText(getApplicationContext(), "事情edit焦点", 1) // //
     * .show(); // LinearLayout01.setVisibility(View.GONE); // // }
     * <p/>
     * //if(isineditarea(LinearLayout01, ev)==false){
     * //LinearLayout01.setVisibility(View.VISIBLE);
     * <p/>
     * //}
     * <p/>
     * return super.dispatchTouchEvent(ev); } //
     * 必不可少，否则所有的组件都不会有TouchEvent了 if
     * (getWindow().superDispatchTouchEvent(ev)) { return true; }
     * return onTouchEvent(ev); }
     * <p/>
     * <p/>
     * //判断是否离开edit区域的方法 public boolean isineditarea(View v,
     * MotionEvent event) { if (v != null && (v instanceof EditText))
     * { int[] leftTop = { 0, 0 }; //获取输入框当前的location位置
     * v.getLocationInWindow(leftTop); int left = leftTop[0]; int top
     * = leftTop[1]; int bottom = top + v.getHeight(); int right =
     * left + v.getWidth(); if (event.getX() > left && event.getX() <
     * right && event.getY() > top && event.getY() < bottom) { //
     * 点击的是输入框区域，保留点击EditText的事件 return false; } else { return true; }
     * } return false; }
     */

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
                    Constants.Url + "?app=public&mod=AppFeedList&act=saveEditorImg&dir=image&sjc="
                            + "1" + sjc + numcode);
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("login_password", new StringBody(
                    Constants.staticpasswordstr));
            entity.addPart("tokey", new StringBody(Constants.statictokeystr));

            // entity.addPart("attach_type",new StringBody("feed_image"));
            // entity.addPart("thumb",new StringBody("1"));
            // entity.addPart("upload_type",new StringBody("image"));

            // Bitmap bmpCompressed=Bitmap.createScaledBitmap(bitmap, 640, 480,
            // true);
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 500, 500,
                    true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // bmpCompressed.compress(CompressFormat.JPEG,100, bos);
            // bmpCompressed.compress(CompressFormat.PNG,100, bos);

            bmpCompressed.compress(CompressFormat.JPEG, 100, bos);
            // imgportrait.setImageBitmap(bmpCompressed);
            byte[] data = bos.toByteArray();

            // sending a String param;

            // entity.addPart("myParam",new StringBody("my value"));

            // sending a Image;

            // entity.addPart("mylmage",new ByteArrayBody(data,"temp.jpg"));
            entity.addPart("mylmage", new ByteArrayBody(data, "temp.jpg"));
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
            String urlstr = "";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(resstr);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";
                urlstr = map.get("url") + "";
            }

            // 返回图片url
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

            photo.compress(CompressFormat.JPEG, 75, iStream);// (0-100)压缩文件
            // bos.flush();
            // bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        // return true;
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
