package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.entity.ChatMessageEntity;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;
import com.example.sjqcjstock.view.PullToRefreshLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 在线客服页面（私信页面也集合进来了）
 * Created by Administrator on 2017/3/16.
 */
public class OnlineServiceActivity extends Activity {

    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取
    // 上下拉刷新控件
    private PullToRefreshLayout ptrl;
    // 获取控件
    private EditText et_sendmessage;
    private Button btn_send;
    private RelativeLayout rl_bottom;
    private RelativeLayout personalDataRl;
    private TextView titleTv;
    // 图片选择框
    private LinearLayout imgShowRl;
    // 获取列表数据
    private ListView chatlistview;
    private ImageView headImg;
    private TextView nameTv;
    private TextView introTv;
    private com.example.sjqcjstock.adapter.personalnewsdetailAdapter personalnewsdetailAdapter;
    private ArrayList<ChatMessageEntity> chatMessageEntityList;
    // 从intent 获取list_id数据
    private String list_idstr;
    private String uidstr;
    // 上传的图片路径
    private String imgurl;
    private Uri uri;
    private Bitmap photo;
    private Bitmap bmpCompressed;
    // 请求的路径
    private String url = "";
    // 最大数的id
    private String maxId;
    // 判断是刷新还是加载 1：刷新 2：加载
    private String type = "1";
    // 接口返回数据
    private String jsonStr;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_online_service);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
        getData();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        dialog.showDialog();
        imgShowRl = (LinearLayout) findViewById(R.id.img_show_rl);
        imgShowRl.getBackground().setAlpha(150);
        imgShowRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击自身空白部分就消失
                imgShowRl.setVisibility(View.GONE);
            }
        });
        et_sendmessage = (EditText) findViewById(R.id.et_sendmessage);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        personalDataRl = (RelativeLayout) findViewById(R.id.personal_data_rl);
        btn_send = (Button) findViewById(R.id.btn_send);
        headImg = (ImageView) findViewById(R.id.head_img);
        nameTv = (TextView) findViewById(R.id.name_tv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        introTv = (TextView) findViewById(R.id.intro_tv);

        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_sendmessage.setOnClickListener(new et_sendmessage_listener());
        btn_send.setOnClickListener(new btn_send_listener());

        // 聊天内容列表
        chatlistview = (ListView) findViewById(R.id.chatlistview);
        // 存储数据的数组列表
        chatMessageEntityList = new ArrayList<ChatMessageEntity>();
        // 为ListView 添加适配器
        personalnewsdetailAdapter = new com.example.sjqcjstock.adapter.personalnewsdetailAdapter(
                OnlineServiceActivity.this);
        chatlistview.setAdapter(personalnewsdetailAdapter);
        // listView 的点击事件，隐藏键盘
        chatlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);
            }
        });
        ptrl = ((PullToRefreshLayout) findViewById(
                R.id.refresh_view));
        // 添加上下拉刷新事件
        ptrl.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            // 下拉加载
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                // 为0代表没有最新聊天记录了
//                if (!"0".equals(maxId)) {
                    type = "2";
                    getData();
//                } else {
//                    // 千万别忘了告诉控件刷新完毕了哦！
//                    ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
//                }
            }
            // 上拉刷新
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                chatMessageEntityList.clear();
//                maxId = null;
                type = "1";
                getData();
            }
        });
        // 获取私信列表传递过来的数据，list_idstr是根据全局的用户id通过异步任务获取
        Intent intent = getIntent();
        // 私信列表传递过来的数据
        list_idstr = intent.getStringExtra("list_id");
        String unamestr = intent.getStringExtra("unamestr");
        uidstr = intent.getStringExtra("uidstr");
        titleTv.setText(unamestr);
        if (null != uidstr && !"".equals(uidstr)) {
            personalDataRl.setVisibility(View.GONE);
        }
    }

    private void getData() {
        url = Constants.newUrl + "/api/message/detail?mid=" + Constants.staticmyuidstr+"&token="+Constants.apptoken;
//        if (isRn) {
////            url = Constants.Url + "?app=public&mod=AppFeedList&act=onlineService&mid=" + Constants.staticmyuidstr+"&service=1";
//        } else {
////            url = Constants.Url + "?app=public&mod=AppFeedList&act=onlineService&mid=" + Constants.staticmyuidstr + "&id=" + list_idstr+"&uid="+uidstr;
//        }
        if (null != uidstr && !"".equals(uidstr)) {
            url += "&uid=" + uidstr +"&list_id="+list_idstr;
        }
        if (null != list_idstr && !"".equals(list_idstr)) {
            url += "&list_id="+list_idstr;
        }

//        if (!"1".equals(type)) {
//            url = url +"&list_id="+list_idstr;
//        }

        if ("2".equals(type) && maxId != null && !"0".equals(maxId)) {
            url += "&max_id=" + maxId;
        }

        /**
         * 进入聊天界面，需要请求的数据 参数： 全局用户id， list_id进行请求
         */
//        new SendInfoTask().execute(new TaskParams(url));

        // 获取私信聊天列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpGet(url);
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    class btn_send_listener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            SubmitData(et_sendmessage.getText().toString());
        }
    }

    /**
     * 提交私信
     *
     * @param content
     */
    private void SubmitData(String content) {
        if ("".equals(content.trim())) {
            Toast.makeText(this, "请输入私信内容", Toast.LENGTH_SHORT).show();
            return;
        }
        /**
         * 发送私信 参数： 列表id 用户id 密码 tokey 内容 目标用户id
         */
//        if (list_idstr == null || list_idstr.equals("")) {
//            // 通过异步任务获取list_idstr
//            // list_id为空的时候，调用另外的接口发送数据
//            new SendInfoTaskaddpersonalletter()
//                    .execute(new TaskParams(
//                            Constants.Url + "?app=public&mod=AppFeedList&act=AppdoPost",
//                            new String[]{"mid", Constants.staticmyuidstr},
//                            new String[]{"login_password", Constants.staticpasswordstr},
//                            new String[]{"content", content},
//                            new String[]{"to", uidstr}));
//        } else {
//            new SendInfoTaskreplypersonalletter().execute(new TaskParams(
//                    Constants.Url + "?app=public&mod=AppFeedList&act=AppdoPost",
//                    new String[]{"id", list_idstr},
//                    new String[]{"mid", Constants.staticmyuidstr},
//                    new String[]{"login_password", Constants.staticpasswordstr},
//                    new String[]{"tokey", Constants.statictokeystr},
//                    new String[]{"content", content},
//                    new String[]{"to", uidstr}));
//        }

        final List dataList = new ArrayList();
        // 微博id
        dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
        dataList.add(new BasicNameValuePair("token", Constants.apptoken));
        // 发送对象的id
        dataList.add(new BasicNameValuePair("uid", uidstr));
        dataList.add(new BasicNameValuePair("content", content));
        if (list_idstr != null && !"".equals(list_idstr)) {
            // 通过异步任务获取list_idstr
            dataList.add(new BasicNameValuePair("list_id", list_idstr));
        }
//        // 上传的附件id
//        dataList.add(new BasicNameValuePair("attach_ids", attach_ids));

        // 发送私信
        new Thread(new Runnable() {
            @Override
            public void run() {
                jsonStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/message/put", dataList);
                handler.sendEmptyMessage(1);
            }
        }).start();
    }

    class et_sendmessage_listener implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                // 隐藏软键盘
                // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                // listView自动滚动到底部
                // CustomToast.makeText(getApplicationContext(), "键盘已弹出",1).show();
                chatlistview.setSelection(chatlistview.getBottom());
            }
            et_sendmessage.setFocusableInTouchMode(true);
            // listView自动滚动到底部
            chatlistview.setSelection(chatlistview.getBottom());
        }
    }

    // 判断控件的点击区域是否在本身，用户隐藏键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isineditarea(rl_bottom, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rl_bottom.getWindowToken(), 0);
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
        if (v != null && (v instanceof RelativeLayout)) {
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
     * 显示发送照片的功能
     *
     * @param view
     */
    public void showImageClick(View view) {
        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);
        imgShowRl.setVisibility(View.VISIBLE);
    }

    /**
     * 打开相机的功能
     *
     * @param view
     */
    public void PhotographClick(View view) {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 调用相机前先删除图片
            ImageUtil.scanFileAsync(OnlineServiceActivity.this,ImageUtil.getSDPath());
            Intent getImageByCamera = new Intent(
                    "android.media.action.IMAGE_CAPTURE");
            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ImageUtil.getSDPath())));
            startActivityForResult(getImageByCamera,
                    REQUEST_CODE_CAPTURE_CAMEIA);
        } else {
            CustomToast.makeText(getApplicationContext(), "请确认已经插入SD卡",
                    Toast.LENGTH_SHORT).show();
        }
        imgShowRl.setVisibility(View.GONE);
    }

    /**
     * 打开相册功能
     *
     * @param view
     */
    public void AlbumClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        imgShowRl.setVisibility(View.GONE);
    }

    /**
     * 关闭发送照片的功能
     *
     * @param view
     */
    public void CancelClick(View view) {
        imgShowRl.setVisibility(View.GONE);
    }

    /**
     * 关闭头像显示的功能
     *
     * @param view
     */
    public void CloseImgClick(View view) {
        personalDataRl.setVisibility(View.GONE);
    }

    /**
     * 调用相机或者调用相册的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 从相册获取图片
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (data != null) {
                uri = data.getData();
                if (uri != null) {
                    // Bitmap bmp=BitmapFactory.decodeFile(uri.toString());
                    // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                    final ContentResolver resolver = getContentResolver();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
                        double proportion = ImageUtil.getProportion(bitmap.getWidth(), bitmap.getHeight());
                        bmpCompressed = Bitmap.createScaledBitmap(bitmap,
                                (int) (bitmap.getWidth() * proportion), (int) (bitmap.getHeight() * proportion), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 上传相册图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (bmpCompressed != null) {
                                    // 将长微博图片上传的服务器
                                    imgurl = sendData(bmpCompressed);
                                    SubmitData(imgurl);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            try {
                final ContentResolver resolver = getContentResolver();
                File file = new File(ImageUtil.getSDPath());
                if (file.exists()) {
                    photo = MediaStore.Images.Media.getBitmap(resolver, Uri.fromFile(file));
                    double proportion = ImageUtil.getProportion(photo.getWidth(), photo.getHeight());
                    bmpCompressed = Bitmap.createScaledBitmap(photo,
                            (int) (photo.getWidth() * proportion), (int) (photo.getHeight() * proportion), true);
                    // 上传相机图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imgurl = sendData(bmpCompressed);
                                SubmitData(imgurl);
                            } catch (Exception e) {
                            }
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 将图片上传到服务器
    public String sendData(Bitmap bitmap) throws Exception {
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpContext localContext = new BasicHttpContext();
//        long sjc = System.currentTimeMillis();
//        int numcode = (int) ((Math.random() * 9 + 1) * 100000);
//        HttpPost httpPost = new HttpPost(
//                Constants.Url + "?app=public&mod=AppFeedList&act=saveEditorImg&dir=image&sjc="
//                        + "1" + sjc + numcode);
//        MultipartEntity entity = new MultipartEntity(
//                HttpMultipartMode.BROWSER_COMPATIBLE);
//        entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
//        entity.addPart("login_password", new StringBody(
//                Constants.staticpasswordstr));
//        entity.addPart("tokey", new StringBody(Constants.statictokeystr));
//        Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
//                true);
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//        byte[] data = bos.toByteArray();
//        entity.addPart("mylmage", new ByteArrayBody(data, "temp.jpg"));
//        httpPost.setEntity(entity);
//        HttpResponse response = httpClient.execute(httpPost, localContext);
//        InputStream in = response.getEntity().getContent();
//        String resstr = HttpUtil.changeInputStream(in);
//        resstr = "[" + resstr + "]";
//        String urlstr = "";
//        // 解析json字符串获得List<Map<String,Object>>
//        List<Map<String, Object>> lists = JsonTools.listKeyMaps(resstr);
//        for (Map<String, Object> map : lists) {
//            urlstr = map.get("url") + "";
//        }
//        if (!"".equals(urlstr) && urlstr.indexOf("https") == -1) {
//            urlstr = urlstr.replace("http", "https");
//        }
//        // 返回图片url
//        return urlstr;

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
        bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 100, bos);
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
        String urlstr = dataObject.getString("url");
        if (!"".equals(urlstr) && urlstr.indexOf("https") == -1) {
            urlstr = urlstr.replace("http", "https");
        }
        // 返回图片url
        return urlstr;
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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            CustomToast.makeText(OnlineServiceActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            // 千万别忘了告诉控件刷新完毕了哦！加载失败
                            ptrl.refreshFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        maxId = jsonObject.getString("max_id");
                        list_idstr = jsonObject.getString("list_id");
                        ArrayList<ChatMessageEntity> chatMessageEntities = (ArrayList<ChatMessageEntity>) JSON.parseArray(jsonObject.getString("data"),ChatMessageEntity.class);
//                        if (1== current){
//                            chatMessageEntityList = chatMessageEntities;
//                        }else{
//                            chatMessageEntityList.addAll(chatMessageEntities);
//                        }
//                        personalnewsdetailAdapter.setlistData(chatMessageEntityList);

                        // 从头部插入
                        chatMessageEntityList.addAll(0, chatMessageEntities);
                        personalnewsdetailAdapter.setlistData(chatMessageEntityList);

                        if (null == uidstr || "".equals(uidstr)) {
                        JSONObject userObject = new JSONObject(jsonObject.getString("service_user"));
                            if (userObject != null) {
                                uidstr = userObject.getString("uid");
                                nameTv.setText(userObject.getString("uname"));
                                introTv.setText(userObject.getString("intro"));
                                ImageLoader.getInstance().displayImage(userObject.getString("avatar_middle"),
                                        headImg, ImageUtil.getOption(), ImageUtil.getAnimateFirstDisplayListener());
                            }
                    }

                    if ("1".equals(type)) {
                        // listView自动滚动到底部
                        chatlistview.setSelection(chatlistview.getBottom());
                    } else {
                        if (chatMessageEntities != null && chatMessageEntities.size() > 0) {
                            chatlistview.setSelection(chatMessageEntities.size());
                        }
                    }
                        // 千万别忘了告诉控件刷新完毕了哦！
                        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(OnlineServiceActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        // 强制关闭软键盘
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_sendmessage.getWindowToken(), 0);
                        et_sendmessage.setText("");
                        chatMessageEntityList.clear();
                        type = "1";
                        getData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
