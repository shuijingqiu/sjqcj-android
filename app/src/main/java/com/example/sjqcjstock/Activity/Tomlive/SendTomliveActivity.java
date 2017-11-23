package com.example.sjqcjstock.Activity.Tomlive;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.ImageUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.richeditor.RichTextEditor;
import com.example.sjqcjstock.view.CustomProgress;
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
import java.util.List;
import java.util.UUID;

/**
 * 发送直播消息的页面
 * Created by Administrator on 2017/1/12.
 */
public class SendTomliveActivity extends Activity{

    // 照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; // 照相获取
    // 网络请求提示
    private CustomProgress dialog;
    // 输入框
    private RichTextEditor editor;
    // 选择手机图片
    private ImageView getimgtoalbum1;
    // 选择照相图片
    private ImageView getimgtotakephoto1;
    // 是否发送推送的选项
    private CheckBox checkBoxProtocol;
    // 房间ID
    private String roomId;
    // 发送消息返回的数据
    private String resstr;
    // 上传的图片路径
    private String imgurl;
    private Uri uri;
    private Bitmap photo;
    private Bitmap bmpCompressed;
    // 是否发送直播消息 1为发送
    private String push = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_send_tomlive);
        ExitApplication.getInstance().addActivity(this);
        findView();
        // 强制开启软键盘
        InputMethodManager imm = (InputMethodManager) editor.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 控件的绑定
     */
    private void findView() {
        dialog = new CustomProgress(this);
        roomId = getIntent().getStringExtra("roomId");
        // 是否收费
        String type = getIntent().getStringExtra("type");
        /**
         * 返回按钮的事件绑定
         */
        findViewById(R.id.goback1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        checkBoxProtocol = (CheckBox) findViewById(R.id.check_box_protocol);
        editor = (RichTextEditor) findViewById(R.id.richEditor);
        // 往RichTextEditor里传入当前Activity的参数
//        editor.sendActivity();
        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.firstclickgetfource();
            }
        });

        getimgtoalbum1 = (ImageView) findViewById(R.id.getimgtoalbum1);
        getimgtoalbum1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从相册获取图片
                getImageFromAlbum();
            }
        });
        getimgtotakephoto1 = (ImageView) findViewById(R.id.getimgtotakephoto1);
        getimgtotakephoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从拍照获取图片
                getImageFromCamera();
            }
        });
        if (type == null || "1".equals(type)){
            checkBoxProtocol.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 打开相册
     */
    protected void getImageFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");// 相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 打开相机
     */
    protected void getImageFromCamera() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
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


    /**
     * 调用相机或者调用相册的回调方法
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
                        double proportion= ImageUtil.getProportion(bitmap.getWidth(),bitmap.getHeight());
                        bmpCompressed = Bitmap.createScaledBitmap(bitmap,
                                (int)(bitmap.getWidth()*proportion), (int)(bitmap.getHeight()*proportion), true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 上传相册图片
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (bmpCompressed !=null){
                                    // 将长微博图片上传的服务器
                                    imgurl = sendData(bmpCompressed);
                                    handler.sendEmptyMessage(2);
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
                                imgurl =  sendData(bmpCompressed);
                                handler.sendEmptyMessage(1);
                            } catch (Exception e) {
                            }
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            if (data != null) {
//                uri = data.getData();
//                if (uri == null) {
//                    // use bundle to get data
//                    Bundle bundle = data.getExtras();
//                    if (bundle != null) {
//                        photo = (Bitmap) bundle.get("data"); // get
//                        double proportion= ImageUtil.getProportion(photo.getWidth(),photo.getHeight());
//                        bmpCompressed = Bitmap.createScaledBitmap(photo,
//                                (int)(photo.getWidth()*proportion), (int) (photo.getHeight()*proportion), true);
//                        // 上传相机图片
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    imgurl =  sendData(bmpCompressed);
//                                    handler.sendEmptyMessage(1);
//                                } catch (Exception e) {
//                                }
//                            }
//                        }).start();
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), "err****",
//                                Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//            }
        }
    }

    // 将图片上传到服务器
    public String sendData(Bitmap bitmap) throws Exception {

//            HttpClient httpClient = new DefaultHttpClient();
//            HttpContext localContext = new BasicHttpContext();
//            long sjc = System.currentTimeMillis();
//            int numcode = (int) ((Math.random() * 9 + 1) * 100000);
//            HttpPost httpPost = new HttpPost(
//                    Constants.Url + "?app=public&mod=AppFeedList&act=saveEditorImg&dir=image&sjc="
//                            + "1" + sjc + numcode);
//            MultipartEntity entity = new MultipartEntity(
//                    HttpMultipartMode.BROWSER_COMPATIBLE);
//            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
//            entity.addPart("login_password", new StringBody(
//                    Constants.staticpasswordstr));
//            entity.addPart("tokey", new StringBody(Constants.statictokeystr));
//            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(),
//                    true);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bmpCompressed.compress(Bitmap.CompressFormat.JPEG, 100, bos);
//            byte[] data = bos.toByteArray();
//            entity.addPart("mylmage", new ByteArrayBody(data, "temp.jpg"));
//            httpPost.setEntity(entity);
//
//            HttpResponse response = httpClient.execute(httpPost, localContext);
//            InputStream in = response.getEntity().getContent();
//            String resstr = HttpUtil.changeInputStream(in);
//
//            resstr = resstr.replace("\n ", "");
//            resstr = resstr.replace("\n", "");
//            resstr = resstr.replace(" ", "");
//            resstr = "[" + resstr + "]";
//            String urlstr = "";
//            // 解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(resstr);
//            for (Map<String, Object> map : lists) {
//                urlstr = map.get("url") + "";
//            }
//            if (!"".equals(urlstr)&&urlstr.indexOf("https")==-1){
//                urlstr = urlstr.replace("http","https");
//            }
//            // 返回图片url
//            return urlstr;

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(
                    Constants.newUrl + "/api/upload/longPost");
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("step", new StringBody("upload"));
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
            // 返回图片url
            return dataObject.getString("url");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 保存图片
     * @param photo
     * @param spath
     */
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
     * 发送消息
     */
    public void sendMessage(View view){
        if (Utils.isFastDoubleClick4()){
            return;
        }
        dialog.showDialog();
        if (((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
            push = "1";
        }else{
            push = "0";
        }
        List<RichTextEditor.EditData> editList = editor.buildEditData();
        StringBuilder longweibobody = dealEditData(editList);
        final String content = longweibobody.toString();
        // 发送聊天的消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                List dataList = new ArrayList();
                // 用户ID
                dataList.add(new BasicNameValuePair("uid", Constants.staticmyuidstr));
                dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                dataList.add(new BasicNameValuePair("room_id", roomId));
                dataList.add(new BasicNameValuePair("content", content));
                dataList.add(new BasicNameValuePair("type", "1"));
                dataList.add(new BasicNameValuePair("push", push));
                resstr = HttpUtil.restHttpPost(Constants.moUrl+"/live/index/send",dataList);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected StringBuilder dealEditData(List<RichTextEditor.EditData> editList) {
        StringBuilder longweibobody = new StringBuilder();
        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.getInputStr() != null) {
                if (!"".equals(longweibobody.toString())){
                    longweibobody.append("<br/>");
                }
                String str2 = itemData.getInputStr();
                longweibobody.append(str2);
            } else if (itemData.getImagePath() != null) {
                if (!"".equals(longweibobody.toString())){
                    longweibobody.append("<br/>");
                }
                String str = itemData.getImagePath();
                longweibobody.append("<img src=\"");
                longweibobody.append(str);
                longweibobody.append("\" />");
            }
        }
        return longweibobody;
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
                        JSONObject jsonObject = new JSONObject(resstr);
                        Toast.makeText(SendTomliveActivity.this,jsonObject.getString("data"),Toast.LENGTH_SHORT);
                        if ("failed".equals(jsonObject.getString("status"))){
                            return;
                        }
                        Constants.isTomlive = true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    finish();
                    break;
                case 1:
                    insertBitmap(getRealFilePath(uri), imgurl,
                            bmpCompressed);
                    String uuid = UUID.randomUUID() + "";
                    String envstr = Environment
                            .getExternalStorageDirectory()
                            + "/sglrBitmap/"
                            + uuid + ".jpg";
                    saveImage(photo, envstr);
                    break;
                case 2:
                    insertBitmap(getRealFilePath(uri), imgurl,
                            bmpCompressed);
                    break;
            }
        }
    };
}
