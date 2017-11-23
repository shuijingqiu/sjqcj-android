package com.example.sjqcjstock.Activity.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.Activity.AgreementActivity;
import com.example.sjqcjstock.Activity.MainActivity;
import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.TimeCountUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomProgress;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册页面
 */
public class RegisterActivity extends Activity {

    //获取控件
    private Button register1;
    private Button shortmessage1;
    private LinearLayout goback1;
    private EditText fillphonecode1;
    private EditText fillpassword2;
    private EditText loginusername1;
    private EditText fillregistercode1;
    //用于接收 验证码id
    private String verify_idstr = "";
    private boolean isName = false;
    // 操作类型为绑定还是注册 （binding 绑定）
    private String type = "";
    // 注册类型(qq 或者微信)
    private String registerType;
    // 第三方注册用的 oauth_token
    private String oauth_token;
    // 第三方注册用的 oauth_token_secret
    private String oauth_token_secret;
    // 调用接口返回的数据
    private String registerStr = "";
    private String codeStr = "";
    private String nameStr = "";
    // 网络请求提示
    private CustomProgress dialog;
    // 验证码定时器
    private TimeCountUtil timeCountUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        dialog = new CustomProgress(this);
        register1 = (Button) findViewById(R.id.register1);
        shortmessage1 = (Button) findViewById(R.id.shortmessage1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        fillphonecode1 = (EditText) findViewById(R.id.fillphonecode1);
        fillregistercode1 = (EditText) findViewById(R.id.fillregistercode1);
        fillpassword2 = (EditText) findViewById(R.id.fillpassword2);
        loginusername1 = (EditText) findViewById(R.id.loginusername1);
        goback1.setOnClickListener(new goback1_listener());
        shortmessage1.setOnClickListener(new shortmessage1_listener());
        register1.setOnClickListener(new register1_listener());
        loginusername1.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 判断用户名是否重复
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                        List dataList = new ArrayList();
                        // 用户名
                        dataList.add(new BasicNameValuePair("uname", loginusername1.getText().toString()));
                        nameStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/register/checkUname", dataList);
                        handler.sendEmptyMessage(2);
                        }
                    }).start();
//                    // 判断用户名是否重复
//                    new SendInfoTasktestusername().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=IsUnameAvailable",
//                                    new String[]{"old_name", "dfsdf"},
//                                    new String[]{"uname", loginusername1.getText().toString()}
//                            )
//                    );
                }
            }
        });

        fillphonecode1.setOnFocusChangeListener(new OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Utils.isMobileNO(fillphonecode1.getText().toString())) {
                        CustomToast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        String name = getIntent().getStringExtra("name");
        if (name != null) {
            loginusername1.setText(name);
            loginusername1.setSelection(loginusername1.length());
        }
        type = getIntent().getStringExtra("type");
        if (type != null && type.equals("binding")) {
            ((TextView) findViewById(R.id.title_tv)).setText("绑定账号");
            ((Button) findViewById(R.id.register1)).setText("立即绑定");
        }
        registerType = getIntent().getStringExtra("registerType");
        oauth_token = getIntent().getStringExtra("oauth_token");
        oauth_token_secret = getIntent().getStringExtra("oauth_token_secret");
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isineditarea(loginusername1, ev) == true) {
                loginusername1.setVisibility(View.VISIBLE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(loginusername1.getWindowToken(), 0);
            }
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


    class register1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            if (Utils.isFastDoubleClick3()) {
                return;
            }
            if (!((CheckBox) findViewById(R.id.check_box_protocol)).isChecked()) {
                CustomToast.makeText(getApplicationContext(), "请阅读《水晶球用户服务协议》", Toast.LENGTH_SHORT).show();
                return;
            }
            String str1 = "";
            String str3 = "";
            String str4 = "";
            if ("".equals(loginusername1.getText().toString().trim())) {
                CustomToast.makeText(getApplicationContext(), "请输入用户名", Toast.LENGTH_SHORT).show();
                return;
            } else {
                str1 = loginusername1.getText().toString().trim();
            }
            if (isName) {
                CustomToast.makeText(getApplicationContext(), "该用户名已被使用", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("".equals(fillpassword2.getText().toString().trim())) {
                CustomToast.makeText(getApplicationContext(), "请输入设置密码", Toast.LENGTH_SHORT).show();
                return;
            } else {
                str3 = fillpassword2.getText().toString().trim();
            }

            if ("".equals(fillphonecode1.getText().toString().trim())) {
                CustomToast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                return;
            } else {
                str4 = fillphonecode1.getText().toString().trim();
            }
            if (!Utils.isMobileNO(fillphonecode1.getText().toString())) {
                CustomToast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("".equals(fillregistercode1.getText().toString().trim())) {
                CustomToast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
            dialog.showDialog();
            final List dataList = new ArrayList();
            // 用户名
            dataList.add(new BasicNameValuePair("uname", str1));
            // 密码
            dataList.add(new BasicNameValuePair("password", str3));
            // 验证码id
            dataList.add(new BasicNameValuePair("verify_id", verify_idstr));
            // 验证码
            dataList.add(new BasicNameValuePair("verification", fillregistercode1.getText().toString()));
            // 手机号
            dataList.add(new BasicNameValuePair("phone", str4));
//                    // 邮箱
//                    dataList.add(new BasicNameValuePair("email", ));
            if (registerType != null && !"".equals(registerType)) {
                // 第三方注册
                // 类型 第三方注册必传
                dataList.add(new BasicNameValuePair("other_type", registerType));
                // 微信、QQ-openid
                dataList.add(new BasicNameValuePair("other_uid", oauth_token));
                // 微信、QQ-access_token
                dataList.add(new BasicNameValuePair("oauth_token", oauth_token_secret));
            }
            // 用户注册
            new Thread(new Runnable() {
                @Override
                public void run() {
                    registerStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/register", dataList);
                    handler.sendEmptyMessage(1);
                }
            }).start();
            Constants.setStaticpasswordstr(fillpassword2.getText().toString());
//
//            if (registerType != null && !"".equals(registerType)) {
//                // 第三方注册
//                new SendInfoTaskregister().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=AppRegister",
//                                new String[]{"uname", str1},
//                                new String[]{"email", str4 + "@qq.com"},
//                                new String[]{"password", str3},
//                                new String[]{"phone", str4},
//                                new String[]{"verify_id", str5},
//                                new String[]{"verification", str6},
//                                new String[]{"other_type", registerType},
//                                new String[]{"oauth_token", oauth_token},
//                                new String[]{"oauth_token_secret", oauth_token_secret},
//                                new String[]{"other_uid", oauth_token}
//                        )
//                );
//            } else {
//                // 普通注册
//                new SendInfoTaskregister().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=AppRegister",
//                                new String[]{"uname", str1},
//                                new String[]{"email", str4 + "@qq.com"},
//                                new String[]{"password", str3},
//                                new String[]{"phone", str4},
//                                new String[]{"verify_id", str5},
//                                new String[]{"verification", str6}
//                        )
//                );
//            }
        }
    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }
    }


    class shortmessage1_listener implements OnClickListener {
        @Override
        public void onClick(View arg0) {
            if (Utils.isFastDoubleClick3()) {
                return;
            }
            if (!Utils.isMobileNO(fillphonecode1.getText().toString())) {
                CustomToast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            //计时器
            timeCountUtil = new TimeCountUtil(RegisterActivity.this, 60000, 1000, shortmessage1);
            timeCountUtil.start();

            // 获取输入手机号的验证码
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    // 手机号
                    dataList.add(new BasicNameValuePair("phone", fillphonecode1.getText().toString()));
                    codeStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/register/verification", dataList);
                    handler.sendEmptyMessage(0);
                }
            }).start();

//            new SendInfoTaskverify().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=verification",
//                            new String[]{"phone", fillphonecode1.getText().toString()}
//                    )
//            );
        }
    }


//    private class SendInfoTaskverify extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String infostr;
//                    String statusstr;
//                    String datastr;
//                    if (map.get("status").toString() == null) {
//                        statusstr = "";
//                    } else {
//                        statusstr = map.get("status") + "";
//                    }
//                    if (map.get("info").toString() == null) {
//                        infostr = "";
//                    } else {
//                        infostr = map.get("info") + "";
//                    }
//                    if (map.get("data").toString() == null) {
//                        datastr = "";
//                    } else {
//                        datastr = map.get("data") + "";
//                    }
//                    if ("1".equals(statusstr)) {
//                        CustomToast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT).show();
//                        shortmessage1.setText("验证已发");
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();
//                        timeCountUtil.onFinish();
//                        timeCountUtil.cancel();
//                        return;
//                    }
//
//                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
//                    for (Map<String, Object> datastrmap : datastrlists) {
//                        verify_idstr = datastrmap.get("id") + "";
//                        verificationstr = datastrmap.get("verification") + "";
//                        //String statusstr= datastrmap.get("status")+"";
//
//                    }
//
//                }
//
//            }
//        }
//
//    }


//    private class SendInfoTaskregister extends AsyncTask<TaskParams, Void, String> {
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "网络超时或无数据，请重试", Toast.LENGTH_SHORT).show();
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String datastr = null;
//                    String infostr = null;
//                    String statusstr = null;
//                    if (map.get("data") == null) {
//                        datastr = "0";
//                    } else {
//                        datastr = map.get("data") + "";
//                    }
//                    if (map.get("info") == null) {
//                        infostr = "";
//                    } else {
//
//                        infostr = map.get("info") + "";
//                    }
//                    if (map.get("status") == null) {
//                        statusstr = "";
//                    } else {
//                        statusstr = map.get("status") + "";
//                    }
//                    shortmessage1.setText("免费获取");
//                    dialog.dismissDlog();
//                    if ("1".equals(datastr)) {
//                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();
//                        // 重新登陆一次
//                        new SendInfoTask().execute(new TaskParams(Constants.Url + "?app=public&mod=Passport&act=AppLogin",
//                                        new String[]{"login_email", loginusername1.getText().toString()},
//                                        new String[]{"login_password", fillpassword2.getText().toString()},
//                                        new String[]{"login_remember", "1"}
//                                )
//                        );
//                        Constants.setStaticpasswordstr(fillpassword2.getText().toString());
//                    } else if ("0".equals(datastr)) {
//                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//            dialog.dismissDlog();
//        }
//
//    }

//
//    private class SendInfoTasktestusername extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//
//            } else {
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String infostr;
//                    String statusstr = map.get("status") + "";
//                    if (map.get("info") == null) {
//                        infostr = "该用户名可用";
//                        isName = false;
//                    } else {
//                        infostr = map.get("info") + "";
//                        isName = true;
//                        loginusername1.requestFocus();
//                    }
//                    //isexist1.setText(infostr);
//                    //
//                    //CustomToast.makeText(getApplicationContext(),infostr ,0).setGravity(Gravity.CENTER, 0, -50);
//
//                    Toast toast = CustomToast.makeText(getApplicationContext(),
//                            infostr, Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER, 0, -50);
//                    toast.show();
//                }
//            }
//
//        }
//
//    }
//
//
//    private class SendInfoTasktestphone extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//
//            } else {
//
//                super.onPostExecute(result);
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String infostr;
//                    if (map.get("info") == null) {
//                        infostr = "该电话可以使用";
//                    } else {
//                        infostr = map.get("info") + "";
//                    }
//                    CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        }
//
//    }
//
//
//    private class SendInfoTasktestemail extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            if (result == null) {
//
//            } else {
//                super.onPostExecute(result);
//                //CustomToast.makeText(supermanlistActivity.this, result, 1).show();
//                //old_name=dfsfsdfsd&uname=dsfdf
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String infostr;
//                    String statusstr = map.get("status") + "";
//
//                    if (map.get("info") == null) {
//                        //infostr="该email不存在";
//                        //infostr="";
//                    } else {
//                        infostr = map.get("info") + "";
//                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    }


//    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_SHORT).show();
//            } else {
//                super.onPostExecute(result);
//
//                result = result.replace("\n ", "");
//                result = result.replace("\n", "");
//                result = result.replace(" ", "");
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String statusstr = map.get("status") + "";
//                    if (!"1".equals(statusstr)) {
//                        CustomToast.makeText(getApplicationContext(), "用户名或密码错误,请重试", Toast.LENGTH_SHORT).show();
//                    }
//                    String unamestr;
//                    String uidstr = map.get("uid") + "";
//
//                    if (map.get("name") == null) {
//                        unamestr = "";
//                    } else {
//                        unamestr = map.get("name") + "";
//                    }
//                    /**向SharedPreferemces中存储数据*/
//                    //获得编辑器
//                    Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();//获得编辑这个文件的编辑器
//                    //存储数据
//                    editor.putString("userid", uidstr);
//                    editor.putString("login_email", loginusername1.getText().toString());
//                    editor.putString("login_password", fillpassword2.getText().toString());
//                    editor.commit();
//
//                    //获得编辑器
//                    Editor editor1 = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();//获得编辑这个文件的编辑器）
//                    //存储数据
//                    editor1.putString("isLogin", "1");
//                    editor1.putString("uidstr", uidstr);
//                    editor1.putString("unamestr", loginusername1.getText().toString());
//                    editor1.putString("loginPwd", fillpassword2.getText().toString());
//                    editor1.commit();
//
//                    Constants.apptoken = map.get("token") + "";
//                    Constants.setStaticmyuidstr(uidstr);
//                    Constants.setStaticuname(loginusername1.getText().toString());
//                    Constants.setStaticpasswordstr(fillpassword2.getText().toString());
//
//                    // 调用更新新站的用户消息接口（修改注册用户必须调用）
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
////                            resstr = HttpUtil.restHttpGet(Constants.moUrl + "/user/updateAppUserMsg?uid=" + Constants.staticmyuidstr + "&token=" + Constants.apptoken + "&register=1");
//                            handler.sendEmptyMessage(0);
//                        }
//                    }).start();
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
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(codeStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code)) {
//                    shortmessage1.setText("验证已发");
                            verify_idstr = jsonObject.getString("verify_id");
                        } else {
                            // 发送不成功
                            timeCountUtil.onFinish();
                            timeCountUtil.cancel();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    try{
                        dialog.dismissDlog();
                        JSONObject jsonObject = new JSONObject(registerStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        if (Constants.successCode.equals(code)) {
                            JSONObject jsonData = new JSONObject(jsonObject.getString("data"));
                            String uidstr = jsonData.getString("uid");
                            String token = jsonData.getString("token");
                            String uname = jsonData.getString("uname");
                            //获得编辑器
                            Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();//获得编辑这个文件的编辑器
                            //存储数据
                            editor.putString("userid", uidstr);
                            editor.putString("login_email", loginusername1.getText().toString());
                            editor.putString("login_password", fillpassword2.getText().toString());
                            editor.commit();
                            //获得编辑器
                            Editor editor1 = getSharedPreferences("loginInfo", MODE_PRIVATE).edit();//获得编辑这个文件的编辑器）
                            //存储数据
                            editor1.putString("isLogin", "1");
                            editor1.putString("uidstr", uidstr);
                            editor1.putString("unamestr", uname);
                            editor1.putString("loginPwd", fillpassword2.getText().toString());
                            editor1.commit();
                            Constants.apptoken = token;
                            Constants.setStaticmyuidstr(uidstr);
                            Constants.setStaticuname(uname);
                            Constants.setStaticpasswordstr(fillpassword2.getText().toString());
                            // 修改登陆状态
                            Constants.isLogin = true;
                            // 注册成功跳转到主页
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismissDlog();
                    }
                    break;
                case 2:
                    try {
                        JSONObject jsonObject = new JSONObject(nameStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code)) {
                            isName = false;
                        } else {
                            isName = true;
                            loginusername1.requestFocus();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    /**
     * 阅读协议的单机事件
     */
    public void serviceClick(View view) {
        // 跳转到协议页面
        Intent intent = new Intent(RegisterActivity.this, AgreementActivity.class);
        intent.putExtra("type", "4");
        startActivity(intent);
    }

}
