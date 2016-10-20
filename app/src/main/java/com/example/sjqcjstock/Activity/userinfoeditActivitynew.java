package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.edituser.edituserinfouserintroActivity;
import com.example.sjqcjstock.Activity.edituser.edituserinfousernameActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class userinfoeditActivitynew extends Activity {

    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取

    // 获取修改的昵称信息
    private static final int REQUEST_CODE_EDITUUSERNAME = 5;
    private static final int REQUEST_CODE_EDITUUSERINTRO = 6;

    // 异步修改用户的信息
    AsyncTask<TaskParams, Void, String> edituserinfotask1 = new SendInfoTaskedituserinfo();
    // AsyncTask<TaskParams, Void, String> edituserinfotask2=new
    // SendInfoTaskedituserinfo();
    // AsyncTask<TaskParams, Void, String> edituserinfotask3=new
    // SendInfoTaskedituserinfo();
    // AsyncTask<TaskParams, Void, String> edituserinfotask4=new
    // SendInfoTaskedituserinfo();

    // 获取控件
    private LinearLayout pickselectsex1;
    private LinearLayout goback1;
    private RelativeLayout selectsexs1;
    private LinearLayout pinkcancelselectsex1;
    private RelativeLayout selectmansex1;
    private RelativeLayout selectwomansex1;
    private TextView sexcontent1;
    private LinearLayout pinkconfirmselectsex1;
    private LinearLayout pickselectheadimg1;
    private RelativeLayout selectheadimg1;
    private RelativeLayout cancelselectheadimg1;

    private RelativeLayout takephotofromalbum1;
    private RelativeLayout takephotofromcameia1;

    private ImageView imgportrait;
    private TextView editname1;
    private TextView userinfo1;

    private LinearLayout pickusername1;
    private LinearLayout pickpersonalintro1;

    // 判断选择性别的属性,默认为男性
    private String selectedsexstr = "1";
    // 从intent获取的用户信息
    String unamestr2;
    String introstr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.userinfoeditnew);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {

        // 获取intent数据
        Intent intent = getIntent();
        unamestr2 = intent.getStringExtra("unamestr");
        String sexstr2 = intent.getStringExtra("sexstr");
        introstr2 = intent.getStringExtra("introstr");
        String avatar_middlestr2 = intent.getStringExtra("avatar_middlestr");

        pickselectsex1 = (LinearLayout) findViewById(R.id.pickselectsex1);
        selectsexs1 = (RelativeLayout) findViewById(R.id.selectsexs1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        pinkcancelselectsex1 = (LinearLayout) findViewById(R.id.pinkcancelselectsex1);
        selectmansex1 = (RelativeLayout) findViewById(R.id.selectmansex1);
        selectwomansex1 = (RelativeLayout) findViewById(R.id.selectwomansex1);
        sexcontent1 = (TextView) findViewById(R.id.sexcontent1);
        pinkconfirmselectsex1 = (LinearLayout) findViewById(R.id.pinkconfirmselectsex1);
        pickselectheadimg1 = (LinearLayout) findViewById(R.id.pickselectheadimg1);
        selectheadimg1 = (RelativeLayout) findViewById(R.id.selectheadimg1);
        cancelselectheadimg1 = (RelativeLayout) findViewById(R.id.cancelselectheadimg1);

        pickusername1 = (LinearLayout) findViewById(R.id.pickusername1);
        takephotofromalbum1 = (RelativeLayout) findViewById(R.id.takephotofromalbum1);
        takephotofromcameia1 = (RelativeLayout) findViewById(R.id.takephotofromcameia1);
        imgportrait = (ImageView) findViewById(R.id.imgportrait);
        editname1 = (TextView) findViewById(R.id.editname1);
        userinfo1 = (TextView) findViewById(R.id.userinfo1);
        pickpersonalintro1 = (LinearLayout) findViewById(R.id.pickpersonalintro1);

        // 初始化昵称
        editname1.setText(unamestr2);

        // 初始化个人简介
        userinfo1.setText(introstr2);
        // 初始化性别
        // 改变默认选项
        // 性别选择框动效设置
        if ("1".equals(sexstr2)) {

            selectedsexstr = "1";
            sexcontent1.setText("男");
        } else if ("2".equals(sexstr2)) {
            selectedsexstr = "2";
            sexcontent1.setText("女");

        }

        if ("1".equals(selectedsexstr)) {
            selectmansex1.setBackgroundResource(R.color.color_dddddd);
        } else if ("2".equals(selectedsexstr)) {
            selectwomansex1.setBackgroundResource(R.color.color_dddddd);
        }

        ImageLoader.getInstance().displayImage(avatar_middlestr2,
                imgportrait);

        pickselectsex1.setOnClickListener(new pickselectsex1_listener());
        goback1.setOnClickListener(new goback1_listener());
        pinkcancelselectsex1
                .setOnClickListener(new pinkcancelselectsex1_listener());
        selectmansex1.setOnClickListener(new selectmansex1_listener());
        selectwomansex1.setOnClickListener(new selectwomansex1_listener());
        pinkconfirmselectsex1
                .setOnClickListener(new pinkconfirmselectsex1_listener());
        pickselectheadimg1
                .setOnClickListener(new pickselectheadimg1_listener());
        cancelselectheadimg1
                .setOnClickListener(new cancelselectheadimg1_listener());
        takephotofromalbum1
                .setOnClickListener(new takephotofromalbum1_listener());
        takephotofromcameia1
                .setOnClickListener(new takephotofromcameia1_listener());
        pickusername1.setOnClickListener(new pickusername1_listener());
        pickpersonalintro1
                .setOnClickListener(new pickpersonalintro1_listener());

        //
        // if("男".equals(sexcontent1.getText().toString())){
        // selectedsexstr="1";
        //
        // }else if("女".equals(sexcontent1.getText().toString())){
        // selectedsexstr="0";
        //
        // }

    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }

    // 进入修改用户简介页面
    class pickpersonalintro1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(userinfoeditActivitynew.this, edituserinfouserintroActivity.class);
            intent.putExtra("introstr", userinfo1.getText().toString());
            startActivityForResult(intent, REQUEST_CODE_EDITUUSERINTRO);
        }
    }

    // 进入修改昵称页面
    class pickusername1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(userinfoeditActivitynew.this, edituserinfousernameActivity.class);
            intent.putExtra("unamestr", editname1.getText().toString());
            startActivityForResult(intent, REQUEST_CODE_EDITUUSERNAME);
        }

    }

    // 修改头像
    class pickselectheadimg1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            selectheadimg1.setVisibility(View.VISIBLE);
            selectheadimg1.getBackground().setAlpha(150);
        }

    }

    class cancelselectheadimg1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            selectheadimg1.setVisibility(View.GONE);
        }

    }

    class takephotofromalbum1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 从相册获取图片
            getImageFromAlbum();
        }

    }

    class takephotofromcameia1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            // 从相机获取图片
            getImageFromCamera();
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

    // 上传头像返回的值
    private String resultImg = "";
    // 上传的头像Bitmap
    private Bitmap bmp;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                String fullpicurlstr = "";
                String picheightstr = "";
                String picurlstr = "";
                String picwidthstr = "";

                /** 解析返回的数据再次访问网络保存图片 */
                resultImg = resultImg.replace("\n ", "");
                resultImg = resultImg.replace("\n", "");
                resultImg = resultImg.replace(" ", "");
                resultImg = "[" + resultImg + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools
                        .listKeyMaps(resultImg);
                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("status") + "";
                    String datastr = map.get("data") + "";
                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");

                    for (Map<String, Object> datastrmap : datastrlists) {
                        fullpicurlstr = datastrmap.get("fullpicurl")
                                + "";
                        picheightstr = datastrmap.get("picheight")
                                + "";
                        picurlstr = datastrmap.get("picurl") + "";
                        picwidthstr = datastrmap.get("picwidth")
                                + "";

                    }
                }
                new SendInfoTasksaveheadimg()
                        .execute(new TaskParams(
                                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveAvatar&step=save",
                                        new String[]{"mid",
                                                Constants.staticmyuidstr},
                                        new String[]{"login_password",
                                                Constants.staticpasswordstr},
                                        new String[]{"tokey",
                                                Constants.statictokeystr},

                                        new String[]{"picurl", picurlstr},
                                        new String[]{"picwidth", picwidthstr},
                                        new String[]{"fullpicurl",
                                                fullpicurlstr}, new String[]{
                                        "x1", "0"}, new String[]{
                                        "y1", "0"}, new String[]{
                                        "x2", "0"}, new String[]{
                                        "y2", "0"}, new String[]{
                                        "w", picwidthstr},
                                        new String[]{"h", picheightstr}

                                        // picurl //图片路径
                                        // picwidth // 图片宽度
                                        // fullpicurl //图片绝路径
                                        // x1 // 选择区域左上角x轴坐标
                                        // y1 // 选择区域左上角y轴坐标
                                        // x2 // 选择区域右下角x轴坐标
                                        // y2 // 选择区域右下角y轴坐标
                                        // w // 选择区的宽度
                                        // h // 选择区的高度

                                )

                        );
            } else if (msg.what == 2) {
                String fullpicurlstr = "";
                String picheightstr = "";
                String picurlstr = "";
                String picwidthstr = "";

                /** 解析返回的数据再次访问网络保存图片 */
                resultImg = resultImg.replace("\n ", "");
                resultImg = resultImg.replace("\n", "");
                resultImg = resultImg.replace(" ", "");
                resultImg = "[" + resultImg + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools
                        .listKeyMaps(resultImg);
                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("status") + "";
                    String datastr = map.get("data") + "";
                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");

                    for (Map<String, Object> datastrmap : datastrlists) {
                        fullpicurlstr = datastrmap
                                .get("fullpicurl") + "";
                        picheightstr = datastrmap.get("picheight")
                                + "";
                        picurlstr = datastrmap.get("picurl")
                                + "";
                        picwidthstr = datastrmap.get("picwidth")
                                + "";

                    }

                }

                new SendInfoTasksaveheadimg()
                        .execute(new TaskParams(
                                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveAvatar&step=save",
                                        new String[]{"mid",
                                                Constants.staticmyuidstr},
                                        new String[]{"login_password",
                                                Constants.staticpasswordstr},
                                        new String[]{"tokey",
                                                Constants.statictokeystr},

                                        new String[]{"picurl", picurlstr},
                                        new String[]{"picwidth",
                                                picwidthstr},
                                        new String[]{"fullpicurl",
                                                fullpicurlstr},
                                        new String[]{"x1", "0"},
                                        new String[]{"y1", "0"},
                                        new String[]{"x2", "0"},
                                        new String[]{"y2", "0"},
                                        new String[]{"w", picwidthstr},
                                        new String[]{"h", picheightstr}

                                        // picurl //图片路径
                                        // picwidth // 图片宽度
                                        // fullpicurl //图片绝路径
                                        // x1 // 选择区域左上角x轴坐标
                                        // y1 // 选择区域左上角y轴坐标
                                        // x2 // 选择区域右下角x轴坐标
                                        // y2 // 选择区域右下角y轴坐标
                                        // w // 选择区的宽度
                                        // h // 选择区的高度

                                )

                        );
            }

        }
    };

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
                    try {
                        bmp = MediaStore.Images.Media.getBitmap(resolver, uri);
                        imgportrait.setImageBitmap(bmp);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    resultImg = sendData(bmp);
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

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
            selectheadimg1.setVisibility(View.GONE);

            // finish();

        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri == null) {
                    // use bundle to get data
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        final Bitmap photo = (Bitmap) bundle.get("data"); // get
                        // bitmap
                        imgportrait.setImageBitmap(photo);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    resultImg = sendData(photo);
                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


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

            // finish();

            selectheadimg1.setVisibility(View.GONE);

            // 修改用户昵称
        } else if (resultCode == REQUEST_CODE_EDITUUSERNAME) {
            Bundle bundle = data.getExtras();
            String Name = bundle.getString("unamestr");
            // myTextView.setText("恭喜您，注册成功。您的用户名是："+Name);
            // System.out.println(Name);

            String username3 = editname1.getText() + "";
            String userinfo3 = userinfo1.getText() + "";

            //向服务器提交更新昵称的请求
            if (Constants.staticLoginType.equals("qq") || Constants.staticLoginType.equals("weixin")) {
                //三方用户登录
                new SendInfoTaskedituserinfo().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveProfile",
                        new String[]{"mid", Constants.staticmyuidstr},
                        //new String[] { "login_password",Constants.staticpasswordstr },
                        new String[]{"tokey", Constants.statictokeystr},
                        new String[]{"sex", selectedsexstr},
                        new String[]{"intro", userinfo3},
                        new String[]{"old_name", unamestr2},
                        new String[]{"uname", Name},
                        new String[]{"type", Constants.staticLoginType}
                ));
            } else {
                //普通用户登录
                new SendInfoTaskedituserinfo().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveProfile",
                        new String[]{"mid", Constants.staticmyuidstr},
                        new String[]{"login_password", Constants.staticpasswordstr},
                        new String[]{"tokey", Constants.statictokeystr},
                        new String[]{"sex", selectedsexstr},
                        new String[]{"intro", userinfo3},
                        new String[]{"old_name", unamestr2},
                        new String[]{"uname", Name}
                ));
            }

            // 网络请求修改昵称
            editname1.setText(Name);

            //用户简介
        } else if (resultCode == REQUEST_CODE_EDITUUSERINTRO) {
            Bundle bundle = data.getExtras();
            String intro = bundle.getString("introstr");
            // myTextView.setText("恭喜您，注册成功。您的用户名是："+Name);
            // System.out.println(Name);
            userinfo1.setText(intro);
            String username3 = editname1.getText() + "";
            String userinfo3 = userinfo1.getText() + "";

            //向服务器提交更新昵称的请求
            if (Constants.staticLoginType.equals("qq") || Constants.staticLoginType.equals("weixin")) {
                //三方用户登录
                new SendInfoTaskedituserinfo().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveProfile",
                        new String[]{"mid", Constants.staticmyuidstr},
                        //new String[] { "login_password",Constants.staticpasswordstr },
                        new String[]{"tokey", Constants.statictokeystr},
                        new String[]{"sex", selectedsexstr},
                        new String[]{"intro", userinfo3},
                        new String[]{"old_name", unamestr2},
                        new String[]{"uname", username3},
                        new String[]{"type", Constants.staticLoginType}
                ));
            } else {
                //普通用户登录
                new SendInfoTaskedituserinfo().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveProfile",
                        new String[]{"mid", Constants.staticmyuidstr},
                        new String[]{"login_password", Constants.staticpasswordstr},
                        new String[]{"tokey", Constants.statictokeystr},
                        new String[]{"sex", selectedsexstr},
                        new String[]{"intro", userinfo3},
                        new String[]{"old_name", unamestr2},
                        new String[]{"uname", username3}
                ));
            }
        }

    }

    public void refresh() {
        onCreate(null);
    }

    // 修改性别
    class pinkconfirmselectsex1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            selectsexs1.setVisibility(View.GONE);

            if ("1".equals(selectedsexstr)) {
                sexcontent1.setText("男");
            } else if ("2".equals(selectedsexstr)) {
                sexcontent1.setText("女");
            }


            String username3 = editname1.getText() + "";
            String userinfo3 = userinfo1.getText() + "";

            //修改用户性别
            if (Constants.staticLoginType.equals("qq") || Constants.staticLoginType.equals("weixin")) {
                //三方用户登录
                new SendInfoTaskedituserinfo().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveProfile",
                        new String[]{"mid", Constants.staticmyuidstr},
                        //new String[] { "login_password",Constants.staticpasswordstr },
                        new String[]{"tokey", Constants.statictokeystr},
                        new String[]{"sex", selectedsexstr},
                        new String[]{"intro", userinfo3},
                        new String[]{"old_name", unamestr2},
                        new String[]{"uname", username3},
                        new String[]{"type", Constants.staticLoginType}
                ));
            } else {
                //普通用户登录
                new SendInfoTaskedituserinfo().execute(new TaskParams(
                        Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveProfile",
                        new String[]{"mid", Constants.staticmyuidstr},
                        new String[]{"login_password", Constants.staticpasswordstr},
                        new String[]{"tokey", Constants.statictokeystr},
                        new String[]{"sex", selectedsexstr},
                        new String[]{"intro", userinfo3},
                        new String[]{"old_name", unamestr2},
                        new String[]{"uname", username3}
                ));
            }

            // try {
            // refresh();
            //
            // } catch (Exception e) {
            // // TODO: handle exception
            // }finally{
            //
            // }

        }

    }

    class selectmansex1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            selectedsexstr = "1";

            selectmansex1.setBackgroundResource(R.color.color_dddddd);
            selectwomansex1.setBackgroundResource(R.color.color_ffffff);

        }

    }

    class selectwomansex1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            selectedsexstr = "2";

            selectwomansex1.setBackgroundResource(R.color.color_dddddd);
            selectmansex1.setBackgroundResource(R.color.color_ffffff);
        }

    }

    class pinkcancelselectsex1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            selectsexs1.setVisibility(View.GONE);

        }

    }

    class pickselectsex1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            selectsexs1.setVisibility(View.VISIBLE);
            // 设置背景透明
            selectsexs1.getBackground().setAlpha(150);

            // 根据当前用户的性别决定下发性别选择框是否为灰色
            if ("男".equals(sexcontent1.getText().toString())) {
                selectmansex1.setBackgroundResource(R.color.color_dddddd);
                selectwomansex1.setBackgroundResource(R.color.color_ffffff);
            } else if ("女".equals(sexcontent1.getText().toString())) {
                selectmansex1.setBackgroundResource(R.color.color_ffffff);
                selectwomansex1.setBackgroundResource(R.color.color_dddddd);
            }
        }

    }

    private class SendInfoTasksaveheadimg extends
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
                String middlestr = "";
                String datastr = map.get("data") + "";
                List<Map<String, Object>> datastrlists = JsonTools
                        .listKeyMaps("[" + datastr + "]");
                System.out.print(datastr);
                for (Map<String, Object> datastrmap : datastrlists) {
                    middlestr = datastrmap.get("middle") + "";
                }
                ImageLoader.getInstance().displayImage(middlestr,
                        imgportrait);
//                // 头像信息展示
//                Drawable drawable;
//
//                URL url;
//                try {
//                    url = new URL(middlestr);
//                    drawable = Drawable.createFromStream(url.openStream(),
//                            "src");
//                    imgportrait.setImageDrawable(drawable);
//
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
            }

        }

    }

    private class SendInfoTaskedituserinfo extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
            // old_name=dfsfsdfsd&uname=dsfdf
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            // 解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String statusstr = map.get("status") + "";

                if ("true".equals(statusstr)) {
                    Constants.setStaticuname(editname1.getText().toString());
                    CustomToast.makeText(userinfoeditActivitynew.this, "更新成功", Toast.LENGTH_LONG).show();
                } else {
                    CustomToast.makeText(userinfoeditActivitynew.this, "更新失败", Toast.LENGTH_LONG).show();
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


            String url = "?app=public&mod=AppFeedList&act=AppSaveAvatar&step=upload";
            long sjc = System.currentTimeMillis();
            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
            url = url + "&sjc=1" + sjc + numcode;
            HttpPost httpPost = new HttpPost(
                    Constants.Url + url);
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("login_password", new StringBody(
                    Constants.staticpasswordstr));
            entity.addPart("tokey", new StringBody(Constants.statictokeystr));

            // Bitmap bmpCompressed=Bitmap.createScaledBitmap(bitmap, 640, 480,
            // true);
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 500, 500,
                    true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            // bmpCompressed.compress(CompressFormat.JPEG,100, bos);
            bmpCompressed.compress(CompressFormat.PNG, 100, bos);


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
            return resstr;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

}
