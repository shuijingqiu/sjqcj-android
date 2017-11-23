package com.example.sjqcjstock.Activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
 * 找回密码的页面
 * Created by Administrator on 2016/6/14.
 */
public class ForgotPassWordActivity extends Activity {

    //获取控件
    private Button register1;
    private Button shortmessage1;
    private LinearLayout goback1;
    private EditText fillphonecode1;
    private EditText fillpassword1;
    private EditText fillpassword2;
    private EditText fillregistercode1;
    //用于接收 验证码id和验证码
    private String verify_idstr;
    // 接口返回数据
    private String passStr;
    private String codeStr;
    // 网络请求提示
    private CustomProgress dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.forgot_password_acitvity);
        // 将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        findView();
    }

    private void findView() {
        dialog = new CustomProgress(this);
        register1 = (Button) findViewById(R.id.register1);
        shortmessage1 = (Button) findViewById(R.id.shortmessage1);
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        fillphonecode1 = (EditText) findViewById(R.id.fillphonecode1);
        fillregistercode1 = (EditText) findViewById(R.id.fillregistercode1);
        fillpassword1 = (EditText) findViewById(R.id.fillpassword1);
        fillpassword2 = (EditText) findViewById(R.id.fillpassword2);
        goback1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        shortmessage1.setOnClickListener(new shortmessage1_listener());
        register1.setOnClickListener(new register1_listener());
        fillphonecode1.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (!Utils.isMobileNO(fillphonecode1.getText() + "")) {
                        CustomToast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });
    }


    /**
     * 找回密码的这个可以不要的
     */
    class register1_listener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            final String pass1 = fillpassword1.getText() + "".trim();
            final String pass2 = fillpassword2.getText() + "".trim();
            final String phone = fillphonecode1.getText() + "".trim();
            final String code = fillregistercode1.getText() + "".trim();
            if (!Utils.isMobileNO(phone)) {
                CustomToast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("".equals(pass1)) {
                CustomToast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("".equals(pass2)) {
                CustomToast.makeText(getApplicationContext(), "请输入确认密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (pass1.length() < 6 || pass1.length() > 15) {
                CustomToast.makeText(getApplicationContext(), "请输大于6位或小于15位的密码", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!pass1.equals(pass2)) {
                CustomToast.makeText(getApplicationContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }
            if ("".equals(code)) {
                CustomToast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
                return;
            }
//            if (!verificationstr.equals(code)) {
//                CustomToast.makeText(getApplicationContext(), "你输入的验证码不正确", Toast.LENGTH_SHORT).show();
//                return;
//            }
            dialog.showDialog();
            // 修改密码
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    // 手机号
                    dataList.add(new BasicNameValuePair("password", pass1));
                    dataList.add(new BasicNameValuePair("repassword", pass2));
                    dataList.add(new BasicNameValuePair("verify_id", verify_idstr));
                    dataList.add(new BasicNameValuePair("verification", code));
                    dataList.add(new BasicNameValuePair("phone", phone));
                    passStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/findPassword", dataList);
                    handler.sendEmptyMessage(1);
                }
            }).start();

//            // 修改密码
//            new ResetPassword().execute(new TaskParams(Constants.Url + "?app=public&mod=Passport&act=AppDoResetPassword",
//                            new String[]{"phone", phone},
//                            new String[]{"password", pass1},
//                            new String[]{"repassword", pass2}
//                    )
//            );

        }
    }

    class shortmessage1_listener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            if (!Utils.isMobileNO(fillphonecode1.getText() + "")) {
                CustomToast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                return;
            }
            //计时器
            TimeCountUtil timeCountUtil = new TimeCountUtil(ForgotPassWordActivity.this, 60000, 1000, shortmessage1);
            timeCountUtil.start();

            // 获取验证码
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    // 手机号
                    dataList.add(new BasicNameValuePair("phone", fillphonecode1.getText().toString()));
                    codeStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/findPassword/verification", dataList);
                    handler.sendEmptyMessage(0);
                }
            }).start();
//            new SendInfoTaskverify().execute(new TaskParams(Constants.Url + "?app=public&mod=Register&act=modifyVerification",
//                            new String[]{"phone", fillphonecode1.getText() + ""}
//                    )
//            );
        }
    }

//
//    private class SendInfoTaskverify extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//            } else {
//                super.onPostExecute(result);
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String infostr;
//                    String statusstr;
//                    String datastr;
//                    statusstr = map.get("status") + "";
//                    infostr = map.get("msg") + "";
//                    datastr = map.get("data") + "";
//
//                    if ("1".equals(statusstr)) {
//                        CustomToast.makeText(getApplicationContext(), "验证码发送成功", Toast.LENGTH_SHORT).show();
//                        shortmessage1.setText("验证已发");
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), infostr, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    List<Map<String, Object>> datastrlists = JsonTools.listKeyMaps("[" + datastr + "]");
//                    for (Map<String, Object> datastrmap : datastrlists) {
//                        verify_idstr = datastrmap.get("id") + "";
//                        verificationstr = datastrmap.get("verification") + "";
//                    }
//                }
//            }
//        }
//    }
//
//    private class ResetPassword extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            if (result == null) {
//                CustomToast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
//            } else {
//                super.onPostExecute(result);
//                result = "[" + result + "]";
//                //解析json字符串获得List<Map<String,Object>>
//                List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//                for (Map<String, Object> map : lists) {
//                    String msgstr;
//                    String statusstr;
//                    statusstr = map.get("status") + "";
//                    msgstr = map.get("msg") + "";
//                    if ("1".equals(statusstr)) {
//                        CustomToast.makeText(getApplicationContext(), msgstr, Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), msgstr, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
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
                        JSONObject jsonObject = new JSONObject(codeStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code)) {
//                    shortmessage1.setText("验证已发");
                            verify_idstr = jsonObject.getString("verify_id");
                        } else {
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    dialog.dismissDlog();
                    try {
                        JSONObject jsonObject = new JSONObject(passStr);
                        String code = jsonObject.getString("code");
                        String msgStr = jsonObject.getString("msg");
                        CustomToast.makeText(getApplicationContext(), msgStr, Toast.LENGTH_SHORT).show();
                        if (Constants.successCode.equals(code))
                            finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismissDlog();
                    }
                    break;
            }
        }
    };
}
