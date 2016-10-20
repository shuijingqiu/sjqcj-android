package com.example.sjqcjstock.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.netutil.JsonTools;
import com.example.sjqcjstock.netutil.TaskParams;
import com.example.sjqcjstock.view.CustomToast;

import java.util.List;
import java.util.Map;

public class bindingweixinActivity extends Activity {

    //获取控件
    LinearLayout goback1;
    Button bangdingregister1;
    Button qqbinding1;
    private EditText loginusername1;
    private EditText loginpassword1;

    //获取token信息
    String qqtokenstr;
    String weixinaccess_tokenstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.bangdingweixinlogin);

        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        //获取Intent的 token
        Intent intent = getIntent();
        qqtokenstr = intent.getStringExtra("qqtoken");
        weixinaccess_tokenstr = intent.getStringExtra("access_token");

        //CustomToast.makeText(getApplicationContext(), qqtokenstr, 1).show();

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        bangdingregister1 = (Button) findViewById(R.id.bangdingregister1);
        qqbinding1 = (Button) findViewById(R.id.qqbinding1);
        loginusername1 = (EditText) findViewById(R.id.loginusername1);
        loginpassword1 = (EditText) findViewById(R.id.loginpassword1);


        goback1.setOnClickListener(new goback1_listener());
        bangdingregister1.setOnClickListener(new bangdingregister1_listener());
        qqbinding1.setOnClickListener(new qqbinding1_listener());

    }

    class goback1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            finish();
        }

    }

    class bangdingregister1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(bindingweixinActivity.this, bindingregistweixinActivity.class);
            intent.putExtra("qqtoken", qqtokenstr);
            intent.putExtra("weixinaccess_token", weixinaccess_tokenstr);

            startActivity(intent);
        }

    }


    class qqbinding1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            //2@qq.com
            //3665306
            new SendInfoTask().execute(new TaskParams(Constants.Url + "?app=index&mod=Index&act=AppdoBindStep",
                            //new String[] { "login_email", "1061550505@qq.com" },
                            //new String[] { "login_password", "12345678" },
                            //new String[] { "login_remember", "1"}
                            //new String[] { "login_email", "2@qq.com" },
                            new String[]{"email", loginusername1.getText().toString()},
                            //new String[] { "login_email", "2621617899@qq.com" },
                            //new String[] { "login_password", "3665306" },
                            //new String[] { "login_password", "111111" },
                            new String[]{"password", loginpassword1.getText().toString()},
                            new String[]{"type", "weixin"},
                            new String[]{"tokey", qqtokenstr},
                            new String[]{"access_token", weixinaccess_tokenstr}

                    )

            );
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
                    String datastr = map.get("data") + "";

                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datastrmap : datastrlists) {
                        String midstr = datastrmap.get("mid") + "";


                        /**向SharedPreferemces中存储数据*/
                        //获得编辑器
                        Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();//获得编辑这个文件的编辑器

                        //存储数据
                        editor.putString("userid", midstr);

                        editor.putString("login_email", loginusername1.getText().toString());
                        editor.putString("login_password", loginpassword1.getText().toString());


                        editor.commit();

                        Constants.setStaticmyuidstr(midstr);
                        Constants.setStatictokeystr(qqtokenstr);

                        //Constants.setStaticuname(unamestr);

                        //if(Integer.parseInt(statusstr)==1){
                        Intent intent = new Intent(bindingweixinActivity.this, MainActivity.class);
                        startActivity(intent);
                        //}

                    }


                }


            }

        }

    }

}
