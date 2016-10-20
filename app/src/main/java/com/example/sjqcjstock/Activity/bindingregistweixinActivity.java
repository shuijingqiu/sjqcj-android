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
import com.example.sjqcjstock.netutil.TimeCountUtil;
import com.example.sjqcjstock.view.CustomToast;

import java.util.List;
import java.util.Map;

public class bindingregistweixinActivity extends Activity {

    // 获取控件
    LinearLayout goback1;
    Button shortmessage1;
    EditText fillphonecode1;
    Button registerbinding1;
    EditText loginusername1;
    EditText loginemail1;
    EditText loginpassword1;
    Button olduserlogin1;

    // 用于接收 验证码id和验证码

    String verify_idstr;
    String verificationstr;

    // 获取token信息
    String qqtokenstr;
    String weixinaccess_tokenstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.bangdingregistweixinlogin);

        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);

        initView();

    }

    private void initView() {
        // TODO Auto-generated method stub

        // 获取Intent的 token

        Intent intent = getIntent();
        qqtokenstr = intent.getStringExtra("qqtoken");
        weixinaccess_tokenstr = intent.getStringExtra("access_token");
        // CustomToast.makeText(getApplicationContext(), qqtokenstr, 1).show();

        goback1 = (LinearLayout) findViewById(R.id.goback1);
        shortmessage1 = (Button) findViewById(R.id.shortmessage1);
        fillphonecode1 = (EditText) findViewById(R.id.fillphonecode1);
        registerbinding1 = (Button) findViewById(R.id.registerbinding1);
        loginusername1 = (EditText) findViewById(R.id.loginusername1);
        loginemail1 = (EditText) findViewById(R.id.loginemail1);
        loginpassword1 = (EditText) findViewById(R.id.loginpassword1);
        olduserlogin1 = (Button) findViewById(R.id.olduserlogin1);

        goback1.setOnClickListener(new goback1_listener());
        shortmessage1.setOnClickListener(new shortmessage1_listener());
        registerbinding1.setOnClickListener(new registerbinding1_listener());
        olduserlogin1.setOnClickListener(new olduserlogin1_listener());
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
            new SendInfoTaskverify()
                    .execute(new TaskParams(
                                    Constants.Url + "?app=public&mod=Register&act=verification",
                                    new String[]{"phone",
                                            fillphonecode1.getText().toString()}

                            )

                    );
        }
    }

    class olduserlogin1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = new Intent(bindingregistweixinActivity.this,
                    bindingweixinActivity.class);
            intent.putExtra("qqtoken", qqtokenstr);
            intent.putExtra("access_token", weixinaccess_tokenstr);
            startActivity(intent);
        }

    }

    class registerbinding1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            if (verify_idstr != null && !"".equals(verify_idstr)) {
                // sjc 有问题是什么东西 以前是一个时间戳  mh
                String sjc = "";
                new SendInfoTaskregisterbinding().execute(new TaskParams(
                                Constants.Url + "?app=index&mod=Index&act=AppdoOtherStep",
                                new String[]{"email",
                                        sjc + "@qq.com"}, new String[]{"password",
                                "111111"}, new String[]{"uname",
                                loginusername1.getText().toString()},
                                new String[]{"type", "weixin"}, new String[]{
                                "tokey", qqtokenstr}, new String[]{
                                "access_token", weixinaccess_tokenstr},
                                new String[]{"phone",
                                        fillphonecode1.getText().toString()},
                                new String[]{"verify_id", verify_idstr},
                                new String[]{"verification", verificationstr}
                        )
                );

            }
        }

    }

    private class SendInfoTaskregisterbinding extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);

                for (Map<String, Object> map : lists) {
                    // String infostr;
                    // String statusstr;
                    // String datastr;
                    // if(map.get("status").toString()==null){
                    // statusstr="";
                    // }
                    String statusstr = map.get("status") + "";
                    String infostr = map.get("info") + "";
                    String datastr = map.get("data") + "";

                    CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_LONG).show();
                    if ("0".equals(statusstr)) {

                    } else {
                        List<Map<String, Object>> datastrlists = JsonTools
                                .listKeyMaps("[" + datastr + "]");

                        for (Map<String, Object> datastrmap : datastrlists) {

                            String midstr = datastrmap.get("mid") + "";

                            /** 向SharedPreferemces中存储数据 */
                            // 获得编辑器
                            Editor editor = getSharedPreferences("userinfo",
                                    MODE_PRIVATE).edit();// 获得编辑这个文件的编辑器

                            // 存储数据
                            editor.putString("userid", midstr);

                            editor.putString("login_email", loginusername1
                                    .getText().toString());
                            editor.putString("login_password", loginpassword1
                                    .getText().toString());

                            editor.commit();

                            Constants.setStaticmyuidstr(midstr);
                            Constants.setStatictokeystr(qqtokenstr);

                            // Constants.setStaticuname(unamestr);

                            // if(Integer.parseInt(statusstr)==1){
                            Intent intent = new Intent(
                                    bindingregistweixinActivity.this,
                                    MainActivity.class);
                            startActivity(intent);

                        }
                    }
                }

            }
        }

    }

    private class SendInfoTaskverify extends
            AsyncTask<TaskParams, Void, String> {

        @Override
        protected String doInBackground(TaskParams... params) {
            return HttpUtil.doInBackground(params);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result == null) {
                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG)
                        .show();
            } else {
                super.onPostExecute(result);
                // CustomToast.makeText(supermanlistActivity.this, result, 1).show();
                result = result.replace("\n ", "");
                result = result.replace("\n", "");
                result = result.replace(" ", "");
                result = "[" + result + "]";
                // 解析json字符串获得List<Map<String,Object>>
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
                    // 计时器
                    TimeCountUtil timeCountUtil = new TimeCountUtil(
                            bindingregistweixinActivity.this, 60000, 1000,
                            shortmessage1);
                    timeCountUtil.start();

                    if ("1".equals(statusstr)) {
                        CustomToast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_LONG)
                                .show();
                        shortmessage1.setText("验证已发");
                        // //计时器
                        // TimeCountUtil timeCountUtil = new
                        // TimeCountUtil(RegisterActivity.this, 60000, 1000,
                        // shortmessage1);
                        // timeCountUtil.start();

                    } else {
                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_LONG)
                                .show();

                    }

                    List<Map<String, Object>> datastrlists = JsonTools
                            .listKeyMaps("[" + datastr + "]");
                    for (Map<String, Object> datastrmap : datastrlists) {

                        verify_idstr = datastrmap.get("id") + "";
                        verificationstr = datastrmap.get("verification")
                                + "";
                        // String statusstr=
                        // datastrmap.get("status")+"";

                    }
                }

            }
        }

    }

}
