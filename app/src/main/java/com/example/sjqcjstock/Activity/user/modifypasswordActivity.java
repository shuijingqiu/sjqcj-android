package com.example.sjqcjstock.Activity.user;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sjqcjstock.R;
import com.example.sjqcjstock.app.ExitApplication;
import com.example.sjqcjstock.constant.Constants;
import com.example.sjqcjstock.netutil.HttpUtil;
import com.example.sjqcjstock.view.CustomToast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改密码页面
 */
public class modifypasswordActivity extends Activity {

    //获取控件
    private LinearLayout goback1;
    private EditText oldpassword1;
    private EditText newpassword2;
    private EditText renewpassword2;
    private Button modifiedsubmit1;
    private TextView titleName;
    // 操作接口数据
    private String jsonStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.modifypassword);
        //将Activity反复链表
        ExitApplication.getInstance().addActivity(this);
        initView();
    }

    private void initView() {
        goback1 = (LinearLayout) findViewById(R.id.goback1);
        oldpassword1 = (EditText) findViewById(R.id.oldpassword1);
        newpassword2 = (EditText) findViewById(R.id.newpassword2);
        renewpassword2 = (EditText) findViewById(R.id.renewpassword2);
        modifiedsubmit1 = (Button) findViewById(R.id.modifiedsubmit1);
        goback1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        modifiedsubmit1.setOnClickListener(new modifiedsubmit1_listener());
        titleName = (TextView) findViewById(R.id.title_name);
        if (Constants.isDefault) {
            titleName.setText("修改密码");
        } else {
            titleName.setText("设置密码");
            findViewById(R.id.oldpassword_ll).setVisibility(View.GONE);
        }
    }


    class modifiedsubmit1_listener implements OnClickListener {

        @Override
        public void onClick(View arg0) {

            if (!newpassword2.getText().toString().equals(renewpassword2.getText().toString())) {
                CustomToast.makeText(getApplicationContext(), "对不起两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                renewpassword2.setText("");
                return;
            }
            // 登陆类型
            String type = Constants.getStaticLoginType();
            // 原始密码
            String oldPassWord = oldpassword1.getText() + "";
            if (Constants.isDefault) {
                type = "";
            } else {
                // 第三方登录的时候传一个假密码 不能为空就行
                oldPassWord = "jiade";
                type = "thirdparty";
            }
//            new SendInfoTask().execute(new TaskParams(Constants.Url + "?app=public&mod=AppFeedList&act=AppdoModifyPassword",
//                            //new String[] { "login_email", "1061550505@qq.com" },
//                            //new String[] { "login_password", "12345678" },
//                            new String[]{"mid", Constants.staticmyuidstr},
//                            new String[]{"login_password", Constants.staticpasswordstr},
//                            new String[]{"tokey", token},
//                            new String[]{"type", type},
//                            new String[]{"oldpassword", oldPassWord},
//                            new String[]{"password", newpassword2.getText().toString()},
//                            new String[]{"repassword", renewpassword2.getText().toString()}
//                    )
//            );

            // 修改密码
            final String finalOldPassWord = oldPassWord;
            final String finalType = type;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List dataList = new ArrayList();
                    dataList.add(new BasicNameValuePair("mid", Constants.staticmyuidstr));
                    dataList.add(new BasicNameValuePair("token", Constants.apptoken));
                    dataList.add(new BasicNameValuePair("type", finalType));
                    dataList.add(new BasicNameValuePair("oldpassword", finalOldPassWord));
                    dataList.add(new BasicNameValuePair("password", newpassword2.getText().toString()));
                    dataList.add(new BasicNameValuePair("repassword", renewpassword2.getText().toString()));
                    jsonStr = HttpUtil.restHttpPost(Constants.newUrl + "/api/user/editPassword", dataList);
                    handler.sendEmptyMessage(0);
                }
            }).start();
        }
    }

//    private class SendInfoTask extends AsyncTask<TaskParams, Void, String> {
//
//        @Override
//        protected String doInBackground(TaskParams... params) {
//            return HttpUtil.doInBackground(params);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            result = result.replace("\n ", "");
//            result = result.replace("\n", "");
//            result = result.replace(" ", "");
//            result = "[" + result + "]";
//            //解析json字符串获得List<Map<String,Object>>
//            List<Map<String, Object>> lists = JsonTools.listKeyMaps(result);
//            for (Map<String, Object> map : lists) {
//                String statusstr = map.get("status") + "";
//
//                if ("1".equals(statusstr)) {
//                    if (Constants.isDefault) {
//                        CustomToast.makeText(getApplicationContext(), "修改密码成功", Toast.LENGTH_SHORT).show();
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), "设置密码成功", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    if (Constants.isDefault) {
//                        CustomToast.makeText(getApplicationContext(), "修改密码失败,请重试", Toast.LENGTH_SHORT).show();
//                    } else {
//                        CustomToast.makeText(getApplicationContext(), "设置密码失败,请重试", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//        }
//    }

    /**
     * 线程更新Ui
     */
    private Handler handler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        CustomToast.makeText(modifypasswordActivity.this,jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                        if (!Constants.successCode.equals(jsonObject.getString("code"))) {
                            return;
                        }
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

}
