package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
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
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class userinfoeditActivity extends Activity {

    //照相状态
    private static final int REQUEST_CODE_PICK_IMAGE = 1; //相册获取
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 2; //照相获取


    //获取控件
    private RadioGroup radioGroup1;
    private RadioButton checkRadioButton;
    private LinearLayout editportrait1;
    private ImageView imgportrait;
    private ImageView updateportrait;
    private TextView isexist1;
    private ImageView edituserinfo1;

    private EditText editname1;
    private EditText userinfo1;
    private Button saveheadimg1;
    private LinearLayout goback1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.userinfoedit);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        //获取intent数据
        Intent intent = getIntent();
        String unamestr2 = intent.getStringExtra("unamestr");
        String sexstr2 = intent.getStringExtra("sexstr");
        String introstr2 = intent.getStringExtra("introstr");
        String avatar_middlestr2 = intent.getStringExtra("avatar_middlestr");

        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        editportrait1 = (LinearLayout) findViewById(R.id.editportrait1);
        imgportrait = (ImageView) findViewById(R.id.imgportrait);
        updateportrait = (ImageView) findViewById(R.id.updateportrait);
        editname1 = (EditText) findViewById(R.id.editname1);
        isexist1 = (TextView) findViewById(R.id.isexist1);
        edituserinfo1 = (ImageView) findViewById(R.id.edituserinfo1);
        userinfo1 = (EditText) findViewById(R.id.userinfo1);
        saveheadimg1 = (Button) findViewById(R.id.saveheadimg1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);


        editname1.setText(unamestr2);
        userinfo1.setText(introstr2);


        //改变默认选项

        if ("0".equals(sexstr2)) {

            radioGroup1.check(R.id.radiowoman);

        } else {

            radioGroup1.check(R.id.radioman);

        }
        //获取默认被选中值

        checkRadioButton = (RadioButton) radioGroup1.
                findViewById(radioGroup1.getCheckedRadioButtonId());

        CustomToast.makeText(this, "默认的选项的值是:" + checkRadioButton.getText(), Toast.LENGTH_LONG).show();


        //注册事件
        radioGroup1.setOnCheckedChangeListener(new radioGroup1_listener());
        imgportrait.setOnClickListener(new imgportrait_listener());
        updateportrait.setOnClickListener(new updateportrait_listener());
        goback1.setOnClickListener(new goback1_listener());


        //头像信息展示
        Drawable drawable;

        URL url;
        try {
            url = new URL(avatar_middlestr2);
            drawable = Drawable.createFromStream(url.openStream(), "src");
            imgportrait.setImageDrawable(drawable);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        edituserinfo1.setOnClickListener(new edituserinfo1_listener());

        editname1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                //if(arg1==false){
                //	CustomToast.makeText(getApplicationContext(), "事情edit焦点", 1)
                //	     .show();
                //}

                if (arg1) {//获得焦点

                } else {//失去焦点
                    new SendInfoTasktesting().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=IsUnameAvailable",
                                    //new String[] { "login_email", "1061550505@qq.com" },
                                    //new String[] { "login_password", "12345678" },
                                    new String[]{"old_name", "dfsfsdfsd"},
                                    new String[]{"uname", "dsffdgdfgdfgdfgdf"}


                            )

                    );
                }
            }
        });
    }

    /**
     * @Override public boolean dispatchTouchEvent(MotionEvent ev) {
     * // TODO Auto-generated method stub
     * <p/>
     * if (ev.getAction() == MotionEvent.ACTION_DOWN) {
     * View v = getCurrentFocus();
     * if (isineditarea(editname1, ev)&&!editname1.isFocused()) {
     * CustomToast.makeText(getApplicationContext(), "事情edit焦点", 1)
     * .show();
     * <p/>
     * new SendInfoTasktesting().execute(new TaskParams(Constants.Url+"?app=public&mod=Register&act=IsUnameAvailable",
     * //new String[] { "login_email", "1061550505@qq.com" },
     * //new String[] { "login_password", "12345678" },
     * new String[] { "old_name", "dfsfsdfsd"},
     * new String[] { "uname", "dsffdgdfgdfgdfgdf"}
     * <p/>
     * <p/>
     * )
     * <p/>
     * );
     * }
     * return super.dispatchTouchEvent(ev);
     * }
     * // 必不可少，否则所有的组件都不会有TouchEvent了
     * if (getWindow().superDispatchTouchEvent(ev)) {
     * return true;
     * }
     * return onTouchEvent(ev);
     * }
     * <p/>
     * <p/>
     * //判断是否离开edit区域的方法
     * public  boolean isineditarea(View v, MotionEvent event) {
     * if (v != null && (v instanceof EditText)) {
     * int[] leftTop = { 0, 0 };
     * //获取输入框当前的location位置
     * v.getLocationInWindow(leftTop);
     * int left = leftTop[0];
     * int top = leftTop[1];
     * int bottom = top + v.getHeight();
     * int right = left + v.getWidth();
     * if (event.getX() > left && event.getX() < right
     * && event.getY() > top && event.getY() < bottom) {
     * // 点击的是输入框区域，保留点击EditText的事件
     * return false;
     * } else {
     * return true;
     * }
     * }
     * return false;
     * }
     */

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }


    class edituserinfo1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            String sexstr3;
            if ("男".equals(checkRadioButton.getText().toString())) {
                sexstr3 = "1";
            } else {
                sexstr3 = "0";
            }
            String username3 = editname1.getText() + "";
            String userinfo3 = userinfo1.getText() + "";
            checkRadioButton.getText();
            new SendInfoTaskedituserinfo().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveProfile",
                            //new String[] { "login_email", "1061550505@qq.com" },
                            //new String[] { "login_password", "12345678" },
                            new String[]{"mid", Constants.staticmyuidstr},
                            new String[]{"login_password", Constants.staticpasswordstr},
                            new String[]{"sex", sexstr3},
                            new String[]{"intro", userinfo3},
                            new String[]{"old_name", "dfsfsdfsd"},
                            new String[]{"uname", username3}


                    )

            );
        }

    }

    class radioGroup1_listener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            // TODO Auto-generated method stub
            //点击事件获取的选择对象
            checkRadioButton = (RadioButton) radioGroup1.findViewById(checkedId);

            CustomToast.makeText(getApplicationContext(), "获取的ID是" +
                    checkRadioButton.getText(), 1).show();

        }

    }

    class imgportrait_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //从相册获取图片
            getImageFromAlbum();
        }

    }


    class updateportrait_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //从相机获取图片
            getImageFromCamera();
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


                        String fullpicurlstr = "";
                        String picheightstr = "";
                        String picurlstr = "";
                        String picwidthstr = "";


                        String result = sendData(bmp);
                        /**解析返回的数据再次访问网络保存图片*/
                        result = result.replace("\n ", "");
                        result = result.replace("\n", "");
                        result = result.replace(" ", "");
                        result = "[" + result + "]";
                        //解析json字符串获得List<Map<String,Object>>
                        List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                        for (Map<String, Object> map : lists) {
                            String statusstr = map.get("status") + "";
                            String datastr = map.get("data") + "";
                            List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");


                            for (Map<String, Object> datastrmap : datastrlists) {
                                fullpicurlstr = datastrmap.get("fullpicurl") + "";
                                picheightstr = datastrmap.get("picheight") + "";
                                picurlstr = datastrmap.get("picurl") + "";
                                picwidthstr = datastrmap.get("picwidth") + "";

                            }


                        }

                        new SendInfoTasksaveheadimg().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveAvatar&step=save",
                                        new String[]{"mid", Constants.staticmyuidstr},
                                        new String[]{"login_password", Constants.staticpasswordstr},
                                        new String[]{"picurl", picurlstr},
                                        new String[]{"picwidth", picwidthstr},
                                        new String[]{"fullpicurl", fullpicurlstr},
                                        new String[]{"x1", "0"},
                                        new String[]{"y1", "0"},
                                        new String[]{"x2", "0"},
                                        new String[]{"y2", "0"},
                                        new String[]{"w", picwidthstr},
                                        new String[]{"h", picheightstr}


//						  picurl           //图片路径
//						   picwidth        // 图片宽度
//						   fullpicurl       //图片绝路径
//						   x1             // 选择区域左上角x轴坐标
//						   y1             // 选择区域左上角y轴坐标
//						   x2            // 选择区域右下角x轴坐标
//						   y2              // 选择区域右下角y轴坐标
//						   w               // 选择区的宽度
//						   h               // 选择区的高度

                                )

                        );


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

                        try {
                            String fullpicurlstr = "";
                            String picheightstr = "";
                            String picurlstr = "";
                            String picwidthstr = "";


                            String result = sendData(photo);
                            /**解析返回的数据再次访问网络保存图片*/
                            result = result.replace("\n ", "");
                            result = result.replace("\n", "");
                            result = result.replace(" ", "");
                            result = "[" + result + "]";
                            //解析json字符串获得List<Map<String,Object>>
                            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                            for (Map<String, Object> map : lists) {
                                String statusstr = map.get("status") + "";
                                String datastr = map.get("data") + "";
                                List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");


                                for (Map<String, Object> datastrmap : datastrlists) {
                                    fullpicurlstr = datastrmap.get("fullpicurl") + "";
                                    picheightstr = datastrmap.get("picheight") + "";
                                    picurlstr = datastrmap.get("picurl") + "";
                                    picwidthstr = datastrmap.get("picwidth") + "";

                                }


                            }

                            new SendInfoTasksaveheadimg().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveAvatar&step=save",
                                            new String[]{"mid", Constants.staticmyuidstr},
                                            new String[]{"login_password", Constants.staticpasswordstr},
                                            new String[]{"picurl", picurlstr},
                                            new String[]{"picwidth", picwidthstr},
                                            new String[]{"fullpicurl", fullpicurlstr},
                                            new String[]{"x1", "0"},
                                            new String[]{"y1", "0"},
                                            new String[]{"x2", "0"},
                                            new String[]{"y2", "0"},
                                            new String[]{"w", picwidthstr},
                                            new String[]{"h", picheightstr}


//								  picurl           //图片路径
//								   picwidth        // 图片宽度
//								   fullpicurl       //图片绝路径
//								   x1             // 选择区域左上角x轴坐标
//								   y1             // 选择区域左上角y轴坐标
//								   x2            // 选择区域右下角x轴坐标
//								   y2              // 选择区域右下角y轴坐标
//								   w               // 选择区的宽度
//								   h               // 选择区的高度

                                    )

                            );

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
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
        }
    }

    private class SendInfoTasksaveheadimg extends AsyncTask<TaskParams, Void, String> {

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
                String middlestr = "";
                String datastr = map.get("data") + "";

                List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps(datastr);

                for (Map<String, Object> datastrmap : datastrlists) {
                    String bigstr = map.get("big") + "";
                    middlestr = map.get("middle") + "";
                    String smallstr = map.get("small") + "";
                    String tinystr = map.get("tiny") + "";
                    String statusstr = map.get("status") + "";

                }

                //头像信息展示
                Drawable drawable;

                URL url;
                try {
                    url = new URL(middlestr);
                    drawable = Drawable.createFromStream(url.openStream(), "src");
                    imgportrait.setImageDrawable(drawable);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }


        }

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


    private class SendInfoTasktesting extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //CustomToast.makeText(supermanlistActivity.this, result, 1).show();
            //old_name=dfsfsdfsd&uname=dsfdf

            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            //解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String infostr;
                String statusstr = map.get("status") + "";

                if (map.get("info") == null) {
                    infostr = "该用户名不存在";
                } else {
                    infostr = map.get("info") + "";
                }
                isexist1.setText(infostr);


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
            //CustomToast.makeText(supermanlistActivity.this, result, 1).show();
            //old_name=dfsfsdfsd&uname=dsfdf
            result = result.replace("\n ", "");
            result = result.replace("\n", "");
            result = result.replace(" ", "");
            result = "[" + result + "]";
            //解析json字符串获得List<Map<String,Object>>
            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
            for (Map<String, Object> map : lists) {
                String infostr;
                String statusstr = map.get("status") + "";

//				if(map.get("info")==null){
//					infostr="该用户名不存在";
//				}else{
//				    infostr= map.get("info")+"";
//				}
//				isexist1.setText(infostr);
            }


        }

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

            HttpPost httpPost = new HttpPost(Constants.Url + "?app=public&mod=AppFeedList&act=AppSaveAvatar&step=upload");

            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            entity.addPart("mid", new StringBody(Constants.staticmyuidstr));
            entity.addPart("login_password", new StringBody(Constants.staticpasswordstr));
            //Bitmap bmpCompressed=Bitmap.createScaledBitmap(bitmap, 640, 480, true);
            Bitmap bmpCompressed = Bitmap.createScaledBitmap(bitmap, 500, 500, true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            //bmpCompressed.compress(CompressFormat.JPEG,100, bos);
            bmpCompressed.compress(CompressFormat.PNG, 100, bos);
            imgportrait.setImageBitmap(bmpCompressed);
            byte[] data = bos.toByteArray();

            // sending a String param;

            //entity.addPart("myParam",new StringBody("my value"));

            // sending a Image;

            //entity.addPart("mylmage",new ByteArrayBody(data,"temp.jpg"));
            entity.addPart("mylmage", new ByteArrayBody(data, "temp.png"));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            int code = response.getStatusLine().getStatusCode();

            //BufferedReader reader=
            //		new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"utf-8"));
            InputStream in = response.getEntity().getContent();
            //String sResponse=reader.readLine();
            String resstr = HttpUtil.changeInputStream(in);
            //saveheadimg1.setVisibility(View.VISIBLE);

            return resstr;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}
