package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.netutil.TimeCountUtil;
import com.example.sjqcjstock.netutil.Utils;
import com.example.sjqcjstock.view.CustomToast;

import java.util.List;
import java.util.Map;

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
    //用于接收 验证码id和验证码
    private String verify_idstr;
    private String verificationstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
//        //将Activity反复链表
//        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
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
                    new SendInfoTasktestusername().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=IsUnameAvailable",
                                    //new String[] { "login_email", "1061550505@qq.com" },
                                    //new String[] { "login_password", "12345678" },
                                    new String[]{"old_name", "dfsdf"},
                                    new String[]{"uname", loginusername1.getText().toString()}
                            )
                    );
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
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub

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

            if (!verificationstr.equals(fillregistercode1.getText().toString().trim())) {
                CustomToast.makeText(getApplicationContext(), "你输入的验证码不正确", Toast.LENGTH_SHORT).show();
                return;
            }
            String str1 = "";
            String str3 = "";
            String str4 = "";
            String str5 = "";
            String str6 = "";
            if ("".equals(loginusername1.getText().toString().trim())) {
                str1 = "1";
            } else {
                str1 = loginusername1.getText().toString().trim();
            }

            if ("".equals(fillpassword2.getText().toString().trim())) {
                str3 = "1";
            } else {
                str3 = fillpassword2.getText().toString().trim();
            }

            if ("".equals(fillphonecode1.getText().toString().trim())) {
                str4 = "1";
            } else {
                str4 = fillphonecode1.getText().toString().trim();
            }

            if ("".equals(verify_idstr) || verify_idstr == null) {
                str5 = "1";
            } else {
                str5 = verify_idstr;
            }
            if ("".equals(verificationstr) || verificationstr == null) {
                str6 = "1";
            } else {
                str6 = verificationstr;
            }

            new SendInfoTaskregister().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=AppRegister",
                            new String[]{"uname", str1},
                            new String[]{"email", str4 + "@qq.com"},
                            new String[]{"password", str3},
                            new String[]{"phone", str4},
                            new String[]{"verify_id", str5},
                            new String[]{"verification", str6}
                    )
            );
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
            // TODO Auto-generated method stub

            if (!Utils.isMobileNO(fillphonecode1.getText().toString())) {
                CustomToast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            new SendInfoTaskverify().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=verification",
                            new String[]{"phone", fillphonecode1.getText().toString()}
                    )
            );
        }
    }


    private class SendInfoTaskverify extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                //CustomToast.makeText(supermanlistActivity.this, result, 1).show();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                //解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String infostr;
                    String statusstr;
                    String datastr;
                    if (map.get("status").toString() == null) {
                        statusstr = "";
                    } else {
                        statusstr = map.get("status") + "";
                    }
                    if (map.get("info").toString() == null) {
                        infostr = "";
                    } else {
                        infostr = map.get("info") + "";
                    }
                    if (map.get("data").toString() == null) {
                        datastr = "";
                    } else {
                        datastr = map.get("data") + "";
                    }
                    if ("1".equals(statusstr)) {
                        CustomToast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_LONG).show();
                        shortmessage1.setText("验证已发");
                    } else {
                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_LONG).show();
                        return;
                    }
                    //计时器
                    TimeCountUtil timeCountUtil = new TimeCountUtil(RegisterActivity.this, 60000, 1000, shortmessage1);
                    timeCountUtil.start();

                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datastrmap : datastrlists) {

                        verify_idstr = datastrmap.get("id") + "";
                        verificationstr = datastrmap.get("verification") + "";
                        //String statusstr= datastrmap.get("status")+"";

                    }

                }

            }
        }

    }


    private class SendInfoTaskregister extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或无数据，请重试", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                //解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    String datastr = null;
                    String infostr = null;
                    String statusstr = null;
                    if (map.get("data") == null) {
                        datastr = "0";
                    } else {
                        datastr = map.get("data") + "";
                    }

                    if (map.get("info") == null) {
                        infostr = "";
                    } else {

                        infostr = map.get("info") + "";
                    }
                    if (map.get("status") == null) {
                        statusstr = "";
                    } else {
                        statusstr = map.get("status") + "";
                    }

                    shortmessage1.setText("免费获取");
                    if ("1".equals(datastr)) {
                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_LONG).show();
                        //				Intent intent =new Intent(RegisterActivity.this,MainTab.class);
                        //				startActivity(intent);

                        new SendInfoTask().execute(new TaskParams(Constants.Url + "?app=public&mod=Passport&act=AppLogin",
                                        //new String[] { "login_email", "1061550505@qq.com" },
                                        //new String[] { "login_password", "12345678" },
                                        //new String[] { "login_remember", "1"}
                                        //new String[] { "login_email", "2@qq.com" },
                                        new String[]{"login_email", loginusername1.getText().toString()},
                                        //new String[] { "login_email", "2621617899@qq.com" },
                                        //new String[] { "login_password", "3665306" },
                                        //new String[] { "login_password", "111111" },
                                        new String[]{"login_password", fillpassword2.getText().toString()},
                                        new String[]{"login_remember", "1"}

                                )

                        );
                        Constants.setStaticpasswordstr(fillpassword2.getText().toString());

                    } else if ("0".equals(datastr)) {
                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_LONG).show();
                    }

                    //			loginusername1.getText().toString());
                    //			editor.putString("login_password", fillpassword2.getText().toString());
                    //
                    //			loginusername1.getText()+"";
                    //			loginemail1.getText()+"";

                }
            }

        }

    }


    private class SendInfoTasktestusername extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {

            } else {
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
                        infostr = "该用户名可用";
                    } else {
                        infostr = map.get("info") + "";
                    }
                    //isexist1.setText(infostr);
                    //
                    //CustomToast.makeText(getApplicationContext(),infostr ,0).setGravity(Gravity.CENTER, 0, -50);

                    Toast toast = CustomToast.makeText(getApplicationContext(),
                            infostr, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -50);
                    toast.show();
                }
            }

        }

    }


    private class SendInfoTasktestphone extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {

            } else {

                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                //解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
                for (Map<String, Object> map : lists) {
                    String infostr;
                    if (map.get("info") == null) {
                        infostr = "该电话可以使用";
                    } else {
                        infostr = map.get("info") + "";
                    }
                    CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();

                }

            }
        }

    }


    private class SendInfoTasktestemail extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {

            } else {
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
                        //infostr="该email不存在";
                        //infostr="";
                    } else {
                        infostr = map.get("info") + "";
                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }


    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "网络超时或用户名密码错误", Toast.LENGTH_LONG).show();
            } else {
                super.onPostExecute(result);

                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                //解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    String statusstr = map.get("status") + "";

                    if (!"1".equals(statusstr)) {
                        CustomToast.makeText(getApplicationContext(), "用户名或密码错误,请重试", Toast.LENGTH_SHORT).show();
                    }
                    String unamestr;
                    String uidstr = map.get("uid") + "";

                    if (map.get("name") == null) {
                        unamestr = "";
                    } else {
                        unamestr = map.get("name") + "";
                    }
                    /**向SharedPreferemces中存储数据*/
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
                    editor1.putString("unamestr", loginusername1.getText().toString());
                    editor1.putString("loginPwd", fillpassword2.getText().toString());
                    editor1.commit();

                    Constants.setStaticmyuidstr(uidstr);
                    Constants.setStaticuname(loginusername1.getText().toString());
                    Constants.setStaticpasswordstr(fillpassword2.getText().toString());

                    if (Integer.parseInt(statusstr) == 1) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        // 并且关闭登陆页面
                        loginActivity.instance.finish();
                        //注册成功后跳转页面关闭当前页面
                        finish();
                    }
                }
            }
        }
    }
}
